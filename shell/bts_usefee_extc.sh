#!/usr/bin/ksh

echo "-------------------------------------------------------------------------"
echo "--                              bts_usefee_extc                        --"
echo "-------------------------------------------------------------------------"

echo '${0} : execFile : ' ${0}
#echo '${1} : args     : ' ${1}
#echo '${2} : args     : ' ${2}

cd /app/arch/billing/bin/ts
bts_usefee_extc

echo "-------------------------------------------------------------------------"
