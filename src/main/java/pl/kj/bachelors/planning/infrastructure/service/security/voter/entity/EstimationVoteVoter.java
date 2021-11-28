package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAction;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

@Component
public class EstimationVoteVoter extends BasePlanningEntityVoter<PlanningAction> {
    @Autowired
    protected EstimationVoteVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected PlanningAction[] getSupportedActions() {
        return new PlanningAction[] { PlanningAction.VOTE };
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningAction action, TeamMember member) {
        return this.hasRole(member, Role.TEAM_MEMBER) ? AccessVote.ALLOW : AccessVote.DENY;
    }
}
