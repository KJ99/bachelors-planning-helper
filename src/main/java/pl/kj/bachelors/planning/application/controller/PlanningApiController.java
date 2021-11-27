package pl.kj.bachelors.planning.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.domain.annotation.Authentication;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningCreateService;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;

@RestController
@RequestMapping("/v1/plannings")
@Authentication
public class PlanningApiController extends BaseApiController {
    private final PlanningCreateService createService;
    private final EntityAccessControlService<Planning> accessControl;

    @Autowired
    public PlanningApiController(
            PlanningCreateService createService,
            EntityAccessControlService<Planning> accessControl) {
        this.createService = createService;
        this.accessControl = accessControl;
    }

    @PostMapping
    @Transactional(rollbackFor = {AccessDeniedException.class, AggregatedApiError.class})
    public ResponseEntity<PlanningResponse> post(@RequestBody PlanningCreateModel model) throws Exception {
        Planning temp = new Planning();
        temp.setTeamId(model.getTeamId());
        this.accessControl.ensureThatUserHasAccess(temp, PlanningAdministrativeAction.CREATE);
        Planning result = this.createService.create(model, Planning.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(result, PlanningResponse.class));
    }
}
