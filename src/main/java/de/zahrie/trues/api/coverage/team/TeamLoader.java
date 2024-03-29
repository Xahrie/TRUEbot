package de.zahrie.trues.api.coverage.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.zahrie.trues.api.coverage.GamesportsLoader;
import de.zahrie.trues.api.coverage.player.PlayerLoader;
import de.zahrie.trues.api.coverage.player.PrimePlayerFactory;
import de.zahrie.trues.api.coverage.player.model.PRMPlayer;
import de.zahrie.trues.api.coverage.team.model.PRMTeam;
import de.zahrie.trues.api.coverage.team.model.AbstractTeam;
import de.zahrie.trues.util.StringUtils;
import de.zahrie.trues.util.io.request.HTML;
import de.zahrie.trues.util.io.request.URLType;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Lade Team und dessen Teaminfos von primeleague.gg
 */
@Getter
@ExtensionMethod(StringUtils.class)
public class TeamLoader extends GamesportsLoader {
  protected static List<AbstractTeam> loadedTeams = new ArrayList<>();

  public static void reset() {
    loadedTeams.clear();
  }

  public static int idFromURL(String url) {
    return Integer.parseInt(url.between("/teams/", "-"));
  }

  @Nullable
  public static TeamLoader create(int teamId) {
    final TeamLoader teamLoader = new TeamLoader(teamId);
    if (teamLoader.html == null || teamLoader.html.text() == null) return null;

    final String teamTitle = teamLoader.html.find("h1").text();
    final String name = teamTitle.before(" (", -1);
    final String abbreviation = teamTitle.between("(", ")", -1);
    teamLoader.team = TeamFactory.getTeam(teamLoader.id, name, abbreviation);
    return teamLoader;
  }

  private PRMTeam team;

  public TeamLoader(@NotNull PRMTeam team) {
    super(URLType.TEAM, team.getPrmId());
    this.team = team;
  }

  public TeamLoader(int teamId) {
    super(URLType.TEAM, teamId);
  }

  public TeamHandler load() {
    if (html == null || html.text() == null) return null;

    final String teamTitle = html.find("h1").text();
    if (teamTitle != null) {
      team.setName(teamTitle.before(" (", -1));
      team.setAbbreviation(teamTitle.between("(", ")", -1));
    } else System.err.println(getId());

    return TeamHandler.builder()
        .html(html)
        .url(url)
        .team(team)
        .players(getPlayers())
        .build();
  }

  public PRMPlayer getPlayer(int prmId) {
    List<String> teamInfos = html.find("div", HTML.TEAM_HEAD).findAll("li").stream()
        .map(HTML::text).map(str -> str.after(":")).toList();
    teamInfos = teamInfos.subList(3, teamInfos.size());
    if (teamInfos.size() == 4) return null;

    for (HTML user : html.find("ul", HTML.PLAYERS + "-l").findAll("li")) {
      final int primeId = user.find("a").getAttribute("href").between("/users/", "-").intValue();
      if (primeId == prmId) {
        final String summonerName = user.find("div", HTML.DESCRIPTION).find("span").text();
        final PRMPlayer primePlayer = PrimePlayerFactory.getPrimePlayer(primeId, summonerName);
        if (primePlayer != null) primePlayer.setTeam(team);
        return primePlayer;
      }
    }
    return null;
  }

  private List<PRMPlayer> getPlayers() {
    List<String> teamInfos = html.find("div", HTML.TEAM_HEAD).findAll("li").stream()
        .map(HTML::text).map(str -> str.after(":")).toList();
    teamInfos = teamInfos.subList(3, teamInfos.size());
    if (teamInfos.size() == 4) return List.of();

    final var players = new ArrayList<PRMPlayer>();
    for (HTML user : html.find("ul", HTML.PLAYERS + "-l").findAll("li")) {
      final int primeId = user.find("a").getAttribute("href").between("/users/", "-").intValue();
      final String summonerName = user.find("div", HTML.DESCRIPTION).find("span").text();
      final PRMPlayer player = PrimePlayerFactory.getPrimePlayer(primeId, summonerName);
      if (player != null) {
        player.setTeam(team);
        players.add(player);
      }
    }

    team.getPlayers().stream().filter(player -> !players.contains((PRMPlayer) player)).filter(Objects::nonNull).forEach(player -> new PlayerLoader(((PRMPlayer) player).getPrmUserId(), player.getSummonerName()).handleLeftTeam());
    return players;
  }

}
