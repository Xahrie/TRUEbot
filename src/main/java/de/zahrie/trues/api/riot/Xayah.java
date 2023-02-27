package de.zahrie.trues.api.riot;

import de.zahrie.trues.api.riot.xayah.Orianna;
import de.zahrie.trues.util.io.cfg.JSON;

/**
 * Created by Lara on 09.02.2023 for TRUEbot
 */
public final class Xayah extends Orianna {

  public static void run() {
    Xayah.loadConfiguration("riotcfg.json");
    var json = JSON.fromFile("connect.json");
    Xayah.setRiotAPIKey(json.getString("riot"));
  }

}
