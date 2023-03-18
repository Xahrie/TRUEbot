package de.zahrie.trues.discord.listener;

import java.util.List;

import de.zahrie.trues.api.Registerer;
import de.zahrie.trues.discord.listener.models.ChannelEvent;
import de.zahrie.trues.discord.listener.models.JoinEvent;
import de.zahrie.trues.discord.listener.models.MemberChangeEvent;
import de.zahrie.trues.discord.listener.models.PermissionChangeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventRegisterer implements Registerer<List<ListenerAdapter>> {
  @Override
  public List<ListenerAdapter> register() {
    return List.of(
        new ChannelEvent(),
        new JoinEvent(),
        new MemberChangeEvent(),
        new PermissionChangeEvent()
    );
  }
}