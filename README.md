Prerequisite: 
1. Java must be installed.
2. Eclipse with testNG plugin.


How to run: (We can also run directly using Pom.xml, But I kept it separately for clear understanding))
1. Download the repository from the git and import to eclipse.
2. Right-click on project -> Maven -> Update project.
3. Open ApiAssignmentTests.java -> Right click -> Run as TestNG (triggers the API tests).
4. Open UIAssignmentTests.java -> Right click -> Run as TestNG (triggers the UI tests).


Observations: The captcha in UI test cannot be automated and one of the best alternatives is to disable the captcha feature in the test environment.


Note: We can improve the tests by:
1. Separating the methods into different pages.
2. Adding necessary logs.
3. By creating testNG.xml file and include into the pom.xml.
4. Capturing the screenshots on failure.
