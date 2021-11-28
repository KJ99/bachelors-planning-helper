package pl.kj.bachelors.planning.integration.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.planning.application.dto.request.ChangeVotingStatusRequest;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.integration.BaseIntegrationTest;
import pl.kj.bachelors.planning.model.PatchOperation;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanningApiControllerTests extends BaseIntegrationTest {

    @MockBean
    private MemberProvider memberProvider;

    @BeforeEach
    public void setUp() {
        given(this.memberProvider.get(1, "uid-1"))
                .willReturn(Optional.of(this.createTeamMember("uid-1", List.of(Role.SCRUM_MASTER))));
        given(this.memberProvider.get(1, "uid-100"))
                .willReturn(Optional.of(this.createTeamMember("uid-100", List.of(Role.TEAM_MEMBER))));
    }

    @Test
    public void testPost_Created() throws Exception {
        var model = new PlanningCreateModel();
        model.setTitle("Test planning #1");
        model.setTeamId(1);
        model.setStartDate("2025-01-01 23:59:59");
        String requestBody = this.serialize(model);
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        MvcResult result = this.mockMvc.perform(
                post("/v1/plannings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .content(requestBody)
        ).andExpect(status().isCreated()).andReturn();
        PlanningResponse response = this.deserialize(result.getResponse().getContentAsString(), PlanningResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isPositive();
        assertThat(response.getStatus()).isEqualTo(PlanningStatus.SCHEDULED.name());
        assertThat(response.getStartDate()).isGreaterThan(Calendar.getInstance());
    }

    @Test
    public void testPost_BadRequest() throws Exception {
        var model = new PlanningCreateModel();
        model.setTitle(" ");
        model.setTeamId(1);
        model.setStartDate("2001-01-01 23:59:59");
        String requestBody = this.serialize(model);
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));

        this.mockMvc.perform(
                post("/v1/plannings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .content(requestBody)
        ).andExpect(status().isBadRequest());

    }

    @Test
    public void testPost_Unauthorized() throws Exception {
        var model = new PlanningCreateModel();
        model.setTitle(" ");
        model.setTeamId(1);
        model.setStartDate("2001-01-01 23:59:59");
        String requestBody = this.serialize(model);

        this.mockMvc.perform(
                post("/v1/plannings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPost_Forbidden() throws Exception {
        var model = new PlanningCreateModel();
        model.setTitle("Test planning #1");
        model.setTeamId(1);
        model.setStartDate("2025-01-01 23:59:59");
        String requestBody = this.serialize(model);
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));

        this.mockMvc.perform(
                post("/v1/plannings")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .content(requestBody)
        ).andExpect(status().isForbidden());

    }

    @Test
    public void testPatch_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Sprint #1111"),
                new PatchOperation("replace", "/start_date", "2999-01-01 12:12:12")
        });
        this.mockMvc.perform(
                patch("/v1/plannings/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testPatch_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Sprint #1111"),
                new PatchOperation("replace", "/start_date", "2000-01-01 12:12:12")
        });
        this.mockMvc.perform(
                patch("/v1/plannings/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testPatch_Unauthorized() throws Exception {
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Sprint #1111"),
                new PatchOperation("replace", "/start_date", "2999-01-01 12:12:12")
        });
        this.mockMvc.perform(
                patch("/v1/plannings/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        String requestBody = this.serialize(new PatchOperation[] {
                new PatchOperation("replace", "/title", "Sprint #1111"),
                new PatchOperation("replace", "/start_date", "2999-01-01 12:12:12")
        });
        this.mockMvc.perform(
                patch("/v1/plannings/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGet_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings?team-id=1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGet_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings?team-id=1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGet_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/plannings?team-id=1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetParticular_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetParticular_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetParticular_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/plannings/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetIncoming_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings/incoming?team-id=1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk());
    }

    @Test
    public void testGetIncoming_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/incoming?team-id=1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetIncoming_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                get("/v1/plannings/incoming?team-id=1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testDelete_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                delete("/v1/plannings/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testDelete_Unauthorized() throws Exception {
        this.mockMvc.perform(
                delete("/v1/plannings/1")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testDelete_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-300"));
        this.mockMvc.perform(
                delete("/v1/plannings/1")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testStart_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                put("/v1/plannings/1/start")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testStart_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                put("/v1/plannings/2/start")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testStart_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                put("/v1/plannings/1/start")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testClose_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                put("/v1/plannings/4/finish")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testClose_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                put("/v1/plannings/1/finish")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testClose_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                put("/v1/plannings/4/finish")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testChangeVotingStatus_True_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        ChangeVotingStatusRequest request = new ChangeVotingStatusRequest();
        request.setEnabled(true);
        this.mockMvc.perform(
                put("/v1/plannings/4/voting")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testChangeVotingStatus_False_NoContent() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        ChangeVotingStatusRequest request = new ChangeVotingStatusRequest();
        request.setEnabled(false);
        this.mockMvc.perform(
                put("/v1/plannings/6/voting")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void testChangeVotingStatus_True_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        ChangeVotingStatusRequest request = new ChangeVotingStatusRequest();
        request.setEnabled(true);
        this.mockMvc.perform(
                put("/v1/plannings/1/voting")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeVotingStatus_False_BadRequest() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        ChangeVotingStatusRequest request = new ChangeVotingStatusRequest();
        request.setEnabled(false);
        this.mockMvc.perform(
                put("/v1/plannings/4/voting")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeVotingStatus_Unauthorized() throws Exception {
        ChangeVotingStatusRequest request = new ChangeVotingStatusRequest();
        request.setEnabled(true);
        this.mockMvc.perform(
                put("/v1/plannings/4/voting")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testChangeVotingStatus_Forbidden() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        ChangeVotingStatusRequest request = new ChangeVotingStatusRequest();
        request.setEnabled(true);
        this.mockMvc.perform(
                put("/v1/plannings/4/voting")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.serialize(request))
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    private TeamMember createTeamMember(String uid, List<Role> roles) {
        var member = new TeamMember();
        member.setUserId(uid);
        member.setRoles(roles);

        return member;
    }
}
