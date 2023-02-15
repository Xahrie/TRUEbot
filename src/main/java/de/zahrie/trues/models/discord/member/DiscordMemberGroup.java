package de.zahrie.trues.models.discord.member;

import java.io.Serial;
import java.io.Serializable;
import java.util.Calendar;

import de.zahrie.trues.models.discord.DiscordGroup;
import de.zahrie.trues.models.discord.GroupAssignReason;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "discord_user_group")
@IdClass(DiscordMemberGroupId.class)
public class DiscordMemberGroup implements Serializable {
  @Serial
  private static final long serialVersionUID = -763378764697829834L;


  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "discord_user", nullable = false)
  @ToString.Exclude
  private DiscordMember member;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "discord_group", nullable = false)
  @ToString.Exclude
  private DiscordGroup group;

  @Id
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "assign_time", nullable = false)
  private Calendar start;

  @Enumerated(EnumType.STRING)
  @Column(name = "reason", nullable = false, length = 8)
  private GroupAssignReason reason;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "permission_end")
  private Calendar end;

  @Column(name = "active", nullable = false)
  private boolean isActive = false;

}