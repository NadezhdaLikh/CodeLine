#!/usr/bin/env bash
ulimit -s 500
timeout --signal=SIGTERM 10s python3 main.py < input.txt
exit $?



