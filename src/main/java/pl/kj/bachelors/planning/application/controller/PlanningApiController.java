package pl.kj.bachelors.planning.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.domain.annotation.Authentication;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningCreateService;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningUpdateService;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;

@RestController
@RequestMapping("/v1/plannings")
@Authentication
public class PlanningApiController extends BaseApiController {
    private final PlanningCreateService createService;
    private final PlanningUpdateService updateService;
    private final EntityAccessControlService<Planning> accessControl;
    private final PlanningRepository repository;

    @Autowired
    public PlanningApiController(
            PlanningCreateService createService,
            PlanningUpdateService updateService,
            EntityAccessControlService<Planning> accessControl,
            PlanningRepository repository) {
        this.createService = createService;
        this.updateService = updateService;
        this.accessControl = accessControl;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<PlanningResponse> post(@RequestBody PlanningCreateModel model) throws Exception {
        Planning temp = new Planning();
        temp.setTeamId(model.getTeamId());
        this.accessControl.ensureThatUserHasAccess(temp, PlanningAdministrativeAction.CREATE);
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
}
