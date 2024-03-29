package de.zahrie.trues.discord.command.models;

import de.zahrie.trues.api.coverage.team.TeamFactory;
import de.zahrie.trues.api.coverage.team.model.PRMTeam;
import de.zahrie.trues.util.StringUtils;
import de.zahrie.trues.api.discord.command.slash.SlashCommand;
import de.zahrie.trues.api.discord.command.slash.annotations.Command;
import de.zahrie.trues.api.discord.command.slash.annotations.Msg;
import de.zahrie.trues.api.discord.command.slash.annotations.Option;
import de.zahrie.trues.api.discord.command.slash.annotations.Perm;
import de.zahrie.trues.api.discord.group.PermissionRole;
import lombok.experimental.ExtensionMethod;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@Command(name = "follow", descripion = "Team verfolgen", perm = @Perm(PermissionRole.MANAGEMENT),
    options = @Option(name = "id", description = "PRM TeamId", type = OptionType.INTEGER))
@ExtensionMethod(StringUtils.class)
public class TeamFollowCommand extends SlashCommand {
  @Override
  @Msg(value = "**{}** wird verfolgt.", error = "**{}** wird nicht mehr verfolgt.")
  public boolean execute(SlashCommandInteractionEvent event) {
    final Integer teamId = find("id").integer();
    final PRMTeam team = TeamFactory.getTeam(teamId);
    if (team != null) return send(team.highlight(), team.getName());
    return reply("Das Team wurde nicht gefunden");
  }
}
