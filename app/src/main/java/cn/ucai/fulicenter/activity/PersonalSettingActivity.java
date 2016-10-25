package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
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
        user= FuLiCenterApplication.getUser();
        if(user==null){
            MFGT.gotoLogin(mContext);
            L.e("为空>>>>>>>>>>>进入登录"+user);
        }else{
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserName());
            tvUserNick.setText(user.getMuserNick());
            L.e("不为空,设置个人资料参数>>>>>>>>>>");
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        user=FuLiCenterApplication.getUser();
        if(user!=null){
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),mContext,ivUserAvatar);
            tvUserName.setText(user.getMuserName());
        }
    }

    @OnClick({R.id.rl_avatar, R.id.rl_userName, R.id.rl_userNick, R.id.bt_signOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
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
}
