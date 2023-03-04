package de.zahrie.trues.api.coverage.match.model;

import java.io.Serial;
import java.io.Serializable;

import de.zahrie.trues.api.coverage.playday.Playday;
import de.zahrie.trues.api.datatypes.calendar.Time;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@DiscriminatorValue("scrimmage")
public class Scrimmage extends Match implements Serializable {
  @Serial
  private static final long serialVersionUID = -5376878014104117438L;

  public Scrimmage(Playday matchday, Time start) {
    super(matchday, start);
  }

}
