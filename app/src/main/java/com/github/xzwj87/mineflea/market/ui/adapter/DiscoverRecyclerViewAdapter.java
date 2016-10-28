package com.github.xzwj87.mineflea.market.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.DiscoverInfo;
import com.squareup.picasso.Picasso;
import com.tencent.tauth.bean.Pic;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by seaice on 2016/8/22.
 */
public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DiscoverInfo> infoList;
    private LayoutInflater mInflate;

    public DiscoverRecyclerViewAdapter(List<DiscoverInfo> lists) {
        infoList = lists;
        initData();
    }

    public interface DiscoverClickListener {
        void onDiscoverItemClick(int position);
    }

    private void initData() {
        mInflate = LayoutInflater.from(AppGlobals.getAppContext());
    }

    //绑定不同类型Holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_discover_goods_info, parent, false);

        return new DiscoverHolder(view);
    }

    //设置holder的view元素
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        DiscoverInfo info = infoList.get(position);
        DiscoverHolder holder = (DiscoverHolder) vh;
        holder.loc.setText(info.getDistance());
        holder.goodsImg.setBackgroundResource(R.mipmap.iv_test);
        holder.publisher.setText(info.getDate());
        holder.title.setText(info.getDes());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public class DiscoverHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_goods_img) ImageView goodsImg;
        @BindView(R.id.tv_title) TextView title;
        @BindView(R.id.tv_likes) TextView likes;
        @BindView(R.id.tv_location) TextView loc;
        @BindView(R.id.tv_publisher_date) TextView publisher;
        @BindView(R.id.tv_price) TextView price;
        @BindView(R.id.tv_note) TextView note;
        @BindView(R.id.btn_explore_more) TextView more;

        public DiscoverHolder(View view) {
            super(view);

            ButterKnife.bind(this,view);
        }
    }

    private void loadImageView(){
    }
}
