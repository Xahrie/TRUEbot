package de.zahrie.trues;

import de.zahrie.trues.api.coverage.team.model.Team;
import de.zahrie.trues.database.Database;
import de.zahrie.trues.database.DatabaseConnector;
import de.zahrie.trues.util.logger.Logger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrimeData {
  private static PrimeData primeData;

  public static PrimeData getInstance() {
    if (primeData == null) {
      primeData = new PrimeData();
      primeData.init();
    }
    return primeData;
  }

  private Team trueTeam;

  public PrimeData() {
    DatabaseConnector.connect();
  }

  private void init() {
    if (this.trueTeam == null) {
      final var logger = Logger.getLogger("Init");
      this.trueTeam = Database.Find.find(Team.class, 142116);
      logger.info("Datenbank geladen");
    }
  }

}
