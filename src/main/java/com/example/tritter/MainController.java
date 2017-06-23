package com.example.tritter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import twitter4j.*;
@Controller

public class MainController {

    int defaultFav = 0;// ふぁぼ初期値
    int defaultRt = 0;// りつい初期値
    int Fav = 0;// ふぁぼ初期値
    int Rt = 0;// りつい初期値
    int notificationCount;//通知の数値をカウント
    int tweetCount;
    int followersCount;
    int friendsCount;
    int apiLimit =0;
    String defaultFavIcon = "♡";
    String changeFavIcon = "❤";
    String defaultRtIcon = "🔁";
    String changeRtIcon = "🔃";
    String accountName = "アカウント名";
    String tweetContents = "ツイート内容";
    String screenName = "スクリーンネーム";
    String tweetID = "";//ツイートID
    String accountimgURL = null;
    String tweetimgURL = null;
    String tweetTime;
    String favCount = "favcount";
    String rtCount = "rtcount";
    String rtfavCount = "rtfavcount";
    String noCount = "nocount";
    
    
    
    
//    List<Tweets> tweets = new ArrayList<>();//ツイートのリスト
    List<String> tweetimgURLList = new ArrayList<String>();
    @Autowired
    private JdbcTemplate jdbc;
    private TritterDao tritterDao;
    
    @GetMapping("/test")
    public String Test(){
        System.out.println(tritterDao.findByAge(23));
        return"";
    }
    
    /**
     * tritterの初期ページ/ツイッターの情報を取得する
     * 
     *  取得した１つのtweet情報（アカウント名/スクリーンネーム/アカウント画像/ツイート内容/fav rt数）を各変数に代入し返却する
     * ツイート画像がない場合はtweetimgURLをnullとする。
     * タイムラインが読み込めなかった場合、{@link TwitterException}を返却する。
     * 新しいツイートがある場合はDBにツイート情報を追加
     * 
     * @param model モデル
     * @return タイムライン情報を返却する。
     */
    @GetMapping("/top") // 最初の状態
    public String top(Model model) {

        if(apiLimit==0){
            apiLimit +=1;
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            List<Status> statuses = twitter.getHomeTimeline();//TLのリスト
            
            
            
            for(int i=0;i<statuses.size();i++){
                int a = statuses.size() -i-1;
                MediaEntity[] mediaEntitys = statuses.get(i).getMediaEntities();
                if (mediaEntitys.length == 0) {//ツイートに画像がない場合 リストに空の値を追加
                    tweetimgURLList.add("");
                }
                for(MediaEntity m:mediaEntitys){//ツイートに画像がある場合 画像のURLをリストに追加
                    tweetimgURLList.add(m.getMediaURL());
                    
                }
                jdbc.update("INSERT INTO Tweet VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                                a,statuses.get(i).getUser().getProfileImageURL(),statuses.get(i).getUser().getName(),
                                statuses.get(i).getUser().getScreenName(),statuses.get(i).getText(),tweetimgURLList.get(i),
                                statuses.get(i).getFavoriteCount(),statuses.get(i).getRetweetCount(),statuses.get(i).getUser().getStatusesCount(),
                                statuses.get(i).getUser().getFollowersCount(),statuses.get(i).getUser().getFriendsCount(),
                                String.valueOf(statuses.get(i).getId()),String.valueOf(statuses.get(i).getCreatedAt()),defaultFavIcon,defaultRtIcon);
            }
            
            List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
            
            model.addAttribute("tweets",tweets);

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
//            System.exit(-1);
        }
        
        }else if (apiLimit ==7){
            
            apiLimit =1;
            
            try {
                Twitter twitter = new TwitterFactory().getInstance();
                List<Status> statuses = twitter.getHomeTimeline();//TLのリスト
                
                
                
                for(int i=0;i<statuses.size();i++){
                    MediaEntity[] mediaEntitys = statuses.get(i).getMediaEntities();
                    if (mediaEntitys.length == 0) {//ツイートに画像がない場合 リストに空の値を追加
                        tweetimgURLList.add("");
                    }
                    for(MediaEntity m:mediaEntitys){//ツイートに画像がある場合 画像のURLをリストに追加
                        tweetimgURLList.add(m.getMediaURL());
                        
                    }

                    for(int j=0;j<jdbc.queryForList("SELECT * FROM TWEET").size();j++){

                     
                     if(jdbc.queryForList("SELECT * FROM TWEET WHERE id = ?",j).get(0).get("tweetID").equals(String.valueOf(statuses.get(i).getId()))){
                     break;
                     }else if (j==jdbc.queryForList("SELECT * FROM TWEET").size()-1){
                         jdbc.update("INSERT INTO TWEET VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                         j+1,statuses.get(i).getUser().getProfileImageURL(),statuses.get(i).getUser().getName(),
                         statuses.get(i).getUser().getScreenName(),statuses.get(i).getText(),tweetimgURLList.get(i),
                         statuses.get(i).getFavoriteCount(),statuses.get(i).getRetweetCount(),statuses.get(i).getUser().getStatusesCount(),
                         statuses.get(i).getUser().getFollowersCount(),statuses.get(i).getUser().getFriendsCount(),
                         String.valueOf(statuses.get(i).getId()),String.valueOf(statuses.get(i).getCreatedAt()),defaultFavIcon,defaultRtIcon);
                         break;
                     }
                    }
                }
                
                List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
                
                model.addAttribute("tweets",tweets);

            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to get timeline: " + te.getMessage());
//                System.exit(-1);
            }
            
        }else{
            apiLimit +=1;
            List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
            
            model.addAttribute("tweets",tweets);
        }
        
