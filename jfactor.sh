#!/bin/bash
BASE=build/out
rm -rf $BASE/*csv $BASE/all.txt
souffle -j4 -F$BASE -D$BASE logic/facts.dl
sort -V -o $BASE/OpcodeExt.csv{,}
for f in $BASE/*csv ; do (echo $f ; sort -V $f | grep '\<A test6()V\>'; echo ; ) >> $BASE/all.txt ; done
