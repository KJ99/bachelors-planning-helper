package pl.kj.bachelors.planning.unit.infrastructure.service.management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningRepository;
import pl.kj.bachelors.planning.infrastructure.service.management.FocusManagementService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FocusManagementServiceTests extends BaseUnitTest {
    @Autowired
    private FocusManagementService service;
    @Autowired
    private PlanningItemRepository itemRepository;
    @Autowired
    private PlanningRepository planningRepository;

    @Test
    @Transactional
    public void testFocus() {
        PlanningItem item = this.itemRepository.findById(3).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.focus(item));
        assertThat(thrown).isNull();
        assertThat(item.isFocused()).isTrue();
    }

    @Test
    @Transactional
    public void testFocus_WrongPlanningStatus() {
        PlanningItem item = this.itemRepository.findById(1).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.focus(item));
        assertThat(thrown).isInstanceOf(ApiError.class);
    }

    @Test
    @Transactional
    public void testFocusNext() {
        Planning planning = this.planningRepository.findById(4).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.focusNext(planning));
        assertThat(thrown).isNull();
        Optional<PlanningItem> item = this.itemRepository.findFirstByPlanningAndFocused(planning, true);
        assertThat(item).isPresent();
        assertThat(item.get().getId()).isEqualTo(5);

    }

    @Test
    @Transactional
    public void testFocusNext_WrongPlanningStatus() {
        Planning planning = this.planningRepository.findById(1).orElseThrow();
        Throwable thrown = catchThrowable(() -> this.service.focusNext(planning));
        assertThat(thrown).isInstanceOf(ApiError.class);
    }

    @Test
    @Transactional
    public void hasNext_ReturnsTrue() {
        Planning planning = this.planningRepository.findById(4).orElseThrow();
        assertThat(this.service.hasNext(planning)).isTrue();
    }

    @Test
    @Transactional
    public void hasNext_ReturnsFalse() {
        Planning planning = this.planningRepository.findById(5).orElseThrow();
        assertThat(this.service.hasNext(planning)).isFalse();
    }
}
