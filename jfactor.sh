#!/bin/bash
BASE=build/out
filter='\<A test17(I)I\>'
rm -rf $BASE && mkdir $BASE
echo "Souffle..."
/usr/bin/time souffle -j4 -F$BASE/../out_tmp -D$BASE logic/jfactor.dl
if [ -s $BASE/ERROR.csv ] ; then echo "Errors encountered!!!" ; cat $BASE/ERROR.csv ; fi
sort -V $BASE/OpcodeExt.csv | grep "$filter" > $BASE/OpcodeExt.facts
rm $BASE/OpcodeExt.csv
for f in $BASE/*csv ; do
  (echo $f ; sort -V $f | grep "$filter"; echo) >> $BASE/all.txt
done
