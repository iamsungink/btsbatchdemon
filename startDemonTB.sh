#!/bin/bash

export driveLabel=/app/arch/btsbatch/
export LibraryDir=lib/
export ConfDir=conf/

export CLASSPATH=.:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}ibatis-2.3.0.677.jar:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}commons-logging-1.0.4.jar:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}log4j-1.2.15.jar:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}ojdbc8.jar:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}commons-net-3.0.1.jar:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}json-simple-1.1.1.jar:
export CLASSPATH=${CLASSPATH}${driveLabel}${ConfDir}:
export CLASSPATH=${CLASSPATH}${driveLabel}${LibraryDir}btsbatch.jar:

java -Dgubun=prod kr.co.ktp.bts.demon.DemonProcessTB
