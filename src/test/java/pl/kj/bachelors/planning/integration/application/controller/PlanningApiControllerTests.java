package pl.kj.bachelors.planning.integration.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.planning.application.dto.request.ChangeVotingStatusRequest;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningResponse;
import pl.kj.bachelors.planning.application.dto.response.planning.PlanningTokenResponse;
import pl.kj.bachelors.planning.domain.model.create.PlanningCreateModel;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.Team;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.TeamProvider;
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
    @MockBean
    private TeamProvider teamProvider;

    @BeforeEach
    public void setUp() {
        given(this.memberProvider.get(1, "uid-1"))
                .willReturn(Optional.of(this.createTeamMember("uid-1", List.of(Role.SCRUM_MASTER))));
        given(this.memberProvider.get(1, "uid-100"))
                .willReturn(Optional.of(this.createTeamMember("uid-100", List.of(Role.TEAM_MEMBER))));
        given(this.memberProvider.get(120, "uid-1"))
                .willReturn(Optional.of(this.createTeamMember("uid-1", List.of(Role.SCRUM_MASTER))));
        given(this.memberProvider.get(120, "uid-100"))
                .willReturn(Optional.of(this.createTeamMember("uid-100", List.of(Role.TEAM_MEMBER))));
        given(this.teamProvider.get(120))
                .willReturn(Optional.of(this.createTeam()));
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
    public void testGetToken_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        MvcResult result = this.mockMvc.perform(
                get("/v1/plannings/4/token")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk()).andReturn();
        String responseBody = result.getResponse().getContentAsString();
        PlanningTokenResponse response = this.deserialize(responseBody, PlanningTokenResponse.class);
        assertThat(response.getAccessToken()).isNotEmpty();
    }

    @Test
    public void testGetToken_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/4/token")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetToken_Forbidden_PlanningNotStarted() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                get("/v1/plannings/1/token")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetToken_Forbidden_NotMember() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-900"));
        this.mockMvc.perform(
                get("/v1/plannings/4/token")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetReport_Ok() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        MvcResult result = this.mockMvc.perform(
                get("/v1/plannings/100/report")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsByteArray()).isNotEmpty();
    }

    @Test
    public void testGetReport_Forbidden_NotFinishedPlanning() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-1"));
        this.mockMvc.perform(
                get("/v1/plannings/4/report")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetReport_Forbidden_InsufficientRole() throws Exception {
        String auth = String.format("%s %s", this.jwtConfig.getType(), this.generateValidAccessToken("uid-100"));
        this.mockMvc.perform(
                get("/v1/plannings/100/report")
                        .header(HttpHeaders.AUTHORIZATION, auth)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void testGetReport_Forbidden_Unauthorized() throws Exception {
        this.mockMvc.perform(
                get("/v1/plannings/100/report")
        ).andExpect(status().isUnauthorized());
    }

    private TeamMember createTeamMember(String uid, List<Role> roles) {
        var member = new TeamMember();
        member.setUserId(uid);
        member.setRoles(roles);

        return member;
    }

    private Team createTeam() {
        Team team = new Team();
        team.setId(120);
        team.setName("team 1");

        return team;
    }
}
