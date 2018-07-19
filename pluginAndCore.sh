#!/bin/sh

usage() {
    echo "usage: [[-b | --build] | [-r | --rebuild] | [-c | --clean]]"
}

perform() {
    cd CockpitCore
    echo ./gradlew $1
    ./gradlew $1
    echo
    cd ..

    cd CockpitPlugin
    echo ./gradlew $1
    ./gradlew $1
    echo
    cd ..
}

# https://stackoverflow.com/a/16349776/4137318
cd "${0%/*}"

# http://linuxcommand.org/lc3_wss0120.php
case $1 in
    -b | --build )      perform build
                        exit
                        ;;
    -c | --clean )      perform clean
                        exit
                        ;;
    -r | --rebuild )    perform clean
                        perform build
                        exit
                        ;;
    * )                 usage
                        exit 1
esac