import csv
import json
import statistics
import sys
from pathlib import Path

FRAMEWORKS = ["manual", "spike", "dagger", "metro", "kotlin-inject", "koin"]
BUILD = [("clean", "clean build"), ("abi", "ABI change"), ("nonabi", "non-ABI change")]
RUNTIME = [("GraphBenchmarkCreate", "create graph"), ("GraphBenchmarkResolve", "resolve from graph"),
           ("GraphBenchmarkColdStart", "cold start")]

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
            samples = [value for fork in result["primaryMetric"]["rawData"] for value in fork]
            times[(benchmark, method)] = statistics.median(samples)
    return times


def jar_kib(framework):
    return sum(jar.stat().st_size for jar in (root / "graphs" / framework / "build" / "libs").glob("*.jar")) / 1024


build = build_times()
runtime = runtime_times()
lines = [
    "| Framework | Clean build (ms) | ABI change (ms) | Non-ABI change (ms) "
    "| Create graph (µs) | Resolve from graph (µs) | Cold start (ms) | Module jar (KiB) |",
    "|---|---:|---:|---:|---:|---:|---:|---:|",
]
series = []
for framework in FRAMEWORKS:
    builds = [build[f"{framework} {title}"] for _, title in BUILD]
    method = "kotlinInject" if framework == "kotlin-inject" else framework
    runtimes = [runtime[(benchmark, method)] for benchmark, _ in RUNTIME]
    create, resolve, cold = runtimes
    lines.append(f"| {framework} | " + " | ".join(f"{value:.0f}" for value in builds)
                 + f" | {create:.3f} | {resolve:.1f} | {cold / 1000:.1f} | {jar_kib(framework):.0f} |")
    series += [{"name": f"{framework} {title}", "unit": "ms", "value": value}
               for (_, title), value in zip(BUILD, builds)]
    series += [{"name": f"{framework} {title}", "unit": "us", "value": value}
               for (_, title), value in zip(RUNTIME, runtimes)]

table = "\n".join(lines) + "\n"
(output / "results.md").write_text(table)
(output / "results-spike.json").write_text(json.dumps([s for s in series if s["name"].startswith("spike ")], indent=2))
(output / "results-baselines.json").write_text(
    json.dumps([s for s in series if not s["name"].startswith("spike ")], indent=2))
print(table)
