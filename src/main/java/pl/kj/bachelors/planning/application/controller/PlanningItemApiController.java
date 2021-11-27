package pl.kj.bachelors.planning.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningItemResponse;
import pl.kj.bachelors.planning.domain.annotation.Authentication;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningItemAdministrativeAction;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningItemCreateService;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

@RestController
@RequestMapping("/v1/plannings/{planningId}/items")
@Authentication
public class PlanningItemApiController extends BaseApiController {
    private final PlanningItemCreateService createService;
    private final EntityAccessControlService<Planning> planningAccessControlService;
    private final PlanningRepository planningRepository;

    @Autowired
    public PlanningItemApiController(
            PlanningItemCreateService createService,
            EntityAccessControlService<Planning> planningAccessControlService,
            PlanningRepository planningRepository) {
        this.createService = createService;
        this.planningAccessControlService = planningAccessControlService;
        this.planningRepository = planningRepository;
    }

    @PostMapping
    public ResponseEntity<PlanningItemResponse> post(
            @PathVariable Integer planningId,
            @RequestBody PlanningItemCreateModel model) throws Exception {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.CREATE);
        model.setPlanning(planning);

        PlanningItem result = this.createService.create(model, PlanningItem.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(result, PlanningItemResponse.class));
    }
}
