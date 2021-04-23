#!/bin/bash
BASE=build/out
filter='\<A test6('
rm -rf $BASE && mkdir $BASE
echo "Souffle..."
time souffle -j4 -F$BASE/../out_tmp -D$BASE logic/jfactor.dl
if [ -s $BASE/ERROR.csv ] ; then echo -e "\nErrors encountered!!!" ; fi
sort -V $BASE/OpcodeExt.csv | grep "$filter" > $BASE/OpcodeExt.facts
rm $BASE/OpcodeExt.csv
for f in $BASE/*csv ; do
  if ! [ -s $f ] ; then continue ; fi
  (echo $f ; sort -V $f | grep "$filter"; echo) >> $BASE/all.txt
done