        System.out.println(apiLimit);
        
        model.addAttribute("notificationCount",notificationCount);
        return "top";
        }
        

    /**
     * リツイート数とふぁぼ数を任意の値に変更する。
     * 
     * フォームに入力された任意の値をリツイート数・ふぁぼ数に反映させる。
     * 入力された値をDB上のリツイート数とふぁぼ数に上書きしその値を取得する。
     * 負の値が入力された場合ポップアップ表示
     * 最大値を超える数値が入力された場合ポップアップ表示
     * 
     * @param form form.rt form.fav共に 0を許容する。負の値は許容しない。最大値9999999
     * @param attr モデル
     * @return リツイート数とふぁぼ数をDB上に上書きしその値を返却
     */
    @PostMapping("/rtFavInput")
    public String rtFavInput(RtFavInputForm form,RedirectAttributes attr) {// りついふぁぼ変更処理
        jdbc.update("UPDATE Tweet SET Fav =  ? WHERE tweetID = ?",form.getFav(),form.getTweetId());
        jdbc.update("UPDATE Tweet SET Rt = ? WHERE tweetID = ?",form.getRt(),form.getTweetId());
        List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
        List<Map<String, Object>> tweet = jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId());
        attr.addFlashAttribute("tweets",tweets);
        attr.addFlashAttribute("tweet",tweet);
        attr.addFlashAttribute("tweetId",form.getTweetId());
        attr.addFlashAttribute("Fav",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Fav"));
        attr.addFlashAttribute("Rt",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Rt"));
        notificationCount = form.getFav() + form.getRt();
       
        return "redirect:/top";
    }

    /**
     * ふぁぼボタンが押された場合に数値増加と表示変更を行う
     * 
     * ふぁぼボタンが押された場合、数値を＋１する。
     * ふぁぼボタンが押された場合、ボタンアイコン表示を変更する。
     * 
     * 複数回のボタン操作を許容する。
     * @param attr モデル
     * @return ふぁぼボタンの数値と表示アイコンを変更し返却
     */
    @PostMapping("/favButton")
    public String favButton(RtFavInputForm form,RedirectAttributes attr) {// ふぁぼボタンを押したときの処理
        
        jdbc.update("UPDATE Tweet SET Fav = Fav+1 WHERE tweetID = ?",form.getTweetId());
        jdbc.update("UPDATE Tweet SET FavIcon = ? WHERE tweetID = ?",changeFavIcon,form.getTweetId());
        List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
        List<Map<String, Object>> tweet = jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId());
        attr.addFlashAttribute("tweets",tweets);
        attr.addFlashAttribute("tweet",tweet);
        attr.addFlashAttribute("tweetId",form.getTweetId());
        attr.addFlashAttribute("Fav",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Fav"));
        attr.addFlashAttribute("Rt",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Rt"));
        attr.addFlashAttribute("favIcon",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("favIcon"));
        attr.addFlashAttribute("rtIcon",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("rtIcon"));
        notificationCount+=1;

        return "redirect:/top";
    }
    /**
     * りついボタンが押された場合に数値増加と表示変更を行う
     * 
     * りついボタンが押された場合、数値を＋１する。
     * りついボタンが押された場合、ボタンアイコン表示を変更する。
     * 
     * 複数回のボタン操作を許容する。
     * @param attr モデル
     * @return りついボタンの数値と表示アイコンを変更し返却
     */
    @PostMapping("/rtButton")
    public String rtButton(RtFavInputForm form,RedirectAttributes attr) {// りついボタンを押したときの処理
        jdbc.update("UPDATE Tweet SET Rt = Rt+1 WHERE tweetID = ?",form.getTweetId());
        jdbc.update("UPDATE Tweet SET RtIcon = ? WHERE tweetID = ?",changeRtIcon,form.getTweetId());
        List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
        List<Map<String, Object>> tweet = jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId());
        attr.addFlashAttribute("tweets",tweets);
        attr.addFlashAttribute("tweet",tweet);
        attr.addFlashAttribute("tweetId",form.getTweetId());
        attr.addFlashAttribute("Fav",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Fav"));
        attr.addFlashAttribute("Rt",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Rt"));
        attr.addFlashAttribute("favIcon",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("favIcon"));
        attr.addFlashAttribute("rtIcon",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("rtIcon"));
        notificationCount+=1;

        return "redirect:/top";
    }
    
    
    /**
     * 入力値を初期化する
     * 
     * ユーザーが変更したリツイートやファボ数を0でアップデートする。
     * 
     * @param attr モデル
     * @return 初期化したファボ数・リツイート数の変数を返却する。
     */
    @PostMapping("/clear")
    public String Clear(RtFavInputForm form,RedirectAttributes attr){
        jdbc.update("UPDATE Tweet SET Fav =  0 WHERE tweetID = ?",form.getTweetId());
        jdbc.update("UPDATE Tweet SET Rt = 0 WHERE tweetID = ?",form.getTweetId());
        jdbc.update("UPDATE Tweet SET FavIcon = ? WHERE tweetID = ?",defaultFavIcon,form.getTweetId());
        jdbc.update("UPDATE Tweet SET RtIcon = ? WHERE tweetID = ?",defaultRtIcon,form.getTweetId());
        List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
        List<Map<String, Object>> tweet = jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId());
        attr.addFlashAttribute("tweets",tweets);
        attr.addFlashAttribute("tweet",tweet);
        attr.addFlashAttribute("tweetId",form.getTweetId());
        attr.addFlashAttribute("Fav",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Fav"));
        attr.addFlashAttribute("Rt",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("Rt"));
        attr.addFlashAttribute("favIcon",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("favIcon"));
        attr.addFlashAttribute("rtIcon",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",form.getTweetId()).get(0).get("rtIcon"));
        attr.addFlashAttribute("favcount", noCount);
        attr.addFlashAttribute("rtcount", noCount);
        notificationCount = 0;
        return "redirect:/top";
    }
    

/**
 * ユーザが選択したツイートのIDと一致するIDを持つツイート情報を表示する。
 * 
 * @param attr モデル
 * @param tweetId ツイートID String型
 * @return 各変数にツイート情報を与えて返却する。
 */
   @PostMapping("/setTweet")
   public String setTweet(RedirectAttributes attr,String tweetId){

       notificationCount = 0;
       List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
       List<Map<String, Object>> tweet = jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId);
       attr.addFlashAttribute("tweets",tweets);
       attr.addFlashAttribute("tweet",tweet);
       attr.addFlashAttribute("Fav",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId).get(0).get("Fav"));
       attr.addFlashAttribute("Rt",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId).get(0).get("Rt"));
       attr.addFlashAttribute("tweetId",tweetId);
       return "redirect:/top";
   }
 
   @PostMapping("/Flaming")
   public String Flaming(RedirectAttributes attr,String tweetId){
       
       String rep = "@"+jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId).get(0).get("ScreenName")+"このツイート面白すぎい！！！！";
       List<Map<String, Object>> tweets = jdbc.queryForList("SELECT * FROM Tweet ORDER BY id DESC");
       List<Map<String, Object>> tweet = jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId);
       attr.addFlashAttribute("tweets",tweets);
       attr.addFlashAttribute("tweet",tweet);
       attr.addFlashAttribute("Fav",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId).get(0).get("Fav"));
       attr.addFlashAttribute("Rt",jdbc.queryForList("SELECT * FROM Tweet WHERE tweetID = ?",tweetId).get(0).get("Rt"));
       attr.addFlashAttribute("tweetId",tweetId);
       attr.addFlashAttribute("favcount", favCount);
       attr.addFlashAttribute("rtcount", rtCount);
       attr.addFlashAttribute("rtfavcount", rtfavCount);
       attr.addFlashAttribute("rep", rep);
       
       
       return "redirect:/top";
   }
   

}
