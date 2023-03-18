package de.zahrie.trues.api.coverage.team.model;

import java.io.Serial;
import java.io.Serializable;

import de.zahrie.trues.util.Format;
import de.zahrie.trues.api.datatypes.number.TrueNumber;

public record Standing(int wins, int losses) implements Serializable {
  @Serial
  private static final long serialVersionUID = -8830455150033695496L;

  public Winrate getWinrate() {
    return new Winrate(new TrueNumber((double) this.wins).divide(this.losses));
  }

  @Override
  public String toString() {
    return wins + " : " + losses;
  }

  public String format(Format format) {
    if (format.equals(Format.LONG)) {
      return wins + " : " + losses;
    }

    if (format.equals(Format.SHORT)) {
      return wins + ":" + losses;
    }

    if (format.equals(Format.ADDITIONAL)) {
      return wins + ":" + losses + " (" + getWinrate() + ")";
    }
    return null;
  }

  public record Winrate(TrueNumber rate) {
    @Override
    public String toString() {
      return rate.percentValue();
    }

  }
}