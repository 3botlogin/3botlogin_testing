# 3botlogin_testing
 - The main purpose of this repo is to provide automation testing against [3botlogin app](https://github.com/3botlogin/3botlogin_app)
 - For now this testsuite runs only on Android platform devices. In the future it should run against IOS platforms as well.


### Scenarios Documentation
   Please find all test scenarios [here](https://docs.google.com/spreadsheets/d/1Umpt1-FDxZPiHbWRlavU7tnO_H7GTuUHxJdjiAf4VxE/edit#gid=0).



### Software prerequisites
To be able to run tests, the following softwares are required:

1- Nodejs

2- [Appium](http://appium.io/)

3- Java 8

4- [Maven](https://maven.apache.org/download.cgi)

5- [Android Studio](https://developer.android.com/studio/) (Only if a virtual device is needed). 
   if you will run your tests on a Real Device, no need for android studio or any equivalent software.
##### Versions used during running testsuite: 
 Nodejs (11.9.0) - Appium (1.14.2) -  Maven (3.6.0)
   
### Tests Execution Steps
1- Replace this [apk](src/3botLogin.apk) with the apk version you want to test.

2- In [Config file](src/global.properties):

   - Please provide `email_password`, `appium_main` (main.js path in your system) and `node_bin` (nodejs bin path in your system).
   - `registeredUser` and `accountPhrase` will be generated for you. please don't enter value for these params.
   - Set `device` to "Android Device" if real android device is used. Otherwise use a the virtual device name.

    
3- To run the testsuite: from the command line: `cd 3botlogin_testing`, then `mvn test`

4- Once tests execution is done. please open `3botlogin_testing/target/surefire-reports/emailable-report.html`
   to check the full report of the passed and failed tests with detailed explanation of the errors (if exists).
   A sample can be found [here](https://3botlogin.github.io/3botlogin_testing/testsuite_results/Android/v1.3.0/emailable-report.html).
   
5- Logs can be found in `log/testlogs.log`. This log file will be generated during running testsuite.







