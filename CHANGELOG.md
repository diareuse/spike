# Changelog

## [0.0.2](https://github.com/diareuse/spike/compare/0.0.1...0.0.2) (2026-03-24)


### Bug Fixes

* **deps:** bump org.jetbrains.compose:org.jetbrains.compose.gradle.plugin ([40d1b79](https://github.com/diareuse/spike/commit/40d1b79aa2a72d91b16b1ad609959855487e0e13))
* **deps:** bump org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin ([2220113](https://github.com/diareuse/spike/commit/222011399d3628ac13d776889ae22c61b831cee2))
* **deps:** bump org.jetbrains.kotlin.multiplatform from 2.3.10 to 2.3.20 ([9d78af4](https://github.com/diareuse/spike/commit/9d78af417d34a0d56d90ca0da7e38aeba87843d8))
* **deps:** bump org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin ([ec61e56](https://github.com/diareuse/spike/commit/ec61e56a595983ff14de9b7074aca28f0a18a0f5))
* **deps:** bump org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin ([899bfa3](https://github.com/diareuse/spike/commit/899bfa3307e4811a63d34e2dae80aaeb0dc3971a))

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
