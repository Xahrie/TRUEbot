package de.zahrie.trues.api.riot.xayah.types.dto.match;

import java.io.Serial;

import de.zahrie.trues.api.riot.xayah.types.dto.DataObject;

public class ParticipantIdentity extends DataObject {
    @Serial
    private static final long serialVersionUID = -5678421894249739595L;
    private int participantId;
    private Player player;

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ParticipantIdentity other = (ParticipantIdentity)obj;
        if(participantId != other.participantId) {
            return false;
        }
        if(player == null) {
          return other.player == null;
        } else return player.equals(other.player);
    }

    /**
     * @return the participantId
     */
    public int getParticipantId() {
        return participantId;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + participantId;
        result = prime * result + (player == null ? 0 : player.hashCode());
        return result;
    }

    /**
     * @param participantId
     *        the participantId to set
     */
    public void setParticipantId(final int participantId) {
        this.participantId = participantId;
    }

    /**
     * @param player
     *        the player to set
     */
    public void setPlayer(final Player player) {
        this.player = player;
    }
}
