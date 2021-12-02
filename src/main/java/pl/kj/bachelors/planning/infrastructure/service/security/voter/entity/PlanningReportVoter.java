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

import java.util.Arrays;
import java.util.List;

@Component
public class PlanningReportVoter extends BasePlanningEntityVoter<PlanningAdministrativeAction> {
    @Autowired
    public PlanningReportVoter(MemberProvider memberProvider) {
        super(memberProvider);
    }

    @Override
    protected PlanningAdministrativeAction[] getSupportedActions() {
        return new PlanningAdministrativeAction[] { PlanningAdministrativeAction.EXPORT_REPORT };
    }

    @Override
    protected AccessVote voteInternal(Planning subject, PlanningAdministrativeAction action, TeamMember member) {
        return this.hasRole(member, Arrays.asList(Role.SCRUM_MASTER, Role.PRODUCT_OWNER)) &&
                subject.hasStatusIn(List.of(PlanningStatus.FINISHED))
                ? AccessVote.ALLOW
                : AccessVote.DENY;
    }
}
