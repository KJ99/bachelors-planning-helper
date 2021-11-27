package pl.kj.bachelors.planning.unit.infrastructure.service.crud.update;

import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.exception.AggregatedApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.update.PlanningItemUpdateModel;
import pl.kj.bachelors.planning.domain.model.update.PlanningUpdateModel;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.crud.update.PlanningItemUpdateServiceImpl;
import pl.kj.bachelors.planning.infrastructure.service.crud.update.PlanningUpdateServiceImpl;
import pl.kj.bachelors.planning.model.PatchOperation;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.io.IOException;
import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class PlanningItemUpdateServiceTests extends BaseUnitTest {
    @Autowired
    private PlanningItemUpdateServiceImpl service;
    @Autowired
    private PlanningItemRepository repository;

    @Test
    public void testUpdate() throws IOException {
        PlanningItem original = this.repository.findById(1).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/title", "new title")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, PlanningItemUpdateModel.class));

        assertThat(thrown).isNull();
        assertThat(original.getTitle()).isEqualTo("new title");
    }

    @Test
    public void testUpdate_ValidationFails() throws IOException {
        PlanningItem original = this.repository.findById(1).orElseThrow();
        PatchOperation[] ops = new PatchOperation[] {
                new PatchOperation("replace", "/title", "   ")
        };
        String patchString = this.serialize(ops);
        JsonPatch patch = JsonPatch.fromJson(this.objectMapper.readTree(patchString));
        Throwable thrown = catchThrowable(() -> this.service.processUpdate(original, patch, PlanningItemUpdateModel.class));
        assertThat(thrown).isInstanceOf(AggregatedApiError.class);
        AggregatedApiError ex = (AggregatedApiError) thrown;
        assertThat(ex.getErrors()).hasSize(1);
    }
}
