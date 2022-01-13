package pl.kj.bachelors.planning.application.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kj.bachelors.planning.application.dto.request.PagingQuery;
import pl.kj.bachelors.planning.application.dto.response.error.ValidationErrorResponse;
import pl.kj.bachelors.planning.application.dto.response.page.PageResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.IncomingPlanningResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningTokenResponse;
import pl.kj.bachelors.planning.application.example.PlanningResponsePageExample;
import pl.kj.bachelors.planning.domain.annotation.Authentication;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.exception.ResourceNotFoundException;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAction;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.report.PlanningReport;
import pl.kj.bachelors.planning.domain.model.search.PlanningSearchModel;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;
import pl.kj.bachelors.planning.domain.service.crud.create.PlanningCreateService;
import pl.kj.bachelors.planning.domain.service.crud.delete.PlanningDeleteService;
import pl.kj.bachelors.planning.domain.service.crud.read.PlanningReadService;
import pl.kj.bachelors.planning.domain.service.crud.update.PlanningUpdateService;
import pl.kj.bachelors.planning.domain.service.management.PlanningManager;
import pl.kj.bachelors.planning.domain.service.report.PlanningReportCreator;
import pl.kj.bachelors.planning.domain.service.report.PlanningReportExporter;
import pl.kj.bachelors.planning.domain.service.security.AccessControlService;
import pl.kj.bachelors.planning.domain.service.security.EntityAccessControlService;
import pl.kj.bachelors.planning.domain.service.security.JwtGenerator;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/plannings")
@Authentication
@Tag(name = "Planning")
public class PlanningApiController extends BaseApiController {
    private final PlanningCreateService createService;
    private final PlanningUpdateService updateService;
    private final EntityAccessControlService<Planning> accessControl;
    private final PlanningRepository repository;
    private final PlanningReadService readService;
    private final PlanningDeleteService deleteService;
    private final AccessControlService<Integer, PlanningAdministrativeAction> createAndReadAccessControl;
    private final PlanningManager manager;
    private final JwtGenerator jwtGenerator;
    private final PlanningReportExporter exporter;
    private final PlanningReportCreator reporter;

    @Autowired
    public PlanningApiController(
            PlanningCreateService createService,
            PlanningUpdateService updateService,
            EntityAccessControlService<Planning> accessControl,
            PlanningRepository repository,
            PlanningReadService readService,
            PlanningDeleteService deleteService,
            AccessControlService<Integer, PlanningAdministrativeAction> createAndReadAccessControl,
            PlanningManager manager,
            JwtGenerator jwtGenerator,
            PlanningReportExporter exporter,
            PlanningReportCreator reporter) {
        this.createService = createService;
        this.updateService = updateService;
        this.accessControl = accessControl;
        this.repository = repository;
        this.readService = readService;
        this.deleteService = deleteService;
        this.createAndReadAccessControl = createAndReadAccessControl;
        this.manager = manager;
        this.jwtGenerator = jwtGenerator;
        this.exporter = exporter;
        this.reporter = reporter;
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanningResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PlanningResponse> post(@RequestBody PlanningCreateModel model) throws Exception {
        this.createAndReadAccessControl.ensureThatUserHasAccess(model.getTeamId(), PlanningAdministrativeAction.CREATE);
        Planning result = this.createService.create(model, Planning.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.map(result, PlanningResponse.class));
    }

    @PatchMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorResponse.class))
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> patch(@PathVariable Integer id, @RequestBody JsonPatch jsonPatch)
            throws Exception {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.UPDATE);

        this.updateService.processUpdate(planning, jsonPatch, PlanningUpdateModel.class);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanningResponsePageExample.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true))),
    })
    @SecurityRequirement(name = "JWT")
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IncomingPlanningResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<IncomingPlanningResponse> getIncoming(@RequestParam("team-id") Integer teamId)
            throws AccessDeniedException, ResourceNotFoundException {
        this.createAndReadAccessControl.ensureThatUserHasAccess(teamId, PlanningAdministrativeAction.READ);
        Optional<Planning> planning = this.readService.readIncoming(teamId);
        IncomingPlanningResponse response = new IncomingPlanningResponse();
        response.setScheduled(planning.isPresent());
        planning.ifPresent(data -> response.setData(this.map(data, PlanningResponse.class)));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanningResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PlanningResponse> getParticular(@PathVariable Integer id)
            throws AccessDeniedException, ResourceNotFoundException {
        Planning planning = this.readService.readParticular(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.READ);

        return ResponseEntity.ok(this.map(planning, PlanningResponse.class));
    }

    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.DELETE);

        this.deleteService.delete(planning);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/start")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<?> start(@PathVariable Integer id)
            throws ResourceNotFoundException, AccessDeniedException, ApiError {
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAction.START);

        this.manager.open(planning);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlanningTokenResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PlanningTokenResponse> getToken(@PathVariable Integer id)
            throws ResourceNotFoundException, AccessDeniedException {
        String uid = RequestHandler.getCurrentUserId().orElseThrow(AccessDeniedException::new);
        Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        this.accessControl.ensureThatUserHasAccess(planning, PlanningAction.JOIN);
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("pid", planning.getId());

        String token = this.jwtGenerator.generateWebSocketAccessToken(uid, tokenData);

        PlanningTokenResponse response = new PlanningTokenResponse();
        response.setAccessToken(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/report")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(hidden = true)
                    )
            ),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
    })
    @SecurityRequirement(name = "JWT")
    public void getReport(@PathVariable Integer id, HttpServletResponse response) {
        try {
            Planning planning = this.repository.findById(id).orElseThrow(ResourceNotFoundException::new);
            this.accessControl.ensureThatUserHasAccess(planning, PlanningAdministrativeAction.EXPORT_REPORT);
            PlanningReport report = this.reporter.createReport(planning);
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            this.exporter.exportTableReport(report, response.getOutputStream());
        } catch (AccessDeniedException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } catch (ResourceNotFoundException e) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


    }
}
