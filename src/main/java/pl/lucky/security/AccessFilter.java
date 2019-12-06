package pl.lucky.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pl.lucky.model.UserRole;

@SessionScope
@Component
public class AccessFilter {

    private Long ownerId;
    private UserRole ownerRole;

    public Long getOwnerId() {
        return ownerId;
    }

    public UserRole getOwnerRole() {
        return ownerRole;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwnerRole(UserRole ownerRole) {
        this.ownerRole = ownerRole;
    }
}
