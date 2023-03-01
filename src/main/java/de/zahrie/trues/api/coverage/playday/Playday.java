package de.zahrie.trues.api.coverage.playday;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import de.zahrie.trues.api.coverage.match.model.Match;
import de.zahrie.trues.api.coverage.stage.model.PlayStage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "coverage_playday", indexes = {
        @Index(name = "idx_playday_2", columnList = "stage, playday_index", unique = true) })
@NamedQuery(name = "Playday.fromStageAndId", query = "FROM Playday WHERE stage = :stage AND idx = :id")
@NamedQuery(name = "Playday.fromStageAndStart", query = "FROM Playday WHERE stage = :stage AND start = :start")
public class Playday implements Serializable {
  @Serial
  private static final long serialVersionUID = -1118100065150854452L;


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coverage_playday_id", columnDefinition = "SMALLINT UNSIGNED not null")
  private int id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "stage", nullable = false)
  @ToString.Exclude
  private PlayStage stage;

  @Column(name = "playday_index", columnDefinition = "TINYINT UNSIGNED not null")
  private short idx;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "playday_start", nullable = false)
  private Calendar start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "playday_end", nullable = false)
  private Calendar end;

  @OneToMany(mappedBy = "matchday")
  @ToString.Exclude
  private Set<Match> matches;

  public Playday(PlayStage stage, short index, Calendar start, Calendar end) {
    this.stage = stage;
    this.idx = index;
    this.start = start;
    this.end = end;
  }

}