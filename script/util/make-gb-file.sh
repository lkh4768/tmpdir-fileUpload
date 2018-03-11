#!/bin/bash

rm -rf ../../data/test/gb.txt
for i in {1..1000}
do
	cat ../../data/test/mb.txt >> ../../data/test/gb.txt
done

