package cn.ucai.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.NewGoodAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

import static android.R.id.list;


public class BoutiqueDtailActivity extends AppCompatActivity {


    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    BoutiqueDtailActivity mContext;
    NewGoodAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    int pageId = 1;
    GridLayoutManager glm;
    BoutiqueBean boutique;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_boutique_dtail);
        ButterKnife.bind(this);
        boutique = (BoutiqueBean) getIntent().getSerializableExtra(I.Boutique.CAT_ID);
        if(boutique == null){
            finish();
        }
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new NewGoodAdapter(mContext,mList);
        initView();
        initData();
        setListener();
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );

        glm = new GridLayoutManager(mContext, I.COLUM_NUM);
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        tvCommonTitle.setText(boutique.getTitle());
    }

    private void initData() {
        downloadNewGoods(I.ACTION_DOWNLOAD);
    }

    private void downloadNewGoods(final int action) {
        NetDao.downloadNewGoods(mContext,boutique.getId(), new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                L.e("result="+result);
                if(result!=null && result.length>0){
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if(action==I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        L.e("he","List"+list.toString());
                        mAdapter.initData(list);
                    }else{
                        mAdapter.addData(list);
                    }
                    if(list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else{
                    mAdapter.setMore(false);
                }
                L.e("he","看这这这");
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
                L.e("error:"+error);
            }
        });
    }

    private void setListener() {
        setPullUpListener();
        setPullDownListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadNewGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = glm.findLastVisibleItemPosition();
                if(newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount()-1
                        && mAdapter.isMore()){
                    pageId++;
                    downloadNewGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = glm.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition==0);
            }
        });
    }

}