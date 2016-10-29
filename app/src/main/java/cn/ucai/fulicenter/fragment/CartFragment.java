package cn.ucai.fulicenter.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    LinearLayoutManager llm;
    MainActivity mContext;
    CartAdapter mAdapter;
    ArrayList<CartBean> mList;
    @BindView(R.id.tv_nothing)
    RelativeLayout tvNothing;
    @BindView(R.id.layout_cart)
    RelativeLayout layoutCart;
    @BindView(R.id.tv_sum_price)
    TextView tvSumPrice;
    @BindView(R.id.tv_save_price)
    TextView tvSavePrice;

    updateCartReceiver mReceiver;
    double price=0;
    String CartId="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mList=new ArrayList<>();
        initView();
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownListener();
        IntentFilter filter=new IntentFilter(I.BROADCAST_UPDATA_CART);
        mReceiver=new updateCartReceiver();
        mContext.registerReceiver(mReceiver,filter);
    }

    private void setPullDownListener() {
        L.e("下拉请求你看到了没");
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                downloadCart();
            }
        });
    }

    private void initData() {
        downloadCart();
    }

    private void downloadCart() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            L.e("到达CartFragmentUser=" + user);
            NetDao.downloadcarts(mContext, user.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    ArrayList<CartBean> list= ResultUtils.getCartFromJson(s);
                    L.e("请求数据成功" + list);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    if (list != null && list.size() > 0) {
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.initData(mList);

                        setCartLayout(true);
                    }else {
                        setCartLayout(false);
                    }
                    sumPrice();
                }

                @Override
                public void onError(String error) {
                    L.e("请求数据失败,错误为+" + error);
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    CommonUtils.showShortToast(error);
                    L.e("error=" + error);
                    setCartLayout(false);
                }
            });
        }
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        llm = new LinearLayoutManager(mContext);
        mList = new ArrayList<>();
        mAdapter = new CartAdapter(mContext, mList);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        //setCartLayout(false);
    }

    private void setCartLayout(boolean hasCart) {
        layoutCart.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        tvNothing.setVisibility(hasCart ? View.GONE : View.VISIBLE);
        rv.setVisibility(hasCart ? View.VISIBLE : View.GONE);
        //sumPrice();
    }

    private void sumPrice(){
        double sumPrice=0;
        double rankPrice=0;
        CartId="";
        if(mList!=null&&mList.size()>0){
            for (CartBean c:mList){
                if(c.isChecked()){
                  sumPrice +=getPrice( c.getGoods().getCurrencyPrice())*c.getCount();
                    rankPrice += getPrice(c.getGoods().getRankPrice())*c.getCount();
                    CartId +=c.getId()+",";
                }
                tvSumPrice.setText("合计: ￥"+rankPrice);
                tvSavePrice.setText("节省:￥"+(sumPrice-rankPrice));
            }
        }else {
            setCartLayout(false);
            tvSumPrice.setText("合计:0");
            tvSavePrice.setText("节省:0");
        }
        price=rankPrice;
    }
    private int getPrice(String price){
        String str=price.substring(price.indexOf("￥")+1);
        return Integer.parseInt(str);
    }
    class updateCartReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            sumPrice();
           // setCartLayout(mList!=null&&mList.size()>0);
        }
    }
    @OnClick(R.id.bt_buy)
    public void onClick() {
        if(price>0&&!CartId.isEmpty()){
            L.e("＞＞＞＞＞＞＞＞＞＞＞＞＞＞＞＞＞＞＞＞CartId=="+CartId);
            MFGT.gotoAddress(mContext,CartId);
            CartId="";
        }else {
            CommonUtils.showLongToast("您还没有选择任何宝贝");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            mContext.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
