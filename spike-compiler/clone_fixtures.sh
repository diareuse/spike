#!/bin/zsh

replace() {
  local source=$1 target=$2
  [[ -d $source ]] || return
  rm -rf $target
  mkdir -p $target
  cp -r $source/* $target
}

for dir in src/test/resources/*/fixtures(/N); do
  name=${${dir%/fixtures}##*/}
  replace "build/tmp/compilations/$name/ksp/kotlin" "$dir/main/kotlin"
  replace "build/tmp/projects/$name/build/generated/ksp" "$dir"
done
