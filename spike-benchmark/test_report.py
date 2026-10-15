import json
import subprocess
import sys
import tempfile
import unittest
import zlib
from pathlib import Path

REPORT = Path(__file__).parent / "report.py"
BUILD = ["clean build", "ABI change", "non-ABI change"]
RUNTIME = {"GraphBenchmarkCreate": "create graph", "GraphBenchmarkResolve": "resolve from graph",
           "GraphBenchmarkColdStart": "cold start"}


def write_measurements(frameworks, root):
    output = root / "output"
    (output / "build").mkdir(parents=True)
    (output / "runtime").mkdir()
    scenarios = [f"{framework} {title}" for framework in frameworks for title in BUILD]
    rows = [["scenario"] + scenarios, ["warm-up build #1"] + ["99999"] * len(scenarios)]
    rows += [["measured build #1"] + [str(value(name)) for name in scenarios]]
    (output / "build" / "benchmark.csv").write_text("\n".join(",".join(row) for row in rows) + "\n")
    results = [{"benchmark": f"bench.runtime.{benchmark}.{framework.replace('-', '_')}",
                "primaryMetric": {"scoreUnit": "us/op", "rawData": [[value(f"{framework} {metric}")]]}}
               for framework in frameworks for benchmark, metric in RUNTIME.items()]
    (output / "runtime" / "main.json").write_text(json.dumps(results))
    for framework in frameworks:
        libs = root / "graphs" / framework / "build" / "libs"
        libs.mkdir(parents=True)
        (libs / f"{framework}.jar").write_bytes(b"x" * 1024)


def value(name):
    return float(zlib.crc32(name.encode()))


def report(frameworks):
    with tempfile.TemporaryDirectory() as directory:
        root = Path(directory)
        write_measurements(frameworks, root)
        subprocess.run([sys.executable, str(REPORT), str(root)], check=True, capture_output=True)
        return {name: json.loads((root / "output" / name).read_text())
                for name in ("results-spike.json", "results-baselines.json", "results.json")}


class BaselinesTest(unittest.TestCase):

    def test_every_framework_records_a_metric_under_the_same_name_so_it_shares_one_chart(self):
        baselines = report(["kotlin-inject", "dagger", "spike"])["results-baselines.json"]

        resolve = [entry for entry in baselines if entry["name"] == "resolve from graph"]
        self.assertEqual(["spike", "dagger", "kotlin-inject"], [entry["extra"] for entry in resolve])
        self.assertEqual([value("spike resolve from graph"), value("dagger resolve from graph"),
                          value("kotlin-inject resolve from graph")], [entry["value"] for entry in resolve])
        self.assertTrue(all(entry["unit"] == "us" for entry in resolve))

    def test_there_is_one_name_per_metric(self):
        baselines = report(["spike", "dagger"])["results-baselines.json"]

        self.assertEqual(BUILD + list(RUNTIME.values()), list(dict.fromkeys(entry["name"] for entry in baselines)))

    def test_build_metrics_are_in_milliseconds(self):
        baselines = report(["dagger"])["results-baselines.json"]

        clean = next(entry for entry in baselines if entry["name"] == "clean build")
        self.assertEqual(("ms", value("dagger clean build")), (clean["unit"], clean["value"]))

    def test_a_single_framework_run_records_only_that_framework(self):
        results = report(["dagger"])

        self.assertEqual({"dagger"}, {entry["extra"] for entry in results["results-baselines.json"]})
        self.assertEqual([], results["results-spike.json"])


class SpikeSeriesTest(unittest.TestCase):

    def test_spike_alerts_on_its_own_named_series_except_create_graph(self):
        spike = report(["spike", "dagger"])["results-spike.json"]

        self.assertEqual([("spike clean build", "ms"), ("spike ABI change", "ms"), ("spike non-ABI change", "ms"),
                          ("spike resolve from graph", "us"), ("spike cold start", "us")],
                         [(entry["name"], entry["unit"]) for entry in spike])

    def test_a_spike_only_run_still_alerts_on_every_spike_series(self):
        spike = report(["spike"])["results-spike.json"]

        self.assertEqual(5, len(spike))


class AllSeriesTest(unittest.TestCase):

    def test_every_measurement_is_named_by_framework_and_metric(self):
        series = report(["spike", "dagger"])["results.json"]

        values = {entry["name"]: entry["value"] for entry in series}
        self.assertEqual(value("dagger cold start"), values["dagger cold start"])
        self.assertEqual(value("spike create graph"), values["spike create graph"])
        self.assertEqual(12, len(series))


if __name__ == "__main__":
    unittest.main()
