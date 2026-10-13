#!/bin/zsh

replace() {
  local source=$1 target=$2
  rm -rf $target
  mkdir -p $target
  cp -r $source/. $target
}

newest() {
  local in_process=$1 gradle=$2
  if [[ -d $in_process && ( ! -d $gradle || $in_process -nt $gradle ) ]]; then
    print $in_process
  elif [[ -d $gradle ]]; then
    print $gradle
  fi
}

for golden in ${(u)$(git ls-files 'src/test/resources/*/fixtures/*' | sed -E 's#(/fixtures(/(app|library))?)/.*#\1#')}; do
  name=${${golden#src/test/resources/}%%/*}
  module=${golden##*/fixtures}
  module=${module#/}
  in_process=build/tmp/compilations/$name${module:+/$module}/ksp/kotlin
  gradle=build/tmp/projects/$name${module:+/$module}/build/generated/ksp
  source=$(newest $in_process $gradle)
  [[ -n $source ]] || continue
  if [[ $source == $in_process ]]; then
    replace $source $golden/main/kotlin
  else
    replace $source $golden
  fi
done
