
call mvn install:install-file -DgroupId=com.sun.jdmk -DartifactId=jmxtools -Dversion=1.2 -Dfile=./lib/jmxtools-1.2.jar -Dpackaging=jar

call mvn install:install-file -DgroupId=ftp4j -DartifactId=ftp4j -Dversion=1.5.1 -Dfile=./lib/ftp4j-1.5.1.jar -Dpackaging=jar

call mvn install:install-file -DgroupId=nl.captcha -DartifactId=simpleCaptcha -Dversion=1.2.1 -Dfile=./lib/simpleCaptcha-1.2.1.jar -Dpackaging=jar

call mvn install