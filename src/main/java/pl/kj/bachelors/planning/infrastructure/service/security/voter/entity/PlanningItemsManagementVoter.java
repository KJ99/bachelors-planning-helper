package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningItemAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

@Component
public class PlanningItemsManagementVoter  extends BasePlanningEntityVoter<PlanningItemAdministrativeAction> {
    @Autowired
    protected PlanningItemsManagementVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected PlanningItemAdministrativeAction[] getSupportedActions() {
        return PlanningItemAdministrativeAction.values();
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningItemAdministrativeAction action, TeamMember member) {
        AccessVote vote;
        switch (action) {
            case CREATE:
                vote = this.hasRole(member, Role.PRODUCT_OWNER) ? AccessVote.ALLOW : AccessVote.DENY;
                break;
            case UPDATE:
            case DELETE:
                vote = subject.getStatus().equals(PlanningStatus.SCHEDULED) ? AccessVote.ALLOW : AccessVote.DENY;
                break;
            case READ:
                vote = AccessVote.ALLOW;
                break;
            default:
                vote = AccessVote.OMIT;
                break;
        }

        return vote;
    }
}
