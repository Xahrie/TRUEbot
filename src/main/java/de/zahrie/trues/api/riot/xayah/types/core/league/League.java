package de.zahrie.trues.api.riot.xayah.types.core.league;

import java.io.Serial;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import de.zahrie.trues.api.riot.xayah.Orianna;
import de.zahrie.trues.api.riot.xayah.types.common.Platform;
import de.zahrie.trues.api.riot.xayah.types.common.Queue;
import de.zahrie.trues.api.riot.xayah.types.common.Region;
import de.zahrie.trues.api.riot.xayah.types.common.Tier;
import de.zahrie.trues.api.riot.xayah.types.core.GhostObject;
import de.zahrie.trues.api.riot.xayah.types.core.searchable.Searchable;

public class League extends GhostObject.ListProxy<LeagueEntry, de.zahrie.trues.api.riot.xayah.types.data.league.LeagueEntry, de.zahrie.trues.api.riot.xayah.types.data.league.League> {
    public static final class Builder {
        private final String id;
        private Platform platform;

        private Builder(final String id) {
            this.id = id;
        }

        public League get() {
            if(platform == null) {
                platform = Orianna.getSettings().getDefaultPlatform();
                if(platform == null) {
                    throw new IllegalStateException(
                        "No platform/region was set! Must either set a default platform/region with Orianna.setDefaultPlatform or Orianna.setDefaultRegion, or include a platform/region with the request!");
                }
            }

            final ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object> builder().put("platform", platform).put("leagueId", id);

            return Orianna.getSettings().getPipeline().get(League.class, builder.build());
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

    public static final class SelectBuilder {
        public final class SubBuilder {
            private Platform platform;
            private final Queue queue;

            private SubBuilder(final Queue queue) {
                this.queue = queue;
            }

            public League get() {
                if(platform == null) {
                    platform = Orianna.getSettings().getDefaultPlatform();
                    if(platform == null) {
                        throw new IllegalStateException(
                            "No platform/region was set! Must either set a default platform/region with Orianna.setDefaultPlatform or Orianna.setDefaultRegion, or include a platform/region with the request!");
                    }
                }

                final ImmutableMap.Builder<String, Object> builder =
                    ImmutableMap.<String, Object> builder().put("platform", platform).put("tier", tier).put("queue", queue);

                return Orianna.getSettings().getPipeline().get(League.class, builder.build());
            }

            public SubBuilder withPlatform(final Platform platform) {
                this.platform = platform;
                return this;
            }

            public SubBuilder withRegion(final Region region) {
                platform = region.getPlatform();
                return this;
            }
        }

        private final Tier tier;

        private SelectBuilder(final Tier tier) {
            this.tier = tier;
        }

        public SubBuilder inQueue(final Queue queue) {
            if(!Queue.RANKED.contains(queue)) {
                final StringBuilder sb = new StringBuilder();
                for(final Queue q : Queue.RANKED) {
                    sb.append(", ").append(q);
                }
                throw new IllegalArgumentException("Queue must be one of [" + sb.substring(2) + "]!");
            }
            return new SubBuilder(queue);
        }
    }

    @Serial
    private static final long serialVersionUID = -4287829961173669465L;

    public static SelectBuilder.SubBuilder challengerInQueue(final Queue queue) {
        return new SelectBuilder(Tier.CHALLENGER).inQueue(queue);
    }

    public static SelectBuilder.SubBuilder grandmasterInQueue(final Queue queue) {
        return new SelectBuilder(Tier.GRANDMASTER).inQueue(queue);
    }

    public static SelectBuilder.SubBuilder masterInQueue(final Queue queue) {
        return new SelectBuilder(Tier.MASTER).inQueue(queue);
    }

    public static Builder withId(final String id) {
        return new Builder(id);
    }

    public League(final de.zahrie.trues.api.riot.xayah.types.data.league.League coreData) {
        super(coreData, 1);
    }

    @Override
    public boolean exists() {
        if(coreData.isEmpty()) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return !coreData.isEmpty();
    }

    @Searchable({String.class})
    public String getId() {
        if(coreData.getId() == null) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return coreData.getId();
    }

    @Override
    protected List<String> getLoadGroups() {
        return List.of(LIST_PROXY_LOAD_GROUP);
    }

    @Searchable({String.class})
    public String getName() {
        if(coreData.getName() == null) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return coreData.getName();
    }

    public Platform getPlatform() {
        return Platform.withTag(coreData.getPlatform());
    }

    public Queue getQueue() {
        if(coreData.getQueue() == null) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return Queue.withTag(coreData.getQueue());
    }

    public Region getRegion() {
        return Platform.withTag(coreData.getPlatform()).getRegion();
    }

    public Tier getTier() {
        if(coreData.getTier() == null) {
            load(LIST_PROXY_LOAD_GROUP);
        }
        return Tier.valueOf(coreData.getTier());
    }

    @Override
    protected void loadCoreData(final String group) {
        ImmutableMap.Builder<String, Object> builder;
      if (group.equals(LIST_PROXY_LOAD_GROUP)) {
        builder = ImmutableMap.builder();
        if (coreData.getPlatform() != null) {
          builder.put("platform", Platform.withTag(coreData.getPlatform()));
        }
        if (coreData.getId() != null) {
          builder.put("leagueId", coreData.getId());
        }
        if (coreData.getTier() != null) {
          builder.put("tier", Tier.valueOf(coreData.getTier()));
        }
        if (coreData.getQueue() != null) {
          builder.put("queue", Queue.withTag(coreData.getQueue()));
        }
        final de.zahrie.trues.api.riot.xayah.types.data.league.League data =
            Orianna.getSettings().getPipeline().get(de.zahrie.trues.api.riot.xayah.types.data.league.League.class, builder.build());
        if (data != null) {
          coreData = data;
        }
        loadListProxyData(LeagueEntry::new);
      }
    }
}