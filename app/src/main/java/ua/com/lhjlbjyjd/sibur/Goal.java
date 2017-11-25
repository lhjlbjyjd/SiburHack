package ua.com.lhjlbjyjd.sibur;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Toreno on 25.11.2017.
 */

public class Goal implements Serializable{
    private boolean state = false, photoRequired = false, emailRequired = false;
    private String description;
    private Bitmap image;


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

    void setState(boolean b) {
        state = b;
    }

    boolean getPhotoRequired() {
        return photoRequired;
    }

    boolean getEmailRequired() {
        return  emailRequired;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
