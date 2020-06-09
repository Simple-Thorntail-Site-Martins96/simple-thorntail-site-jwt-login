@echo off
@echo *** Start login module ***
@echo ** Start Backend server **
cd login
start "BACKEND Console" mvn clean thorntail:run -DskipTests
@echo ** Start Frontend server **
cd ../login-fe
start "FORNTEND Console" ng serve
cd ..
@echo *** Starting env triggered, end batch ***