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

public class Masteries extends GhostObject.ListProxy<Mastery, de.zahrie.trues.api.riot.xayah.types.data.staticdata.Mastery, de.zahrie.trues.api.riot.xayah.types.data.staticdata.Masteries> {
    public static final class Builder {
        private Set<String> includedData;
        private Platform platform;
        private String version, locale;

        private Builder() {}

        public Masteries get() {
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

            return Orianna.getSettings().getPipeline().get(Masteries.class, builder.build());
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
        private Iterable<String> names;
        private Platform platform;
        private boolean streaming = false;
        private String version, locale;

        @SuppressWarnings("unchecked")
        private SubsetBuilder(final Iterable<?> ids, final boolean areIds) {
            if(areIds) {
                this.ids = (Iterable<Integer>)ids;
            } else {
                names = (Iterable<String>)ids;
            }
        }

        public SearchableList<Mastery> get() {
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
            } else {
                builder.put("names", names);
            }

            final CloseableIterator<Mastery> result = Orianna.getSettings().getPipeline().getMany(Mastery.class, builder.build(), streaming);
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
    private static final long serialVersionUID = 6445191739106133028L;

    public static Masteries get() {
        return new Builder().get();
    }

    public static SubsetBuilder named(final Iterable<String> names) {
        return new SubsetBuilder(names, false);
    }

    public static SubsetBuilder named(final String... names) {
        return new SubsetBuilder(Arrays.asList(names), false);
    }

    public static SubsetBuilder withIds(final int... ids) {
        final List<Integer> list = new ArrayList<>(ids.length);
        for(final int id : ids) {
            list.add(id);
        }
        return new SubsetBuilder(list, true);
    }

    public static SubsetBuilder withIds(final Iterable<Integer> ids) {
        return new SubsetBuilder(ids, true);
    }

    public static Builder withIncludedData(final Iterable<String> includedData) {
        return new Builder().withIncludedData(includedData);
    }

    public static Builder withIncludedData(final String... includedData) {
        return new Builder().withIncludedData(includedData);
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

    private final Supplier<MasteryTree> tree = Suppliers.memoize(() -> {
        load(LIST_PROXY_LOAD_GROUP);
        if(coreData.getTree() == null) {
            return null;
        }
        return new MasteryTree(coreData.getTree());
    });

    public Masteries(final de.zahrie.trues.api.riot.xayah.types.data.staticdata.Masteries coreData) {
        super(coreData, 1);
    }

    @Override
    public boolean exists() {
        if(coreData.isEmpty()) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return !coreData.isEmpty();
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

    public MasteryTree getTree() {
        return tree.get();
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
        final de.zahrie.trues.api.riot.xayah.types.data.staticdata.Masteries data =
            Orianna.getSettings().getPipeline().get(de.zahrie.trues.api.riot.xayah.types.data.staticdata.Masteries.class, builder.build());
        if (data != null) {
          coreData = data;
        }
        loadListProxyData(data1 -> {
          final Mastery mastery = new Mastery(data1);
          mastery.markAsGhostLoaded(Mastery.MASTERY_LOAD_GROUP);
          return mastery;
        });
      }
    }
}
