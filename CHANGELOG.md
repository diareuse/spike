# Changelog

## [0.0.4](https://github.com/diareuse/spike/compare/0.0.3...0.0.4) (2026-04-24)


### Bug Fixes

* **deps:** bump org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin ([7f9af9c](https://github.com/diareuse/spike/commit/7f9af9c089e4d9def7201e63b8b34fe5f3892182))

## [0.0.3](https://github.com/diareuse/spike/compare/0.0.2...0.0.3) (2026-04-24)


### Features

* **compiler:** implement originating file tracking for KSP incremental processing ([a922e57](https://github.com/diareuse/spike/commit/a922e579186f4c26b86892e664f2acf138d39f88))
* **compiler:** implement thread-safe singleton support ([709c902](https://github.com/diareuse/spike/commit/709c9022ecd64e5f60c14b56645c7c606db16e54))
* **compose:** add support for navigation extras in `spikeViewModel` ([f9ceff9](https://github.com/diareuse/spike/commit/f9ceff92c535f4f4a721439bb9f6a1e54d5b2bfc))


### Bug Fixes

* **compiler:** handle missing annotations and parameters gracefully ([e077d30](https://github.com/diareuse/spike/commit/e077d300476eccb7b95e06163e3706afc17ac946))
* **deps:** bump com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin ([fa2ebcb](https://github.com/diareuse/spike/commit/fa2ebcb52dfacbb52732c542f946acbf93eee6ec))
* **deps:** bump google-ksp from 2.3.6 to 2.3.7 ([40cdf13](https://github.com/diareuse/spike/commit/40cdf13ebfe55fbf48c01a828f64492124efb092))
* **deps:** bump io.ktor:ktor-server-core from 3.4.2 to 3.4.3 ([6ab06b6](https://github.com/diareuse/spike/commit/6ab06b68709c590d3a824c411de0482b9d800a24))
* **deps:** bump org.jetbrains.kotlin.multiplatform from 2.3.20 to 2.3.21 ([05e5715](https://github.com/diareuse/spike/commit/05e57159c53b0fe499fb7fcf7d1890077cf2451d))
* **deps:** bump org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin ([910ede3](https://github.com/diareuse/spike/commit/910ede35c28cc11e53576dfd46652f06baa7b4b9))
* **deps:** bump org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin ([ecd52d9](https://github.com/diareuse/spike/commit/ecd52d9fb13473cbc052a6f6af6b6e97c480a4ec))

## [0.0.2](https://github.com/diareuse/spike/compare/0.0.1...0.0.2) (2026-04-19)


### Features

* **compiler:** add instruction set generation for MegaGenerator ([32b3bb6](https://github.com/diareuse/spike/commit/32b3bb6d86c32e3d572b9705816fc23de360cb7c))
* **compiler:** add new Generator for advanced factory generation ([010434e](https://github.com/diareuse/spike/commit/010434e208779ffa4ca5f1d053f5c64232a4573d))
* **compiler:** enhance MegaGenerator to support FileSpec output ([a9469af](https://github.com/diareuse/spike/commit/a9469af075284d057a38d10262e3ce27801c8286))


### Bug Fixes

* **compiler:** add logger support to improve diagnostics ([046baf1](https://github.com/diareuse/spike/commit/046baf143c39d2144675f10298dd617725716ed2))
* **compiler:** consolidate dependency tree inversion logic ([c7d9eb6](https://github.com/diareuse/spike/commit/c7d9eb6a68f45e336638b3676617591d1312381f))
* **compiler:** correct dependency id resolution in MegaGenerator ([bcd9ccc](https://github.com/diareuse/spike/commit/bcd9ccc15aab92e7d0afd42d481342e62633ca28))
* **compiler:** deprecate properties handling in MegaGenerator ([7085170](https://github.com/diareuse/spike/commit/7085170c7d723d26854f55d7f75740932abd940b))
* **compiler:** enhance dependency resolution in MegaGenerator ([153437f](https://github.com/diareuse/spike/commit/153437f4153972fc07bf5ed092b65e4605d81abe))
* **compiler:** enhance multibindings handling in MegaGenerator ([69615ee](https://github.com/diareuse/spike/commit/69615ee4c3d88e087059f5dd81fe54b10b919f84))
* **compiler:** enhance parameter handling in type collection ([185b6d5](https://github.com/diareuse/spike/commit/185b6d58f5d58b009e285c6a5929f917608b3e38))
* **compiler:** fixed "Method too large" ([e8e1b31](https://github.com/diareuse/spike/commit/e8e1b31e849f6ac59d80b0643d02dccf51e90552))
* **compiler:** improve batch initialization in MegaGenerator ([e6accd8](https://github.com/diareuse/spike/commit/e6accd8c84363c49c5cb6d0efcbcd3433e41927c))
* **compiler:** improve error handling in multibinding resolution ([96f9bdb](https://github.com/diareuse/spike/commit/96f9bdb6d45449cfb99f01cefed2d8f898beb3de))
* **compiler:** improve generic type handling in MultibindsMap ([7298fd8](https://github.com/diareuse/spike/commit/7298fd8ba85f45acb270c7b415fb25d0c3d908d2))
* **compiler:** improve MegaGenerator class and type handling ([978fe3a](https://github.com/diareuse/spike/commit/978fe3ad8fa1e93f58c9ac8e3c3101c6852dc7cf))
* **compiler:** improve multibinding resolution and checks ([680cb46](https://github.com/diareuse/spike/commit/680cb46d0cb46b19841a6eb012be1b72f7aa4005))
* **compiler:** optimize dependency traversal and type handling ([d4f8ffd](https://github.com/diareuse/spike/commit/d4f8ffda562aa9dcff37396f3d6475ea94a66269))
* **compiler:** refactor memorizes and providers in MegaGenerator ([df9b290](https://github.com/diareuse/spike/commit/df9b2909db416b8cf94c82f5953027bff0ac4ea7))
* **compiler:** refactor MultibindsMap generation logic ([af6b7cf](https://github.com/diareuse/spike/commit/af6b7cf12904456b8dd0e334b6e0fccd5a3a92c8))
* **compiler:** remove unused import statements in DependencyGraphGenerator ([48bc27c](https://github.com/diareuse/spike/commit/48bc27c604dade1978dd082348291eec1fc0d788))
* **compiler:** replace `toClassName` with `toTypeName` in MegaGenerator ([7edaa82](https://github.com/diareuse/spike/commit/7edaa827d0f33ab5845a399e3681ca2d4834e46a))
* **compiler:** resolve bugs in MegaGenerator and test resources ([7513707](https://github.com/diareuse/spike/commit/751370747c86e0f7016069b8e5896ad9a5ec0575))
* **compiler:** simplify dependency resolution in TypeFactory ([a092a42](https://github.com/diareuse/spike/commit/a092a42cc07ffc7fc8aa94cf1b0825c9f835771b))
* **compiler:** update `createDependencyFactory` to use `objectBuilder` ([1a7176f](https://github.com/diareuse/spike/commit/1a7176fbc40482c0d55d4096f52243388a2c980e))
* **compiler:** update entry point generation for factories ([004d1d7](https://github.com/diareuse/spike/commit/004d1d73a83268513a3b6898e14c103e635f8137))
* **deps:** bump androidx-lifecycle from 2.9.6 to 2.10.0 ([0b10648](https://github.com/diareuse/spike/commit/0b1064895a4bc6deba5bbe852279220cb91353bc))
* **deps:** bump gradle-wrapper from 9.4.0 to 9.4.1 ([b976448](https://github.com/diareuse/spike/commit/b976448bdf114546ff3680ab0151bcff85e8d033))
* **deps:** bump io.ktor:ktor-server-core from 3.4.1 to 3.4.2 ([d98c747](https://github.com/diareuse/spike/commit/d98c7476410542af4f64e12f536c1650f94d43da))
* **deps:** bump kotlinpoet from 2.2.0 to 2.3.0 ([6b5d40e](https://github.com/diareuse/spike/commit/6b5d40ebdaefe79d06e658a567565c180f7d5eb8))
* **deps:** bump org.jetbrains.compose:org.jetbrains.compose.gradle.plugin ([40d1b79](https://github.com/diareuse/spike/commit/40d1b79aa2a72d91b16b1ad609959855487e0e13))
* **deps:** bump org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin ([2220113](https://github.com/diareuse/spike/commit/222011399d3628ac13d776889ae22c61b831cee2))
* **deps:** bump org.jetbrains.kotlin.multiplatform from 2.3.10 to 2.3.20 ([9d78af4](https://github.com/diareuse/spike/commit/9d78af417d34a0d56d90ca0da7e38aeba87843d8))
* **deps:** bump org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin ([ec61e56](https://github.com/diareuse/spike/commit/ec61e56a595983ff14de9b7074aca28f0a18a0f5))
* **deps:** bump org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin ([899bfa3](https://github.com/diareuse/spike/commit/899bfa3307e4811a63d34e2dae80aaeb0dc3971a))
* **test:** add annotation support for multibindings ([a67cdf5](https://github.com/diareuse/spike/commit/a67cdf509102110b6dfeab0fc8aafa3cb137e279))

## 0.0.1 (2026-03-06)


### Features

* add support for entry point factories ([2fc144a](https://github.com/diareuse/spike/commit/2fc144af0fdcac79c70bc3bbf231da8937727635))
* **androidx-compose:** add new module for Jetpack Compose ([f4f58ac](https://github.com/diareuse/spike/commit/f4f58acca36a3d1ae2038388da6cd6d367c7fc95))
* **androidx:** add ViewModel support with new module ([0e0c1da](https://github.com/diareuse/spike/commit/0e0c1da7c1be982252d855e60f3247662a3f9aa3))
* **compiler:** add factory accessor for entry point generation ([af0c66f](https://github.com/diareuse/spike/commit/af0c66f5ceb16744cb830c0d4f2d06191a7c64fd))
* **compiler:** add support for variance-aware types ([5ee8265](https://github.com/diareuse/spike/commit/5ee82652c0525cd747bba982e4f8302d6c1dd67e))
* **generator:** add deferred type factories and composition ([1790929](https://github.com/diareuse/spike/commit/179092934939a0c9e7cdde3eb52c7d18442d1bcc))
* **generator:** add support for compositing ([d5fa051](https://github.com/diareuse/spike/commit/d5fa051275b85e2db3f7e87fd7471184ea1f2e6e))
* **generator:** add support for inlining properties ([158ec6e](https://github.com/diareuse/spike/commit/158ec6e45c497c3ec4cca417da483ac23a95e3a8))
* **generator:** add support for internal visibility in generated types ([a41662e](https://github.com/diareuse/spike/commit/a41662e630288333581a68c33fc85cf70f8e5bab))
* **generator:** enhance callable and multibinding support ([fdc0f3b](https://github.com/diareuse/spike/commit/fdc0f3bfe8b454c876794517edd0f2bf3854b85a))
* introduce code generation for dependency containers ([c29bd98](https://github.com/diareuse/spike/commit/c29bd98f3e39563e89ebf167ece6fe7544eff186))
* **ktor:** add new `spike-ktor` module with routing support ([faa0609](https://github.com/diareuse/spike/commit/faa060975e9894d45ce3482c6c381c34c0c9a83a))
* **processor:** introduce multibinding support for dependency graph ([87bbdd7](https://github.com/diareuse/spike/commit/87bbdd7db943239723c9331cc83285f7a12facff))
* **symbol-processor:** add support for qualifiers in dependency graph ([513d417](https://github.com/diareuse/spike/commit/513d417382697e9d0ffec60ee47b2b989cb78794))


### Bug Fixes

* **build:** update build script for ktor project ([20bfc86](https://github.com/diareuse/spike/commit/20bfc86dc775e8be0265d3b49528ae268fae3ed0))
* correct variable naming in `DependencyContainer` implementation ([e7e12fc](https://github.com/diareuse/spike/commit/e7e12fcc29d44e3d49d5768c3b00a5a6e678a149))
* **deps:** bump gradle-wrapper from 9.3.1 to 9.4.0 ([d69bdbb](https://github.com/diareuse/spike/commit/d69bdbbdb0ffe9ef2667b3bb606f05e65e518410))
* **deps:** bump io.ktor:ktor-server-core from 3.4.0 to 3.4.1 ([c2422e2](https://github.com/diareuse/spike/commit/c2422e2a2aff40e8eec23d2689e0cf1ea6fa142c))
* **deps:** bump org.jetbrains.compose:org.jetbrains.compose.gradle.plugin ([6f92da8](https://github.com/diareuse/spike/commit/6f92da8a8b20154f21b40505480375230b22a65e))
* enforce companion object requirement for entry points ([31c3943](https://github.com/diareuse/spike/commit/31c39437af2865967cbdcbfaf1fa2b187cf96c49))
* **generator:** handle parametrized types in multibindings ([c396271](https://github.com/diareuse/spike/commit/c396271bdac7432bec7ca2f76fd73ad0bb1d1faa))
* **graph:** improve error message for missing factory ([63a6a02](https://github.com/diareuse/spike/commit/63a6a02f8f6ab61721e1103da0ab76932680db9f))
* **graph:** prevent duplicate entries in multibind collection ([2150580](https://github.com/diareuse/spike/commit/21505805172a2d7cc93452be0e15128b9aca2397))
* **processor:** enforce marking for multiple constructors ([e3c235b](https://github.com/diareuse/spike/commit/e3c235b9b5319bde7271b89c28e02d9645e03000))
* **processor:** handle virtual factories in entry point generation ([b5c25bc](https://github.com/diareuse/spike/commit/b5c25bca1000f2bc6c879241317dfb423fe584cb))
* replace `TODO` stubs with functional implementations ([ab71586](https://github.com/diareuse/spike/commit/ab715866311ea437472f8feacc64a889f8b55860))
* **spike-core:** change retention to BINARY in `EntryPoint` ([23fbfa1](https://github.com/diareuse/spike/commit/23fbfa1bcd9cc61b1a89b0e2ce7fa7588793f8da))
* **symbol-processor:** handle nullable parent declaration safely ([48cb01a](https://github.com/diareuse/spike/commit/48cb01a681c056ae28d03bee1affee93afdf20bf))
* **test-harness:** ensure Kotlin compilation is verified ([e2a3ef3](https://github.com/diareuse/spike/commit/e2a3ef3615bec141f4c16425e3634ea372dd1649))
* **test-harness:** handle null case for `compileKotlin` assertion ([4331e80](https://github.com/diareuse/spike/commit/4331e80cea2d7fafdb1ebcfff03a0f946b4e61de))
* **test:** add check for missing output files in tests ([c46b772](https://github.com/diareuse/spike/commit/c46b7724fe8b39d1f7de18d326b4d202d0417cb0))
