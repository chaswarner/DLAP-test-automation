#Feature: Data is moved from the Curate to the Publish zone and is transformed according to onboarded workbook metadata.
#
#  Scenario: Publish scheduling.
#    Given an example raw file is processed all the way to the publish zone and I have an expected output xxx
#    When I select the appropriate table via impala
#    Then the results should match the expected output xxx
#
#
### Duplicates workbooks validation
#  Scenario: Rows in the Publish zone data set should have columns per the workbook.
#    Given a data set in the RW zone and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone
#
#
#  Scenario: Rows in the Publish zone data set should have their columns transformed correctly.
#    Given an ingested data set with expected output xxx
#    When I query Hive/Impala for each row
#    Then the row matches our expected output
#
#
#  Scenario: For publish, a data file should show up in the publish folder in HDFS.
#    Given a data set in the RAW zne and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone'
#
#  Scenario: For publish, a data file should show up in the publish folder in HDFS as the correct file type.
#    Given a data set in the RAW zne and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone'
#
#  Scenario: For publish, a data file should show up in the publish folder in HDFS with the correct partitions.
#    Given a data set in the RAW zne and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone'
#
#  Scenario: For publish, a data file should show up in the publish folder in HDFS.
#    Given a data set in the RAW zne and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone'
#
#  Scenario: For publish, a data file should show up in the publish folder in HDFS with the correct compression   --  see if this needs tested  - parquet in GZIP format.
#    Given a data set in the RAW zne and its counterpart in the curate zone
#    When I query Cloudera Naviator for the data set's onboarded workbook metadata
#    Then fields flagged for encrption in the metadata should be actually encrypted in the curate zone'
#
#
#  Scenario: Curate workbook schema validation.
#    Given I have parsed a workbook
#    When I query Cloudera Navigator for that workbook's metadata
#    Then I should see Hive columns in the appropriate DB