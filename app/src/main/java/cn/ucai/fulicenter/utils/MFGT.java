package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.activity.AddressActivity;
import cn.ucai.fulicenter.activity.BoutiqueDtailActivity;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.activity.CollectActivity;
import cn.ucai.fulicenter.activity.GoodsDtailActivity;
import cn.ucai.fulicenter.activity.LoginActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.PersonalSettingActivity;
import cn.ucai.fulicenter.activity.RegisterActivity;
import cn.ucai.fulicenter.activity.UpdateNickActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;

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
        startActivity(context,intent);
    }
    public static void gotoGoodsDtailActivity(Context context,int goodsId){
        Intent intent = new Intent();
        intent.setClass(context, GoodsDtailActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
    }
    public static void startActivity(Context context, Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }


    public static void gotoBoutiqueChildActivity(Context context, BoutiqueBean bean){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueDtailActivity.class);
        intent.putExtra(I.Boutique.CAT_ID,bean);
        startActivity(context,intent);
    }
    public static void gotoCategoryChildActivity(Context context,int catId,String groupName, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,groupName);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
    }
    public static void gotoLogin(Activity context){
        Intent intent=new Intent();
        intent.setClass(context,LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_LOGIN);

    }
    public static void gotoRegister(Activity context){
        Intent intent=new Intent();
        intent.setClass(context,RegisterActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_REGISTER);
    }
    public static void startActivityForResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void gotoPersonalSetting(Activity context){
        Intent intent=new Intent();
        intent.setClass(context, PersonalSettingActivity.class);
        startActivity(context,intent);
    }
    public static void gotoUpdateNick(Activity context){
        Intent intent=new Intent();
        intent.setClass(context, UpdateNickActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_NICK);
    }
    public static void gotoCollect(Activity context){
        Intent intent=new Intent();
        intent.setClass(context, CollectActivity.class);
        startActivity(context,intent);
    }
    public static void gotoAddress(Activity context,String cartIds){
        Intent intent=new Intent();
        intent.setClass(context, AddressActivity.class).putExtra(I.Cart.ID,cartIds);
        startActivity(context,intent);
    }

}
