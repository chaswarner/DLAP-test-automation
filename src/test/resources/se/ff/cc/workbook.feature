## Just verify DB tables created and columns are correct
  ## Verify additionally in Impala

Feature: Workbook dataset to Cloudera on-boarding

  Background : A workbook is on-boarded as a data set using cif-onboarding.

  Scenario: A workbook, parsed from an excel spreadsheet, should exist in Cloudera Navigator as metadata.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

##Just check that a database exists with the right name
  Scenario: Curate data set db creation.
    Given I have parsed a curate workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then I should see Hive DB created with appropriate name

## describe test_curate_fin.test_cash_detail - confirm the columns match the workbook.
  Scenario: Publish workbook schema validation.
    Given I have parsed a publish workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then I should see appropriate Hive columns in the appropriate DB

## Test that the data is available in Impala as well.
  Scenario: On-boarded dataset is available in Impala as well.
    Given I have parsed a workbook
    When I query Impala for the dataset - or Cloudera Navigator for Impala datasets
    Then I should see the dataset with the right name and columns

