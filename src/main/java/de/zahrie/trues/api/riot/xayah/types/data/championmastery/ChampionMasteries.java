package de.zahrie.trues.api.riot.xayah.types.data.championmastery;

import java.io.Serial;

import de.zahrie.trues.api.riot.xayah.types.data.CoreData;

public class ChampionMasteries extends CoreData.ListProxy<ChampionMastery> {
    @Serial
    private static final long serialVersionUID = 3856003080318985663L;
    private String platform, summonerId;

    public ChampionMasteries() {
        super();
    }

    public ChampionMasteries(final int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(!super.equals(obj)) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ChampionMasteries other = (ChampionMasteries)obj;
        if(platform == null) {
            if(other.platform != null) {
                return false;
            }
        } else if(!platform.equals(other.platform)) {
            return false;
        }
        if(summonerId == null) {
          return other.summonerId == null;
        } else return summonerId.equals(other.summonerId);
    }

    /**
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * @return the summonerId
     */
    public String getSummonerId() {
        return summonerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (platform == null ? 0 : platform.hashCode());
        result = prime * result + (summonerId == null ? 0 : summonerId.hashCode());
        return result;
    }

    /**
     * @param platform
     *        the platform to set
     */
    public void setPlatform(final String platform) {
        this.platform = platform;
    }

    /**
     * @param summonerId
     *        the summonerId to set
     */
    public void setSummonerId(final String summonerId) {
        this.summonerId = summonerId;
    }
}
