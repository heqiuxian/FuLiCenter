package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.view.DisplayUtils;

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.et_register_nick)
    EditText etRegisterNick;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.et_register_againpassword)
    EditText etRegisterAgainpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(this, "注册");
    }

    @Override
    protected void initData() {

    }

    private void onRegister() {
        NetDao.Register(this, etRegisterUsername.getText().toString(), etRegisterNick.getText().toString(), etRegisterPassword.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if(result.getRetCode()==0){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }else if (result.getRetCode()==I.MSG_REGISTER_USERNAME_EXISTS){
                    Toast.makeText(RegisterActivity.this, "用户已经存在", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                L.e("register");
            }
        });
    }

    @Override
    protected void setListener() {

    }


    @OnClick(R.id.btregister_register)
    public void onClick() {
        String userName =etRegisterUsername.getText().toString().trim();
        if(userName==null||userName.length()==0){
            etRegisterUsername.setError("帐号不能为空");
            return;
        }
        String userNick=etRegisterNick.getText().toString().trim();
        if(userNick==null||userNick.length()==0){
            etRegisterNick.setError("昵称不能为空");
            return;
        }
        String userPassword =etRegisterPassword.getText().toString().trim();
        if(userPassword==null||userPassword.length()==0){
            etRegisterPassword.setError("请再次确认密码");
            return;
        }
        String userAgain =etRegisterAgainpassword.getText().toString().trim();
        if(userAgain==null||userAgain.length()==0){
            etRegisterAgainpassword.setError("确认密码不能为空");
            return;
        }
        if(!userName.matches("[a-zA-Z]\\w{5,15}")) {
            etRegisterUsername.setError("用户名以字母开头,6-16位");
            return;
        }
        onRegister();
    }
}
