import csv
import json
import statistics
import sys
from pathlib import Path

FRAMEWORKS = ["manual", "spike", "dagger", "metro", "kotlin-inject", "koin"]
BUILD = [("clean", "clean build"), ("abi", "ABI change"), ("nonabi", "non-ABI change")]
RUNTIME = [("GraphBenchmarkCreate", "create graph"), ("GraphBenchmarkResolve", "resolve from graph"),
           ("GraphBenchmarkColdStart", "cold start"), ("GraphBenchmarkScreen", "open screen")]

root = Path(sys.argv[1])
output = root / "output"


def build_times():
    with open(output / "build" / "benchmark.csv") as file:
        rows = list(csv.reader(file))
    scenarios = rows[0][1:]
    measured = [row[1:] for row in rows if row[0].startswith("measured build")]
    return {scenario: statistics.median(float(row[index]) for row in measured)
            for index, scenario in enumerate(scenarios)}


def runtime_times():
    times = {}
    for report in (output / "runtime").glob("*.json"):
        for result in json.loads(report.read_text()):
            benchmark, method = result["benchmark"].split(".")[-2:]
            metric = result["primaryMetric"]
            if metric["scoreUnit"] != "us/op":
                raise ValueError(f"{result['benchmark']} reports {metric['scoreUnit']}, expected us/op")
            samples = [value for fork in metric["rawData"] for value in fork]
            times[(benchmark, method)] = statistics.median(samples)
    return times


def jar_kib(framework):
    return sum(jar.stat().st_size for jar in (root / "graphs" / framework / "build" / "libs").glob("*.jar")) / 1024


build = build_times()
runtime = runtime_times()
lines = [
    "| Framework | Clean build (ms) | ABI change (ms) | Non-ABI change (ms) "
    "| Create graph (µs) | Resolve from graph (µs) | Cold start (ms) | Open screen (µs) | Module jar (KiB) |",
    "|---|---:|---:|---:|---:|---:|---:|---:|---:|",
]
series = []
for framework in [f for f in FRAMEWORKS if f"{f} clean build" in build]:
    builds = [build[f"{framework} {title}"] for _, title in BUILD]
    method = framework.replace("-", "_")
    runtimes = [runtime[(benchmark, method)] for benchmark, _ in RUNTIME]
    create, resolve, cold, screen = runtimes
    lines.append(f"| {framework} | " + " | ".join(f"{value:.0f}" for value in builds)
                 + f" | {create:.3f} | {resolve:.1f} | {cold / 1000:.1f} | {screen:.3f} | {jar_kib(framework):.0f} |")
    series += [{"framework": framework, "metric": title, "unit": "ms", "value": value}
               for (_, title), value in zip(BUILD, builds)]
    series += [{"framework": framework, "metric": title, "unit": "us", "value": value}
               for (_, title), value in zip(RUNTIME, runtimes)]


def bench(name, measurement, **extra):
    return {"name": name, "unit": measurement["unit"], "value": measurement["value"], **extra}


def write(name, benches):
    (output / name).write_text(json.dumps(benches, indent=2))


metrics = [title for _, title in BUILD + RUNTIME]
table = "\n".join(lines) + "\n"
(output / "results.md").write_text(table)
write("results.json", [bench(f"{s['framework']} {s['metric']}", s) for s in series])
write("results-spike.json", [bench(f"spike {s['metric']}", s)
                             for s in series if s["framework"] == "spike" and s["metric"] != "create graph"])
write("results-baselines.json", [bench(s["metric"], s, extra=s["framework"])
                                 for s in sorted(series, key=lambda s: metrics.index(s["metric"]))])
print(table)
