package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.dao.SharePrefrenceUtils;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class UpdateNickActivity extends BaseActivity {

    @BindView(R.id.et_Nick)
    EditText etNick;
    UpdateNickActivity mContext;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        L.e(">>>>>>>>>>>>>>>>>>>>>>>>到这");
        DisplayUtils.initBackWithTitle(mContext, getResources().getString(R.string.update_user_nick));
    }

    @Override
    protected void initData() {
        user = FuLiCenterApplication.getUser();
        L.e("查看user="+user);
        if (user != null) {
            etNick.setText(user.getMuserNick());
            etNick.setSelectAllOnFocus(true);
        } else {
            finish();
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_upDateNick)
    public void onClick() {
        String nick = etNick.getText().toString().trim();
        if (nick.isEmpty()) {
            Toast.makeText(this, "昵称不可为空", Toast.LENGTH_SHORT).show();
        }
        if (nick.equals(user.getMuserNick())) {
            Toast.makeText(this, "昵称不能相同", Toast.LENGTH_SHORT).show();
        } else {
            updateNick(nick);
        }
    }

    private void updateNick(String nick) {
        NetDao.updateNick(mContext, user.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result= ResultUtils.getResultFromJson(s,User.class);
                L.e("result>>>>>>>>>>>>>>>>>"+result);
                if(result==null){
                    Toast.makeText(mContext, "更改昵称失败", Toast.LENGTH_SHORT).show();
                }else {
                    if(result.isRetMsg()){
                        L.e("result.isRetMsg>>>>>>>>"+result.isRetMsg());
                        User u= (User) result.getRetData();
                        UserDao dao=new UserDao(mContext);
                        boolean isSuccess=dao.updateUser(u);
                        if(isSuccess){
                            L.e("isSuccess>>>>>>>>"+isSuccess);
                            FuLiCenterApplication.setUser(u);
                            setResult(RESULT_OK);
                            Toast.makeText(mContext, "昵称更改成功", Toast.LENGTH_SHORT).show();
                            MFGT.finish(mContext);
                        }else {
                            Toast.makeText(mContext, "昵称更改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if(result.getRetCode()==I.MSG_USER_SAME_NICK){
                            Toast.makeText(mContext, "昵称未修改", Toast.LENGTH_SHORT).show();
                        }else if(result.getRetCode()==I.MSG_USER_UPDATE_NICK_FAIL){
                            Toast.makeText(mContext, "用户不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
