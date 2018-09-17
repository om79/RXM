package viroopa.com.medikart.MedicineReminder.Model;

import android.net.Uri;

/**
 * Created by ABCD on 30/04/2016.
 */
public class M_alarmsoundlist {
    private String title;
    private Uri uri;

    public M_alarmsoundlist(String title, Uri uri) {
        this.title = title;
        this.uri = uri;
    }
    public String getTitle() {
        return title;
    }
    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}