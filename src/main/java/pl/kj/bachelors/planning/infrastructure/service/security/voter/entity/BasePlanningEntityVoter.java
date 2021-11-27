package pl.kj.bachelors.planning.infrastructure.service.security.voter.entity;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kj.bachelors.planning.domain.model.entity.Planning;
import pl.kj.bachelors.planning.domain.model.extension.AccessVote;
import pl.kj.bachelors.planning.domain.model.extension.Role;
import pl.kj.bachelors.planning.domain.model.remote.TeamMember;
import pl.kj.bachelors.planning.domain.service.user.MemberProvider;

import java.util.List;
import java.util.Optional;

public abstract   class BasePlanningEntityVoter<A> extends BaseEntityVoter<Planning, A> {
    private final MemberProvider memberProvider;

    @Autowired
    protected BasePlanningEntityVoter(MemberProvider memberProvider) {
        this.memberProvider = memberProvider;
    }

    @Override
    protected AccessVote voteInternal(Planning subject, A action, String userId) {
        Optional<TeamMember> member = this.memberProvider.get(subject.getTeamId(), userId);
        return member.isPresent() ? this.voteInternal(subject, action, member.get()) : AccessVote.DENY;
    }

    protected abstract AccessVote voteInternal(Planning subject, A action, TeamMember member);

    protected boolean hasRole(TeamMember member, Role role) {
        return member.getRoles().contains(role);
    }

    protected boolean hasRole(TeamMember member, List<Role> roles) {
        return member.getRoles().stream().anyMatch(roles::contains);
    }
}
