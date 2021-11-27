package pl.kj.bachelors.planning.domain.model.remote;

import pl.kj.bachelors.planning.domain.model.extension.Role;

import java.util.List;

public class TeamMember {
    private String userId;
    private List<Role> roles;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
