package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;


/**
 * Created by Administrator on 2016/10/19.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mcontext;
    ArrayList<BoutiqueBean> mlist;
    boolean isMore;

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        mcontext = context;
        mlist = list;
        mlist.addAll(list);
    }
    public boolean isMore(){
        return isMore;
    }
    public void setMore(boolean more){
        isMore=more;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new FooterViewHolder(LayoutInflater.from(mcontext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new BoutiqueViewHolder(LayoutInflater.from(mcontext)
                    .inflate(R.layout.item_boutique, parent, false));

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FooterViewHolder){
            ((FooterViewHolder) holder).tvFooter.setText(getFooterString());

        }
        if(holder instanceof BoutiqueAdapter.BoutiqueViewHolder){
            BoutiqueBean boutiquebean=mlist.get(position);
            ImageLoader.downloadImg(mcontext,((BoutiqueViewHolder) holder).ivBoutiqueImg,boutiquebean.getImageurl());
            ((BoutiqueViewHolder)holder).tvBoutiqueTitle.setText(boutiquebean.getTitle());
            ((BoutiqueViewHolder) holder).tvBoutiqueName.setText(boutiquebean.getName());
            ((BoutiqueViewHolder) holder).tvBoutiqueDescription.setText(boutiquebean.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mlist!=null?mlist.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    private int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    public void initData(ArrayList<BoutiqueBean> list) {
        if(mlist!=null){
            mlist.clear();
        }
        mlist.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ArrayList<BoutiqueBean> list) {
        mlist.addAll(list);
        notifyDataSetChanged();
    }


    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;
        @BindView(R.id.layout_boutique_item)
        RelativeLayout layoutBoutiqueItem;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
