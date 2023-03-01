package de.zahrie.trues.api.coverage.player;

import java.io.Serial;
import java.io.Serializable;

import de.zahrie.trues.api.coverage.player.model.PrimePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Lara on 15.02.2023 for TRUEbot
 */
@Getter
@AllArgsConstructor
public class PlayerModel implements Serializable {

  @Serial
  private static final long serialVersionUID = -1890044855143728407L;

  protected String url;
  protected PrimePlayer player;

}