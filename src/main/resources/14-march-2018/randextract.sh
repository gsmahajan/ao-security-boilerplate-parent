#!/bin/sh

#configuration MAX_LINES is the number of lines to extract
MAX_LINES="$(shuf -i 20-100 -n 1)"

#number of lines in the file (is a limit)
NUM_LINES=`wc -l $1 | cut -d' ' -f1`

#generate a random number
#in bash the variable $RANDOM returns diferent values on each call
if [ "$RANDOM." != "$RANDOM." ]
then
    #bigger number (0 to 3276732767)
    RAND=$RANDOM$RANDOM
else
    RAND=`date +'%s'`
fi 

#The start line
START_LINE=`expr $RAND % '(' $NUM_LINES - $MAX_LINES ')'`

tail -n +$START_LINE $1 | head -n $MAX_LINES
