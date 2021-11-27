package pl.kj.bachelors.planning.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningUpdateService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.ValidationService;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@Service
public class PlanningUpdateServiceImpl
        extends BaseEntityUpdateService<Planning, Integer, PlanningUpdateModel, PlanningRepository>
        implements PlanningUpdateService {
    @Autowired
    protected PlanningUpdateServiceImpl(PlanningRepository repository, ValidationService validationService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(repository, validationService, modelMapper, objectMapper);
    }

    @Override
    protected void preUpdate(Planning original, PlanningUpdateModel updateModel) throws Exception {
        if(!original.getStatus().equals(PlanningStatus.SCHEDULED)) {
            throw new AccessDeniedException("You cannot update planning that is already finished or progressing");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if(!df.format(original.getStartAt().getTime()).equals(updateModel.getStartDate())) {
            TimeZone timeZone = RequestHandler.getRequestTimeZone().orElse(TimeZone.getDefault());
            updateModel.setTimeZone(timeZone.getID());
        }
    }
}
