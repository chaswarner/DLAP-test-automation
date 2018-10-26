

Feature: Incremental file loading


Background: Full file has been uploaded with action types as null


  Scenario: Day 1 record are all loaded.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 1 record actions are null.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 2 records have an action type.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed

  Scenario: Day 2 records are updated in Hue SCD.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 2 records are maybe updated properly in Hue Norm.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 2 records are with dflag should not show up in Hue Norm.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 3 record count is correct in Hue SCD.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 3 record count is correct in Hue norm.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 3 records action type is updated.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook

  Scenario: Day 3 records flags are updated correctly.
    Given I have parsed a workbook
    When I query Cloudera Navigator for that workbook's metadata
    Then it should match the parsed workbook