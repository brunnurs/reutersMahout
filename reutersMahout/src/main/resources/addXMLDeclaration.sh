#!/bin/bash
for f in *.sgm; do
	echo "<?xml version=\"1.1\"?>" > tempfile
	cat $f >> tempfile
	mv tempfile $f
done
