#!/bin/bash

rm -rf data/test/10mb.txt
for i in {1..10}
do
	cat data/test/mb.txt >> data/test/10mb.txt
done

