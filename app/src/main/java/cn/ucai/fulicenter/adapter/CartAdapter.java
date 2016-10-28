package cn.ucai.fulicenter.adapter;

/**
 * Created by Administrator on 2016/10/27.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.net.OkHttpUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;


/**
 * Created by Administrator on 2016/10/19.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mcontext;
    ArrayList<CartBean> mList;

    public CartAdapter(Context context, ArrayList<CartBean> list) {
        mcontext = context;
        mList = list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CartViewHolder holder = new CartViewHolder(LayoutInflater.from(mcontext)
                .inflate(R.layout.item_cart, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CartBean cartbean = mList.get(position);
        GoodsDetailsBean goods = cartbean.getGoods();
        CartViewHolder holder1 = (CartViewHolder) holder;
        if (goods != null) {
            ImageLoader.downloadImg(mcontext, holder1.ivPicture, goods.getGoodsThumb());
            holder1.tvName.setText(goods.getGoodsName());
            holder1.tvMoney.setText(goods.getCurrencyPrice());
        }
        holder1.tvNum.setText("(" + cartbean.getCount() + ")");
        holder1.checkbox.setChecked(false);
        holder1.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartbean.setChecked(isChecked);
                mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
            }
        });
        holder1.ivAdd.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;

    }


    public void initData(ArrayList<CartBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_Name)
        TextView tvName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.iv_add)
        ImageView ivAdd;
        @BindView(R.id.iv_del)
        ImageView ivDel;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick({R.id.iv_picture, R.id.tv_money, R.id.tv_Name})
        public void gotoDetail() {
            int position= (int) ivAdd.getTag();
            CartBean cart=mList.get(position);
            MFGT.gotoGoodsDtailActivity(mcontext,cart.getGoodsId());
        }

        @OnClick(R.id.iv_add)
        public void addCart() {
            final int positon = (int) ivAdd.getTag();
            CartBean cart = mList.get(positon);
            NetDao.updateCart(mcontext, cart.getId(), cart.getCount() + 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        mList.get(positon).setCount(mList.get(positon).getCount() + 1);
                        mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                        tvNum.setText("(" + (mList.get(positon).getCount()) + ")");
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        @OnClick(R.id.iv_del)
        public void delCart() {
            final int position = (int) ivAdd.getTag();
            CartBean cart = mList.get(position);
            if (cart.getCount() > 1) {
                NetDao.updateCart(mcontext, cart.getId(), cart.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            mList.get(position).setCount(mList.get(position).getCount() - 1);
                            mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                            tvNum.setText("(" + (mList.get(position).getCount()) + ")");
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                NetDao.deleteCart(mcontext, cart.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            mList.remove(position);
                            mcontext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
}




