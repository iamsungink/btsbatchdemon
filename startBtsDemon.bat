@echo off

set driveLabel=V:\btsbatch\
set LibraryDir=lib\

set classpath=%classpath%;%driveLabel%%LibraryDir%ibatis-2.3.0.677.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%commons-logging-1.0.4.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%log4j-1.2.15.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%ojdbc5.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%commons-net-3.0.1.jar;
set classpath=%classpath%;%driveLabel%%LibraryDir%btsbatch.jar;

java kr.co.ktp.bts.demon.DemonSvr