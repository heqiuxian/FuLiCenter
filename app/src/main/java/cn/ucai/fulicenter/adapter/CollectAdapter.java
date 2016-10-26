package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CollectAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CollectBean> mList;
    boolean isMore;

    public CollectAdapter(final Context context, ArrayList<CollectBean> list) {
        mContext=context;
        mList=new ArrayList<>();
        mList.addAll(list);
    }

    public boolean isMore(){
        return isMore;
    }
    public void setMore(boolean more){
        isMore=more;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            holder = new CollectViewHolder(View.inflate(mContext, R.layout.item_collect, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==I.TYPE_FOOTER){
            FooterViewHolder footerViewHolder= (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFootString());
        }else {
            CollectViewHolder vh= (CollectViewHolder) holder;
            CollectBean goods=mList.get(position);
            ImageLoader.downloadImg(mContext,vh.ivGoodsThumb,goods.getGoodsThumb());
            vh.tvGoodsName.setText(goods.getGoodsName());

            vh.layoutGoods.setTag(goods.getGoodsId());
            vh.ivCollectDelete.setTag(goods.getGoodsId());

        }
    }
    private int getFootString(){
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initData(ArrayList<CollectBean> list) {
        if(mList!=null){
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<CollectBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CollectViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.ivCollectDelete)
        ImageView ivCollectDelete;
       @BindView(R.id.layout_goods)
        RelativeLayout layoutGoods;

        CollectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.layout_goods)
        public void onGoodsItemClick(){
            CollectBean collectbean= (CollectBean) layoutGoods.getTag();
            MFGT.gotoGoodsDtailActivity(mContext,collectbean.getGoodsId());
        }
        @OnClick(R.id.ivCollectDelete)
        public void deleteCollect(){
           final CollectBean goods= (CollectBean) layoutGoods.getTag();
            L.e(">>>>>>>>>>>>>>>>>>>>>>>>");
            String username = FuLiCenterApplication.getUser().getMuserName();
            NetDao.deleteCollects(mContext, username,goods.getGoodsId(),new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null&&result.isSuccess()){
                        mList.remove(goods);
                        notifyDataSetChanged();
                        L.e(">>>>>>>成功"+mList.toString());
                    }else {
                        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                        L.e(">>>>>>>失败");
                    }
                }
                @Override
                public void onError(String error) {
                    Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                    L.e(">>>>>>>>>>失败"+error);
                }
            });
        }
    }
}
