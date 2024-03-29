package de.zahrie.trues.api.discord.channel;

import de.zahrie.trues.api.database.connector.Database;
import de.zahrie.trues.api.database.connector.Table;
import de.zahrie.trues.api.database.query.Id;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.discord.group.DiscordGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
@Table("discord_channel")
@Log
public abstract class AbstractDiscordChannel implements ADiscordChannel, Id {
  protected int id; // discord_channel_id
  protected final long discordId; // discord_id
  protected final DiscordChannelType channelType; // channel_type
  protected String name; // channel_name
  protected ChannelType permissionType; // permission_type

  public AbstractDiscordChannel(long discordId, String name, ChannelType permissionType, @NotNull DiscordChannelType channelType) {
    this.discordId = discordId;
    this.name = name;
    this.permissionType = permissionType;
    this.channelType = DiscordChannelType.valueOf(channelType.name());
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
    new Query<>(AbstractDiscordChannel.class).col("channel_name", name).update(id);
    Database.connection().commit();
  }

  public void setPermissionType(ChannelType permissionType) {
    this.permissionType = permissionType;
    new Query<>(AbstractDiscordChannel.class).col("permission_type", permissionType).update(id);
  }

  public boolean updatePermission(Role role) {
    final DiscordGroup group = DiscordGroup.of(role);
    if (group == null) return false;
    updateForGroup(group);
    return true;
  }
}
