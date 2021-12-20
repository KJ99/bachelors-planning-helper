package pl.kj.bachelors.planning.infrastructure.service.crud.update;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.update.PlanningItemUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningItemUpdateService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.service.ValidationService;

@Service
public class PlanningItemUpdateServiceImpl
    extends BaseEntityUpdateService<PlanningItem, Integer, PlanningItemUpdateModel, PlanningItemRepository>
    implements PlanningItemUpdateService {
    @Autowired
    protected PlanningItemUpdateServiceImpl(PlanningItemRepository repository, ValidationService validationService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(repository, validationService, modelMapper, objectMapper);
    }
}
