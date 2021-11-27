package pl.kj.bachelors.planning.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.planning.application.dto.request.PagingQuery;
import pl.kj.bachelors.planning.application.dto.response.page.PageResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.domain.annotation.Authentication;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAction;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.search.PlanningSearchModel;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningCreateService;
import pl.kj.bachelors.planning.domain.service.crud.delete.PlanningDeleteService;
import pl.kj.bachelors.planning.domain.service.crud.read.PlanningReadService;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningUpdateService;
import pl.kj.bachelors.planning.domain.service.management.PlanningManager;
import pl.kj.bachelors.planning.domain.service.security.AccessControlService;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

import java.util.Map;

@RestController
@RequestMapping("/v1/plannings")
@Authentication
public class PlanningApiController extends BaseApiController {
    private final PlanningCreateService createService;
    private final PlanningUpdateService updateService;
    private final EntityAccessControlService<Planning> accessControl;
    private final PlanningRepository repository;
    private final PlanningReadService readService;
    private final PlanningDeleteService deleteService;
    private final AccessControlService<Integer, PlanningAdministrativeAction> createAndReadAccessControl;
    private final PlanningManager manager;

    @Autowired
    public PlanningApiController(
            PlanningCreateService createService,
            PlanningUpdateService updateService,
            EntityAccessControlService<Planning> accessControl,
            PlanningRepository repository,
            PlanningReadService readService,
            PlanningDeleteService deleteService,
            AccessControlService<Integer, PlanningAdministrativeAction> createAndReadAccessControl,
            PlanningManager manager) {
        this.createService = createService;
        this.updateService = updateService;
        this.accessControl = accessControl;
        this.repository = repository;
        this.readService = readService;
        this.deleteService = deleteService;
        this.createAndReadAccessControl = createAndReadAccessControl;
        this.manager = manager;
    }

    @PostMapping
    public ResponseEntity<PlanningResponse> post(@RequestBody PlanningCreateModel model) throws Exception {
        this.createAndReadAccessControl.ensureThatUserHasAccess(model.getTeamId(), PlanningAdministrativeAction.CREATE);
        Planning result = this.createService.create(model, Planning.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(result, PlanningResponse.class));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable Integer id, @RequestBody JsonPatch jsonPatch)
            throws Exception {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.UPDATE);

        this.updateService.processUpdate(planning, jsonPatch, PlanningUpdateModel.class);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<PlanningResponse>> get(
            @RequestParam("team-id") Integer teamId,
            @RequestParam Map<String, String> params) throws AccessDeniedException {
        this.createAndReadAccessControl.ensureThatUserHasAccess(teamId, PlanningAdministrativeAction.READ);

        PagingQuery query = this.parseQueryParams(params, PagingQuery.class);
        PlanningSearchModel search = this.parseQueryParams(params, PlanningSearchModel.class);
        Pageable pageable = PageRequest.of(query.getPage(), query.getPageSize());

        Page<Planning> page = this.readService.readPagedByTeam(teamId, pageable, search);

        return ResponseEntity.ok(this.createPageResponse(page, PlanningResponse.class));
    }


    @GetMapping("/incoming")
    public ResponseEntity<PlanningResponse> getIncoming(@RequestParam("team-id") Integer teamId)
            throws AccessDeniedException, ResourceNotFoundException {
        this.createAndReadAccessControl.ensureThatUserHasAccess(teamId, PlanningAdministrativeAction.READ);
        Planning planning = this.readService.readIncoming(teamId).orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(this.map(planning, PlanningResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanningResponse> getParticular(@PathVariable Integer id)
            throws AccessDeniedException, ResourceNotFoundException {
        Planning planning = this.readService.readParticular(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.READ);

        return ResponseEntity.ok(this.map(planning, PlanningResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.DELETE);

        this.deleteService.delete(planning);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> start(@PathVariable Integer id)
            throws ResourceNotFoundException, AccessDeniedException, ApiError {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAction.START);

        this.manager.open(planning);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<?> finish(@PathVariable Integer id)
            throws ResourceNotFoundException, AccessDeniedException, ApiError {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAction.COMPLETE);

        this.manager.close(planning);

        return ResponseEntity.noContent().build();
    }

}
