package cn.ucai.fulicenter;

import android.app.Application;

import cn.ucai.fulicenter.bean.User;

/**
 * Created by Administrator on 2016/10/17.
 */

public class FuLiCenterApplication extends Application {
    public static FuLiCenterApplication application;
    private static FuLiCenterApplication instance;
    private static String userName;
    private static User user;


    public static String getUseName() {
        return userName;
    }

    public static void setUseName(String useName) {
        FuLiCenterApplication.userName = useName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        instance = this;
    }
    public static FuLiCenterApplication getInstance(){
        if(instance==null){
            instance=new FuLiCenterApplication();
        }
        return instance;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        FuLiCenterApplication.user = user;
    }
}
