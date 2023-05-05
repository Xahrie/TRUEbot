package de.zahrie.trues.api.coverage.match.model;

import java.time.LocalDateTime;

import de.zahrie.trues.api.coverage.league.model.LeagueBase;
import de.zahrie.trues.api.coverage.match.MatchResult;
import de.zahrie.trues.api.coverage.match.log.EventStatus;
import de.zahrie.trues.api.coverage.playday.Playday;
import de.zahrie.trues.api.database.connector.Table;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.datatypes.calendar.TimeRange;
import lombok.Getter;

@Getter
@Table("coverage")
public abstract class LeagueMatch extends Match implements AScheduleable, ATournament {
  protected final LeagueBase league;
  protected final int matchIndex;
  protected final int matchId;
  protected TimeRange range;

  public LeagueMatch(Playday playday, MatchFormat format, LocalDateTime start, short rateOffset, EventStatus status, String lastMessage, boolean active, MatchResult result, LeagueBase league, int matchIndex, Integer matchId, TimeRange timeRange) {
    super(playday, format, start, rateOffset, status, lastMessage, active, result);
    this.league = league;
    this.matchIndex = matchIndex;
    this.matchId = matchId;
    this.range = timeRange;
  }

  @Override
  public void setRange(TimeRange timeRange) {
    if (getRange().getStartTime() != range.getStartTime() || getRange().getEndTime() != timeRange.getEndTime()) {
      new Query<LeagueMatch>().col("scheduling_start", timeRange.getStartTime()).col("scheduling_end", timeRange.getEndTime()).update(id);
    }
    this.range = timeRange;
  }

  @Override
  public String toString() {
    return league.getName() + " - " + getHomeAbbr() + " vs. " + getGuestAbbr();
  }
}