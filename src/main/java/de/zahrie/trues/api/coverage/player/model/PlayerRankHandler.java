package de.zahrie.trues.api.coverage.player.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import de.zahrie.trues.api.coverage.player.PlayerHandler;
import de.zahrie.trues.api.coverage.season.PRMSeason;
import de.zahrie.trues.api.coverage.season.Season;
import de.zahrie.trues.api.coverage.season.SeasonFactory;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.datatypes.number.TrueNumber;
import de.zahrie.trues.api.discord.group.DiscordGroup;
import de.zahrie.trues.api.discord.user.DiscordUser;
import de.zahrie.trues.util.io.log.DevInfo;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerRankHandler {
  private final Player player;
  private final List<PlayerRank> ranks;
  public PlayerRankHandler(Player player) {
    this(player, new Query<>(PlayerRank.class).where("player", player).entityList());
  }

  public Rank updateRank(Rank.RankTier tier, Rank.Division division, short points, int wins, int losses) {
    final PlayerRank rank = new PlayerRank(player, tier, Rank.Division.valueOf(division.name()), points, wins, losses).create();
    if (ranks.stream().noneMatch(r -> r.getSeason().equals(rank.getSeason()))) ranks.add(rank);
    if (rank == null || tier.ordinal() <= rank.getRank().tier().ordinal()) return rank.getRank();

    final DiscordUser discordUser = player.getDiscordUser();
    if (discordUser == null) return rank.getRank();

    discordUser.addGroup(DiscordGroup.VIP, LocalDateTime.now(), 7);
    return rank.getRank();
  }

  public List<PlayerRank> getAll() {
    return ranks;
  }

  private PlayerRank current;

  @NonNull
  public PlayerRank getCurrent() {
    if (current == null) this.current = determineCurrent();
    return current;
  }

  @NonNull
  private PlayerRank determineCurrent() {
    final PRMSeason lastSeason = SeasonFactory.getLastPRMSeason();
    if (lastSeason == null) {
      new DevInfo().warn(new NoSuchElementException("Die letzte Season wurde nicht gefunden."));
      return unranked();
    }
    return getRankOf(lastSeason);
  }

  @NonNull
  public PlayerRank getRankOf(Season season) {
    return ranks.stream().filter(r -> r.getSeason().getId() >= season.getId()).max(Comparator.comparing(PlayerRank::getSeasonId)).orElse(unranked());
  }

  @Nullable
  public PlayerRank getLast() {
    return ranks.stream().max(Comparator.naturalOrder()).orElse(null);
  }

  @NonNull
  public PlayerRank getLastRelevant() {
    int maxGames = 0;
    PlayerRank rank = unranked();
    for (PlayerRank playerRank : new ArrayList<>(ranks)) {
      int games = determineGamesOfRank(playerRank);
      if (games >= 50) return determineRankOfFactor(games, playerRank);
      if (maxGames < games) {
        maxGames = games;
        rank = playerRank;
      }
    }
    return determineRankOfFactor(maxGames, rank);
  }

  private PlayerRank determineRankOfFactor(int games, PlayerRank playerRank) {
    final double factor = Math.max(1, games / 50.);
    final int mmr = playerRank.getRank().getMMR();
    final Rank rank = Rank.fromMMR(mmr);
    final TrueNumber winrate = playerRank.getWinrate().getWinrate().rate();
    final int wins = factor == 1 ? playerRank.getWins() : Math.round(winrate.multiply(games).intValue());
    final int losses = factor == 1 ? playerRank.getLosses() : games - wins;
    return new PlayerRank(player, rank, wins, losses);
  }

  private int determineGamesOfRank(PlayerRank playerRank) {
    final LocalDateTime endTime = playerRank.getSeason().getRange().getEndTime();
    final Duration between = Duration.between(endTime, LocalDateTime.now());
    if (between.isPositive()) {
      final int days = (int) Math.round(between.getSeconds() / (3600.0 * 24));
      return playerRank.getWinrate().getGames() - days;
    }
    return playerRank.getWinrate().getGames();
  }

  public void createRank() {
    if (ranks.isEmpty()) new PlayerHandler(null, player).updateElo();
  }

  private PlayerRank unranked() {
    return new PlayerRank(player, Rank.RankTier.UNRANKED, Rank.Division.IV, (short) 0, 0, 0);
  }

  private PlayerRank average() {
    return new PlayerRank(player, Rank.RankTier.SILVER, Rank.Division.I, (byte) 0, 0, 0);
  }
}