

Feature: Workbook dataset to Cloudera on-boarding


  Scenario: A workbook, parsed from an excel spreadsheet, should exist in Cloudera Navigator as metadata.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook
