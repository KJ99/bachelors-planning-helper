package pl.kj.bachelors.planning.infrastructure.service.crud.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.service.ModelValidator;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningCreateService;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningItemCreateService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;

@Service
public class PlanningItemCreateServiceImpl
        extends BaseEntityCreateService<PlanningItem, Integer, PlanningItemRepository, PlanningItemCreateModel>
        implements PlanningItemCreateService {
    @Autowired
    protected PlanningItemCreateServiceImpl(ModelMapper modelMapper, PlanningItemRepository repository, ModelValidator validator) {
        super(modelMapper, repository, validator);
    }
}
