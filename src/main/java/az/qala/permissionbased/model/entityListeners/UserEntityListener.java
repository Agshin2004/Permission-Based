package az.qala.permissionbased.model.entityListeners;

import az.qala.permissionbased.model.entity.User;
import az.qala.permissionbased.model.entity.UserProfile;
import jakarta.persistence.PrePersist;

public class UserEntityListener {

    @PrePersist
    public void prePersist(User user) {
        if (user.getProfile() == null) {
            UserProfile profile = new UserProfile();

            profile.setUser(user);
            user.setProfile(profile);
        }
    }
    
}
