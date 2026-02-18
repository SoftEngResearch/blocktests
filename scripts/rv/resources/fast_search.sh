#!/bin/bash

while read -r line; do
  [[ -z $line ]] && continue
  IFS="'" read -ra parts <<< "$line"
  method="${parts[1]}"
  package="${parts[3]}"

  temp="${parts[4]}"
  temp="${temp#*(}"
  file="${temp%%)*}"

  temp="${parts[6]}"
  temp="${temp#*(}"
  temp="${temp%%)*}"
  spec="${temp%%:*}"
  spec="${spec%%.aj}"
  spec="${spec%%.java}"
  spec="${spec%MonitorAspect}"
  spec_line="${temp#*:}"

  echo "${package},\"${method}\",${file},${spec},${spec_line}"
done
