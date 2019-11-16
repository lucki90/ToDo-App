package pl.lucky.security;

import org.springframework.web.context.annotation.SessionScope;
import pl.lucky.model.UserRole;

@SessionScope
public class AccessFilter {

    private static Long ownerId;
    private static UserRole ownerRole;

    public static Long getOwnerId() {
        return ownerId;
    }

    static void setOwnerId(Long ownerId) {
        AccessFilter.ownerId = ownerId;
    }

    public static UserRole getOwnerRole() {
        return ownerRole;
    }

    static void setOwnerRole(UserRole ownerRole) {
        AccessFilter.ownerRole = ownerRole;
    }
}
