#!/bin/sh

set -e

#set -x

root="rootfs-demo"
usr_dir="$root/usr/lib/x86_64-linux-gnu/"
lib64_dir="$root/lib64/"
bin_dir="$root/bin"
proc_dir="$root/proc"

rm -rf $usr_dir
rm -rf $lib64_dir
rm -rf $bin_dir
rm -rf $proc_dir

mkdir -p $usr_dir $lib64_dir $bin_dir $proc_dir

for f in $(ldd /bin/bash /bin/ls /bin/cp /bin/cat | awk '/=>/ {print $3}'); do
	cp -av $f  $usr_dir
done;

for f in $(ldd /bin/bash /bin/ls /bin/cp /bin/cat | awk '/lib64/ {print $1}'); do
	cp -av $f $lib64_dir
done;

for ex in  /bin/bash /bin/ls; do
	cp $ex $bin_dir
done
