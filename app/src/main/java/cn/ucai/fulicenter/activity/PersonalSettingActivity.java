package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class PersonalSettingActivity extends BaseActivity {

    @BindView(R.id.iv_userAvatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_userNick)
    TextView tvUserNick;
    PersonalSettingActivity mContext;
    User user=null;
    OnSetAvatarListener mOnSetAvatarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_setting);
        ButterKnife.bind(this);
        mContext=this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,getResources().getString(R.string.user_settings));
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        if(user==null){
            finish();
            return;
        }
        showInfo();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e("onActivityResult,requestCode="+requestCode+",resultCode="+resultCode);
        if(resultCode!=RESULT_OK){
            return;
        }
        mOnSetAvatarListener.setAvatar(requestCode,data,ivUserAvatar);
        if(requestCode== I.REQUEST_CODE_NICK){
            Toast.makeText(mContext, "修改昵称成功", Toast.LENGTH_SHORT).show();
        }
        if(requestCode==OnSetAvatarListener.REQUEST_CROP_PHOTO){
            updateAvatar();
        }
    }

    private void updateAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(mContext,
                user.getMavatarPath()+"/"+user.getMuserName()
                        +I.AVATAR_SUFFIX_JPG));
        
        NetDao.updateAvatar(mContext, user.getMuserName(), file, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e("s="+s);
                Result result = ResultUtils.getResultFromJson(s,User.class);
                L.e("result="+result);
                if(result==null){
                    Toast.makeText(mContext, "更新用户头像失败", Toast.LENGTH_SHORT).show();
                }else{
                    User u = (User) result.getRetData();
                    if(result.isRetMsg()){
                        FuLiCenterApplication.setUser(u);
                        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(u),mContext,ivUserAvatar);
                        Toast.makeText(mContext, "更新用户头像成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "更新用户头像失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(mContext, "更新用户头像失败", Toast.LENGTH_SHORT).show();
                L.e("error="+error);
            }
        });

    }

    @OnClick({R.id.rl_avatar, R.id.rl_userName, R.id.rl_userNick, R.id.bt_signOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                mOnSetAvatarListener = new OnSetAvatarListener(mContext,R.id.layout_upload_avatar,
                        user.getMuserName(),I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.rl_userName:
                Toast.makeText(mContext, "用户名作为登录名不能被修改", Toast.LENGTH_SHORT).show();;
                break;
            case R.id.rl_userNick:
                MFGT.gotoUpdateNick(mContext);
                break;
            case R.id.bt_signOut:
                signOut();
                break;
        }
    }

    private void signOut(){
        if(user!=null){
            SharePrefrenceUtils.getInstance(mContext).removeUser();
            FuLiCenterApplication.setUser(null);
            MFGT.gotoLogin(mContext);
        }
        finish();
    }
    private void showInfo(){
        user = FuLiCenterApplication.getUser();
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserName());
            tvUserNick.setText(user.getMuserNick());
        }
    }
}
