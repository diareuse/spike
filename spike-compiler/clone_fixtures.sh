#!/bin/zsh

main() {
  local source target
  source="build/tmp/projects/$1/build/generated/ksp/"
  target="src/test/resources/$1/fixtures"
  rm -rf $target/*
  cp -r $source/* $target
}

for dir in src/test/resources/*(/N); do
  main "${dir##*/}"
done