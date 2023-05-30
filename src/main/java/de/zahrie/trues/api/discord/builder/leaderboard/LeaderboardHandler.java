package de.zahrie.trues.api.discord.builder.leaderboard;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.zahrie.trues.util.io.cfg.JSON;
import org.json.JSONArray;
import org.json.JSONObject;

public class LeaderboardHandler {
  private static final Map<PublicLeaderboard, LocalDateTime> leaderboards;

  static {
    final LocalDateTime dateTime = LocalDate.of(2000, Month.JANUARY, 1).atTime(LocalTime.MIN);
    leaderboards = load().stream().collect(HashMap::new, (m, v) -> m.put(v, dateTime), HashMap::putAll);
  }

  private static List<PublicLeaderboard> load() {
    final JSONArray dataArray = JSON.read("leaderboards.json").getJSONArray("data");
    return IntStream.range(0, dataArray.length()).mapToObj(dataArray::getJSONObject).map(PublicLeaderboard::fromJSON).collect(Collectors.toList());
  }

  public static void handleLeaderboards() {
    final LocalDateTime dateTime = LocalDateTime.now();
    leaderboards.forEach((leaderboard, last) -> {
      final int frequency = leaderboard.getCustomQuery().getFrequencyInMinutes();
      if (frequency == 0) return;
      if (Duration.between(last, dateTime).get(ChronoUnit.MINUTES) >= frequency - 1) {
        leaderboard.updateData();
        leaderboards.replace(leaderboard, LocalDateTime.now());
      }
    });
  }

  public static void add(PublicLeaderboard leaderboard) {
    leaderboards.put(leaderboard, LocalDateTime.now());
    write();
  }

  private static void write() {
    final var leaderboardContent = new JSONObject();
    final var data = new JSONArray(leaderboards.keySet().stream().map(PublicLeaderboard::toJSON).toList());
    leaderboardContent.put("data", data);
    JSON.write("leaderboards.json", leaderboardContent.toString());
  }
}
