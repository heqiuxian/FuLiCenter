package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
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
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDtailActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;


/**
 * Created by Administrator on 2016/10/19.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mcontext;
    ArrayList<BoutiqueBean> mlist;

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        mcontext=context;
        mlist=new ArrayList<>();
        mlist.addAll(list);
    }

    @Override
    public BoutiqueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BoutiqueViewHolder holder = new BoutiqueViewHolder(LayoutInflater.from(mcontext)
                .inflate(R.layout.item_boutique, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiqueBean boutiquebean = mlist.get(position);
        ImageLoader.downloadImg(mcontext, ((BoutiqueViewHolder) holder).ivBoutiqueImg, boutiquebean.getImageurl());
        ((BoutiqueViewHolder) holder).tvBoutiqueTitle.setText(boutiquebean.getTitle());
        ((BoutiqueViewHolder) holder).tvBoutiqueName.setText(boutiquebean.getName());
        ((BoutiqueViewHolder) holder).tvBoutiqueDescription.setText(boutiquebean.getDescription());
        ((BoutiqueViewHolder) holder).layoutBoutiqueItem.setTag(boutiquebean);
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() : 0;
    }


    public void initData(ArrayList<BoutiqueBean> list) {
        if (mlist != null) {
            mlist.clear();
        }
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
        @OnClick(R.id.layout_boutique_item)
        public void onItemClick() {
            BoutiqueBean Bean = (BoutiqueBean) layoutBoutiqueItem.getTag();
            MFGT.gotoBoutiqueChildActivity(mcontext,Bean);
        }
    }


}
