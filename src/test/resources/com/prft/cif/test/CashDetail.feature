#### DLAPDEV-135,
#### CURATE_DAILY_CASH.xlsx

#Feature: Data is moved from the Curate to the Publish zone and is transformed according to on-boarded workbook metadata.
#
#  Background: I have moved a data set to Publish Zone
#
#
#  Scenario: Test fixture of input and expected output files.
#    Given an example raw file is processed all the way to the publish zone and I have an expected output xxx
#    When I select the appropriate table via impala
#    Then the results should match the expected output xxx



#
#
#  Scenario: Rows in the Publish zone data set should have columns per the workbook.
#    Given a data set in the RW zone and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone
#
#
#  Scenario: Rows in the Publish zone data set should .
#    Given a data set in the RAW zne and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone
#
#
#  Scenario: Curate workbook schema validation.
#    Given I have parsed a workbook
#    When I query Cloudera Navigator for that workbook's metadata
#    Then I should see Hive columns in the appropriate DB