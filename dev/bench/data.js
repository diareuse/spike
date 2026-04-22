window.BENCHMARK_DATA = {
  "lastUpdate": 1776887459054,
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
      }
    ]
  }
}