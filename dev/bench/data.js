window.BENCHMARK_DATA = {
  "lastUpdate": 1776595031383,
  "repoUrl": "https://github.com/diareuse/spike",
  "entries": {
    "Benchmark": [
      {
        "commit": {
          "author": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "317f487b4db4af90c18883f303d1b1a5f12ed936",
          "message": "chore(benchmark): add git stash step before result generation\n\n- Added `git stash` command in the benchmark workflow to prevent uncommitted changes from interfering with subsequent steps.\n- Ensures clean state before generating `results.json`.",
          "timestamp": "2026-03-11T18:30:35+01:00",
          "tree_id": "2b16a4b31b180d6f6d60d8393ea7f44a220337f1",
          "url": "https://github.com/diareuse/spike/commit/317f487b4db4af90c18883f303d1b1a5f12ed936"
        },
        "date": 1773250659845,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2651.96,
            "unit": "ms"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "8244fb303fcda939d7ed7ddf1b52c6d83c859559",
          "message": "docs: add build status badge to README\n\n- Added a static badge showing build status to the README file.\n- Links to benchmarking history for more detailed insights.",
          "timestamp": "2026-03-11T19:05:39+01:00",
          "tree_id": "a7593a03070826ac021a8f3cc4ab35e7c2d1b293",
          "url": "https://github.com/diareuse/spike/commit/8244fb303fcda939d7ed7ddf1b52c6d83c859559"
        },
        "date": 1773252742469,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2720.32,
            "unit": "ms"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "6912c2a6f0896fac4d7f1fea20a004f7800ae729",
          "message": "docs: add build status badge to README\n\n- Added a static badge showing build status to the README file.\n- Links to benchmarking history for more detailed insights.",
          "timestamp": "2026-03-11T19:07:50+01:00",
          "tree_id": "8ac92c943d679acfdec2fb93b9fb41bca3e535e1",
          "url": "https://github.com/diareuse/spike/commit/6912c2a6f0896fac4d7f1fea20a004f7800ae729"
        },
        "date": 1773252887378,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2687.33,
            "unit": "ms"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "60a590e1caa7d65a1b82cee8d5fd8a76c0a934f1",
          "message": "chore(compiler): refine `pass` method to improve clarity\n\n- Updated `pass` method to accept `TypeFactoryCreator` as a parameter.\n- Modified all implementations to pass the respective creator instance.\n- Adjusted `TypeFactoryCreatorChain` implementation to align with new changes.\n- Ensures more explicit context binding for error handling and execution flow.",
          "timestamp": "2026-03-11T19:45:14+01:00",
          "tree_id": "259554d6bd304c99727900acd0832c539d848313",
          "url": "https://github.com/diareuse/spike/commit/60a590e1caa7d65a1b82cee8d5fd8a76c0a934f1"
        },
        "date": 1773255116538,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2676.17,
            "unit": "ms"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "6194ad7f46fdddc68b008c89980890ec66b13042",
          "message": "refactor(compiler): simplify and unify generator chain logic\n\n- Removed specialized chain classes and replaced them with generic chain implementations like `GeneratorChain`.\n- Deleted `DependencyContainerFileWithType`, `EntryPointFileWithType`, and `GeneratorChainGeneric`.\n- Introduced a companion object in `GeneratorChain` to handle type, object, and file generation.\n- Simplified `DependencyGraphGenerator` by reducing redundant code through shared chain logic.\n- Added `GeneratorFileWithType` for streamlined file generation involving types.",
          "timestamp": "2026-03-11T20:55:34+01:00",
          "tree_id": "1a3490b775005b5b5943bbc85910450e5f96031e",
          "url": "https://github.com/diareuse/spike/commit/6194ad7f46fdddc68b008c89980890ec66b13042"
        },
        "date": 1773260113105,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2663.69,
            "unit": "ms"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "5b9a5c9686f181ddf084131b416e1f4ff9a6ad66",
          "message": "refactor(compiler): simplify dependency writing in `DependencyFactoryGenerator`\n\n- Extracted dependency writing logic into `writeDependencies` for reuse.\n- Introduced `enqueueNestedTypes` to manage nested type queuing.\n- Added `appendDependencyInstructions` for streamlined instruction appending.\n- Improved modularity and readability of `DependencyFactoryGenerator`.",
          "timestamp": "2026-04-19T12:20:43+02:00",
          "tree_id": "92a336397c5bc0e84f5ae56bf8e4e5f1abdce71e",
          "url": "https://github.com/diareuse/spike/commit/5b9a5c9686f181ddf084131b416e1f4ff9a6ad66"
        },
        "date": 1776594474479,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2938.06,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 500.847,
            "unit": "ms"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "49699333+dependabot[bot]@users.noreply.github.com",
            "name": "dependabot[bot]",
            "username": "dependabot[bot]"
          },
          "committer": {
            "email": "hello@depasquale.wiki",
            "name": "Viktor De Pasquale",
            "username": "diareuse"
          },
          "distinct": true,
          "id": "b976448bdf114546ff3680ab0151bcff85e8d033",
          "message": "fix(deps): bump gradle-wrapper from 9.4.0 to 9.4.1\n\nBumps [gradle-wrapper](https://github.com/gradle/gradle) from 9.4.0 to 9.4.1.\n- [Release notes](https://github.com/gradle/gradle/releases)\n- [Commits](https://github.com/gradle/gradle/compare/v9.4.0...v9.4.1)\n\n---\nupdated-dependencies:\n- dependency-name: gradle-wrapper\n  dependency-version: 9.4.1\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-19T12:30:16+02:00",
          "tree_id": "b9bab3ad585784731f1a6f15008196d578927a26",
          "url": "https://github.com/diareuse/spike/commit/b976448bdf114546ff3680ab0151bcff85e8d033"
        },
        "date": 1776595030963,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3085.82,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 438.444,
            "unit": "ms"
          }
        ]
      }
    ]
  }
}