package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.ResultUtils;

public class AddressActivity extends BaseActivity implements PaymentHandler{
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
    ArrayList<CartBean> mList=null;
    String[] ids=new String[]{};
    double rankPrice=0;

    private static String URL = "http://218.244.151.190/demo/charge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mContext = this;
        mList=new ArrayList<>();

        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
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
        ids=cartId.split(",");
        getOrderList();
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.bt_commit)
    public void onClick() {
        String name=etUserName.getText().toString();
        if(TextUtils.isEmpty(name)){
            etUserName.setError("收件名不能为空");
            etUserName.requestFocus();
            return;
        }
        String phone=etPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            etPhone.setError("手机号不能为空");
            etPhone.requestFocus();
            return;
        }
        if(!phone.matches("[\\d]{11}")){
            etPhone.setError("手机号码格式错误");
            etPhone.requestFocus();
            return;
        }
        String address=etAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            etAddress.setError("地址不能为空");
            etAddress.requestFocus();
            return;
        }

        // 产生个订单号
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 构建账单json对象
        JSONObject bill = new JSONObject();


        try {
            bill.put("order_no", orderNo);
            bill.put("amount", rankPrice*100);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    @Override
    public void handlePaymentResult(Intent data) {
        CommonUtils.showLongToast("爷给钱了!!!!!");
    }

    public void getOrderList() {
        NetDao.downloadcarts(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<CartBean> list= ResultUtils.getCartFromJson(s);
                L.e("list有没有拿到东西啊>>"+list.toString());
                if(list==null||list.size()==0){
                    finish();
                }else {
                    mList.addAll(list);
                    sumPrice();
                    L.e("sumPrice="+mList.toString());
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }
    private void sumPrice() {
        rankPrice=0;
        if(mList!=null&&mList.size()>0) {
            L.e("mList>>>>>>"+mList.toString());
            for (CartBean c : mList) {
                L.e("c.id+"+c.getId());
                for (String id :ids) {
                    L.e("orderID="+id);
                    if (id.equals(String.valueOf(c.getId()))) {
                        rankPrice = getPrice(c.getGoods().getRankPrice()) * c.getCount();
                        L.e("rankPrice>>>>>>>>"+rankPrice);
                    }
                }
            }
        }
        tvSumPrice.setText("合计:￥"+Double.valueOf(rankPrice));
    }
    private int getPrice(String price){
        price=price.substring(price.indexOf("￥")+1);
        return Integer.parseInt(price);
    }
}
