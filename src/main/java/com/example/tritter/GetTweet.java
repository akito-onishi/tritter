package com.example.tritter;
import twitter4j.*;

import java.util.List;
public class GetTweet {
    public static void main(String[] args) {
        try {
            // gets Twitter instance with default credentials
            Twitter twitter = new TwitterFactory().getInstance();
            User user = twitter.verifyCredentials();
            
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Showing @" + user.getScreenName() + "'s home timeline.");
            System.out.println("プロフィール画像のURL:"+user.getProfileImageURL());
          //ついーとしてみる
           // twitter.updateStatus("st");
            System.out.println("一つ目のツイートを表示: "+statuses.get(0).getText());
            
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - 「" + status.getText()+"」       ふぁぼ"+status.getFavoriteCount()+"     りつい"+status.getRetweetCount());
            }
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
}
