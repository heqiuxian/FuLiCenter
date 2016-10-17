package cn.ucai.fulicenter;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.bean.CartBean;

/**
 * Created by Administrator on 2016/10/17.
 */

public class FuLiCenterApplication extends Application {
    public static Context applicationContext;
    public static FuLiCenterApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=this;
        instance=this;
    }
    public static FuLiCenterApplication getInstance(){
        return instance;
    }
    private int collectCount;
    private List<CartBean> cartList=new ArrayList<CartBean>();


    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public List<CartBean> getCartList() {
        return cartList;
    }

    public void setCartList(List<CartBean> cartList) {
        this.cartList = cartList;
    }
}
