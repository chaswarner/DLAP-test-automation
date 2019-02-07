## Just verify DB tables created and columns are correct
## Verify additionally in Impala

Feature: Cash Detail dataset on-boarding

  Background : A workbook is on-boarded as a data set using cif-onboarding.

## Check for onboarding process completed successfully and created .complete file
  @Ignore
  Scenario: Check for completed file existance
    Given I have copy the workbook in staging directory
    When onbarding process kicks off
    Then I should see completed file

##Just check that a database exists with the right name. Database name should be
  @Ignore
  Scenario: Curate data set schema validation.
    Given I have parsed a workbook
    When I query Hive for the expected database name
    Then I should see Hive DB columns that match the columns from the workbook


## describe test_curate_fin.test_cash_detail - confirm the columns match what we expect from the workbook. Check Impala instead of Hive
  @Ignore
  Scenario: HBase RowKey validation.
    Given I have parsed a curate workbook
    When I query HBase for the row keys in the data set
    Then I should see expected row keys in hbase

### Test that HBase contains sort key
#
#
###Test that HDFS structure has been created
#  Scenario: SUCCESS folder is created in HDFS path at /env/zone/domain/file
#    Given I have parsed a curate workbook
#    When I query Cloudera Navigator for the existence of HDFS target folder for the parquet files
#    Then I should see the correct path and folder name
