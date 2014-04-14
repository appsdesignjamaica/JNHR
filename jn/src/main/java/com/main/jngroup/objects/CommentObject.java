package com.main.jngroup.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nove1398 on 4/13/2014.
 */
public class CommentObject{
    private String commenterName;
    private String commentBody;
    private String commentTime;

    public CommentObject(){

    }


    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime( String commentTime ) {
        this.commentTime = commentTime;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName( String commenterName ) {
        this.commenterName = commenterName;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody( String commentBody ) {
        this.commentBody = commentBody;
    }


}
