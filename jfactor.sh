#!/bin/bash
BASE=build/out
rm -rf $BASE/*csv $BASE/all.txt
souffle -j4 -F$BASE/../out_tmp -D$BASE logic/jfactor.dl
if [ -s $BASE/ERROR.csv ] ; then echo "Errors encountered!!!" ; cat $BASE/ERROR.csv ; fi
sort -V $BASE/OpcodeExt.csv | grep '\<A test14()V\>' > $BASE/OpcodeExt.facts
rm $BASE/OpcodeExt.csv
for f in $BASE/*csv ; do
  (echo $f ; sort -V $f | grep '\<A test14()V\>'; echo) >> $BASE/all.txt
done
