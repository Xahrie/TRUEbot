package de.zahrie.trues.api.riot.xayah.datapipeline.kernel.data;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.merakianalytics.datapipelines.PipelineContext;
import com.merakianalytics.datapipelines.iterators.CloseableIterator;
import com.merakianalytics.datapipelines.iterators.CloseableIterators;
import com.merakianalytics.datapipelines.sources.Get;
import com.merakianalytics.datapipelines.sources.GetMany;
import de.zahrie.trues.api.riot.xayah.datapipeline.common.HTTPClient;
import de.zahrie.trues.api.riot.xayah.datapipeline.common.Utilities;
import de.zahrie.trues.api.riot.xayah.datapipeline.kernel.data.Kernel.Configuration;
import de.zahrie.trues.api.riot.xayah.types.common.Platform;
import de.zahrie.trues.api.riot.xayah.types.data.spectator.CurrentMatch;
import de.zahrie.trues.api.riot.xayah.types.data.spectator.FeaturedMatches;

public class SpectatorAPI extends KernelService {
    public SpectatorAPI(final Configuration config, final HTTPClient client) {
        super(config, client);
    }

    @Get(CurrentMatch.class)
    public CurrentMatch getCurrentMatch(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final String summonerId = (String)query.get("summonerId");
        Utilities.checkNotNull(platform, "platform", summonerId, "summonerId");

        final String endpoint = "lol/spectator/v4/active-games/by-summoner/" + summonerId;

      return get(CurrentMatch.class, endpoint, ImmutableMap.of("platform", platform.getTag()));
    }

    @Get(FeaturedMatches.class)
    public FeaturedMatches getFeaturedMatches(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        Utilities.checkNotNull(platform, "platform");

        final String endpoint = "lol/spectator/v4/featured-games";

      return get(FeaturedMatches.class, endpoint, ImmutableMap.of("platform", platform.getTag()));
    }

    @SuppressWarnings("unchecked")
    @GetMany(CurrentMatch.class)
    public CloseableIterator<CurrentMatch> getManyCurrentMatch(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Iterable<String> summonerIds = (Iterable<String>)query.get("summonerIds");
        Utilities.checkNotNull(platform, "platform", summonerIds, "summonerIds");

        final Iterator<String> iterator = summonerIds.iterator();
        return CloseableIterators.from(new Iterator<>() {
          @Override
          public boolean hasNext() {
            return iterator.hasNext();
          }

          @Override
          public CurrentMatch next() {
            final String summonerId = iterator.next();

            final String endpoint = "lol/spectator/v4/active-games/by-summoner/" + summonerId;

            return get(CurrentMatch.class, endpoint, ImmutableMap.of("platform", platform.getTag()));
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        });
    }

    @SuppressWarnings("unchecked")
    @GetMany(FeaturedMatches.class)
    public CloseableIterator<FeaturedMatches> getManyFeaturedMatches(final Map<String, Object> query, final PipelineContext context) {
        final Iterable<Platform> platforms = (Iterable<Platform>)query.get("platforms");
        Utilities.checkNotNull(platforms, "platforms");

        final Iterator<Platform> iterator = platforms.iterator();
        return CloseableIterators.from(new Iterator<>() {
          @Override
          public boolean hasNext() {
            return iterator.hasNext();
          }

          @Override
          public FeaturedMatches next() {
            final Platform platform = iterator.next();

            final String endpoint = "lol/spectator/v4/featured-games";

            return get(FeaturedMatches.class, endpoint, ImmutableMap.of("platform", platform.getTag()));
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        });
    }
}
