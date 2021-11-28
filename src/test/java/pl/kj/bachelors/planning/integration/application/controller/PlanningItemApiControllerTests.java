package pl.kj.bachelors.planning.integration.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.planning.application.dto.request.SetEstimationRequest;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningItemResponse;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.integration.BaseIntegrationTest;
import pl.kj.bachelors.planning.model.PatchOperation;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanningItemApiControllerTests extends BaseIntegrationTest {
    @MockBean
    private MemberProvider memberProvider;

    @BeforeEach
    public void setUp() {
        given(this.memberProvider.get(1, "uid-1"))
                .willReturn(Optional.of(this.createTeamMember("uid-1", List.of(Role.PRODUCT_OWNER))));
        given(this.memberProvider.get(1, "uid-2"))
                .willReturn(Optional.of(this.createTeamMember("uid-1", List.of(Role.SCRUM_MASTER))));
        given(this.memberProvider.get(1, "uid-100"))
                .willReturn(Optional.of(this.createTeamMember("uid-100", List.of(Role.TEAM_MEMBER))));
    }

    @Test
    public void testPost_Created() throws Exception {
        PlanningItemCreateModel model = new PlanningItemCreateModel();
        model.setTitle("Task foobar");
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(model);

        this.mockMvc.perform(
                post("/v1/plannings/1/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isCreated());
    }

    @Test
    public void testPost_BadRequest() throws Exception {
        PlanningItemCreateModel model = new PlanningItemCreateModel();
        model.setTitle("  ");
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(model);

        this.mockMvc.perform(
                post("/v1/plannings/1/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPost_Unauthorized() throws Exception {
        PlanningItemCreateModel model = new PlanningItemCreateModel();
        model.setTitle("Task foobar");
        String requestBody = this.serialize(model);

        this.mockMvc.perform(
                post("/v1/plannings/1/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPost_Forbidden() throws Exception {
        PlanningItemCreateModel model = new PlanningItemCreateModel();
        model.setTitle("Task foobar");
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        String requestBody = this.serialize(model);

        this.mockMvc.perform(
                post("/v1/plannings/1/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testPatch_NoContent() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Task 150")
        });
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                patch("/v1/plannings/1/items/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testPatch_BadRequest() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "   ")
        });
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                patch("/v1/plannings/1/items/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPatch_Unauthorized() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Task 150")
        });

        this.mockMvc.perform(
                patch("/v1/plannings/1/items/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch_Forbidden() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Task 150")
        });
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));

        this.mockMvc.perform(
                patch("/v1/plannings/1/items/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGet_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings/1/items")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGet_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/1/items")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGet_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/plannings/1/items")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetParticular_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings/1/items/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetParticular_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/1/items/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetParticular_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/plannings/1/items/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetFocused_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings/1/items/focused")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetFocused_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/1/items/focused")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetFocused_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/plannings/1/items/focused")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testDelete_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/plannings/1/items/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/plannings/1/items/-1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testDelete_Unauthorized() throws Exception {
        this.mockMvc.perform(
                delete("/v1/plannings/1/items/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                delete("/v1/plannings/1/items/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testImportFromCsv() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String content =
                        "title,description\n" +
                        "Task 1,Task 1 desc\n" +
                        "Task 2,Task 2 desc";
        var mockFile = new MockMultipartFile("file", content.getBytes(StandardCharsets.UTF_8));
        this.mockMvc.perform(
                multipart("/v1/plannings/1/items/import")
                        .file(mockFile)
                        .header(HttpHeaders.AUTHORIZATION, auth)

        ).andExpect(status().isNoContent());
    }

    @Test
    public void testFocus_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        this.mockMvc.perform(
                put("/v1/plannings/4/items/3/focus")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testFocus_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        this.mockMvc.perform(
                put("/v1/plannings/1/items/1/focus")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testFocus_Unauthorized() throws Exception {
        this.mockMvc.perform(
                put("/v1/plannings/4/items/3/focus")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testFocus_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                put("/v1/plannings/4/items/3/focus")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testFocusNext_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        MvcResult result = this.mockMvc.perform(
                put("/v1/plannings/4/items/next/focus")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk()).andReturn();
        PlanningItemResponse response = this.deserialize(
                result.getResponse().getContentAsString(),
                PlanningItemResponse.class
        );
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(5);
    }

    @Test
    public void testFocusNext_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        this.mockMvc.perform(
                put("/v1/plannings/1/items/next/focus")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testFocusNext_Unauthorized() throws Exception {
        this.mockMvc.perform(
                put("/v1/plannings/4/items/next/focus")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testFocusNext_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                put("/v1/plannings/4/items/next/focus")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testEstimate_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        this.mockMvc.perform(
                put("/v1/plannings/4/items/4/estimate")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"value\": \"M\"}")
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testEstimate_BadRequest_WrongValue() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        this.mockMvc.perform(
                put("/v1/plannings/4/items/4/estimate")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"value\": \"fake-estim\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testEstimate_BadRequest_NotFocused() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-2"));
        var request = new SetEstimationRequest();
        request.setValue(Estimation.M.name());
        this.mockMvc.perform(
                put("/v1/plannings/4/items/5/estimate")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testEstimate_Unauthorized() throws Exception {
        var request = new SetEstimationRequest();
        request.setValue(Estimation.M.name());
        this.mockMvc.perform(
                put("/v1/plannings/4/items/4/estimate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testEstimate_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        var request = new SetEstimationRequest();
        request.setValue(Estimation.M.name());
        this.mockMvc.perform(
                put("/v1/plannings/4/items/4/estimate")
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
        ).andExpect(status().isForbidden());
    }

    private TeamMember createTeamMember(String uid, List<Role> roles) {
        var member = new TeamMember();
        member.setUserId(uid);
        member.setRoles(roles);

        return member;
    }
}
