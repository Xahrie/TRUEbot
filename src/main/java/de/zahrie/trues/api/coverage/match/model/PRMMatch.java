package de.zahrie.trues.api.coverage.match.model;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

import de.zahrie.trues.api.coverage.league.model.PRMLeague;
import de.zahrie.trues.api.coverage.match.log.EventStatus;
import de.zahrie.trues.api.coverage.match.log.MatchLog;
import de.zahrie.trues.api.coverage.match.log.MatchLogAction;
import de.zahrie.trues.api.coverage.participator.model.Participator;
import de.zahrie.trues.api.coverage.playday.Playday;
import de.zahrie.trues.api.coverage.playday.config.SchedulingRange;
import de.zahrie.trues.api.database.connector.SQLUtils;
import de.zahrie.trues.api.database.connector.Table;
import de.zahrie.trues.api.database.query.Entity;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.database.query.SQLEnum;
import de.zahrie.trues.api.datatypes.calendar.TimeRange;
import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Table(value = "coverage", department = "prime")
@ExtensionMethod(SQLUtils.class)
public class PRMMatch extends LeagueMatch implements Entity<PRMMatch> {
  @Serial private static final long serialVersionUID = -4791824102421564240L;

  public PRMMatch(@Nullable Playday matchday, @NotNull LocalDateTime start, @NotNull PRMLeague league,
                  @NotNull SchedulingRange schedulingRange, int matchId) {
    this(matchday, MatchFormat.TWO_GAMES, start, (short) 0, EventStatus.CREATED, "keine Infos", true, "-:-", league, league.getMatches().size() + 1, matchId, schedulingRange);
  }

  public PRMMatch(@Nullable Playday playday, @NotNull MatchFormat format, @NotNull LocalDateTime start, Short rateOffset,
                  @NotNull EventStatus status, @NotNull String lastMessage, boolean active, @NotNull String result,
                  @NotNull PRMLeague league, int matchIndex, int matchId, @NotNull TimeRange timeRange) {
    super(playday, format, start, rateOffset, status, lastMessage, active, result, league, matchIndex, matchId, timeRange);
  }

  private PRMMatch(int id, Integer playdayId, MatchFormat format, LocalDateTime start, short rateOffset, EventStatus status,
                   String lastMessage, boolean active, String result, int leagueId, int matchIndex, int matchId, TimeRange timeRange) {
    super(id, playdayId, format, start, rateOffset, status, lastMessage, active, result, leagueId, matchIndex, matchId, timeRange);
  }

  public PrimeMatchImpl get() {
    return new PrimeMatchImpl(this);
  }

  public static PRMMatch get(List<Object> objects) {
    return new PRMMatch(
        (int) objects.get(0),
        objects.get(2).intValue(),
        new SQLEnum<>(MatchFormat.class).of(objects.get(3)),
        (LocalDateTime) objects.get(4),
        objects.get(5).shortValue(),
        new SQLEnum<>(EventStatus.class).of(objects.get(6)),
        (String) objects.get(7),
        (boolean) objects.get(8),
        (String) objects.get(9),
        (int) objects.get(10),
        (int) objects.get(11),
        (int) objects.get(12),
        new TimeRange((LocalDateTime) objects.get(13), (LocalDateTime) objects.get(14))
    );
  }

  @Override
  public PRMMatch create() {
    final PRMMatch match = new Query<>(PRMMatch.class).key("match_id", matchId)
        .col("matchday", playdayId).col("coverage_format", format).col("coverage_start", start).col("rate_offset", rateOffset)
        .col("status", status).col("last_message", lastMessage).col("active", active).col("result", result).col("coverage_group", leagueId)
        .col("coverage_index", matchIndex).col("scheduling_start", range.getStartTime()).col("scheduling_end", range.getEndTime())
        .insert(this);
    if (match.getLogs().stream().noneMatch(log -> log.getAction().equals(MatchLogAction.CREATE))) {
      new MatchLog(this, MatchLogAction.CREATE, "Spiel erstellt", null).create();
    }
    if (match.getParticipators().length == 0) {
      final Participator home = new Participator(match, true).create();
      final Participator guest = new Participator(match, false).create();
      if (home.getId() != 0 && guest.getId() != 0) this.participators = new Participator[]{home, guest};
      else this.participators = null;
    }
    return match;
  }

  @Override
  public String getTypeString() {
    return "Prime League";
  }
}
