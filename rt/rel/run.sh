#! /usr/bin/env bash

# set -e
# set -x

DB=gka-hackathon
ENGINE="--engine gka-2022-05-19"

rai get-database $DB && rai delete-database $DB
rai create-database $DB

BASE_PATH="$PWD/build/out_tmp"
rai load-csv $DB "$BASE_PATH/bytecode.csv" $ENGINE -r bytecode --schema "idx:int"
rai load-csv $DB "$BASE_PATH/alloc-type.csv" $ENGINE -r alloc_type
rai load-csv $DB "$BASE_PATH/label.csv" $ENGINE -r label --schema "stmt_idx:int"
rai load-csv $DB "$BASE_PATH/var.csv" $ENGINE -r var --schema "var_idx:int"

rai load-model $DB rt/rel/prologue.rel $ENGINE --model "jfactor/prologue"
rai load-model $DB rt/rel/bytecode-semantics.rel $ENGINE --model "jfactor/bytecode-semantics"