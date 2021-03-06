#!/bin/bash
BASE=build/out
rm -rf $BASE/*csv $BASE/all.txt
souffle -j4 -F$BASE/../out_tmp -D$BASE logic/jfactor.dl
sort -V $BASE/OpcodeExt.csv | grep '\<A test12(I)V\>' > $BASE/OpcodeExt.facts && rm $BASE/OpcodeExt.csv
for f in $BASE/*csv ; do (echo $f ; sort -V $f | grep '\<A test12(I)V\>'; echo ; ) >> $BASE/all.txt ; done
