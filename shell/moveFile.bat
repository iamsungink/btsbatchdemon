echo "-------------------------------------------------------------------------"
echo "--                              File Move                              --"
echo "-------------------------------------------------------------------------"

echo '%1 : File sourceFile : ' %1
echo '%2 : File targetFile : ' %2

copy %1 %2
echo "copy %1 %2"
del %1
echo "del %1"
echo "-------------------------------------------------------------------------"
