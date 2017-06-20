package com.example.tritter;

public class Tweets {
    String accountName;
    String tweetContents;
    String screenName;
    String accountimgURL;
    String tweetimgURL;
    int fav;// ふぁぼ初期値
    int rt;// りつい初期値
    String tweetId;
    String tweetTime;
    
    public Tweets(String accountimgURL,String accountName, String screenName,String tweetContents, String tweetimgURL, int fav, int rt, String tweetId,String tweetTime){

        this.accountimgURL = accountimgURL;
        this.accountName = accountName;
        this.screenName = screenName;
        this.tweetContents = tweetContents;
        this.tweetimgURL = tweetimgURL;
        this.fav = fav;
        this.rt = rt;
        this.tweetId = tweetId;
        this.tweetTime = tweetTime;

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
    
    public String getTweetId(){
        return this.tweetId;
    }
    
    public String getTweetTime(){
        return this.tweetTime;
    }
}
