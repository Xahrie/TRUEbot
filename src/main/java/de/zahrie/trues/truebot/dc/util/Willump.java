package de.zahrie.trues.truebot.dc.util;

import de.zahrie.trues.truebot.dc.listener.JoinEvent;
import de.zahrie.trues.truebot.dc.util.cmd.CommandHandler;
import net.dv8tion.jda.api.JDA;

/**
 * Created by Lara on 09.02.2023 for TRUEbot
 */
public class Willump {
  public static JDA client;

  public static void run() {
    Willump.client = new BotConfigurator().run();

    Willump.handleEvents();
    CommandHandler.handleCommands();

    try {
      client.awaitReady();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static void handleEvents() {
    client.addEventListener(
        new CommandHandler(),
        new BotConfigurator(),
        new JoinEvent());
  }

}
