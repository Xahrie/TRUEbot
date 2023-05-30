package de.zahrie.trues.api.scouting;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.zahrie.trues.api.coverage.player.model.Player;
import de.zahrie.trues.api.coverage.team.model.Team;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.datatypes.collections.SortedList;
import de.zahrie.trues.api.riot.game.Selection;
import de.zahrie.trues.api.riot.performance.Lane;
import de.zahrie.trues.api.riot.performance.Performance;
import de.zahrie.trues.util.StringUtils;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(StringUtils.class)
public abstract class AnalyzeManager {
  private static final Map<PlayerLane, LaneGames> laneExperience = new HashMap<>();

  public static void reset() {
    laneExperience.clear();
  }

  public static void delete(Player player) {
    laneExperience.keySet().removeIf(key -> key.player().equals(player));
  }

  protected static int get(Lane lane, Player player, ScoutingGameType gameType, int days) {
    final PlayerLane playerLane = new PlayerLane(player, lane, gameType, days);
    final LaneGames laneGames = laneExperience.get(playerLane);
    return laneGames == null || laneGames.inserted().isBefore(LocalDateTime.now().minusDays(1)) ? create(playerLane) : laneGames.amount();
  }

  private static int create(PlayerLane playerLane) {
    int lookingFor = 0;
    final List<Object[]> list = playerLane.analyze().performance().get("lane", Lane.class).get("count(performance_id)", Integer.class).groupBy("lane").descending("count(`performance_id`)").list();
    for (Object[] o : list) {
      if (o[0] == null) continue;
      final Lane lane = (o[0].toString()).toEnum(Lane.class);
      if (lane == null || lane.equals(Lane.UNKNOWN)) continue;

      final int amount = ((Long) o[1]).intValue();
      final PlayerLane pl = new PlayerLane(playerLane.player(), lane, playerLane.gameType(), playerLane.days());
      laneExperience.put(pl, new LaneGames(amount, LocalDateTime.now()));
      if (lane.equals(playerLane.lane())) lookingFor = amount;
    }
    return lookingFor;
  }

  protected final Team team;
  protected final List<Player> players;
  protected final ScoutingGameType gameType;
  protected final int days;

  public AnalyzeManager(Team team, List<Player> players) {
    this(team, new SortedList<>(players), ScoutingGameType.TEAM_GAMES, 180);
  }

  public AnalyzeManager(Team team, List<Player> players, ScoutingGameType gameType, int days) {
    this.team = team;
    this.players = new SortedList<>(players);
    this.gameType = gameType;
    this.days = days;
  }

  public abstract Query<Performance> performance();
  public abstract Query<Selection> selection();
  protected abstract Query<Performance> gameTypeString(Query<Performance> query);

  public LocalDateTime getStart() {
    return LocalDateTime.now().minusDays(days);
  }

  public AnalyzeManager with(List<Player> players) {
    this.players.addAll(players);
    return this;
  }

  public record PlayerLane(Player player, Lane lane, ScoutingGameType gameType, int days) {
    public PlayerAnalyzer analyze() {
      return player.analyze(gameType, days);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof PlayerLane that)) return false;
      return days == that.days && player.equals(that.player) && lane == that.lane && gameType == that.gameType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(player, lane, gameType, days);
    }
  }

  public record LaneGames(Integer amount, LocalDateTime inserted) {
  }
}