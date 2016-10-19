package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Intent;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.GoodsDtailActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.R;

import static cn.ucai.fulicenter.I.Property.goodsId;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoGoodsDtailActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, GoodsDtailActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
}
