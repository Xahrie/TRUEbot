package de.zahrie.trues.api.coverage.team.model;

import java.io.Serial;
import java.io.Serializable;

import de.zahrie.trues.api.coverage.league.model.League;
import de.zahrie.trues.api.coverage.league.model.PRMLeague;
import de.zahrie.trues.api.coverage.season.SeasonFactory;
import de.zahrie.trues.api.coverage.team.leagueteam.LeagueTeam;
import de.zahrie.trues.api.database.QueryBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
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
@DiscriminatorValue("1")
public class PRMTeam extends Team implements Serializable {
  @Serial
  private static final long serialVersionUID = 6594410747323472599L;

  @Column(name = "team_id")
  private Integer prmId;

  @Embedded
  private TeamRecord record;

  public PRMTeam(int prmId, String name, String abbreviation) {
    super(name, abbreviation);
    this.setPrmId(prmId);
  }

  public LeagueTeam getCurrentLeague() {
    return QueryBuilder.hql(LeagueTeam.class,
        "FROM LeagueTeam WHERE team = " + this + " AND league.stage.season = " + SeasonFactory.getLastPRMSeason() +
            " ORDER BY league.stage.start desc").list().stream().findFirst().orElse(null);
  }

  public PRMLeague getLastLeague() {
    return QueryBuilder.hql(League.class,
            "SELECT league FROM LeagueTeam WHERE team = " + this + " ORDER BY league.stage.start desc").list().stream()
        .filter(l -> l instanceof PRMLeague).map(l -> (PRMLeague) l).findFirst().orElse(null);
  }

  public void setScore(League division, String score) {
    final String place = score.split("\\.")[0];
    final short placeInteger = Short.parseShort(place);
    final String wins = score.split("\\(")[1].split("/")[0];
    final short winsInteger = Short.parseShort(wins);
    final String losses = score.split("/")[1].split("\\)")[0];
    final short lossesInteger = Short.parseShort(losses);
    final TeamScore teamScore = new TeamScore(division, placeInteger, winsInteger, lossesInteger);
    getCurrentLeague().setScore(teamScore);
  }

  public void setRecord(String record, short seasons) {
    final String wins = record.split(" / ")[0];
    final short winsInteger = Short.parseShort(wins);
    final String losses = record.split(" / ")[1];
    final short lossesInteger = Short.parseShort(losses);
    this.record = new TeamRecord(seasons, winsInteger, lossesInteger);
  }
}