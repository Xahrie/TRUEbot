package de.zahrie.trues.api.riot.xayah.types.core.staticdata;

import java.io.Serial;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.ImmutableMap;
import de.zahrie.trues.api.riot.xayah.Orianna;
import de.zahrie.trues.api.riot.xayah.types.common.Platform;
import de.zahrie.trues.api.riot.xayah.types.common.Region;
import de.zahrie.trues.api.riot.xayah.types.common.Season;
import de.zahrie.trues.api.riot.xayah.types.core.GhostObject;

public class Patch extends GhostObject<de.zahrie.trues.api.riot.xayah.types.data.staticdata.Patch> implements Comparable<Patch> {
    public static final class Builder {
        private String name;
        private Platform platform;

        private Builder() {}

        public Patch get() {
            if(platform == null) {
                platform = Orianna.getSettings().getDefaultPlatform();
                if(platform == null) {
                    throw new IllegalStateException(
                        "No platform/region was set! Must either set a default platform/region with Orianna.setDefaultPlatform or Orianna.setDefaultRegion, or include a platform/region with the request!");
                }
            }

            final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object> builder().put("platform", platform);
            if(name != null) {
                builder.put("name", name);
            }

            return Orianna.getSettings().getPipeline().get(Patch.class, builder.build());
        }

        public Builder named(final String name) {
            this.name = name;
            return this;
        }

        public Builder withPlatform(final Platform platform) {
            this.platform = platform;
            return this;
        }

        public Builder withRegion(final Region region) {
            platform = region.getPlatform();
            return this;
        }
    }

    public static final String PATCH_LOAD_GROUP = "patch";
    @Serial
    private static final long serialVersionUID = -3616467806524426695L;

    public static Patch get() {
        return new Builder().get();
    }

    public static Builder named(final String name) {
        return new Builder().named(name);
    }

    public static Builder withPlatform(final Platform platform) {
        return new Builder().withPlatform(platform);
    }

    public static Builder withRegion(final Region region) {
        return new Builder().withRegion(region);
    }

    public Patch(final de.zahrie.trues.api.riot.xayah.types.data.staticdata.Patch coreData) {
        super(coreData, 1);
    }

    @Override
    public int compareTo(final Patch o) {
        return getStartTime().compareTo(o.getStartTime());
    }

    @Override
    public boolean exists() {
        if(coreData.getSeason() == -1) {
            load(PATCH_LOAD_GROUP);
        }
        return coreData.getSeason() != -1;
    }

    public DateTime getEndTime() {
        if(coreData.getEndTime() == null) {
            load(PATCH_LOAD_GROUP);
        }
        return coreData.getEndTime();
    }

    @Override
    protected List<String> getLoadGroups() {
        return List.of(PATCH_LOAD_GROUP);
    }

    public String getName() {
        if(coreData.getName() == null) {
            load(PATCH_LOAD_GROUP);
        }
        return coreData.getName();
    }

    public Platform getPlatform() {
        return Platform.withTag(coreData.getPlatform());
    }

    public Region getRegion() {
        return Platform.withTag(coreData.getPlatform()).getRegion();
    }

    public Season getSeason() {
        if(coreData.getSeason() == -1) {
            load(PATCH_LOAD_GROUP);
        }
        return Season.withId(coreData.getSeason());
    }

    public DateTime getStartTime() {
        if(coreData.getStartTime() == null) {
            load(PATCH_LOAD_GROUP);
        }
        return coreData.getStartTime();
    }

    @Override
    protected void loadCoreData(final String group) {
        final ImmutableMap.Builder<String, Object> builder;
      if (group.equals(PATCH_LOAD_GROUP)) {
        builder = ImmutableMap.builder();
        if (coreData.getName() != null) {
          builder.put("name", coreData.getName());
        }
        if (coreData.getPlatform() != null) {
          builder.put("platform", Platform.withTag(coreData.getPlatform()));
        }
        final de.zahrie.trues.api.riot.xayah.types.data.staticdata.Patch data =
            Orianna.getSettings().getPipeline().get(de.zahrie.trues.api.riot.xayah.types.data.staticdata.Patch.class, builder.build());
        if (data != null) {
          coreData = data;
        }
      }
    }

}
