package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodsDtailActivity extends AppCompatActivity {
    int goodsID;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvShop)
    TextView tvShop;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.ivCart)
    ImageView ivCart;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.fid)
    FlowIndicator fid;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvMoney)
    TextView tvMoney;

    @BindView(R.id.activity_goods_dtail)
    LinearLayout activityGoodsDtail;
    Context mContext;
    @BindView(R.id.wvAbout)
    WebView wvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_dtail);
        ButterKnife.bind(this);
        mContext = this;
        initData();
    }

    private void initData() {
        goodsID = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        NetDao.downloadGoodDetails(mContext, goodsID, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                finish();
                CommonUtils.showShortToast(error);

            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean details) {
        tvName.setText(details.getGoodsName());
        tvMoney.setText(details.getCurrencyPrice());
        salv.startPlayLoop(fid, getAlbumImgUrl(details), getAlbumImgCount(details));
        wvAbout.loadDataWithBaseURL(null, details.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAlbumImgCount(GoodsDetailsBean details) {
        if(details.getProperties().length>0 && details.getProperties()!=null){
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] albumImgUrl=new String[]{};
        if(details.getProperties().length>0 && details.getProperties()!=null){
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            albumImgUrl=new String[albums.length];
            for(int i=0;i<albums.length;i++){
                albumImgUrl[i]= albums[i].getImgUrl();
            }
        }
        return albumImgUrl;
    }


}
