#### DLAPDEV-135,
#### CURATE_DAILY_CASH.xlsx

  Feature: PHI Encryption

    Background: I have ingested the data set "data set name or xlsx"

     Scenario: Fields flagged for encryption by the workbook should be encrypted in the target zone.
       Given a data set in the RAW zone and its counterpart in the curate zone
       When I query Cloudera Navigator for the data set's onboarded workbook metadata
       Then fields flagged for encryption in the metadata should be actually encrypted in the curate zone

     Scenario: Data from the RAW zone should be completely ingested to the curate zone according to the metadata provided by the workbook.
       Given a data set in the RAW zone and its counterpart in the curate zone
       When I query Cloudera Navigator for the data set's onboarded workbook metadata
       Then fields flagged for encryption in the metadata should be actually encrypted in the curate zone
