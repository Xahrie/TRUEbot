package de.zahrie.trues.api.coverage.match.log;

import de.zahrie.trues.api.database.connector.Listing;

@Listing(Listing.ListingType.ORDINAL)
public enum EventStatus {
  QUESTION,
  CREATED,
  SCHEDULING_SUGGEST,
  SCHEDULING_CONFIRM,
  LINEUP_SUBMIT,
  HOSTING_REQUEST,
  SCORE_REPORT,
  PLAYED
}
