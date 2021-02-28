#!/bin/bash
BASE=build/out
rm -rf $BASE/*csv $BASE/all.txt
souffle -j4 -F$BASE -D$BASE logic/facts.dl
sort -V $BASE/OpcodeExt.csv | grep '\<A test9_inner2(I)V\>' > $BASE/OpcodeExt.facts && rm $BASE/OpcodeExt.csv
for f in $BASE/*csv ; do (echo $f ; sort -V $f | grep '\<A test9_inner2(I)V\>'; echo ; ) >> $BASE/all.txt ; done
