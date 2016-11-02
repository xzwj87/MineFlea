package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserGoodsInfo;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/27/16.
 */

public class UserGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private UserGoodsInfo mGoods;
    private UserGoodsCallback mCb;
    private Context mContext;

    public interface UserGoodsCallback {
        int getGoodsCount();
        UserGoodsInfo getGoodsAtPos(int pos);
    }

    public UserGoodsAdapter(){}

    public void setCallback(UserGoodsCallback cb){
        mCb = cb;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View root = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_user_goods,parent,false);

        return new GoodsViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mGoods = mCb.getGoodsAtPos(position);

        GoodsViewHolder vh = (GoodsViewHolder)holder;
        vh.tvName.setText(mGoods.getName());
        vh.tvPrice.setText(String.valueOf(mGoods.getPrice()));
        vh.tvLikes.setText(String.valueOf(mGoods.getLikes()));

        if(URLUtil.isNetworkUrl(mGoods.getUrlList().get(0))) {
            Picasso.with(mContext)
                    .load(Uri.parse(mGoods.getUrlList().get(0)))
                    .resize(1024, 1024)
                    .centerCrop()
                    .into(vh.goodsImage);
        }else{
            Picasso.with(mContext)
                    .load(Uri.fromFile(new File(mGoods.getUrlList().get(0))))
                    .resize(1024, 1024)
                    .centerCrop()
                    .into(vh.goodsImage);
        }

    }

    @Override
    public int getItemCount() {
        return mCb.getGoodsCount();
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_goods_name) TextView tvName;
        @BindView(R.id.tv_goods_price) TextView tvPrice;
        @BindView(R.id.tv_likes) TextView tvLikes;
        @BindView(R.id.iv_goods_image) ImageView goodsImage;

        public GoodsViewHolder(View item){
            super(item);

            ButterKnife.bind(this,item);
        }
    }
}
