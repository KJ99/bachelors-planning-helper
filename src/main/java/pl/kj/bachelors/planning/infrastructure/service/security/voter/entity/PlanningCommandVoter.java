package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.entity.PlanningItem;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.CommandType;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;
import pl.kj.bachelors.planning.infrastructure.repository.PlanningItemRepository;

import java.util.List;
import java.util.Optional;

@Component
public class PlanningCommandVoter extends BasePlanningEntityVoter<CommandType> {
    private final PlanningItemRepository itemRepository;

    @Autowired
    public PlanningCommandVoter(MemberProvider memberProvider, PlanningItemRepository itemRepository) {
        super(memberProvider);
        this.itemRepository = itemRepository;
    }

    @Override
    protected CommandType[] getSupportedActions() {
        return CommandType.values();
    }

    @Override
    protected AccessVote voteInternal(Planning subject, CommandType action, TeamMember member) {
        Optional<PlanningItem> focused = itemRepository.findFirstByPlanningAndFocused(subject, true);
        boolean allow;
        switch (action) {
            case RUN:
                allow = subject.hasStatusIn(List.of(PlanningStatus.PROGRESSING)) && focused.isEmpty();
                break;
            case FOCUS:
            case FOCUS_NEXT:
                allow = subject.hasStatusIn(List.of(PlanningStatus.PROGRESSING)) && this.hasRole(member, Role.SCRUM_MASTER);
                break;
            case START_VOTING:
            case RESET_VOTES:
            case ESTIMATE:
                allow = subject.hasStatusIn(List.of(PlanningStatus.PROGRESSING)) &&
                        this.hasRole(member, Role.SCRUM_MASTER) &&
                        focused.isPresent();
                break;
            case STOP_VOTING:
                allow = subject.hasStatusIn(List.of(PlanningStatus.VOTING)) &&
                        this.hasRole(member, Role.SCRUM_MASTER);
                break;
            case VOTE:
                allow = subject.hasStatusIn(List.of(PlanningStatus.VOTING)) &&
                        this.hasRole(member, Role.TEAM_MEMBER);
                break;
            default:
                allow = true;
                break;
        }

        return allow ? AccessVote.ALLOW : AccessVote.DENY;
    }
}
