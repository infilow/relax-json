#!/usr/bin/env bash

set -e

show_usage() {
  echo "Usage: $(basename "$0") [-h|--help] <binary>
where :
  -h| --help Display this help text
  -b Increase artifact's build-version.
  -j Update jackson release version.
" 1>&2
  exit 1
}

increase_build() {
  eval "mvn build-helper:parse-version versions:set -DnewVersion='\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.incrementalVersion}-\${parsedVersion.nextBuildNumber}' versions:commit"
}

update_jackson() {
  eval "mvn versions:set-property -Dproperty=jackson.version -DnewVersion=${JACKSON_V} versions:commit"
}

if [[ ($# -eq 1) && ($1 == "-b") ]]; then
  increase_build
elif [[ ($# -eq 2) && ($1 == "-j") ]]; then
  JACKSON_V=$2
  update_jackson
elif [[ ($# -ne 2) || ($1 == "--help") || $1 == "-h" ]]; then
  show_usage
fi
