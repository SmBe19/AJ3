#!/bin/sh

if [ ! -f "$1" ]; then
    echo "File $1 does not exist!"
    echo "Usage: convert.sh file [width] [height]"
    exit 1
fi
[ -z "$2" ] && width=128 || width="$2"
[ -z "$3" ] && height=128 || height="$3"
filename="${1%.*}"
inkscape -z -e ${filename}.png -w $width -h $height ${filename}.svg
