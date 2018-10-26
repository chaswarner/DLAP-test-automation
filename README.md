# CucumberParallel
 Run Cucumber tests faster by running them in parallel using maven plugins cucumber-jvm-parallel-plugin and maven-failsafe-plugin
Execute in parallel using the command: 
mvn  -Dtest=SomePatternThatDontMatchAnything -DfailIfNoTests=false verify

Tests are currently executed against test tables in dev environment. 
