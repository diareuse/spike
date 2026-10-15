import json
import sys
from pathlib import Path

METRICS = ["create graph", "resolve from graph", "cold start"]


def slower_than_baseline(series, candidate="spike", baseline="dagger", tolerance=1.10):
    values = {entry["name"]: entry["value"] for entry in series}
    slower = []
    for metric in METRICS:
        mine, theirs = values.get(f"{candidate} {metric}"), values.get(f"{baseline} {metric}")
        if mine is not None and theirs is not None and mine > theirs * tolerance:
            slower.append((metric, mine, theirs))
    return slower


def main(root):
    output = root / "output"
    series = json.loads((output / "results.json").read_text())
    slower = slower_than_baseline(series)
    lines = ["### Spike vs Dagger at runtime", ""]
    if not any(entry["name"].startswith("dagger ") for entry in series):
        lines.append("Dagger was not measured, so Spike was not compared.")
    elif slower:
        lines += ["| Metric | Spike (µs) | Dagger (µs) |", "|---|---:|---:|"]
        lines += [f"| {metric} | {mine:.3f} | {theirs:.3f} |" for metric, mine, theirs in slower]
    else:
        lines.append("Spike is as fast as Dagger or faster on every runtime metric.")
    report = "\n".join(lines) + "\n"
    (output / "parity.md").write_text(report)
    print(report)
    return 1 if slower else 0


if __name__ == "__main__":
    sys.exit(main(Path(sys.argv[1])))
