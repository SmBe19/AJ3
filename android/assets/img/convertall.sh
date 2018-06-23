#!/bin/sh
cd "$(dirname "$0")"
./convert.sh bg.svg 800 480
./convert.sh map.svg 2048 2048
for plane in a; do
    ./convert.sh airplane_buy_$plane.svg 128 32
    ./convert.sh airplane_map_$plane.svg 128 32
done
for city in 0 1 2 3 4 5 6; do
    ./convert.sh city_$city.svg 64 64
done