package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

@Component
public class ParticularPlanningReadVoter  extends BasePlanningEntityVoter<PlanningAdministrativeAction> {
    @Autowired
    protected ParticularPlanningReadVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected PlanningAdministrativeAction[] getSupportedActions() {
        return new PlanningAdministrativeAction[] { PlanningAdministrativeAction.READ };
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningAdministrativeAction action, TeamMember member) {
        return AccessVote.ALLOW;
    }
}
