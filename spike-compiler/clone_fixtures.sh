#!/bin/zsh

main() {
  local source target
  source="build/tmp/$1/build/generated/ksp/main/kotlin"
  target="src/test/resources/$1/fixtures"
  cp -r $source/* $target
}

main $@