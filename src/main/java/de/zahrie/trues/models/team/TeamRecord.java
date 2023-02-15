package de.zahrie.trues.models.team;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
class TeamRecord implements Serializable {
  private int wins;
  private int losses;

  float getWinrate() {
    return this.wins * 1f / this.losses;
  }
}