package com.example.tritter;

import lombok.Data;

@Data
public class Tweets {
    String accountName;
    String tweetContents;
    String screenName;
    String accountimgURL;
    String tweetimgURL;
    int fav;// ふぁぼ初期値
    int rt;// りつい初期値
    int tweetCount;
    int followersCount;
    int friendsCount;
    String tweetId;
    String tweetTime;
    
    public Tweets(String accountimgURL,String accountName, String screenName,String tweetContents, String tweetimgURL, int fav, int rt, int tweetCount,
                  int followersCount,int friendsCount,String tweetId,String tweetTime){

        this.accountimgURL = accountimgURL;
        this.accountName = accountName;
        this.screenName = screenName;
        this.tweetContents = tweetContents;
        this.tweetimgURL = tweetimgURL;
        this.fav = fav;
        this.rt = rt;
        this.tweetCount = tweetCount;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
        this.tweetId = tweetId;
        this.tweetTime = tweetTime;
        

    }
 
}
