#!/bin/bash

echo "-------------------------------------------------------------------------"
echo "--                              BAT00000_MA                            --"
echo "-------------------------------------------------------------------------"

echo '${0} : execFile : ' ${0}
echo '${1} : args     : ' ${1}

cd /app/arch/btsbatch/data
echo "unzip ${1}"
unzip ${1}

echo "-------------------------------------------------------------------------"
