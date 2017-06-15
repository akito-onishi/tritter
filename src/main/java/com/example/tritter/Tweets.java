package com.example.tritter;

public class Tweets {
    String accountName;
    String tweetContents;
    String screenName;
    String accountimgURL;
    String tweetimgURL;
    int fav;// ふぁぼ初期値
    int rt;// りつい初期値
    
    public Tweets(String accountimgURL,String accountName, String screenName,String tweetContents, String tweetimgURL, int fav, int rt){

        this.accountimgURL = accountimgURL;
        this.accountName = accountName;
        this.screenName = screenName;
        this.tweetContents = tweetContents;
        this.tweetimgURL = tweetimgURL;
        this.fav = fav;
        this.rt = rt;

    }
    
    public String getAccountimgURL(){
        return this.accountimgURL;
    }
    
    public String getAccountName(){
        return this.accountName;
    }
    
    public String getScreenName(){
        return this.screenName;
    }
    
    public String getTweetContents(){
        return this.tweetContents;
    }
    
    public String getTweetimgURL(){
        return this.tweetimgURL;
    }
    
    public int getFav(){
        return this.fav;
    }
    
    public int getRt(){
        return this.rt;
    }
}
