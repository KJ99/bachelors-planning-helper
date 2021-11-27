package pl.kj.bachelors.planning.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
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
import pl.kj.bachelors.planning.domain.model.update.PlanningItemUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningItemCreateService;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningItemUpdateService;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/v1/plannings/{planningId}/items")
@Authentication
public class PlanningItemApiController extends BaseApiController {
    private final PlanningItemCreateService createService;
    private final EntityAccessControlService<Planning> planningAccessControlService;
    private final PlanningRepository planningRepository;
    private final PlanningItemUpdateService updateService;
    private final PlanningItemRepository repository;

    @Autowired
    public PlanningItemApiController(
            PlanningItemCreateService createService,
            EntityAccessControlService<Planning> planningAccessControlService,
            PlanningRepository planningRepository,
            PlanningItemUpdateService updateService,
            PlanningItemRepository repository) {
        this.createService = createService;
        this.planningAccessControlService = planningAccessControlService;
        this.planningRepository = planningRepository;
        this.updateService = updateService;
        this.repository = repository;
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

    @PatchMapping("/{itemId}")
    public ResponseEntity<?> update(
            @PathVariable Integer planningId,
            @PathVariable Integer itemId,
            @RequestBody JsonPatch jsonPatch) throws Exception {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.UPDATE);
        PlanningItem item = this.repository.findById(itemId).orElseThrow(ResourceNotFoundException::new);

        this.updateService.processUpdate(item, jsonPatch, PlanningItemUpdateModel.class);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<PlanningItemResponse>> get(@PathVariable Integer planningId)
            throws ResourceNotFoundException, AccessDeniedException {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.READ);

        List<PlanningItem> result = this.repository.findByPlanning(planning);

        return ResponseEntity.ok(this.mapCollection(result, PlanningItemResponse.class));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<PlanningItemResponse> getParticular(
            @PathVariable Integer planningId,
            @PathVariable Integer itemId) throws ResourceNotFoundException, AccessDeniedException {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.READ);

        PlanningItem result = this.repository.findById(itemId).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(result, PlanningItemResponse.class));
    }

    @GetMapping("/focused")
    public ResponseEntity<PlanningItemResponse> getFocused(@PathVariable Integer planningId)
            throws ResourceNotFoundException, AccessDeniedException {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.READ);

        PlanningItem result = this.repository.findFirstByPlanningAndFocused(planning, true)
                .orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(result, PlanningItemResponse.class));
    }
}
