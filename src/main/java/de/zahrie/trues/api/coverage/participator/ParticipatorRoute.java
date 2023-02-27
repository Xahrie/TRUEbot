package de.zahrie.trues.api.coverage.participator;

import de.zahrie.trues.api.coverage.league.model.League;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.ToString;

@Embeddable
public class ParticipatorRoute {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "route_group")
  @ToString.Exclude
  private League routeLeague;

  @Enumerated(EnumType.STRING)
  @Column(name = "route_type", length = 6)
  private RouteType routeType;

  @Column(name = "route_value", columnDefinition = "TINYINT UNSIGNED")
  private Short routeValue;

}
