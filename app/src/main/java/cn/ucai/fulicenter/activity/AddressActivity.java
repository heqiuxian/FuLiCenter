package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;

public class AddressActivity extends BaseActivity {
    AddressActivity mContext;
    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_sumPrice)
    TextView tvSumPrice;
    String cartId="";
    User user=null;
    ArrayList<CartBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mContext = this;
    }

    @Override
    protected void initView() {
        //DisplayUtils.initBackWithTitle(mContext,getResources().getString(R.string.Receiving_information));
}

    @Override
    protected void initData() {
        cartId=getIntent().getStringExtra(I.Cart.ID);
        user= FuLiCenterApplication.getUser();
        if(cartId==null||cartId.equals("")||user==null){
            finish();
        }
        String[] ids=cartId.split(",");
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_commit)
    public void onClick() {
    }
}
