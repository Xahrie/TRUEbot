package de.zahrie.trues.discord.scheduler.models;

import java.time.LocalDate;

import de.zahrie.trues.api.coverage.match.MatchFactory;
import de.zahrie.trues.api.scheduler.Schedule;
import de.zahrie.trues.api.scheduler.ScheduledTask;
import de.zahrie.trues.discord.notify.NotificationManager;
import de.zahrie.trues.discord.scouting.teaminfo.TeamInfoManager;
import lombok.experimental.ExtensionMethod;
import lombok.extern.java.Log;

@Schedule
@Log
@ExtensionMethod(MatchFactory.class)
public class TeamInfoUpdater extends ScheduledTask {
  @Override
  public void execute() {
    TeamInfoManager.loadAllData();
    if (!NotificationManager.getDay().equals(LocalDate.now())) NotificationManager.create();
    NotificationManager.sendNotifications();
  }

  @Override
  protected String name() {
    return "Teaminfo";
  }
}
