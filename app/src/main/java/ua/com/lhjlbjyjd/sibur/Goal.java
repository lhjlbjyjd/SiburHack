package ua.com.lhjlbjyjd.sibur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Toreno on 25.11.2017.
 */

public class Goal implements Serializable{
    private boolean state = false, photoRequired = false, emailRequired = false;
    private String description;
    private Bitmap image;
    private Date endDate = new Date(0), beginDate = new Date(0);
    private String bitmapUrl;


    Goal(String description, boolean state, boolean photoRequired, boolean emailRequired, String bitmapUrl, Context context) {
        this.description = description;
        this.state = state;
        this.photoRequired = photoRequired;
        this.emailRequired = emailRequired;
        this.bitmapUrl = bitmapUrl;
        //image = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_image);
    }

    public String getBitmapUrl() {
        return bitmapUrl;
    }

    public void setBitmapUrl(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;
    }

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

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setBeginDate(long i){
        beginDate = new Date(i);
    }

    public void setEndDate(long i){
        endDate = new Date(i);
    }

    public void setPhotoRequired(boolean photoRequired) {
        this.photoRequired = photoRequired;
    }
}
