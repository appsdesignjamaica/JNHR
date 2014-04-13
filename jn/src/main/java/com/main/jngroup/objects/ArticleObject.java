package com.main.jngroup.objects;

/**
 * Created by nove1398 on 4/11/2014.
 */
public class ArticleObject {
    private int articleId;
    private String articleName;
    private int articleType;
    private String articleDate;
    private String posterLastName;
    private String posterFirstName;

    public ArticleObject(){

    }
    public int getArticleId() {
        return articleId;
    }

    public void setArticleId( int articleId ) {
        this.articleId = articleId;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate( String articleDate ) {
        this.articleDate = articleDate;
    }

    public String getPosterLastName() {
        return posterLastName;
    }

    public void setPosterLastName( String posterLastName ) {
        this.posterLastName = posterLastName;
    }

    public String getPosterFirstName() {
        return posterFirstName;
    }

    public void setPosterFirstName( String posterFirstName ) {
        this.posterFirstName = posterFirstName;
    }
    public String getArticleName() {
        return articleName;
    }

    public void setArticleName( String articleName ) {
        this.articleName = articleName;
    }

    public int getArticleType() {
        return articleType;
    }

    public void setArticleType( int articleType ) {
        this.articleType = articleType;
    }

    public String getPosterName(){
        String fname = this.posterFirstName.substring( 0,1 );
        String lname = this.posterLastName.substring( 0,1 ).toUpperCase();
        return fname.toUpperCase()  + ". " + lname + this.posterLastName.substring( 1 );
    }
}
