package pl.kj.bachelors.planning.integration.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import pl.kj.bachelors.planning.domain.model.create.PlanningItemCreateModel;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.integration.BaseIntegrationTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlanningItemApiControllerTests extends BaseIntegrationTest {
    @MockBean
    private MemberProvider memberProvider;

    @BeforeEach
    public void setUp() {
        given(this.memberProvider.get(1, "uid-1"))
                .willReturn(Optional.of(this.createTeamMember("uid-1", List.of(Role.PRODUCT_OWNER))));
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

    private TeamMember createTeamMember(String uid, List<Role> roles) {
        var member = new TeamMember();
        member.setUserId(uid);
        member.setRoles(roles);

        return member;
    }
}
