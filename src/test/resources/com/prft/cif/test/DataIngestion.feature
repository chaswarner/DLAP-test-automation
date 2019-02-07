Feature: first round of data ingestion testing

  Background : Data ingestion check.

  @DataTest
  Scenario: check for Hive table populated with data
    Given I have copy the data file in HDFS directory
    When Inotify process kicks off
    Then I should see data in hive table
