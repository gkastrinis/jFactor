#!/bin/bash
FILTER='\<A test16('
BASE=build/out
rm -rf $BASE && mkdir -p $BASE
if [[ "$(uname -s)" =~ "Darwin" ]]; then
	time souffle -F$BASE/../out_tmp -D$BASE rt/souffle/jfactor.dl
else
	time souffle -j8 -F$BASE/../out_tmp -D$BASE rt/souffle/jfactor.dl
fi
echo -e "\nERRORS: $(cat $BASE/ERROR.csv | wc -l)"
sort -V $BASE/OpcodeExt.csv | grep "$FILTER" > $BASE/OpcodeExt.facts
rm $BASE/OpcodeExt.csv
for f in $BASE/*csv ; do
	if ! [ -s $f ] ; then continue ; fi
	(echo $f ; sort -V $f | grep "$FILTER"; echo) >> $BASE/all.txt
done
