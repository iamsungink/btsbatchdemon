#!/bin/bash

echo "-------------------------------------------------------------------------"
echo "--                              bts_agree_dcc                          --"
echo "-------------------------------------------------------------------------"

echo '${0} : execFile : ' ${0}
#echo '${1} : args     : ' ${1}
#echo '${2} : args     : ' ${2}

cd /app/arch/billing/bin/ts
bts_agree_dcc

echo "-------------------------------------------------------------------------"
