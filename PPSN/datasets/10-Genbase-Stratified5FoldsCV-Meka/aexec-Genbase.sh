#!/bin/bash

nt=8

for s in 1 2 3 4 5 6 7 8 9 10
do
for f in 1 2 3 4 5
do
java -Xmx20g -cp Epochx.jar:automeka.jar:lib/* meka.classifiers.multilabel.meta.AutoMEKA -t Genbase.arff -verbosity 6 -E Genbase-train$f.arff -D Genbase-test$f.arff -K 2 -V 5 -P 100 -G 100 -R 5 -M 0.10 -X 0.90 -N $nt -H $s -A Auto-MEKA_Grammar-equal.bnf -Y $f -L 450 -W Genbase-Exp-Equal >> Output-Equal-Genbase_$s'-'$f.txt;
java -Xmx20g -cp Epochx.jar:automeka.jar:lib/* meka.classifiers.multilabel.meta.AutoMEKA -t Genbase.arff -verbosity 6 -E Genbase-train$f.arff -D Genbase-test$f.arff -K 2 -V 5 -P 100 -G 100 -R 5 -M 0.10 -X 0.90 -N $nt -H $s -A Auto-MEKA_Grammar-compact.bnf -Y $f -L 450 -W Genbase-Exp-Compact >> Output-Compact-Genbase_$s'-'$f.txt;
done
done
