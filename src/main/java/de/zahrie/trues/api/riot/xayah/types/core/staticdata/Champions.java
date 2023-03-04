package de.zahrie.trues.api.riot.xayah.types.core.staticdata;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.merakianalytics.datapipelines.iterators.CloseableIterator;
import com.merakianalytics.datapipelines.iterators.CloseableIterators;
import de.zahrie.trues.api.riot.xayah.Orianna;
import de.zahrie.trues.api.riot.xayah.types.common.Platform;
import de.zahrie.trues.api.riot.xayah.types.common.Region;
import de.zahrie.trues.api.riot.xayah.types.core.GhostObject;
import de.zahrie.trues.api.riot.xayah.types.core.searchable.SearchableList;
import de.zahrie.trues.api.riot.xayah.types.core.searchable.SearchableLists;

public class Champions extends GhostObject.ListProxy<de.zahrie.trues.api.riot.xayah.types.core.staticdata.Champion, de.zahrie.trues.api.riot.xayah.types.data.staticdata.Champion, de.zahrie.trues.api.riot.xayah.types.data.staticdata.Champions> {
    public static final class Builder {
        private Set<String> includedData;
        private Platform platform;
        private String version, locale;

        private Builder() {}

        public Champions get() {
            if(platform == null) {
                platform = Orianna.getSettings().getDefaultPlatform();
                if(platform == null) {
                    throw new IllegalStateException(
                        "No platform/region was set! Must either set a default platform/region with Orianna.setDefaultPlatform or Orianna.setDefaultRegion, or include a platform/region with the request!");
                }
            }

            if(version == null) {
                version = Orianna.getSettings().getCurrentVersion(platform);
            }

            if(locale == null) {
                locale = Orianna.getSettings().getDefaultLocale();
                locale = locale == null ? platform.getDefaultLocale() : locale;
            }

            if(includedData == null) {
                includedData = ImmutableSet.of("all");
            }

            final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object> builder().put("platform", platform).put("version", version)
                .put("locale", locale).put("includedData", includedData);

            return Orianna.getSettings().getPipeline().get(Champions.class, builder.build());
        }

        public Builder withIncludedData(final Iterable<String> includedData) {
            this.includedData = Sets.newHashSet(includedData);
            return this;
        }

        public Builder withIncludedData(final String... includedData) {
            this.includedData = Sets.newHashSet(includedData);
            return this;
        }

        public Builder withLocale(final String locale) {
            this.locale = locale;
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

        public Builder withVersion(final String version) {
            this.version = version;
            return this;
        }
    }

    public static final class SubsetBuilder {
        private Iterable<Integer> ids;
        private Set<String> includedData;
        private Iterable<String> names, keys;
        private Platform platform;
        private boolean streaming = false;
        private String version, locale;

        private SubsetBuilder(final Iterable<Integer> ids) {
            this.ids = ids;
        }

        private SubsetBuilder(final Iterable<String> keys, final boolean areNames) {
            if(areNames) {
                names = keys;
            } else {
                this.keys = keys;
            }
        }

        public SearchableList<de.zahrie.trues.api.riot.xayah.types.core.staticdata.Champion> get() {
            if(platform == null) {
                platform = Orianna.getSettings().getDefaultPlatform();
                if(platform == null) {
                    throw new IllegalStateException(
                        "No platform/region was set! Must either set a default platform/region with Orianna.setDefaultPlatform or Orianna.setDefaultRegion, or include a platform/region with the request!");
                }
            }

            if(version == null) {
                version = Orianna.getSettings().getCurrentVersion(platform);
            }

            if(locale == null) {
                locale = Orianna.getSettings().getDefaultLocale();
                locale = locale == null ? platform.getDefaultLocale() : locale;
            }

            if(includedData == null) {
                includedData = ImmutableSet.of("all");
            }

            final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object> builder().put("platform", platform).put("version", version)
                .put("locale", locale).put("includedData", includedData);

            if(ids != null) {
                builder.put("ids", ids);
            } else if(names != null) {
                builder.put("names", names);
            } else {
                builder.put("keys", keys);
            }

            final CloseableIterator<de.zahrie.trues.api.riot.xayah.types.core.staticdata.Champion> result =
                Orianna.getSettings().getPipeline().getMany(de.zahrie.trues.api.riot.xayah.types.core.staticdata.Champion.class, builder.build(), streaming);
            return streaming ? SearchableLists.from(CloseableIterators.toLazyList(result)) : SearchableLists.from(CloseableIterators.toList(result));
        }

        public SubsetBuilder streaming() {
            streaming = true;
            return this;
        }

