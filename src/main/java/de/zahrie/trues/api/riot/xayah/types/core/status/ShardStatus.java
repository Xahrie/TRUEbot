package de.zahrie.trues.api.riot.xayah.types.core.status;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import de.zahrie.trues.api.riot.xayah.Orianna;
import de.zahrie.trues.api.riot.xayah.types.common.Platform;
import de.zahrie.trues.api.riot.xayah.types.common.Region;
import de.zahrie.trues.api.riot.xayah.types.core.GhostObject;

public class ShardStatus extends GhostObject<de.zahrie.trues.api.riot.xayah.types.data.status.ShardStatus> {
    public static final class Builder {
        private Platform platform;

        private Builder() {}

        public ShardStatus get() {
            if(platform == null) {
                platform = Orianna.getSettings().getDefaultPlatform();
                if(platform == null) {
                    throw new IllegalStateException(
                        "No platform/region was set! Must either set a default platform/region with Orianna.setDefaultPlatform or Orianna.setDefaultRegion, or include a platform/region with the request!");
                }
            }

            final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object> builder().put("platform", platform);

            return Orianna.getSettings().getPipeline().get(ShardStatus.class, builder.build());
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

    @Serial
    private static final long serialVersionUID = -7141887712080838849L;
    public static final String SHARD_STATUS_LOAD_GROUP = "shard-status";

    public static ShardStatus get() {
        return new Builder().get();
    }

    public static Builder withPlatform(final Platform platform) {
        return new Builder().withPlatform(platform);
    }

    public static Builder withRegion(final Region region) {
        return new Builder().withRegion(region);
    }

    private final Supplier<List<String>> locales = Suppliers.memoize(() -> {
        load(SHARD_STATUS_LOAD_GROUP);
        if(coreData.getLocales() == null) {
            return null;
        }
        return Collections.unmodifiableList(coreData.getLocales());
    });

    private final Supplier<List<Service>> services = Suppliers.memoize(() -> {
        load(SHARD_STATUS_LOAD_GROUP);
        if(coreData.getServices() == null) {
            return null;
        }
        final List<Service> services = new ArrayList<>(coreData.getServices().size());
        for(final de.zahrie.trues.api.riot.xayah.types.data.status.Service service : coreData.getServices()) {
            services.add(new Service(service));
        }
        return Collections.unmodifiableList(services);
    });

    public ShardStatus(final de.zahrie.trues.api.riot.xayah.types.data.status.ShardStatus coreData) {
        super(coreData, 1);
    }

    @Override
    public boolean exists() {
        if(coreData.getName() == null) {
            load(SHARD_STATUS_LOAD_GROUP);
        }
        return coreData.getName() != null;
    }

    public String getHostname() {
        if(coreData.getHostname() == null) {
            load(SHARD_STATUS_LOAD_GROUP);
        }
        return coreData.getHostname();
    }

    @Override
    protected List<String> getLoadGroups() {
        return List.of(SHARD_STATUS_LOAD_GROUP);
    }

    public List<String> getLocales() {
        return locales.get();
    }

    public String getName() {
        if(coreData.getName() == null) {
            load(SHARD_STATUS_LOAD_GROUP);
        }
        return coreData.getName();
    }

    public Platform getPlatform() {
        return Platform.withTag(coreData.getPlatform());
    }

    public Region getRegion() {
        return Platform.withTag(coreData.getPlatform()).getRegion();
    }

    public String getRegionTag() {
        if(coreData.getRegionTag() == null) {
            load(SHARD_STATUS_LOAD_GROUP);
        }
        return coreData.getRegionTag();
    }

    public List<Service> getServices() {
        return services.get();
    }

    public String getSlug() {
        if(coreData.getSlug() == null) {
            load(SHARD_STATUS_LOAD_GROUP);
        }
        return coreData.getSlug();
    }

    @Override
    protected void loadCoreData(final String group) {
        final ImmutableMap.Builder<String, Object> builder;
      if (group.equals(SHARD_STATUS_LOAD_GROUP)) {
        builder = ImmutableMap.builder();
        if (coreData.getPlatform() != null) {
          builder.put("platform", Platform.withTag(coreData.getPlatform()));
        }
        final de.zahrie.trues.api.riot.xayah.types.data.status.ShardStatus data =
            Orianna.getSettings().getPipeline().get(de.zahrie.trues.api.riot.xayah.types.data.status.ShardStatus.class, builder.build());
        if (data != null) {
          coreData = data;
        }
      }
    }
}
