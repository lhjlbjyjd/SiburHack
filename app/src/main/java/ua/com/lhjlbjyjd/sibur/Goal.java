package ua.com.lhjlbjyjd.sibur;

/**
 * Created by Toreno on 25.11.2017.
 */

public class Goal {
    private boolean state = false, photoRequired = false, emailRequired = false;
    private String description;

    Goal(String description, boolean state, boolean photoRequired, boolean emailRequired) {
        this.description = description;
        this.state = state;
        this.photoRequired = photoRequired;
        this.emailRequired = emailRequired;
    }
 ///
    String getDescription() {
        return  description;
    }

    boolean getState() {
        return state;
    }

    boolean getPhotoRequired() {
        return photoRequired;
    }

    boolean getEmailRequired() {
        return  emailRequired;
    }
}
