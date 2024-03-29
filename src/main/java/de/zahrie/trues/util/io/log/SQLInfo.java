package de.zahrie.trues.util.io.log;

import java.time.LocalDateTime;

import de.zahrie.trues.api.discord.user.DiscordUser;
import de.zahrie.trues.api.logging.CustomLog;
import de.zahrie.trues.api.logging.ServerLog;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SQLInfo extends AbstractLog<SQLInfo> {

  public SQLInfo(String message) {
    super(message);
  }

  public SQLInfo doCommand(DiscordUser user, String command, String full) {
    new ServerLog(user, null, full, ServerLog.ServerLogAction.COMMAND).create();
    return this;
  }

  @Override
  protected SQLInfo doLog() {
    if (Level.DATABASE_LOG.contains(level)) new CustomLog(LocalDateTime.now(), toString(), level).create();
    return this;
  }
}
