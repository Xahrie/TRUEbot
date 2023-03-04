package de.zahrie.trues.api.riot.xayah.datapipeline.transformers.dtodata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.merakianalytics.datapipelines.PipelineContext;
import com.merakianalytics.datapipelines.transformers.AbstractDataTransformer;
import com.merakianalytics.datapipelines.transformers.Transform;
import de.zahrie.trues.api.riot.xayah.types.data.status.Incident;
import de.zahrie.trues.api.riot.xayah.types.data.status.Message;
import de.zahrie.trues.api.riot.xayah.types.data.status.Service;
import de.zahrie.trues.api.riot.xayah.types.data.status.ShardStatus;
import de.zahrie.trues.api.riot.xayah.types.data.status.Translation;

public class StatusTransformer extends AbstractDataTransformer {
    final DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    @Transform(from = de.zahrie.trues.api.riot.xayah.types.dto.status.Incident.class, to = Incident.class)
    public Incident transform(final de.zahrie.trues.api.riot.xayah.types.dto.status.Incident item, final PipelineContext context) {
        final Incident incident = new Incident();
        incident.setActive(item.isActive());
        incident.setCreated(DateTime.parse(item.getCreated_at(), formatter));
        incident.setId(item.getId());
        final List<Message> updates = new ArrayList<>(item.getUpdates().size());
        for(final de.zahrie.trues.api.riot.xayah.types.dto.status.Message update : item.getUpdates()) {
            updates.add(transform(update, context));
        }
        incident.setUpdates(updates);
        return incident;
    }

    @Transform(from = de.zahrie.trues.api.riot.xayah.types.dto.status.Message.class, to = Message.class)
    public Message transform(final de.zahrie.trues.api.riot.xayah.types.dto.status.Message item, final PipelineContext context) {
        final Message message = new Message();
        message.setAuthor(item.getAuthor());
        message.setContent(item.getContent());
        message.setCreated(DateTime.parse(item.getCreated_at(), formatter));
        message.setId(item.getId());
        message.setSeverity(item.getSeverity());
        final Map<String, Translation> translations = new HashMap<>();
        for(final de.zahrie.trues.api.riot.xayah.types.dto.status.Translation translation : item.getTranslations()) {
            translations.put(translation.getLocale(), transform(translation, context));
        }
        message.setTranslations(translations);
        message.setUpdated(DateTime.parse(item.getUpdated_at(), formatter));
        message.setHeading(item.getHeading());
        return message;
    }

    @Transform(from = de.zahrie.trues.api.riot.xayah.types.dto.status.Service.class, to = Service.class)
    public Service transform(final de.zahrie.trues.api.riot.xayah.types.dto.status.Service item, final PipelineContext context) {
        final Service service = new Service();
        final List<Incident> incidents = new ArrayList<>(item.getIncidents().size());
        for(final de.zahrie.trues.api.riot.xayah.types.dto.status.Incident incident : item.getIncidents()) {
            incidents.add(transform(incident, context));
        }
        service.setIncidents(incidents);
        service.setName(item.getName());
        service.setStatus(item.getStatus());
        return service;
    }

    @Transform(from = de.zahrie.trues.api.riot.xayah.types.dto.status.ShardStatus.class, to = ShardStatus.class)
    public ShardStatus transform(final de.zahrie.trues.api.riot.xayah.types.dto.status.ShardStatus item, final PipelineContext context) {
        final ShardStatus status = new ShardStatus();
        status.setHostname(item.getHostname());
        status.setLocales(new ArrayList<>(item.getLocales()));
        status.setName(item.getName());
        status.setRegionTag(item.getRegion_tag());
        status.setSlug(item.getSlug());
        status.setPlatform(item.getPlatform());
        final List<Service> services = new ArrayList<>(item.getServices().size());
        for(final de.zahrie.trues.api.riot.xayah.types.dto.status.Service service : item.getServices()) {
            services.add(transform(service, context));
        }
        status.setServices(services);
        return status;
    }

    @Transform(from = de.zahrie.trues.api.riot.xayah.types.dto.status.Translation.class, to = Translation.class)
    public Translation transform(final de.zahrie.trues.api.riot.xayah.types.dto.status.Translation item, final PipelineContext context) {
        final Translation translation = new Translation();
        translation.setContent(item.getContent());
        translation.setLocale(item.getLocale());
        translation.setHeading(item.getHeading());
        return translation;
    }