        public SubsetBuilder withIncludedData(final Iterable<String> includedData) {
            this.includedData = Sets.newHashSet(includedData);
            return this;
        }

        public SubsetBuilder withIncludedData(final String... includedData) {
            this.includedData = Sets.newHashSet(includedData);
            return this;
        }

        public SubsetBuilder withLocale(final String locale) {
            this.locale = locale;
            return this;
        }

        public SubsetBuilder withPlatform(final Platform platform) {
            this.platform = platform;
            return this;
        }

        public SubsetBuilder withRegion(final Region region) {
            platform = region.getPlatform();
            return this;
        }

        public SubsetBuilder withVersion(final String version) {
            this.version = version;
            return this;
        }
    }

    @Serial
    private static final long serialVersionUID = -5852031149115607129L;

    public static Champions get() {
        return new Builder().get();
    }

    public static SubsetBuilder named(final Iterable<String> names) {
        return new SubsetBuilder(names, true);
    }

    public static SubsetBuilder named(final String... names) {
        return new SubsetBuilder(Arrays.asList(names), true);
    }

    public static SubsetBuilder withIds(final int... ids) {
        final List<Integer> list = new ArrayList<>(ids.length);
        for(final int id : ids) {
            list.add(id);
        }
        return new SubsetBuilder(list);
    }

    public static SubsetBuilder withIds(final Iterable<Integer> ids) {
        return new SubsetBuilder(ids);
    }

    public static Builder withIncludedData(final Iterable<String> includedData) {
        return new Builder().withIncludedData(includedData);
    }

    public static Builder withIncludedData(final String... includedData) {
        return new Builder().withIncludedData(includedData);
    }

    public static SubsetBuilder withKeys(final Iterable<String> keys) {
        return new SubsetBuilder(keys, false);
    }

    public static SubsetBuilder withKeys(final String... keys) {
        return new SubsetBuilder(Arrays.asList(keys), false);
    }

    public static Builder withLocale(final String locale) {
        return new Builder().withLocale(locale);
    }

    public static Builder withPlatform(final Platform platform) {
        return new Builder().withPlatform(platform);
    }

    public static Builder withRegion(final Region region) {
        return new Builder().withRegion(region);
    }

    public static Builder withVersion(final String version) {
        return new Builder().withVersion(version);
    }

    private final Supplier<Set<String>> includedData = Suppliers.memoize(() -> {
        if(coreData.getIncludedData() == null) {
            return null;
        }
        return Collections.unmodifiableSet(coreData.getIncludedData());
    });

    public Champions(final de.zahrie.trues.api.riot.xayah.types.data.staticdata.Champions coreData) {
        super(coreData, 1);
    }

    @Override
    public boolean exists() {
        if(coreData.isEmpty()) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return !coreData.isEmpty();
    }

    public String getFormat() {
        if(coreData.getFormat() == null) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return coreData.getFormat();
    }

    public Set<String> getIncludedData() {
        return includedData.get();
    }

    @Override
    protected List<String> getLoadGroups() {
        return List.of(LIST_PROXY_LOAD_GROUP);
    }

    public String getLocale() {
        return coreData.getLocale();
    }

    public Platform getPlatform() {
        return Platform.withTag(coreData.getPlatform());
    }

    public Region getRegion() {
        return Platform.withTag(coreData.getPlatform()).getRegion();
    }

    public String getType() {
        if(coreData.getType() == null) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return coreData.getType();
    }

    public String getVersion() {
        return coreData.getVersion();
    }

    @Override
    protected void loadCoreData(final String group) {
        final ImmutableMap.Builder<String, Object> builder;
      if (group.equals(LIST_PROXY_LOAD_GROUP)) {
        builder = ImmutableMap.builder();
        if (coreData.getPlatform() != null) {
          builder.put("platform", Platform.withTag(coreData.getPlatform()));
        }
        if (coreData.getVersion() != null) {
          builder.put("version", coreData.getVersion());
        }
        if (coreData.getLocale() != null) {
          builder.put("locale", coreData.getLocale());
        }
        if (coreData.getIncludedData() != null) {
          builder.put("includedData", coreData.getIncludedData());
        }
        final de.zahrie.trues.api.riot.xayah.types.data.staticdata.Champions data =
            Orianna.getSettings().getPipeline().get(de.zahrie.trues.api.riot.xayah.types.data.staticdata.Champions.class, builder.build());
        if (data != null) {
          coreData = data;
        }
        loadListProxyData(data1 -> {
          final Champion champion = new Champion(data1);
          champion.markAsGhostLoaded(Champion.CHAMPION_LOAD_GROUP);
          return champion;
        });
      }
    }
}
