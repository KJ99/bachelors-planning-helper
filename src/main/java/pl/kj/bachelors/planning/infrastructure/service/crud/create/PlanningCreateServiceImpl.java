package pl.kj.bachelors.planning.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.service.ModelValidator;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningCreateService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import java.util.TimeZone;

@Service
public class PlanningCreateServiceImpl
        extends BaseEntityCreateService<Planning, Integer, PlanningRepository, PlanningCreateModel>
        implements PlanningCreateService {
    @Autowired
    public PlanningCreateServiceImpl(ModelMapper modelMapper, PlanningRepository repository, ModelValidator validator) {
        super(modelMapper, repository, validator);
    }

    @Override
    protected void preCreate(PlanningCreateModel model) {
        TimeZone timeZone = RequestHandler.getRequestTimeZone().orElse(TimeZone.getDefault());
        model.setTimeZone(timeZone.getID());
        model.setStatus(PlanningStatus.SCHEDULED);
    }
}
