#!/usr/bin/env bash

rm -rf cockpit/build
rm -rf CockpitCore/build
rm -rf CockpitPlugin/build
rm -rf sample/build
rm -rf build
rm -rf ~/.m2/repository/com/polidea/cockpit/

cd CockpitCore/
./gradlew build
cd ../CockpitPlugin/
./gradlew install
cd ..
echo "include ':cockpit'//, ':sample'" > settings.gradle
./gradlew :cockpit:install
echo "include ':cockpit', ':sample'" > settings.gradle
