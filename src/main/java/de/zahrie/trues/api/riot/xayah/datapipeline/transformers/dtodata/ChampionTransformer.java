package de.zahrie.trues.api.riot.xayah.datapipeline.transformers.dtodata;

import java.util.ArrayList;
import java.util.HashSet;

import com.merakianalytics.datapipelines.PipelineContext;
import com.merakianalytics.datapipelines.transformers.AbstractDataTransformer;
import com.merakianalytics.datapipelines.transformers.Transform;
import de.zahrie.trues.api.riot.xayah.types.data.champion.ChampionRotation;
import de.zahrie.trues.api.riot.xayah.types.dto.champion.ChampionInfo;

public class ChampionTransformer extends AbstractDataTransformer {
    @Transform(from = ChampionInfo.class, to = ChampionRotation.class)
    public ChampionRotation transform(final ChampionInfo item, final PipelineContext context) {
        final ChampionRotation rotation = new ChampionRotation();
        rotation.setFreeChampionIds(new HashSet<>(item.getFreeChampionIds().size()));
        for(final Integer id : item.getFreeChampionIds()) {
            rotation.getFreeChampionIds().add(id);
        }
        rotation.setFreeChampionIdsForNewPlayers(new HashSet<>(item.getFreeChampionIdsForNewPlayers().size()));
        for(final Integer id : item.getFreeChampionIdsForNewPlayers()) {
            rotation.getFreeChampionIdsForNewPlayers().add(id);
        }
        rotation.setMaxNewPlayerLevel(item.getMaxNewPlayerLevel());
        rotation.setPlatform(item.getPlatform());
        return rotation;
    }

    @Transform(from = ChampionRotation.class, to = ChampionInfo.class)
    public ChampionInfo transform(final ChampionRotation item, final PipelineContext context) {
        final ChampionInfo info = new ChampionInfo();
        info.setFreeChampionIds(new ArrayList<>(item.getFreeChampionIds().size()));
        for(final Integer id : item.getFreeChampionIds()) {
            info.getFreeChampionIds().add(id);
        }
        info.setFreeChampionIdsForNewPlayers(new ArrayList<>(item.getFreeChampionIdsForNewPlayers().size()));
        for(final Integer id : item.getFreeChampionIdsForNewPlayers()) {
            info.getFreeChampionIdsForNewPlayers().add(id);
        }
        info.setMaxNewPlayerLevel(item.getMaxNewPlayerLevel());
        info.setPlatform(item.getPlatform());
        return info;
    }
}
