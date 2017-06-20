package com.example.tritter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    int Fav;
    int Rt;
    int tweetNum = 0;
    boolean favButtonbool=false;// ふぁぼボタンを押したかどうか
    boolean rtButtonbool=false;//りついボタンを押したかどうか
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
    List<Tweets> tweets = new ArrayList<>();//ツイートのリスト
    List<String> tweetimgURLList = new ArrayList<String>();
    @Autowired
    private TritterDao tritterDao;
    
    @GetMapping("/test")
    public String Test(){
        System.out.println(tritterDao.findByAge(23));
        return"";
    }
    
    /**
     * tritterの初期ページ
     * 
     * tritterの初期ページの設定を行う。
     * 
     * @param model モデル
     * @return 初期設定情報を返却する。
     */
    @GetMapping("/top") // 最初の状態
    public String top(Model model) {

        model.addAttribute("fav", Fav);
        model.addAttribute("rt", Rt);
        model.addAttribute("favIcon", defaultFavIcon);
        model.addAttribute("rtIcon", defaultRtIcon);
        model.addAttribute("accountName",accountName);
        model.addAttribute("tweetContents",tweetContents);
        model.addAttribute("screenName",screenName);
        model.addAttribute("accountimgURL",accountimgURL);
        model.addAttribute("tweetimgURL",tweetimgURL);
        model.addAttribute("tweets", tweets);
        model.addAttribute("tweetNum", tweetNum);
        model.addAttribute("tweetID",tweetID);
        model.addAttribute("tweetTime",tweetTime);
        return "top";
        
    }

    /**
     * リツイート数とふぁぼ数を任意の値に変更する。
     * 
     * フォームに入力された任意の値をリツイート数・ふぁぼ数に反映させる。
     * 入力された値をリツイート数とふぁぼ数の変数に代入し返却する。
     * 負の値が入力された場合ポップアップ表示
     * 最大値を超える数値が入力された場合ポップアップ表示
     * 
     * @param form form.rt form.fav共に 0を許容する。負の値は許容しない。最大値9999999
     * @param attr モデル
     * @return リツイート数とふぁぼ数を返却
     */
    @PostMapping("/rtFavInput")
    public String rtFavInput(RtFavInputForm form,RedirectAttributes attr) {// りついふぁぼ変更処理
        Fav = form.getFav();// 変数に代入
        Rt = form.getRt();// 変数に代入
        favButtonbool=false;//初期化
        rtButtonbool=false;//初期化

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
    public String favButton(RedirectAttributes attr) {// ふぁぼボタンを押したときの処理
        Fav+= 1;// ふぁぼ＋１
        defaultFavIcon = changeFavIcon;// 表示変更
        favButtonbool=true;

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
    public String rtButton(RedirectAttributes attr) {// りついボタンを押したときの処理
        Rt+=1;//りつい+1
        defaultRtIcon = changeRtIcon;// 表示変更
        rtButtonbool=true;

        return "redirect:/top";
    }
    /**
     * 入力値を初期化する
     * 
     * ユーザーが変更したリツイートやファボ数を0で返却し初期値に戻す。
     * 
     * @param attr モデル
     * @return 初期化したファボ数・リツイート数の変数を返却する。
     */
    @PostMapping("/clear")
    public String Clear(RedirectAttributes attr){
        Fav = defaultFav;
        Rt = defaultRt;
        defaultFavIcon = "♡";
        defaultRtIcon = "🔁";
        favButtonbool=false;//初期化
        rtButtonbool=false;//初期化
        return "redirect:/top";
    }
    
/**
 * ツイッターの情報を取得する
 * 
 * 取得した１つのtweet情報（アカウント名/スクリーンネーム/アカウント画像/ツイート内容/fav rt数）を各変数に代入し返却する
 * ツイート画像がない場合はtweetimgURLをnullとする。
 * タイムラインが読み込めなかった場合、{@link TwitterException}を返却する。
 * @param attr モデル
 * @return tweet情報が代入された各変数を返却する
 */
   @PostMapping("/getTweet")
   public String getTweet(RedirectAttributes attr){//ツイート取得メソッド
       try {
           Twitter twitter = new TwitterFactory().getInstance();
           List<Status> statuses = twitter.getHomeTimeline();//TLのリスト
           
           tweetNum = statuses.size();
           for(int i=0;i<statuses.size();i++){
               MediaEntity[] mediaEntitys = statuses.get(i).getMediaEntities();
               if (mediaEntitys.length == 0) {//ツイートに画像がない場合 リストに空の値を追加
                   tweetimgURLList.add("");
               }
               for(MediaEntity m:mediaEntitys){//ツイートに画像がある場合 画像のURLをリストに追加
                   tweetimgURLList.add(m.getMediaURL());
                   
               }
               
               tweets.add(new Tweets(statuses.get(i).getUser().getProfileImageURL(),statuses.get(i).getUser().getName(),
                       statuses.get(i).getUser().getScreenName(),statuses.get(i).getText(),tweetimgURLList.get(i),
                       statuses.get(i).getFavoriteCount(),statuses.get(i).getRetweetCount(),String.valueOf(statuses.get(i).getId()),
                       String.valueOf(statuses.get(i).getCreatedAt())));
               
           }

       } catch (TwitterException te) {
           te.printStackTrace();
           System.out.println("Failed to get timeline: " + te.getMessage());
           System.exit(-1);
       }

       return "redirect:/top";
   }
/**
 * ユーザが選択したツイートのIDと一致するIDを持つツイート情報を表示する。
 * 
 * @param attr モデル
 * @param tweetId ツイートID String型
 * @return 各変数にツイート情報を与えて
 */
   @PostMapping("/setTweet")
   public String setTweet(RedirectAttributes attr,String tweetId){

            for(Tweets tweet: tweets){

                if(tweetId.equals(tweet.getTweetId())){
                    
                    defaultFav = tweet.getFav();
                    defaultRt = tweet.getRt();
                    Fav = defaultFav;
                    Rt = defaultRt;
                    accountName = tweet.getAccountName();
                    tweetContents = tweet.getTweetContents();
                    screenName = tweet.getScreenName();
                    accountimgURL = tweet.getAccountimgURL();
                    tweetimgURL =tweet.getTweetimgURL();
                    tweetID = tweet.getTweetId();
                    tweetTime = tweet.getTweetTime();
                    break;
                }
            }

       return "redirect:/top";
   }
   

}
