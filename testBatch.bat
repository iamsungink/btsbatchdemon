@echo off

set driveLabel=C:\tss_batch\
set LibraryDir=lib\

set classpath=%classpath%;%driveLabel%%LibraryDir%ibatis-2.3.0.677.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%commons-logging-1.0.4.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%log4j-1.2.15.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%ojdbc5.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%commons-net-3.0.1.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%tssbatch.jar;

java kr.co.skmns.tss.batch.BatchProcessManager BAT00000_IA IA 20110827 132519 00000 DEMON c:/tss_batch/data ML01.SKMC.20110826_01.dat c:/tss_batch/data_back/IA

