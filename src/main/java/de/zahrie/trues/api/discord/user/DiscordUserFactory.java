package de.zahrie.trues.api.discord.user;

import de.zahrie.trues.api.community.application.Application;
import de.zahrie.trues.api.community.application.TeamPosition;
import de.zahrie.trues.api.community.application.TeamRole;
import de.zahrie.trues.api.community.member.Membership;
import de.zahrie.trues.api.database.query.Query;
import de.zahrie.trues.api.discord.group.RoleGranter;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;

public class DiscordUserFactory {
  @NonNull
  public static DiscordUser getDiscordUser(@NonNull UserSnowflake member) {
    final long memberId = member.getIdLong();
    var user = new Query<>(DiscordUser.class).where("discord_id", memberId).entity();
    if (user == null) user = createDiscordUser(member);
    if (member instanceof Member m) user.setNickname(m.getEffectiveName());
    return user;
  }

  public static DiscordUser createDiscordUser(UserSnowflake member) {
    return new DiscordUser(member.getIdLong(), member.getAsMention()).create();
  }

  public static Application apply(DiscordUser user, TeamRole role, TeamPosition position, String appNotes) {
    return user.getApplications().create(role, position, appNotes, true);
  }

  public static void addOrgaRole(DiscordUser user, TeamRole role, TeamPosition position) {
    role = (role.equals(TeamRole.TRYOUT)) ? TeamRole.ORGA_TRYOUT : TeamRole.ORGA;
    getMembership(user, role, position);
    new RoleGranter(user).addOrgaRole(role, position);
  }

  public static void getMembership(DiscordUser user, TeamRole role, TeamPosition position) {
    Membership membership = user.getMemberships().stream().filter(msp -> msp.getPosition().equals(position)).findFirst().orElse(null);
    if (membership == null) membership = new Membership(user, position).create();
    membership.updateRoleAndPosition(role, position);
  }
}
