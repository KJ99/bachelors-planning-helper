package pl.kj.bachelors.planning.unit.infrastructure.service.crud.update;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.AccessDeniedException;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.crud.update.PlanningUpdateServiceImpl;
import pl.kj.bachelors.planning.infrastructure.user.RequestHandler;
import pl.kj.bachelors.planning.model.PatchOperation;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.when;

public class PlanningUpdateServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningUpdateServiceImpl service;
    @Autowired
    private PlanningRepository repository;

    @BeforeEach
    public void setUp() {
        when(RequestHandler.getRequestTimeZone()).thenReturn(Optional.of(TimeZone.getTimeZone("Europe/London")));
    }

    @Test
    public void testUpdate() throws IOException {
        Planning original = this.repository.findById(1).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/title", "hello"),
                new PatchOperation("replace", "/start_date", "4999-06-06 12:20:20")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, PlanningUpdateModel.class));

        assertThat(thrown).isNull();
        assertThat(original.getTitle()).isEqualTo("hello");
        assertThat(original.getStartAt().get(Calendar.YEAR)).isEqualTo(4999);
    }

    @Test
    public void testUpdate_ValidationFails() throws IOException {
        Planning original = this.repository.findById(1).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/title", "   "),
                new PatchOperation("replace", "/start_date", "2000-06-06 12:20:20")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, PlanningUpdateModel.class));

        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
    }

    @Test
    public void testUpdate_AccessDenied() throws IOException {
        Planning original = this.repository.findById(2).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/title", "hello"),
                new PatchOperation("replace", "/start_date", "4999-06-06 12:20:20")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, PlanningUpdateModel.class));

        assertThat(thrown).isInstanceOf(AccessDeniedException.class);
    }
}
