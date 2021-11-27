package pl.kj.bachelors.planning.unit.infrastructure.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.model.remote.UserProfile;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.infrastructure.service.remote.MemberRemoteProviderImpl;
import pl.kj.bachelors.planning.infrastructure.service.remote.ProfileRemoteProviderImpl;
import pl.kj.bachelors.planning.infrastructure.service.user.MemberProviderService;
import pl.kj.bachelors.planning.infrastructure.service.user.ProfileProviderService;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MemberProviderServiceTests extends BaseUnitTest {
    @Autowired
    private MemberProviderService service;
    @MockBean
    private MemberRemoteProviderImpl remoteProvider;

    @BeforeEach
    public void setUp() {
        TeamMember member = new TeamMember();
        member.setUserId("uid-100");
        member.setRoles(Arrays.asList(Role.OWNER, Role.SCRUM_MASTER));
        TeamMember member2 = new TeamMember();
        member2.setUserId("uid-200");
        member2.setRoles(List.of(Role.TEAM_MEMBER));

        given(this.remoteProvider.get(1, "uid-100")).willReturn(Optional.of(member));
        given(this.remoteProvider.get(1, "uid-200")).willReturn(Optional.of(member2));
    }
    @Test
    public void testGet_FromCache() {
        Optional<TeamMember> result = this.service.get(1, "uid-100");

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo("uid-100");
        assertThat(result.get().getRoles()).contains(Role.ADMIN).contains(Role.PRODUCT_OWNER);
    }

    @Test
    public void testGet_NoCache_Field() {
        this.service.setNoCache(true);
        Optional<TeamMember> result = this.service.get(1, "uid-100");

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo("uid-100");
        assertThat(result.get().getRoles()).contains(Role.OWNER).contains(Role.SCRUM_MASTER);
    }

    @Test
    public void testGet_NoCache_Header() {
        this.requestHandlerMock.when(() -> RequestHandler.getHeaderValue(HttpHeaders.CACHE_CONTROL))
                .thenReturn(Optional.of("no-cache"));

        Optional<TeamMember> result = this.service.get(1, "uid-100");

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo("uid-100");
        assertThat(result.get().getRoles()).contains(Role.OWNER).contains(Role.SCRUM_MASTER);
    }

    @Test
    public void testGet_NotFoundInCache() {
        Optional<TeamMember> result = this.service.get(1, "uid-200");

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo("uid-200");
        assertThat(result.get().getRoles()).contains(Role.TEAM_MEMBER);
    }

    @Test
    public void testGet_NotFoundAnywhere() {
        Optional<TeamMember> result = this.service.get(1, "fake-uid");

        assertThat(result).isEmpty();
    }
}
