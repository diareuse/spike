import json
import tempfile
import unittest
from pathlib import Path

from parity import main, slower_than_baseline


def series(**values):
    return [{"name": name.replace("_", " "), "unit": "us", "value": value} for name, value in values.items()]


class SlowerThanBaselineTest(unittest.TestCase):

    def test_nothing_is_slower_when_spike_is_at_or_below_dagger(self):
        measured = series(spike_resolve_from_graph=2.7, dagger_resolve_from_graph=3.3,
                          spike_create_graph=0.002, dagger_create_graph=0.002)

        self.assertEqual([], slower_than_baseline(measured))

    def test_a_metric_beyond_the_tolerance_is_reported_with_both_values(self):
        measured = series(spike_resolve_from_graph=4.0, dagger_resolve_from_graph=3.3)

        self.assertEqual([("resolve from graph", 4.0, 3.3)], slower_than_baseline(measured))

    def test_a_metric_within_the_tolerance_is_not_reported(self):
        measured = series(spike_cold_start=90.0, dagger_cold_start=85.0)

        self.assertEqual([], slower_than_baseline(measured, tolerance=1.10))

    def test_metrics_missing_for_either_framework_are_skipped(self):
        measured = series(spike_resolve_from_graph=9.0)

        self.assertEqual([], slower_than_baseline(measured))


class MainTest(unittest.TestCase):

    def run_main(self, spike, baselines):
        with tempfile.TemporaryDirectory() as directory:
            output = Path(directory) / "output"
            output.mkdir()
            (output / "results-spike.json").write_text(json.dumps(spike))
            (output / "results-baselines.json").write_text(json.dumps(baselines))
            code = main(Path(directory))
            return code, (output / "parity.md").read_text()

    def test_fails_and_names_the_metric_when_spike_is_slower_than_dagger(self):
        code, report = self.run_main(series(spike_resolve_from_graph=9.0), series(dagger_resolve_from_graph=3.3))

        self.assertEqual(1, code)
        self.assertIn("resolve from graph", report)

    def test_passes_when_spike_is_not_slower(self):
        code, _ = self.run_main(series(spike_resolve_from_graph=2.7), series(dagger_resolve_from_graph=3.3))

        self.assertEqual(0, code)

    def test_reads_metrics_that_spike_only_records_as_a_baseline(self):
        code, report = self.run_main([], series(spike_create_graph=0.01, dagger_create_graph=0.002))

        self.assertEqual(1, code)
        self.assertIn("create graph", report)

    def test_reports_no_comparison_when_dagger_was_not_measured(self):
        code, report = self.run_main(series(spike_resolve_from_graph=9.0), [])

        self.assertEqual(0, code)
        self.assertIn("not measured", report)


if __name__ == "__main__":
    unittest.main()
