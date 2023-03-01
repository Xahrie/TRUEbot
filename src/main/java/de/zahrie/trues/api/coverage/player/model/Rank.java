package de.zahrie.trues.api.coverage.player.model;

import java.io.Serial;
import java.io.Serializable;

import de.zahrie.trues.api.riot.xayah.types.common.Division;
import de.zahrie.trues.api.riot.xayah.types.common.Tier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "player_ranked", indexes = @Index(name = "idx_lplayer", columnList = "player"))
@NamedQuery(name = "Rank.fromPlayer", query = "FROM Rank WHERE player = :player")
public class Rank implements Serializable {
  @Serial
  private static final long serialVersionUID = 7228597517124074472L;
  
  @Id
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player", nullable = false)
  @ToString.Exclude
  private Player player;

  @Column(name = "tier", length = 11, nullable = false)
  @Enumerated(EnumType.STRING)
  private Tier tier;

  @Column(name = "division", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private Division division;

  @Column(name = "points", nullable = false)
  private byte points;

  @Column(name = "wins", columnDefinition = "SMALLINT UNSIGNED")
  private int wins;

  @Column(name = "losses", columnDefinition = "SMALLINT UNSIGNED")
  private int losses;

  @Column(name = "mmr", columnDefinition = "SMALLINT UNSIGNED")
  private int mmr;

  public String getWinrate() {
    if (this.wins > 0) {
      return Math.round(this.wins * 100.0 / (this.wins + this.losses))  + "%";
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Rank && this.player.equals(((Rank) obj).getPlayer());
  }

  Rank(Player player, Tier tier, Division division, byte points, int wins, int losses) {
    this.player = player;
    this.tier = tier;
    this.division = division;
    this.points = points;
    this.wins = wins;
    this.losses = losses;
  }

}