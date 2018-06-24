#!/bin/sh
cd "$(dirname "$0")"
./convert.sh bg.svg 800 480
./convert.sh map.svg 2048 2048

for plane in a b c d; do
    ./convert.sh airplane_buy_$plane.svg 128 32
    ./convert.sh airplane_map_$plane.svg 128 64
done
for city in 0 1 2 3 4 5 6; do
    ./convert.sh city_$city.svg 64 64
done

./convert.sh window.svg 64 64
./convert.sh button_depressed.svg 64 64
./convert.sh button_pressed.svg 64 64
./convert.sh button_disabled.svg 64 64
./convert.sh add.svg 64 64
./convert.sh remove.svg 64 64
./convert.sh close.svg 64 64
./convert.sh level_up.svg 64 64
./convert.sh buy_airplane.svg 64 64
./convert.sh list_airplane.svg 64 64

./convert.sh wait_empty.svg 64 64
./convert.sh wait_full.svg 64 64
./convert.sh wait_time_plus.svg 64 64
./convert.sh wait_time_minus.svg 64 64
./convert.sh wait_passenger_plus.svg 64 64
./convert.sh wait_passenger_minus.svg 64 64
