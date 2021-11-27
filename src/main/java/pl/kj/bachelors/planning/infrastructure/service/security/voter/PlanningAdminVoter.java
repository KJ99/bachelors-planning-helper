package pl.kj.bachelors.planning.infrastructure.service.security.voter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

@Component
public class PlanningAdminVoter extends BasePlanningVoter<PlanningAdministrativeAction> {

    @Autowired
    protected PlanningAdminVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningAdministrativeAction action, TeamMember member) {
        AccessVote vote;
        switch (action) {
            case READ:
                vote = AccessVote.ALLOW;
                break;
            case CREATE:
            case UPDATE:
                vote = this.hasRole(member, Role.SCRUM_MASTER) ? AccessVote.ALLOW : AccessVote.DENY;
                break;
            case DELETE:
                vote = this.hasRole(member, Role.SCRUM_MASTER) && subject.getStatus().equals(PlanningStatus.SCHEDULED)
                        ? AccessVote.ALLOW
                        : AccessVote.DENY;
                break;
            default:
                vote = AccessVote.OMIT;
                break;
        }

        return vote;
    }

    @Override
    protected PlanningAdministrativeAction[] getSupportedActions() {
        return PlanningAdministrativeAction.values();
    }
}
