window.BENCHMARK_DATA = {
  "lastUpdate": 1778819839009,
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
          "id": "0b1064895a4bc6deba5bbe852279220cb91353bc",
          "message": "fix(deps): bump androidx-lifecycle from 2.9.6 to 2.10.0\n\nBumps `androidx-lifecycle` from 2.9.6 to 2.10.0.\n\nUpdates `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose` from 2.9.6 to 2.10.0\n- [Release notes](https://github.com/JetBrains/compose-jb/releases)\n- [Changelog](https://github.com/JetBrains/compose-multiplatform/blob/master/CHANGELOG.md)\n- [Commits](https://github.com/JetBrains/compose-jb/commits)\n\nUpdates `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-savedstate` from 2.9.6 to 2.10.0\n- [Release notes](https://github.com/JetBrains/compose-jb/releases)\n- [Changelog](https://github.com/JetBrains/compose-multiplatform/blob/master/CHANGELOG.md)\n- [Commits](https://github.com/JetBrains/compose-jb/commits)\n\n---\nupdated-dependencies:\n- dependency-name: org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose\n  dependency-version: 2.10.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n- dependency-name: org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-savedstate\n  dependency-version: 2.10.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-19T12:30:56+02:00",
          "tree_id": "e134235778d0d9468777897fd47145fdc69c51fb",
          "url": "https://github.com/diareuse/spike/commit/0b1064895a4bc6deba5bbe852279220cb91353bc"
        },
        "date": 1776595089368,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2923.44,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 506.233,
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
          "id": "d98c7476410542af4f64e12f536c1650f94d43da",
          "message": "fix(deps): bump io.ktor:ktor-server-core from 3.4.1 to 3.4.2\n\nBumps [io.ktor:ktor-server-core](https://github.com/ktorio/ktor) from 3.4.1 to 3.4.2.\n- [Release notes](https://github.com/ktorio/ktor/releases)\n- [Changelog](https://github.com/ktorio/ktor/blob/main/CHANGELOG.md)\n- [Commits](https://github.com/ktorio/ktor/commits)\n\n---\nupdated-dependencies:\n- dependency-name: io.ktor:ktor-server-core\n  dependency-version: 3.4.2\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-19T12:38:19+02:00",
          "tree_id": "aad604f4f003af5edd21e6f87a4a9ae2177a98c6",
          "url": "https://github.com/diareuse/spike/commit/d98c7476410542af4f64e12f536c1650f94d43da"
        },
        "date": 1776595510754,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2896.55,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 498.905,
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
          "id": "6b5d40ebdaefe79d06e658a567565c180f7d5eb8",
          "message": "fix(deps): bump kotlinpoet from 2.2.0 to 2.3.0\n\nBumps `kotlinpoet` from 2.2.0 to 2.3.0.\n\nUpdates `com.squareup:kotlinpoet` from 2.2.0 to 2.3.0\n- [Release notes](https://github.com/square/kotlinpoet/releases)\n- [Changelog](https://github.com/square/kotlinpoet/blob/main/docs/changelog.md)\n- [Commits](https://github.com/square/kotlinpoet/compare/2.2.0...2.3.0)\n\nUpdates `com.squareup:kotlinpoet-ksp` from 2.2.0 to 2.3.0\n- [Release notes](https://github.com/square/kotlinpoet/releases)\n- [Changelog](https://github.com/square/kotlinpoet/blob/main/docs/changelog.md)\n- [Commits](https://github.com/square/kotlinpoet/compare/2.2.0...2.3.0)\n\n---\nupdated-dependencies:\n- dependency-name: com.squareup:kotlinpoet\n  dependency-version: 2.3.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n- dependency-name: com.squareup:kotlinpoet-ksp\n  dependency-version: 2.3.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-19T12:38:20+02:00",
          "tree_id": "ec0220c10401240699546970b4b8b9267ecab2e5",
          "url": "https://github.com/diareuse/spike/commit/6b5d40ebdaefe79d06e658a567565c180f7d5eb8"
        },
        "date": 1776595542941,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3006.28,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 533.856,
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
          "id": "1b5a51c9a5314048d02e3fb05649e94b3073e567",
          "message": "chore(master): release 0.0.2",
          "timestamp": "2026-04-19T12:48:27+02:00",
          "tree_id": "3ebbf2a9bc1f6db91b4e90ede290021ec0c64567",
          "url": "https://github.com/diareuse/spike/commit/1b5a51c9a5314048d02e3fb05649e94b3073e567"
        },
        "date": 1776596105410,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3096.46,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 426.652,
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
          "id": "60cfc8c0b9335479e7151dfe35565c374f6ae03d",
          "message": "refactor(compiler): modularize method and property generation\n\n- Extracted `generateMethods` and `generateProperties` into private methods in `EntryPointGenerator`.\n- Delegated factory method creation to a new `createFactoryMethod` function.\n- Moved instruction pointer and instantiation logic into dedicated methods in `DependencyFactoryGenerator`.\n- Improved modularity and streamlined code structure in generators.",
          "timestamp": "2026-04-20T17:22:58+02:00",
          "tree_id": "582eb14aaf1fdf3d584ad98222c75df79ec1de8b",
          "url": "https://github.com/diareuse/spike/commit/60cfc8c0b9335479e7151dfe35565c374f6ae03d"
        },
        "date": 1776699037543,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3238.38,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 530.343,
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
          "id": "946c43ae4d6f0fd451729eeb4a6a83792a4f06a9",
          "message": "docs(compose): improve `spikeViewModel` documentation\n\n- Added detailed KDoc with usage example for `spikeViewModel`.\n- Updated parameter descriptions, including `ViewModelStoreOwner`.\n- Enhanced clarity on `CreationExtras` and custom `ViewModel` creation.\n- Fixed formatting inconsistencies in code comments and examples.",
          "timestamp": "2026-04-21T16:49:33+02:00",
          "tree_id": "7bc3f98e161c456c5449cef1fa3b294a58d82197",
          "url": "https://github.com/diareuse/spike/commit/946c43ae4d6f0fd451729eeb4a6a83792a4f06a9"
        },
        "date": 1776783416011,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3125.55,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 522.691,
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
          "id": "c4a36e93df726c2e37bc9610005e27d3c9c0b607",
          "message": "docs(readme): update usage instructions and add integrations section\n\nUpdates the documentation to reflect current library capabilities and provides a more comprehensive getting started guide.\n\n- Added an **Integrations** section highlighting support for `spike-androidx`, `spike-androidx-compose`, and `spike-ktor`.\n- Updated annotation descriptions to clarify usage of `@Include` on functions and `@Inject` for specific constructor selection.\n- Enhanced the \"Define Your Components\" example to demonstrate:\n    - Interface binding using `bindAs`.\n    - Automatic injection of `Lazy<T>` and `spike.Provider<T>`.\n    - Using `@Include` on functions to provide third-party dependencies.\n- Clarified the requirement of a `companion object` for `@EntryPoint` interfaces to support generated extension factories.",
          "timestamp": "2026-04-21T18:00:52+02:00",
          "tree_id": "f99fb3da9a2adea5a4f397d07afe2f24d6a080f2",
          "url": "https://github.com/diareuse/spike/commit/c4a36e93df726c2e37bc9610005e27d3c9c0b607"
        },
        "date": 1776787712892,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3093.04,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 524.914,
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
          "id": "6ab06b68709c590d3a824c411de0482b9d800a24",
          "message": "fix(deps): bump io.ktor:ktor-server-core from 3.4.2 to 3.4.3\n\nBumps [io.ktor:ktor-server-core](https://github.com/ktorio/ktor) from 3.4.2 to 3.4.3.\n- [Release notes](https://github.com/ktorio/ktor/releases)\n- [Changelog](https://github.com/ktorio/ktor/blob/main/CHANGELOG.md)\n- [Commits](https://github.com/ktorio/ktor/commits)\n\n---\nupdated-dependencies:\n- dependency-name: io.ktor:ktor-server-core\n  dependency-version: 3.4.3\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-22T21:42:56+02:00",
          "tree_id": "294d129cabddf72ddcb2ed1ecf7c1a85fb9ce316",
          "url": "https://github.com/diareuse/spike/commit/6ab06b68709c590d3a824c411de0482b9d800a24"
        },
        "date": 1776887407557,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2970.29,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 472.369,
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
          "id": "5dbda333226057089e91df5bc59d0215120c14e3",
          "message": "chore(deps): bump googleapis/release-please-action from 4 to 5\n\nBumps [googleapis/release-please-action](https://github.com/googleapis/release-please-action) from 4 to 5.\n- [Release notes](https://github.com/googleapis/release-please-action/releases)\n- [Changelog](https://github.com/googleapis/release-please-action/blob/main/CHANGELOG.md)\n- [Commits](https://github.com/googleapis/release-please-action/compare/v4...v5)\n\n---\nupdated-dependencies:\n- dependency-name: googleapis/release-please-action\n  dependency-version: '5'\n  dependency-type: direct:production\n  update-type: version-update:semver-major\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-22T21:43:21+02:00",
          "tree_id": "5771ccaf71ed882463da48920460d526c8214e05",
          "url": "https://github.com/diareuse/spike/commit/5dbda333226057089e91df5bc59d0215120c14e3"
        },
        "date": 1776887458690,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3130.4,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 524.389,
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
          "id": "05e57159c53b0fe499fb7fcf7d1890077cf2451d",
          "message": "fix(deps): bump org.jetbrains.kotlin.multiplatform from 2.3.20 to 2.3.21\n\nBumps [org.jetbrains.kotlin.multiplatform](https://github.com/JetBrains/kotlin) from 2.3.20 to 2.3.21.\n- [Release notes](https://github.com/JetBrains/kotlin/releases)\n- [Changelog](https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md)\n- [Commits](https://github.com/JetBrains/kotlin/compare/v2.3.20...v2.3.21)\n\n---\nupdated-dependencies:\n- dependency-name: org.jetbrains.kotlin.multiplatform\n  dependency-version: 2.3.21\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-23T21:45:13+02:00",
          "tree_id": "3e7293a7388106c284480be6a7d4f222a0a52a10",
          "url": "https://github.com/diareuse/spike/commit/05e57159c53b0fe499fb7fcf7d1890077cf2451d"
        },
        "date": 1776973886776,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2445.3,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 521.246,
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
          "id": "ecd52d9fb13473cbc052a6f6af6b6e97c480a4ec",
          "message": "fix(deps): bump org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin\n\nBumps [org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin](https://github.com/JetBrains/kotlin) from 2.3.20 to 2.3.21.\n- [Release notes](https://github.com/JetBrains/kotlin/releases)\n- [Changelog](https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md)\n- [Commits](https://github.com/JetBrains/kotlin/compare/v2.3.20...v2.3.21)\n\n---\nupdated-dependencies:\n- dependency-name: org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin\n  dependency-version: 2.3.21\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-23T21:44:08+02:00",
          "tree_id": "2b986a78fcb78b9f6a86c3a014e1a53e6e6b2f9d",
          "url": "https://github.com/diareuse/spike/commit/ecd52d9fb13473cbc052a6f6af6b6e97c480a4ec"
        },
        "date": 1776973904409,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3351.36,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 447.269,
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
          "id": "910ede35c28cc11e53576dfd46652f06baa7b4b9",
          "message": "fix(deps): bump org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin\n\nBumps [org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin](https://github.com/JetBrains/kotlin) from 2.3.20 to 2.3.21.\n- [Release notes](https://github.com/JetBrains/kotlin/releases)\n- [Changelog](https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md)\n- [Commits](https://github.com/JetBrains/kotlin/compare/v2.3.20...v2.3.21)\n\n---\nupdated-dependencies:\n- dependency-name: org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin\n  dependency-version: 2.3.21\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-23T21:46:30+02:00",
          "tree_id": "3d378dc5e1d52cec7757cc7ecda5b147d2cc536e",
          "url": "https://github.com/diareuse/spike/commit/910ede35c28cc11e53576dfd46652f06baa7b4b9"
        },
        "date": 1776974033071,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2923.69,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 467.982,
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
          "id": "fa2ebcb52dfacbb52732c542f946acbf93eee6ec",
          "message": "fix(deps): bump com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin\n\nBumps [com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin](https://github.com/google/ksp) from 2.3.6 to 2.3.7.\n- [Release notes](https://github.com/google/ksp/releases)\n- [Commits](https://github.com/google/ksp/compare/2.3.6...2.3.7)\n\n---\nupdated-dependencies:\n- dependency-name: com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin\n  dependency-version: 2.3.7\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-23T21:47:00+02:00",
          "tree_id": "b5e9766377ff240a3c6fd1c67b6d7f1d97e48014",
          "url": "https://github.com/diareuse/spike/commit/fa2ebcb52dfacbb52732c542f946acbf93eee6ec"
        },
        "date": 1776974084281,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3045.95,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 495.639,
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
          "id": "40cdf13ebfe55fbf48c01a828f64492124efb092",
          "message": "fix(deps): bump google-ksp from 2.3.6 to 2.3.7\n\nBumps `google-ksp` from 2.3.6 to 2.3.7.\n\nUpdates `com.google.devtools.ksp:symbol-processing-api` from 2.3.6 to 2.3.7\n- [Release notes](https://github.com/google/ksp/releases)\n- [Commits](https://github.com/google/ksp/compare/2.3.6...2.3.7)\n\nUpdates `com.google.devtools.ksp` from 2.3.6 to 2.3.7\n- [Release notes](https://github.com/google/ksp/releases)\n- [Commits](https://github.com/google/ksp/compare/2.3.6...2.3.7)\n\n---\nupdated-dependencies:\n- dependency-name: com.google.devtools.ksp:symbol-processing-api\n  dependency-version: 2.3.7\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n- dependency-name: com.google.devtools.ksp\n  dependency-version: 2.3.7\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-23T21:52:46+02:00",
          "tree_id": "a3d8397efc8caf17fa34e0153d796d89e97f9fbb",
          "url": "https://github.com/diareuse/spike/commit/40cdf13ebfe55fbf48c01a828f64492124efb092"
        },
        "date": 1776974409693,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2925.37,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 475.301,
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
          "id": "0d02297ea4168338257752e78e52ada88d9d7533",
          "message": "refactor(compiler): clean up code and enable TypeFactory toString\n\n- Uncommented the `toString` implementation in `TypeFactory.Class` to improve debug visibility.\n- Replaced wildcard imports with explicit imports in `DependencyHolderGenerator.kt`.\n- Reformatted parameter lists in `Singleton.kt` for better readability.",
          "timestamp": "2026-04-24T17:11:08+02:00",
          "tree_id": "72562bca9ad1535259bdff8df2a05af02858afb2",
          "url": "https://github.com/diareuse/spike/commit/0d02297ea4168338257752e78e52ada88d9d7533"
        },
        "date": 1777043954951,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3216.77,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 543.951,
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
          "id": "a922e579186f4c26b86892e664f2acf138d39f88",
          "message": "feat(compiler): implement originating file tracking for KSP incremental processing\n\nThis change enables KSP incremental processing by tracking source files throughout the compilation process and attaching them as originating files to generated KotlinPoet specs.\n\n- Add `KSFile` utilities to resolve and require source files from `KSNode`.\n- Refactor `DependencyGraphFactory` into a nested `DependencyGraph.Factory` to streamline graph creation.\n- Update `GraphContext` and `FileGeneratorContext` to collect and propagate `KSFile` references.\n- Decouple `DependencyGraphGenerator` from the KSP environment by using a functional collector for file writing.\n- Implement an `AtomicBoolean` gate in `SpikeSymbolProcessor` to prevent redundant processing rounds.\n- Ensure all generated entry points and factories include originating files for proper incremental build support.",
          "timestamp": "2026-04-24T19:41:36+02:00",
          "tree_id": "81e2dd72cdff5154afcd3b062df1dd57cae1618f",
          "url": "https://github.com/diareuse/spike/commit/a922e579186f4c26b86892e664f2acf138d39f88"
        },
        "date": 1777052934224,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2861.93,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 486.133,
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
          "id": "af797e1cacd7afec7ed818fadea19ef59c350411",
          "message": "chore(master): release 0.0.3",
          "timestamp": "2026-04-24T19:54:42+02:00",
          "tree_id": "2de0e3c199ab51dde51cbc2e79081b1c576ca2aa",
          "url": "https://github.com/diareuse/spike/commit/af797e1cacd7afec7ed818fadea19ef59c350411"
        },
        "date": 1777053724963,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2861.73,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 473.343,
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
          "id": "aa2887ef50bba8b9142a96a701b31aac8ba1da42",
          "message": "docs(readme): add version badges and installation instructions\n\nInclude GitHub version and changelog badges to the README. Added comprehensive installation snippets for both Gradle version catalogs (`libs.versions.toml`) and standard Kotlin DSL build scripts, detailing the available Spike modules.",
          "timestamp": "2026-04-24T20:06:53+02:00",
          "tree_id": "963d1443a5b6d735f338d23a7b6d166b56532159",
          "url": "https://github.com/diareuse/spike/commit/aa2887ef50bba8b9142a96a701b31aac8ba1da42"
        },
        "date": 1777054474905,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2987.46,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 523.975,
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
          "id": "d7d88b13c74bbb4cd02f157efe072f375734bb6f",
          "message": "docs(readme): add version badges and installation instructions\n\nInclude GitHub version and changelog badges to the README. Added comprehensive installation snippets for both Gradle version catalogs (`libs.versions.toml`) and standard Kotlin DSL build scripts, detailing the available Spike modules.",
          "timestamp": "2026-04-24T20:08:00+02:00",
          "tree_id": "abc71ce15d5c6b9190c479cfc5115810aa15cf72",
          "url": "https://github.com/diareuse/spike/commit/d7d88b13c74bbb4cd02f157efe072f375734bb6f"
        },
        "date": 1777054560161,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3008.63,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 531.211,
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
          "id": "8beb1b38247793fb879c9069a75ce12513ccd006",
          "message": "docs(readme): link GitHub Tag badge to releases page",
          "timestamp": "2026-04-24T20:10:21+02:00",
          "tree_id": "4001a165afde775d26ebb0e7c31368491703f0a3",
          "url": "https://github.com/diareuse/spike/commit/8beb1b38247793fb879c9069a75ce12513ccd006"
        },
        "date": 1777054666570,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2931.35,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 517.699,
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
          "id": "7f9af9c089e4d9def7201e63b8b34fe5f3892182",
          "message": "fix(deps): bump org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin\n\nBumps [org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin](https://github.com/JetBrains/kotlin) from 2.3.20 to 2.3.21.\n- [Release notes](https://github.com/JetBrains/kotlin/releases)\n- [Changelog](https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md)\n- [Commits](https://github.com/JetBrains/kotlin/compare/v2.3.20...v2.3.21)\n\n---\nupdated-dependencies:\n- dependency-name: org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin\n  dependency-version: 2.3.21\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-24T21:43:52+02:00",
          "tree_id": "ade086a93c240ea380871b83279575e9273f8639",
          "url": "https://github.com/diareuse/spike/commit/7f9af9c089e4d9def7201e63b8b34fe5f3892182"
        },
        "date": 1777060280047,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2976.02,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 534.439,
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
          "id": "64ec2cda293e9391312119919855fecc8cf36234",
          "message": "chore(master): release 0.0.4",
          "timestamp": "2026-04-27T02:57:50+02:00",
          "tree_id": "affb0cb9fb8de1d7c11ec847bc84c11b80b88e4e",
          "url": "https://github.com/diareuse/spike/commit/64ec2cda293e9391312119919855fecc8cf36234"
        },
        "date": 1777251905241,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3213.4,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 444.839,
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
          "id": "53bea502b52de837f15b227006532d4ce429d212",
          "message": "chore: remove lint",
          "timestamp": "2026-04-27T14:56:07+02:00",
          "tree_id": "0eb9e396f782d0123bd820b73f500b02ef6b25ea",
          "url": "https://github.com/diareuse/spike/commit/53bea502b52de837f15b227006532d4ce429d212"
        },
        "date": 1777295009391,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2950.27,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 502.689,
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
          "id": "026b3d5b43c62709a6ef951b87cd13194f6414d3",
          "message": "chore(repo): add CODEOWNERS file\n\nInitialize the CODEOWNERS file to assign global ownership of the repository to @diareuse.",
          "timestamp": "2026-04-28T07:32:29+02:00",
          "tree_id": "a1e1b71419877c36607382510a61a71d5dd2a320",
          "url": "https://github.com/diareuse/spike/commit/026b3d5b43c62709a6ef951b87cd13194f6414d3"
        },
        "date": 1777354851929,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3167.1,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 451.832,
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
          "id": "341780bf5d8ecc10e77af44cb8c1eac8c1ea79a0",
          "message": "fix(deps): bump dev.detekt:dev.detekt.gradle.plugin\n\nBumps [dev.detekt:dev.detekt.gradle.plugin](https://github.com/detekt/detekt) from 2.0.0-alpha.2 to 2.0.0-alpha.3.\n- [Release notes](https://github.com/detekt/detekt/releases)\n- [Commits](https://github.com/detekt/detekt/compare/v2.0.0-alpha.2...v2.0.0-alpha.3)\n\n---\nupdated-dependencies:\n- dependency-name: dev.detekt:dev.detekt.gradle.plugin\n  dependency-version: 2.0.0-alpha.3\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-28T07:37:02+02:00",
          "tree_id": "10217ead38aeecbfaa53baf598205888f9f87ea5",
          "url": "https://github.com/diareuse/spike/commit/341780bf5d8ecc10e77af44cb8c1eac8c1ea79a0"
        },
        "date": 1777355057865,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3070.16,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 528.047,
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
          "id": "8d07562a921916b75f1e2036c9ca1339c49cde69",
          "message": "test(harness): update KSP output path tracking and test dependencies\n\n- Update KSP generated output path from `build/generated/ksp/main/kotlin` to the base `build/generated/ksp/` directory.\n- Modify the test harness to track `outputFiles` and `fixtures` using relative paths instead of absolute file filtering.\n- Remove the redundant `-PtestClasspath` property from the Gradle test runner arguments.\n- Add `ktor-client-java`, `ktor-server-test`, and the `kotlin-jvm` plugin to the version catalog.",
          "timestamp": "2026-04-28T07:48:34+02:00",
          "tree_id": "6a823a93380a66ca845a7c8954b2bba70f32314c",
          "url": "https://github.com/diareuse/spike/commit/8d07562a921916b75f1e2036c9ca1339c49cde69"
        },
        "date": 1777355787712,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3258.29,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 463.877,
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
          "id": "a272554de3dfd365f522b7b686dfa6481b4302a5",
          "message": "fix(compiler): suppress lint warnings in generated code\n\nAdd `@file:Suppress` annotations to generated Kotlin files to silence warnings for `ClassName`, `RedundantVisibilityModifier`, `UNCHECKED_CAST`, and `unused`. This ensures that code produced by the compiler does not trigger lint or compiler warnings in the consumer project.\n\n- Update `EntryPointGenerator`, `InstructionSetGenerator`, and `DependencyFactoryGenerator` to suppress naming and visibility warnings.\n- Update `DependencyHolderGenerator` to suppress additional unchecked cast and unused member warnings.\n- Refactor string template in `DependencyFactoryGenerator` to use simple variable interpolation.",
          "timestamp": "2026-04-28T10:58:07+02:00",
          "tree_id": "e3cb1c4367e35e2add0ea2f72046db028a411a06",
          "url": "https://github.com/diareuse/spike/commit/a272554de3dfd365f522b7b686dfa6481b4302a5"
        },
        "date": 1777367132234,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 3191.94,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 433.703,
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
          "id": "4477674ef0a5c3cc0228963746bc597a0d65e7b2",
          "message": "chore(master): release 0.0.5",
          "timestamp": "2026-04-28T14:37:56+02:00",
          "tree_id": "d475d401b6eaa4ca8b5ee59d4ec5a97d4fe8a35c",
          "url": "https://github.com/diareuse/spike/commit/4477674ef0a5c3cc0228963746bc597a0d65e7b2"
        },
        "date": 1777380243368,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2307.35,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 431.136,
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
          "id": "e1ce6d751d272759a8dff99ff8baaff8e890efa3",
          "message": "fix(deps): bump gradle-wrapper from 9.4.1 to 9.5.0\n\nBumps [gradle-wrapper](https://github.com/gradle/gradle) from 9.4.1 to 9.5.0.\n- [Release notes](https://github.com/gradle/gradle/releases)\n- [Commits](https://github.com/gradle/gradle/compare/v9.4.1...v9.5.0)\n\n---\nupdated-dependencies:\n- dependency-name: gradle-wrapper\n  dependency-version: 9.5.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-04-29T07:44:13+02:00",
          "tree_id": "e3f4dc088e9fc18d5b5e483f00c050e9d6161b76",
          "url": "https://github.com/diareuse/spike/commit/e1ce6d751d272759a8dff99ff8baaff8e890efa3"
        },
        "date": 1777441917295,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2930.03,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 495.758,
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
          "id": "2ba3293330758f29634389f9ef630b5aea859311",
          "message": "chore(master): release 0.0.6",
          "timestamp": "2026-05-04T03:01:18+02:00",
          "tree_id": "35799885bf506be1a3ccb2a28adf441e5c570afc",
          "url": "https://github.com/diareuse/spike/commit/2ba3293330758f29634389f9ef630b5aea859311"
        },
        "date": 1777856919898,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2712.84,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 472.846,
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
          "id": "6e44f4dea911e343989ab3d1234937221ec74c2f",
          "message": "fix(deps): bump gradle-wrapper from 9.5.0 to 9.5.1\n\nBumps [gradle-wrapper](https://github.com/gradle/gradle) from 9.5.0 to 9.5.1.\n- [Release notes](https://github.com/gradle/gradle/releases)\n- [Commits](https://github.com/gradle/gradle/compare/v9.5.0...v9.5.1)\n\n---\nupdated-dependencies:\n- dependency-name: gradle-wrapper\n  dependency-version: 9.5.1\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-05-13T06:34:14+02:00",
          "tree_id": "9ad677dcf86da7818592df9c48fed00d5d6e5e96",
          "url": "https://github.com/diareuse/spike/commit/6e44f4dea911e343989ab3d1234937221ec74c2f"
        },
        "date": 1778647298628,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2812.3,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 507.542,
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
          "id": "e2706877f94fc1d85c1f7deb3b5c15499effbdc8",
          "message": "fix(deps): bump google-ksp from 2.3.7 to 2.3.8\n\nBumps `google-ksp` from 2.3.7 to 2.3.8.\n\nUpdates `com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin` from 2.3.7 to 2.3.8\n- [Release notes](https://github.com/google/ksp/releases)\n- [Commits](https://github.com/google/ksp/compare/2.3.7...2.3.8)\n\nUpdates `com.google.devtools.ksp:symbol-processing-api` from 2.3.7 to 2.3.8\n- [Release notes](https://github.com/google/ksp/releases)\n- [Commits](https://github.com/google/ksp/compare/2.3.7...2.3.8)\n\nUpdates `com.google.devtools.ksp` from 2.3.7 to 2.3.8\n- [Release notes](https://github.com/google/ksp/releases)\n- [Commits](https://github.com/google/ksp/compare/2.3.7...2.3.8)\n\n---\nupdated-dependencies:\n- dependency-name: com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin\n  dependency-version: 2.3.8\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n- dependency-name: com.google.devtools.ksp:symbol-processing-api\n  dependency-version: 2.3.8\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n- dependency-name: com.google.devtools.ksp\n  dependency-version: 2.3.8\n  dependency-type: direct:production\n  update-type: version-update:semver-patch\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-05-13T22:03:19+02:00",
          "tree_id": "da6ea3c48faa675f769be76c5fe742798a239d03",
          "url": "https://github.com/diareuse/spike/commit/e2706877f94fc1d85c1f7deb3b5c15499effbdc8"
        },
        "date": 1778703052298,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2809.08,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 450.98,
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
          "id": "bd1ed40c3ead6ee43c5a31667e4e1a41eebe5747",
          "message": "fix(deps): bump org.jetbrains.compose:org.jetbrains.compose.gradle.plugin\n\nBumps [org.jetbrains.compose:org.jetbrains.compose.gradle.plugin](https://github.com/JetBrains/compose-multiplatform) from 1.10.3 to 1.11.0.\n- [Release notes](https://github.com/JetBrains/compose-multiplatform/releases)\n- [Changelog](https://github.com/JetBrains/compose-multiplatform/blob/master/CHANGELOG.md)\n- [Commits](https://github.com/JetBrains/compose-multiplatform/compare/v1.10.3...v1.11.0)\n\n---\nupdated-dependencies:\n- dependency-name: org.jetbrains.compose:org.jetbrains.compose.gradle.plugin\n  dependency-version: 1.11.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-05-13T22:03:31+02:00",
          "tree_id": "304ad50c7ae81cc0fcf10f1d598d73b288c420ee",
          "url": "https://github.com/diareuse/spike/commit/bd1ed40c3ead6ee43c5a31667e4e1a41eebe5747"
        },
        "date": 1778703059259,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2749.6,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 468.7,
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
          "id": "fb97fb7f3bacafe5a4bb927f5b16bcbdee7a68dd",
          "message": "fix(deps): bump ktor from 3.4.3 to 3.5.0\n\nBumps `ktor` from 3.4.3 to 3.5.0.\n\nUpdates `io.ktor:ktor-server-core` from 3.4.3 to 3.5.0\n- [Release notes](https://github.com/ktorio/ktor/releases)\n- [Changelog](https://github.com/ktorio/ktor/blob/main/CHANGELOG.md)\n- [Commits](https://github.com/ktorio/ktor/commits)\n\nUpdates `io.ktor:ktor-client-java` from 3.4.3 to 3.5.0\n- [Release notes](https://github.com/ktorio/ktor/releases)\n- [Changelog](https://github.com/ktorio/ktor/blob/main/CHANGELOG.md)\n- [Commits](https://github.com/ktorio/ktor/commits)\n\nUpdates `io.ktor:ktor-server-test-host` from 3.4.3 to 3.5.0\n- [Release notes](https://github.com/ktorio/ktor/releases)\n- [Changelog](https://github.com/ktorio/ktor/blob/main/CHANGELOG.md)\n- [Commits](https://github.com/ktorio/ktor/commits)\n\n---\nupdated-dependencies:\n- dependency-name: io.ktor:ktor-server-core\n  dependency-version: 3.5.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n- dependency-name: io.ktor:ktor-client-java\n  dependency-version: 3.5.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n- dependency-name: io.ktor:ktor-server-test-host\n  dependency-version: 3.5.0\n  dependency-type: direct:production\n  update-type: version-update:semver-minor\n...\n\nSigned-off-by: dependabot[bot] <support@github.com>",
          "timestamp": "2026-05-15T06:29:51+02:00",
          "tree_id": "4dd20fc333c4a1f5643804d763780100d0f222ca",
          "url": "https://github.com/diareuse/spike/commit/fb97fb7f3bacafe5a4bb927f5b16bcbdee7a68dd"
        },
        "date": 1778819838665,
        "tool": "customSmallerIsBetter",
        "benches": [
          {
            "name": "Build Time",
            "value": 2830.15,
            "unit": "ms"
          },
          {
            "name": "Run Time",
            "value": 494.202,
            "unit": "ms"
          }
        ]
      }
    ]
  }
}