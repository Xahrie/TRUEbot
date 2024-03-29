package de.zahrie.trues.api.coverage.player;

import de.zahrie.trues.api.coverage.GamesportsLoader;
import de.zahrie.trues.api.coverage.Loader;
import de.zahrie.trues.api.coverage.player.model.PRMPlayer;
import de.zahrie.trues.api.coverage.team.TeamFactory;
import de.zahrie.trues.api.coverage.team.TeamLoader;
import de.zahrie.trues.api.coverage.team.model.PRMTeam;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.util.StringUtils;
import de.zahrie.trues.util.io.log.Console;
import de.zahrie.trues.util.io.request.HTML;
import de.zahrie.trues.util.io.request.URLType;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;

@Getter
@ExtensionMethod(StringUtils.class)
public class PlayerLoader extends GamesportsLoader implements Loader {
  public static int idFromURL(String url) {
    return url.between("/users/", "-").intValue();
  }

  public static PlayerLoader create(int primeId) {
    final PlayerLoader loader = new PlayerLoader(primeId);
    if (loader.html == null || loader.html.text() == null) return null;

    final String attribute = loader.html.find("ul", HTML.ICON_INFO)
        .find("li").find("a").getAttribute("href");
    if (attribute == null) return null;

    final int teamId = TeamLoader.idFromURL(attribute);
    final TeamLoader team = TeamFactory.getTeamLoader(teamId);
    if (team == null) return null;

    final PRMPlayer player = team.getPlayer(primeId);
    player.setTeam(team.getTeam());
    loader.player = player;
    return loader;
  }

  private PRMPlayer player;

  public PlayerLoader(int primeId, String summonerName) {
    super(URLType.PLAYER, primeId);
    this.player = PrimePlayerFactory.getPrimePlayer(primeId, summonerName);
  }

  private PlayerLoader(int primeId) {
    super(URLType.PLAYER, primeId);
  }

  @Override
  public PlayerHandler load() {
    return new PlayerHandler(url, player);
  }

  public void handleLeftTeam() {
    final String attribute = html.find("ul", HTML.ICON_INFO + "-l")
        .find("li").find("a").getAttribute("href");
    if (attribute == null) return;

    final int teamId = TeamLoader.idFromURL(attribute);
    PRMTeam team = TeamFactory.getTeam(teamId);
    if (team == null) {
      new Console("Team " + teamId + " konnte nicht gefunden werden").debug();
      return;
    }

    if (team.getId() == 0) team = new Query<>(PRMTeam.class).where("prm_id", team.getPrmId()).entity();
    player.setTeam(team);
  }
}
