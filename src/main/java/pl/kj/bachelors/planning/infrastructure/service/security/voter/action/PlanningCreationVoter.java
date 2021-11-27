package pl.kj.bachelors.planning.infrastructure.service.security.voter.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAdministrativeAction;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

@Component
public class PlanningCreationVoter extends BaseTeamBasedActionVoter<PlanningAdministrativeAction>{
    @Autowired
    protected PlanningCreationVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    public PlanningAdministrativeAction[] getSupportedActions() {
        return new PlanningAdministrativeAction[] { PlanningAdministrativeAction.CREATE };
    }

    @Override
    protected AccessVote voteInternal(PlanningAdministrativeAction action, TeamMember member) {
        return this.hasRole(member, Role.SCRUM_MASTER) ? AccessVote.ALLOW : AccessVote.DENY;
    }

}
