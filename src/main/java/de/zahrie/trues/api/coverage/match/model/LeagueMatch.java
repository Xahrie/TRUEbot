package de.zahrie.trues.api.coverage.match.model;

import java.time.LocalDateTime;
import java.util.Objects;

import de.zahrie.trues.api.coverage.league.model.AbstractLeague;
import de.zahrie.trues.api.coverage.match.log.EventStatus;
import de.zahrie.trues.api.coverage.playday.Playday;
import de.zahrie.trues.api.database.connector.Table;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.datatypes.calendar.TimeRange;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Table("coverage")
public abstract class LeagueMatch extends Match implements AScheduleable, ATournament {
  protected final Integer leagueId;
  protected final int matchIndex;
  protected final int matchId;
  protected TimeRange range;

  public LeagueMatch(@Nullable Playday playday, @NotNull MatchFormat format, @NotNull LocalDateTime start, Short rateOffset,
                     @NotNull EventStatus status, @NotNull String lastMessage, boolean active, @NotNull String result,
                     @NotNull AbstractLeague league, int matchIndex, @NotNull Integer matchId, @NotNull TimeRange timeRange) {
    super(playday, format, start, rateOffset, status, lastMessage, active, result);
    this.league = league;
    this.leagueId = league.getId();
    this.matchIndex = matchIndex;
    this.matchId = matchId;
    this.range = timeRange;
  }

  protected LeagueMatch(int id, Integer playdayId, MatchFormat format, LocalDateTime start, short rateOffset, EventStatus status,
                        String lastMessage, boolean active, String result, int leagueId, int matchIndex, Integer matchId,
                        TimeRange timeRange) {
    super(id, playdayId, format, start, rateOffset, status, lastMessage, active, result);
    this.leagueId = leagueId;
    this.matchIndex = matchIndex;
    this.matchId = matchId;
    this.range = timeRange;
  }

  @Override
  public void setRange(TimeRange timeRange) {
    if (getRange().getStartTime() != range.getStartTime() || getRange().getEndTime() != timeRange.getEndTime()) {
      new Query<>(LeagueMatch.class).col("scheduling_start", timeRange.getStartTime()).col("scheduling_end", timeRange.getEndTime()).update(id);
    }
    this.range = timeRange;
  }

  @Override
  public String toString() {
    return getLeague().getName() + " - " + getHomeAbbr() + " vs. " + getGuestAbbr();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof final LeagueMatch that)) return false;
    if (!super.equals(o)) return false;
    return getMatchId() == that.getMatchId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getMatchId());
  }

  private AbstractLeague league;

  public AbstractLeague getLeague() {
    if (league == null) this.league = new Query<>(AbstractLeague.class).entity(leagueId);
    return league;
  }
}
