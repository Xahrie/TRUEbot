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
import de.zahrie.trues.api.riot.xayah.types.data.status.ShardStatus;

public class StatusAPI extends KernelService {
    public StatusAPI(final Configuration config, final HTTPClient client) {
        super(config, client);
    }

    @SuppressWarnings("unchecked")
    @GetMany(ShardStatus.class)
    public CloseableIterator<ShardStatus> getManyShardStatus(final Map<String, Object> query, final PipelineContext context) {
        final Iterable<Platform> platforms = (Iterable<Platform>)query.get("platforms");
        Utilities.checkNotNull(platforms, "platforms");

        final Iterator<Platform> iterator = platforms.iterator();
        return CloseableIterators.from(new Iterator<ShardStatus>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ShardStatus next() {
                final Platform platform = iterator.next();

                final String endpoint = "lol/status/v3/shard-data";
                final ShardStatus data = get(ShardStatus.class, endpoint, ImmutableMap.of("platform", platform.getTag()));
                if(data == null) {
                    return null;
                }

                return data;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Get(ShardStatus.class)
    public ShardStatus getShardStatus(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        Utilities.checkNotNull(platform, "platform");

        final String endpoint = "lol/status/v3/shard-data";
        final ShardStatus data = get(ShardStatus.class, endpoint, ImmutableMap.of("platform", platform.getTag()));
        if(data == null) {
            return null;
        }

        return data;
    }
}
