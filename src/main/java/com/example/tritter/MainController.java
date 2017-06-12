package com.example.tritter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
//        return "sample";
//    }
    
    
    Twitter twitter = new TwitterFactory().getInstance();
    User user;
    Status status;
    
    int fav_num = 0;// ふぁぼの変数
    int rt_num = 0;// りついの変数
    int default_fav = 1000;// ふぁぼ初期値
    int default_rt = 1000;// りつい初期値
    boolean fav_buttonbool=false;// ふぁぼボタンを押したかどうか
    boolean rt_buttonbool=false;//りついボタンを押したかどうか
    String accountName;
    String tweetContents;
    String screenName;
    String accountimgURL;
    
    
    @GetMapping("/sample") // 最初の状態
    public String sample(Model model) {
        
        model.addAttribute("fav", default_fav);
        model.addAttribute("rt", default_rt);
        model.addAttribute("favpush", "♡");// ふぁぼ押す前の初期値
        model.addAttribute("rtpush", "🔁");// ふぁぼ押す前の初期値
        fav_num = default_fav;// 変数に代入
        rt_num = default_rt;// 変数に代入

        return "sample";
    }

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
        
        return "sample";
    }

    @GetMapping("/fav_button")
    public String fav_button(Model model) {// ふぁぼボタンを押したときの処理
        fav_num+=1;
        model.addAttribute("fav", fav_num);// ふぁぼ＋１
        model.addAttribute("rt", rt_num);// りついはそのまま
        model.addAttribute("favpush", "♥");// 表示変更
        model.addAttribute("rtpush", "🔁");// ふぁぼ押す前の初期値
        fav_buttonbool=true;
        if(rt_buttonbool){//りついが押されてたら表示を変更する
            model.addAttribute("rtpush", "🔃");// 表示変更
        }
        return "sample";
    }

    @GetMapping("/rt_button")
    public String rt_button(Model model) {// ふぁぼボタンを押したときの処理
        rt_num+=1;
        model.addAttribute("fav", fav_num);// ふぁぼはそのまま
        model.addAttribute("rt", rt_num);// りつい+1
        model.addAttribute("favpush", "♡");// ふぁぼ押す前の初期値
        model.addAttribute("rtpush", "🔃");// 表示変更
        rt_buttonbool=true;
        if(fav_buttonbool){//ふぁぼが押されてたら表示を変更する
            model.addAttribute("favpush", "♥");// 表示変更
        }
        return "sample";
    }
    
    @GetMapping("/clear")
    public String clear(Model model){//クリアボタン
        sample(model);
        fav_buttonbool=false;//初期化
        rt_buttonbool=false;//初期化
        return "sample";
        
    }
    
    @GetMapping("/setTweet")
   public String setTweet(Model model){//
       
       model.addAttribute("accountname",accountName);
       model.addAttribute("tweetcontents",tweetContents);
       model.addAttribute("screenname","@"+screenName);
       getAccountName(model);
       getScreenName(model);
       
      
       return "sample";
   }
    
   public String getAccountName(Model model){//アカウント名取得メソッド
       try {
           user = twitter.verifyCredentials();
           accountName = user.getName();
           
       } catch (TwitterException te) {
           te.printStackTrace();
           System.out.println("Failed to get timeline: " + te.getMessage());
           System.exit(-1);
       }
       return accountName;
   }
   
   public String getScreenName(Model model){//スクリーンネーム取得メソッド
       try {
           user = twitter.verifyCredentials();
           screenName = user.getScreenName();
           
       } catch (TwitterException te) {
           te.printStackTrace();
           System.out.println("Failed to get timeline: " + te.getMessage());
           System.exit(-1);
       }
       return screenName;
   }
   
   public String getAccountImg(Model model){//アカウント画像取得メソッド
       try {
           user = twitter.verifyCredentials();
           accountimgURL = user.getProfileImageURL();
           
       } catch (TwitterException te) {
           te.printStackTrace();
           System.out.println("Failed to get timeline: " + te.getMessage());
           System.exit(-1);
       }
       return accountimgURL;
   }
  
}
