package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import cn.ucai.fulicenter.utils.MFGT;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    @OnClick({R.id.btLogin, R.id.btRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btLogin:
                isNull();
                break;
            case R.id.btRegister:
                MFGT.gotoRegister(this);
                break;
        }
    }

    public void isNull() {
        String userName = etUserName.getText().toString().trim();
        if (userName == null || userName.length() == 0) {
            etUserName.setError("帐号不能为空");
            return;
        }
        String password=etPassword.getText().toString().trim();
        if(password==null||password.length()==0) {
            etPassword.setError("密码不能为空");
            return;
        }
        onLogin();
    }

    private void onLogin() {
        NetDao.Login(this,etUserName.getText().toString(), etPassword.getText().toString(), new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                if(result.getRetCode()==0){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }else if(result.getRetCode()== I.MSG_LOGIN_UNKNOW_USER){
                    Toast.makeText(LoginActivity.this, "账户名不存在", Toast.LENGTH_SHORT).show();
                }else if(result.getRetCode()==I.MSG_LOGIN_ERROR_PASSWORD){
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==I.REQUEST_CODE_REGISTER){
            String username = data.getStringExtra(I.User.USER_NAME);
            etUserName.setText(username);
        }
    }
}

