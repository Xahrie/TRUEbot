package de.zahrie.trues.api.coverage.match.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;

import de.zahrie.trues.api.coverage.league.model.League;
import de.zahrie.trues.api.coverage.playday.Playday;
import de.zahrie.trues.api.coverage.stage.Betable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
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
@DiscriminatorValue("prm")
@NamedQuery(name = "PrimeMatch.fromMatchId", query = "FROM PrimeMatch WHERE matchId = :matchId")
public class PrimeMatch extends ScheduleableMatch implements Betable, Serializable {
  @Serial
  private static final long serialVersionUID = -6145053153275706756L;

  @Column(name = "match_id")
  private Integer matchId;

  public PrimeMatch(Playday matchday, Calendar start, League league, Calendar schedulingStart, Calendar schedulingEnd, Integer matchId) {
    super(matchday, start, league, schedulingStart, schedulingEnd);
    this.matchId = matchId;
  }

  public PrimeMatchImpl get() {
    return new PrimeMatchImpl(this);
  }
}