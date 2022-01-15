package pl.kj.bachelors.planning.application.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kj.bachelors.planning.application.dto.response.health.HealthCheckResponse;
import pl.kj.bachelors.planning.application.dto.response.health.SingleCheckResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningItemResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.application.model.HealthCheckResult;
import pl.kj.bachelors.planning.application.model.SingleCheckResult;
import pl.kj.bachelors.planning.domain.config.ApiConfig;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.report.PlanningItemReport;
import pl.kj.bachelors.planning.domain.model.report.PlanningReport;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {
    private final ApiConfig config;

    @Autowired
    public MapperConfig(ApiConfig config) {
        this.config = config;
    }

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<SingleCheckResult, SingleCheckResponse>() {
            @Override
            protected void configure() {
                using(ctx -> ((SingleCheckResult) ctx.getSource()).isActive() ? "On" : "Off")
                        .map(source, destination.getStatus());
            }
        });

        mapper.addMappings(new PropertyMap<HealthCheckResult, HealthCheckResponse>() {
            @Override
            protected void configure() {
                using(ctx -> ((HealthCheckResult) ctx.getSource())
                        .getResults()
                        .stream()
                        .map(item -> mapper().map(item, SingleCheckResponse.class))
                        .collect(Collectors.toList())
                ).map(source, destination.getResults());
            }
        });

        mapper.addMappings(new PropertyMap<PlanningCreateModel, Planning>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    PlanningCreateModel model = (PlanningCreateModel) ctx.getSource();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Calendar result;
                    try {
                        result = Calendar.getInstance();
                        result.setTime(df.parse(model.getStartDate()));
                    } catch (ParseException e) {
                        result = null;
                    }

                    return result;
                }).map(source, destination.getStartAt());

                skip(destination.getId());
            }
        });

        mapper.addMappings(new PropertyMap<Planning, PlanningResponse>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getStartAt());
                map().setId(source.getId());
            }
        });

        mapper.addMappings(new PropertyMap<Planning, PlanningUpdateModel>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    Planning entity = (Planning) ctx.getSource();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    return df.format(entity.getStartAt().getTime());
                }).map(source, destination.getStartDate());
            }
        });

        mapper.addMappings(new PropertyMap<PlanningUpdateModel, Planning>() {
            @Override
            protected void configure() {
                using(ctx -> {
                    PlanningUpdateModel model = (PlanningUpdateModel) ctx.getSource();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Calendar result;
                    try {
                        result = Calendar.getInstance();
                        result.setTime(df.parse(model.getStartDate()));
                    } catch (ParseException e) {
                        result = null;
                    }

                    return result;
                }).map(source, destination.getStartAt());
            }
        });

        mapper.addMappings(new PropertyMap<Planning, PlanningReport>() {

            @Override
            protected void configure() {
                skip(destination.getTeamName());
                skip(destination.getResults());
                using(ctx -> {
                   Planning source = (Planning) ctx.getSource();
                   SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH);

                   return dateFormat.format(source.getStartAt().getTime());
                }).map(source, destination.getDate());
            }
        });

        mapper.addConverter(new Converter<PlanningItem, PlanningItemReport>() {
            @Override
            public PlanningItemReport convert(MappingContext<PlanningItem, PlanningItemReport> ctx) {
                PlanningItem source = (ctx).getSource();
                PlanningItemReport report = new PlanningItemReport();
                report.setTitle(source.getTitle());
                report.setEstimation(source.getEstimation().name());
                report.setStoryPoints(source.getEstimation().value);

                return report;
            }
        });

        mapper.addMappings(new PropertyMap<PlanningItem, PlanningItemResponse>() {
            @Override
            protected void configure() {
                map().setEstimation(source.getEstimation().value);
            }
        });
        return mapper;
    }
}
