package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.PlanningStatus;
import pl.kj.bachelors.planning.domain.model.extension.action.PlanningAction;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

import java.util.Arrays;

@Component
public class PlanningJoinVoter extends BasePlanningEntityVoter<PlanningAction> {
    @Autowired
    protected PlanningJoinVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected PlanningAction[] getSupportedActions() {
        return new PlanningAction[] {
                PlanningAction.JOIN
        };
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningAction action, TeamMember member) {
        return subject.hasStatusIn(Arrays.asList(PlanningStatus.PROGRESSING, PlanningStatus.VOTING))
                ? AccessVote.ALLOW
                : AccessVote.DENY;
    }
}
