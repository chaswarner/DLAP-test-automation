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


    Scenario: The record count in the curate zone data set should match the record count in the RAW file.
      Given a data set in the RAW zne and its counterpart in the curate zone
      When I query Hive for the record count
      Then the record count should match the record count of the file in the RAW zone


    Scenario: Rows in the Curate zone data set should have columns per the workbook.
      Given a data set in the RW zone and its counterpart in the curate zone
      When I query Cloudera Naviator for the data set's onboarded workbook metadata
      Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone


    Scenario: Rows in the Curate zone data set should .
      Given a data set in the RAW zne and its counterpart in the curate zone
      When I query Cloudera Naviator for the data set's onboarded workbook metadata
      Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone