#! /usr/bin/env bash

# set -e
# set -x

DB=gka-hackathon

rai get-database $DB && rai delete-database $DB
rai create-database $DB

BASE_PATH="/Users/gkastrinis/Work/jFactor/build/out_tmp"
rai load-csv $DB "$BASE_PATH/bytecode.csv" -r bytecode --schema "idx:int"
rai load-csv $DB "$BASE_PATH/alloc-type.csv" -r alloc_type
rai load-csv $DB "$BASE_PATH/label.csv" -r label --schema "stmt_idx:int"
rai load-csv $DB "$BASE_PATH/var.csv" -r var --schema "var_idx:int"

rai load-model $DB rt/rel/prologue.rel --model "jfactor/prologue"