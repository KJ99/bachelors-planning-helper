package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

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
public class PlanningModificationVoter extends BasePlanningEntityVoter<PlanningAdministrativeAction> {

    @Autowired
    protected PlanningModificationVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningAdministrativeAction action, TeamMember member) {
        return subject.getStatus().equals(PlanningStatus.SCHEDULED) && this.hasRole(member, Role.SCRUM_MASTER)
                ? AccessVote.ALLOW
                : AccessVote.DENY;
    }

    @Override
    protected PlanningAdministrativeAction[] getSupportedActions() {
        return new PlanningAdministrativeAction[] {
                PlanningAdministrativeAction.UPDATE,
                PlanningAdministrativeAction.DELETE
        };
    }
}
