

#Feature: Incremental file loading incremental testing of scd flag update for views
## Count of columns should be # + 4 (timestamp, cflag dflag, feeddate) for scd table
## verify that timestamps get updated as they want feed date - date that the extract came and date that something actually changed
## RME_Incremental_Test_Cases_2nd_Iteration.xlsx

#Background: Full file has been uploaded with action types as null

#
#  Scenario: Day 1 record are all loaded.
#    Given Day 1 RME incremental files are ingested
#    When I query Hive SCD table
#    Then the record count should be correct
#
#  Scenario: Day 1 record actions are null.
#    Given Day 1 RME incremental files are ingested
#    When I query Hive SCD table
#    Then all record actions should be null
#
#  Scenario: Day 2 records have an action type.
#    Given Day 2 RME incremental files are ingested after Day 1 files
#    When I query Hive SCD table for the records
#    Then all records should have an action
#
#  Scenario: Day 2 records are updated in Hue SCD.
#    Given Day 2 RME incremental files are ingested after Day 1 files
#    When I query Hive SCD table for the records
#    Then there should be new records in addition to old records
#
#  Scenario: Day 2 records are maybe updated properly in Hue Norm.
#    Given Day 2 RME incremental files are ingested after Day 1 files
#    When I query Hive SCD table for the records
#    Then the record should be updated in Hue Norm table
#
#  Scenario: Day 2 records are with dflag should not show up in Hue Norm.
#    Given Day 2 RME incremental files are ingested after Day 1 files
#    When I query Hive SCD table for the records
#    Then the record should be updated in Hue Norm table
#
#  Scenario: Day 3 record count is correct in Hue SCD.
#    Given Day 3 RME incremental files are ingested after Day 2 ingestion
#    When I query Hive SCD table for the records
#    Then the record should be updated in Hue Norm table
#
#  Scenario: Day 3 record count is correct in Hue norm.
#    Given Day 3 RME incremental files are ingested after Day 2 ingestion
#    When I query Hive SCD table for the records
#    Then the record count should not change in Hue Norm table
#
#  Scenario: Day 3 records action type is updated.
#    Given Day 3 RME incremental files are ingested after Day 2 ingestion
#    When I query Hive SCD table for the records
#    Then the record should have their action type updated
#
#  Scenario: Day 3 records flags are updated correctly.
#    Given Day 3 RME incremental files are ingested after Day 2 ingestion
#    When I query Hive SCD table for the records
#    Then the record should be updated in Hue Norm table