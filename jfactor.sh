#!/bin/bash
BASE=build/out
rm -rf $BASE/*csv
souffle -j4 -F$BASE -D$BASE logic/facts.dl && rm -rf $BASE/all.txt
for f in $BASE/*csv ; do (echo $f ; cat $f ; echo ; ) >> $BASE/all.txt ; done
