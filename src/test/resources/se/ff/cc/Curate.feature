#### DLAPDEV-135,
#### CURATE_DAILY_CASH.xlsx

Feature: Data is moved from the RAW to Curate zone and is tranformed according to onboarded workbook metadata

  Background: I have ingested the data set "data set name or xlsx"

  Scenario: Data from the RAW zone should be completely ingested to the curate zone according to the metadata provided by the workbook.
    Given a data set in the RAW zone and its counterpart in the curate zone
    When I query Cloudera Navigator for the data set's onboarded workbook metadata
    Then fields flagged for encryption in the metadata should be actually encrypted in the curate zone

  Scenario: Fields flagged for encryption by the workbook should be encrypted in the target zone.
    Given a data set in the RAW zne and its counterpart in the curate zone
    When I select rows with columns flagged for encryption
    Then fields flagged for encryption in the metdata should be actually encrypted in the curate zone

###   Row counting is problematic - design review needed
  Scenario: The record count in the curate zone data set should match the record count in the RAW file.
    Given a data set in the RAW zne and its counterpart in the curate zone
    When I query Hive for the record count
    Then the record count should match the record count of the file in the RAW zone


  Scenario: Rows in the Curate zone data set should have columns per the workbook.
    Given a data set in the RW zone and its counterpart in the curate zone
    When I query Cloudera Naviator for the data set's onboarded workbook metadata
    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone


  Scenario: Rows in the Curate zone data set should be transformed correctly.
    Given a data set in the RAW zne and its counterpart in the curate zone
    When I query Hive for data rows containing columns that required transform
    Then the data in the row/column should be tranformed correctly




  Scenario: RAW zone file transfer.
    Given I have parsed a workbook
    And I have a datafile
    When I query Cloudera Navigator for that dataset's metadata
    Then I should see the RAW file in the appropriate HDFS folder

  Scenario: Bad data files result in an error being logged.
    Given an bad example raw file is processed all the way to the publish zone and I have an expected output xxx
    When I query Hive for the record count
    Then the record count should match the record count of the file in the RAW zone

  Scenario: Incomplete data files result in an error being logged.
    Given an bad example raw file is processed all the way to the publish zone and I have an expected output xxx
    When I query Hive for the record count
    Then the record count should match the record count of the file in the RAW zone

  Scenario: Invalid data file formats result in an error being logged.
    Given a data file with an invalid file type
    When I attempt to ingest the file
    Then an error should be thrown for the file type

  Scenario: Field-level expression/transformation validation.
    Given I have parsed a workbook and the data has been ingested to the target zone.
    When I query Hive for a field that has been transformed.
    Then I should see the correct tranformed data in the query result.