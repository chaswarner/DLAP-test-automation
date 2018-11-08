## Just verify DB tables created and columns are correct
## Verify additionally in Impala

Feature: Workbook dataset to Cloudera on-boarding

  Background : A workbook is on-boarded as a data set using cif-onboarding.

##Just check that a database exists with the right name. Database name should be
  Scenario: Curate data set db creation.
    Given I have parsed a workbook
    When I query Cloudera Navigator for the expected Hive database
    Then I should see Hive DB created with appropriate name in the correct location

## describe test_curate_fin.test_cash_detail - confirm the columns match what we expect from the workbook. Check Impala instead of Hive
  Scenario: Curate data set schema validation.
    Given I have parsed a curate workbook
    When I query Cloudera Navigator for the list of columns in the data set database
    Then I should see appropriate columns in the appropriate DB available via Impala

## Test that HBase contains sort key


##Test that HDFS structure has been created
Scenario: SUCCESS folder is created in HDFS path at /env/zone/domain/file
    Given I have parsed a curate workbook
    When I query Cloudera Navigator for the existence of HDFS target folder for the parquet files
    Then I should see the correct path and folder name
