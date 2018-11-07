## Just verify DB tables created and columns are correct
  ## Verify additionally in Impala

Feature: Workbook dataset to Cloudera on-boarding


  Scenario: A workbook, parsed from an excel spreadsheet, should exist in Cloudera Navigator as metadata.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

##Just check that a database exists with the right name
  Scenario: Curate data set db creation.
    Given I have parsed a curate workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then I should see Hive DB created with appropriate name

## describe test_curate_fin.test_cash_detail - confirmt he columns match the workbook.
  Scenario: Publish workbook schema validation.
    Given I have parsed a publish workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then I should see appropriate Hive columns in the appropriate DB


