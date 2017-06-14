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
//    @Value("${app.name}")
//    private String appname;
//    
//    @Autowired
//    private JdbcTemplate jdbc;
//    
//    @GetMapping("/h2")
//    public String h2(){
//        System.out.println(jdbc.queryForList("SELECT * FROM person"));
//        return "top";
//    }
    
    int fav_num = 0;// ふぁぼの変数
    int rt_num = 0;// りついの変数
    int default_fav = 0;// ふぁぼ初期値
    int default_rt = 0;// りつい初期値
    boolean fav_buttonbool=false;// ふぁぼボタンを押したかどうか
    boolean rt_buttonbool=false;//りついボタンを押したかどうか
    String default_fav_icon = "♡";
    String change_fav_icon = "❤";
    String default_rt_icon = "🔁";
    String change_rt_icon = "🔃";
    String accountName;
    String tweetContents;
    String screenName;
    String accountimgURL;
    String tweetimgURL;
    int tweet = 0;//test用仮引数
    
    
    /**
     * tritterの初期ページ
     * 
     * @param model 
     * @return 
     */
    @GetMapping("/top") // 最初の状態
    public String top(Model model) {
        
        model.addAttribute("fav", default_fav);
        model.addAttribute("rt", default_rt);
        model.addAttribute("favpush", default_fav_icon);// ふぁぼ押す前の初期値
        model.addAttribute("rtpush", default_rt_icon);// ふぁぼ押す前の初期値
        
        //model.addAttribute("tweets", Arrays.asList("tweet1", "tweet2", "tweet3"));

        return "top";
    }

    /**
     * 
     * 
     * @param rt
     * @param fav
     * @param model
     * @return
     */
    @GetMapping("/rt_fav")
    public String rt_fav(int rt, int fav, Model model) {// りついふぁぼ変更処理
        fav_num = fav;// 変数に代入
        rt_num = rt;// 変数に代入
        model.addAttribute("fav", fav);
        model.addAttribute("rt", rt);
        model.addAttribute("favpush", "♡");
        model.addAttribute("rtpush", "🔁");
        
        fav_buttonbool=false;//初期化
        rt_buttonbool=false;//初期化
        
        return "top";
    }

    /**
     * 
     * @param model
     * @return
     */

    @PostMapping("/fav_button")
    public String fav_button(RedirectAttributes attr) {// ふぁぼボタンを押したときの処理
        default_fav+=1;// ふぁぼ＋１
        default_fav_icon = change_fav_icon;// 表示変更
        fav_buttonbool=true;
        if(rt_buttonbool){//りついが押されてたら表示を変更する
            default_rt_icon = change_rt_icon;// 表示変更
        }
        return "redirect:/top";
    }

    @PostMapping("/rt_button")
    public String rt_button(RedirectAttributes attr) {// りついボタンを押したときの処理
        default_rt+=1;//りつい+1
        default_rt_icon = change_rt_icon;// 表示変更
        rt_buttonbool=true;
        if(fav_buttonbool){//ふぁぼが押されてたら表示を変更する
            default_fav_icon = change_fav_icon;// 表示変更
        }
        return "redirect:/top";
    }
    
    @PostMapping("/clear")
    public String Clear(RedirectAttributes attr){
        top(attr);
        fav_buttonbool=false;//初期化
        rt_buttonbool=false;//初期化
        return "redirect:/top";
    }
    

   @GetMapping("/setTweet")
   public String getTweet(Model model){//ツイート取得メソッド
       try {
           Twitter twitter = new TwitterFactory().getInstance();
           User user = twitter.verifyCredentials();
           
           accountName = user.getName();//アカウント名を代入
           screenName = user.getScreenName();//スクリーンネームを代入
           accountimgURL = user.getProfileImageURL();//アカウント画像のURLを代入
           List<Status> statuses = twitter.getHomeTimeline();//TLのリスト
           tweetContents = statuses.get(tweet).getText();//最新(Listの0ｂ番目)のツイート内容
           default_fav = statuses.get(tweet).getFavoriteCount();//ふぁぼ数代入
           default_rt = statuses.get(tweet).getRetweetCount();//りつい数代入
           
           MediaEntity[] mediaEntitys = statuses.get(tweet).getMediaEntities();//ツイートメディアのリスト
           if(mediaEntitys.length ==0){//リストが空だったら
               tweetimgURL = "";
           }else{//リストに値が入っていたら
           MediaEntity mediaentity = mediaEntitys[0];//リストの1つ目の要素を与える
           tweetimgURL = mediaentity.getMediaURL();//ツイート画像URLを代入
           }
           
       } catch (TwitterException te) {
           te.printStackTrace();
           System.out.println("Failed to get timeline: " + te.getMessage());
           System.exit(-1);
       }
       model.addAttribute("accountname",accountName);
       model.addAttribute("tweetcontents",tweetContents);
       model.addAttribute("screenname","@"+screenName);
       model.addAttribute("fav",default_fav);
       model.addAttribute("rt",default_rt);
       model.addAttribute("accountimgURL",accountimgURL);
       model.addAttribute("tweetimgURL",tweetimgURL);
       return "top";
   }
   
   
  
}
