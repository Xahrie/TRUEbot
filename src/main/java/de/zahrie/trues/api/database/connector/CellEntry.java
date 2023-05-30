package de.zahrie.trues.api.database.connector;

import de.zahrie.trues.api.discord.command.slash.Column;

public record CellEntry(Object entry) {

  @Override
  public String toString() {
    return String.valueOf(this.entry);
  }

  public String round(Column column) {
    return round(column.round());
  }

  public String round(int amount) {
    if (this.entry instanceof Double || this.entry instanceof Float) {
      final double d = (double) this.entry;
      if (d != 0) {
        return String.valueOf(Math.round(d * Math.pow(10, amount)) / Math.pow(10, amount));
      }
    }
    return this.toString();
  }

}
