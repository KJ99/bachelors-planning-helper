package pl.kj.bachelors.planning.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningItemResponse;
import pl.kj.bachelors.planning.domain.annotation.Authentication;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAction;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningItemAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.update.PlanningItemUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningItemCreateService;
import pl.kj.bachelors.planning.domain.service.crud.delete.PlanningItemDeleteService;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningItemUpdateService;
import pl.kj.bachelors.planning.domain.service.file.CsvImporter;
import pl.kj.bachelors.planning.domain.service.management.FocusManager;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

import javax.servlet.annotation.MultipartConfig;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/v1/plannings/{planningId}/items")
@Authentication
@MultipartConfig(maxFileSize = 10 * 1024 * 1024)
public class PlanningItemApiController extends BaseApiController {
    private final PlanningItemCreateService createService;
    private final EntityAccessControlService<Planning> planningAccessControlService;
    private final PlanningRepository planningRepository;
    private final PlanningItemUpdateService updateService;
    private final PlanningItemRepository repository;
    private final PlanningItemDeleteService deleteService;
    private final CsvImporter csvImporter;
    private final FocusManager focusManager;

    @Autowired
    public PlanningItemApiController(
            PlanningItemCreateService createService,
            EntityAccessControlService<Planning> planningAccessControlService,
            PlanningRepository planningRepository,
            PlanningItemUpdateService updateService,
            PlanningItemRepository repository,
            PlanningItemDeleteService deleteService,
            CsvImporter csvImporter,
            FocusManager focusManager) {
        this.createService = createService;
        this.planningAccessControlService = planningAccessControlService;
        this.planningRepository = planningRepository;
        this.updateService = updateService;
        this.repository = repository;
        this.deleteService = deleteService;
        this.csvImporter = csvImporter;
        this.focusManager = focusManager;
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
        PlanningItem item = this.repository.findFirstByIdAndPlanning(itemId, planning)
                .orElseThrow(ResourceNotFoundException::new);

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

        PlanningItem result = this.repository.findFirstByIdAndPlanning(itemId, planning)
                .orElseThrow(ResourceNotFoundException::new);

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

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> delete(@PathVariable Integer planningId, @PathVariable Integer itemId) throws Exception {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.DELETE);
        PlanningItem entity = this.repository.findFirstByIdAndPlanning(itemId, planning)
                .orElseThrow(ResourceNotFoundException::new);

        this.deleteService.delete(entity);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importFromCsv(
            @RequestParam("file") MultipartFile file,
            @PathVariable Integer planningId
    ) throws Exception {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningItemAdministrativeAction.CREATE);
        List<PlanningItemCreateModel> models = this.csvImporter.importFromCsv(file, PlanningItemCreateModel.class);
        for (PlanningItemCreateModel model : models) {
            model.setPlanning(planning);
            this.createService.create(model, PlanningItem.class);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{itemId}/focus")
    public ResponseEntity<?> focus(@PathVariable Integer planningId, @PathVariable Integer itemId)
            throws ResourceNotFoundException, AccessDeniedException, ApiError {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningAction.CHANGE_CURRENT);
        PlanningItem item = this.repository.findFirstByIdAndPlanning(itemId, planning)
                .orElseThrow(ResourceNotFoundException::new);


        this.focusManager.focus(item);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/next/focus")
    public ResponseEntity<?> focus(@PathVariable Integer planningId)
            throws ResourceNotFoundException, AccessDeniedException, ApiError {
        Planning planning = this.planningRepository.findById(planningId).orElseThrow(ResourceNotFoundException::new);
        this.planningAccessControlService.ensureThatUserHasAccess(planning, PlanningAction.CHANGE_CURRENT);

        this.focusManager.focusNext(planning);

        var focused = this.repository.findFirstByPlanningAndFocused(planning, true).orElseThrow();

        return ResponseEntity.ok(this.map(focused, PlanningItemResponse.class));
    }
}
