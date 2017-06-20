This folder is the root folder of a project based on the ProxiAD Platform.

This file contains instructions for starting with this project. 

_____________________

STARTING INSTRUCTIONS
_____________________

In order to work properly with your new project, you must:

1/ Use the following environment:
   SUN JDK 1.8 (for Eclipse Neon)
   SUN JRE 1.7 (for compilation)
   Maven 3
   Eclipse Neon
   
2/ Setup your Environment variables
   MAVEN_HOME, M2_HOME and JAVA_HOME

3/ Run Eclipse using the workspace of your choice
   Checkout the project from our Git Hub
   Import the maven project in the workspace
   Use the "clean install" maven goal for building the project
   If it fail, try to run install_lib.bat and re-run the maven goal
   
4/ Run your mysql server, create the database whith these two scripts
-	databaseStructureDump.sql
-	setReferentiel.sql
It will create the structure of the database with a default user to connect to the application 

5/ Add a Tomcat 6 server to the workspace and add the web module gpec-web
Start the server, it will open the default browser and display the GPEC home page

6/ Login to the application with the default user and import your data


_____________________

LICENSE
_____________________

OpenSource - BSD







