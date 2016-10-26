package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.User;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.MFGT;
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
    GoodsDtailActivity mContext;
    @BindView(R.id.wvAbout)
    WebView wvAbout;

    boolean isCollected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_dtail);
        mContext = this;
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isCollect();
    }
    @OnClick(R.id.ivCollect)
    public void onClick() {
        final User user=FuLiCenterApplication.getUser();
        if(user==null){
            MFGT.gotoLogin(mContext);
        }else{
            if(isCollected){
                NetDao.deleteCollects(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null&&result.isSuccess()){
                            isCollected=!isCollected;
                            updateGoodsCollectStatus();
                            CommonUtils.showLongToast(result.getMsg());
                        }

                    }
                    @Override
                    public void onError(String error) {

                    }
                });
            }else{
                NetDao.addCollects(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null&&result.isSuccess()){
                            isCollected=!isCollected;
                            updateGoodsCollectStatus();
                            CommonUtils.showLongToast(result.getMsg());
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });

            }
        }
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
        if (details.getProperties().length > 0 && details.getProperties() != null) {
            return details.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAlbumImgUrl(GoodsDetailsBean details) {
        String[] albumImgUrl = new String[]{};
        if (details.getProperties().length > 0 && details.getProperties() != null) {
            AlbumsBean[] albums = details.getProperties()[0].getAlbums();
            albumImgUrl = new String[albums.length];
            for (int i = 0; i < albums.length; i++) {
                albumImgUrl[i] = albums[i].getImgUrl();
            }
        }
        return albumImgUrl;
    }

    private void isCollect() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            NetDao.isColected(mContext, user.getMuserName(), goodsID, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollected = true;
                    } else {
                        isCollected = false;
                    }
                    updateGoodsCollectStatus();
                }

                @Override
                public void onError(String error) {
                    isCollected = false;
                    updateGoodsCollectStatus();
                }
            });
        }
        updateGoodsCollectStatus();
    }

    private void updateGoodsCollectStatus() {
        if (isCollected) {
            ivCollect.setImageResource(R.mipmap.bg_collect_out);
        } else {
            ivCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }
    @OnClick(R.id.ivShare)
        public void showShare(){
            ShareSDK.initSDK(this);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle("标题");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
            oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite("ShareSDK");
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
            oks.show(this);
        }
    }