    @Transform(from = Incident.class, to = de.zahrie.trues.api.riot.xayah.types.dto.status.Incident.class)
    public de.zahrie.trues.api.riot.xayah.types.dto.status.Incident transform(final Incident item, final PipelineContext context) {
        final de.zahrie.trues.api.riot.xayah.types.dto.status.Incident incident = new de.zahrie.trues.api.riot.xayah.types.dto.status.Incident();
        incident.setActive(item.isActive());
        incident.setCreated_at(item.getCreated().toString(formatter));
        incident.setId(item.getId());
        final List<de.zahrie.trues.api.riot.xayah.types.dto.status.Message> updates = new ArrayList<>(item.getUpdates().size());
        for(final Message update : item.getUpdates()) {
            updates.add(transform(update, context));
        }
        incident.setUpdates(updates);
        return incident;
    }

    @Transform(from = Message.class, to = de.zahrie.trues.api.riot.xayah.types.dto.status.Message.class)
    public de.zahrie.trues.api.riot.xayah.types.dto.status.Message transform(final Message item, final PipelineContext context) {
        final de.zahrie.trues.api.riot.xayah.types.dto.status.Message message = new de.zahrie.trues.api.riot.xayah.types.dto.status.Message();
        message.setAuthor(item.getAuthor());
        message.setContent(item.getContent());
        message.setCreated_at(item.getCreated().toString(formatter));
        message.setId(item.getId());
        message.setSeverity(item.getSeverity());
        final List<de.zahrie.trues.api.riot.xayah.types.dto.status.Translation> translations = new ArrayList<>(item.getTranslations().size());
        for(final Translation translation : item.getTranslations().values()) {
            translations.add(transform(translation, context));
        }
        message.setTranslations(translations);
        message.setUpdated_at(item.getUpdated().toString(formatter));
        message.setHeading(item.getHeading());
        return message;
    }

    @Transform(from = Service.class, to = de.zahrie.trues.api.riot.xayah.types.dto.status.Service.class)
    public de.zahrie.trues.api.riot.xayah.types.dto.status.Service transform(final Service item, final PipelineContext context) {
        final de.zahrie.trues.api.riot.xayah.types.dto.status.Service service = new de.zahrie.trues.api.riot.xayah.types.dto.status.Service();
        final List<de.zahrie.trues.api.riot.xayah.types.dto.status.Incident> incidents = new ArrayList<>(item.getIncidents().size());
        for(final Incident incident : item.getIncidents()) {
            incidents.add(transform(incident, context));
        }
        service.setIncidents(incidents);
        service.setName(item.getName());
        service.setSlug(item.getName().toLowerCase());
        service.setStatus(item.getStatus());
        return service;
    }

    @Transform(from = ShardStatus.class, to = de.zahrie.trues.api.riot.xayah.types.dto.status.ShardStatus.class)
    public de.zahrie.trues.api.riot.xayah.types.dto.status.ShardStatus transform(final ShardStatus item, final PipelineContext context) {
        final de.zahrie.trues.api.riot.xayah.types.dto.status.ShardStatus status = new de.zahrie.trues.api.riot.xayah.types.dto.status.ShardStatus();
        status.setHostname(item.getHostname());
        status.setLocales(new ArrayList<>(item.getLocales()));
        status.setName(item.getName());
        status.setPlatform(item.getPlatform());
        status.setRegion_tag(item.getRegionTag());
        status.setSlug(item.getSlug());
        final List<de.zahrie.trues.api.riot.xayah.types.dto.status.Service> services = new ArrayList<>(item.getServices().size());
        for(final Service service : item.getServices()) {
            services.add(transform(service, context));
        }
        status.setServices(services);
        return status;
    }

    @Transform(from = Translation.class, to = de.zahrie.trues.api.riot.xayah.types.dto.status.Translation.class)
    public de.zahrie.trues.api.riot.xayah.types.dto.status.Translation transform(final Translation item, final PipelineContext context) {
        final de.zahrie.trues.api.riot.xayah.types.dto.status.Translation translation = new de.zahrie.trues.api.riot.xayah.types.dto.status.Translation();
        translation.setContent(item.getContent());
        translation.setLocale(item.getLocale());
        translation.setHeading(item.getHeading());
        return translation;
    }
}
