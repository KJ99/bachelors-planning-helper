package pl.kj.bachelors.planning.unit.infrastructure.service.management;

import com.google.protobuf.Api;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kj.bachelors.planning.domain.exception.ApiError;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.entity.Vote;
import pl.kj.bachelors.planning.domain.model.extension.Estimation;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;
import pl.kj.bachelors.planning.infrastructure.repository.VoteRepository;
import pl.kj.bachelors.planning.infrastructure.service.management.VoteManagementService;
import pl.kj.bachelors.planning.unit.BaseUnitTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class VoteManagementServiceTests extends BaseUnitTest {
    @Autowired
    private VoteRepository repository;
    @Autowired
    private PlanningItemRepository itemRepository;
    @Autowired
    private VoteManagementService service;

    @Test
    @Transactional
    public void testVote() throws ApiError {
        String uid = "uid-100";
        PlanningItem item = this.itemRepository.findById(10).orElseThrow();
        Estimation value = Estimation.L;

        this.service.vote(item, uid, value);
        List<Vote> votes = this.repository.findByPlanningItem(item);
        assertThat(votes).isNotEmpty();
        assertThat(votes.stream().anyMatch(v -> v.getUserId().equals(uid)));
        assertThat(votes.stream().anyMatch(v -> v.getPlanningItem().getId() == item.getId()));
        assertThat(votes.stream().anyMatch(v -> v.getValue().equals(value)));
    }

    @Test
    @Transactional
    public void testVote_PlanningInWrongStatus() {
        String uid = "uid-100";
        PlanningItem item = this.itemRepository.findById(1).orElseThrow();
        Estimation value = Estimation.L;

        Throwable thrown = catchThrowable(() -> this.service.vote(item, uid, value));

        assertThat(thrown).isInstanceOf(ApiError.class);
        assertThat(((ApiError) thrown).getCode()).isEqualTo("PL.131");

    }

    @Test
    @Transactional
    public void testVote_ItemNotFocused() {
        String uid = "uid-100";
        PlanningItem item = this.itemRepository.findById(9).orElseThrow();
        Estimation value = Estimation.L;

        Throwable thrown = catchThrowable(() -> this.service.vote(item, uid, value));

        assertThat(thrown).isInstanceOf(ApiError.class);
        assertThat(((ApiError) thrown).getCode()).isEqualTo("PL.132");

    }

    @Test
    @Transactional
    public void testVote_AlreadyVoted() {
        String uid = "uid-2";
        PlanningItem item = this.itemRepository.findById(10).orElseThrow();
        Estimation value = Estimation.L;

        Throwable thrown = catchThrowable(() -> this.service.vote(item, uid, value));

        assertThat(thrown).isInstanceOf(ApiError.class);
        assertThat(((ApiError) thrown).getCode()).isEqualTo("PL.133");

    }

    @Test
    @Transactional
    public void testClearVotes() throws ApiError {
        PlanningItem item = this.itemRepository.findById(10).orElseThrow();

        this.service.clearVotes(item);

        assertThat(this.repository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    public void testClearVotes_PlanningInWrongStatus() {
        PlanningItem item = this.itemRepository.findById(1).orElseThrow();

        Throwable thrown = catchThrowable(() -> this.service.clearVotes(item));

        assertThat(thrown).isInstanceOf(ApiError.class);
        assertThat(((ApiError) thrown).getCode()).isEqualTo("PL.134");
    }

    @Test
    @Transactional
    public void testClearVotes_ItemNotFocused() {
        PlanningItem item = this.itemRepository.findById(9).orElseThrow();

        Throwable thrown = catchThrowable(() -> this.service.clearVotes(item));

        assertThat(thrown).isInstanceOf(ApiError.class);
        assertThat(((ApiError) thrown).getCode()).isEqualTo("PL.135");
    }
}
