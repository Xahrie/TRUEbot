package de.zahrie.trues.truebot.models.coverage.season;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("prime")
public class PrimeSeason extends Season implements Serializable {
  @Serial
  private static final long serialVersionUID = 3498814029985658723L;


  @Column(name = "season_id", columnDefinition = "SMALLINT UNSIGNED")
  private int primeId;

}
