package pl.kj.bachelors.planning.unit.infrastructure.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import pl.kj.bachelors.planning.domain.model.remote.UserProfile;
import pl.kj.bachelors.planning.infrastructure.service.remote.ProfileRemoteProviderImpl;
import pl.kj.bachelors.planning.infrastructure.service.user.ProfileProviderService;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class ProfileProviderServiceTests extends BaseUnitTest {
    @Autowired
    private ProfileProviderService service;
    @MockBean
    private ProfileRemoteProviderImpl remoteProvider;

    @BeforeEach
    public void setUp() {
        UserProfile profile1 = new UserProfile();
        profile1.setFirstName("Mark");
        profile1.setLastName("Doe");

        UserProfile profile2 = new UserProfile();
        profile2.setFirstName("Steven");
        profile2.setLastName("Doe");

        given(this.remoteProvider.get("uid-100")).willReturn(Optional.of(profile1));
        given(this.remoteProvider.get("uid-200")).willReturn(Optional.of(profile2));
    }

    @Test
    public void testGet_FromCache() {
        Optional<UserProfile> result = this.service.get("uid-100");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testGet_NoCache_Field() {
        this.service.setNoCache(true);
        Optional<UserProfile> result = this.service.get("uid-100");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Mark");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testGet_NoCache_Header() {
        this.requestHandlerMock.when(() -> RequestHandler.getHeaderValue(HttpHeaders.CACHE_CONTROL))
                .thenReturn(Optional.of("no-cache"));

        Optional<UserProfile> result = this.service.get("uid-100");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Mark");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testGet_NotFoundInCache() {
        Optional<UserProfile> result = this.service.get("uid-200");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Steven");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void testGet_NotFoundAnywhere() {
        Optional<UserProfile> result = this.service.get("fake-uid");

        assertThat(result).isEmpty();
    }
}
