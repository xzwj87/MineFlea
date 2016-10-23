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
import com.github.xzwj87.mineflea.market.ui.DiscoverClickListener;

import java.util.List;
import butterknife.ButterKnife;

/**
 * Created by seaice on 2016/8/22.
 */
public class DiscoverRecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DiscoverClickListener{

    private List<DiscoverInfo> infoList;
    private LayoutInflater mInflate;
    private DiscoverClickListener discoverClicklistener;

    public DiscoverRecylerViewAdapter(List<DiscoverInfo> lists) {
        infoList = lists;
        initData();
    }

    private void initData() {
        mInflate = LayoutInflater.from(AppGlobals.getAppContext());
    }

    //绑定不同类型Holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_discover_recyler_view, parent, false);
        return new DiscoverHolder(view);
    }

    //设置holder的view元素
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        DiscoverInfo info = infoList.get(position);
        DiscoverHolder holder = (DiscoverHolder) vh;
        holder.distance.setText(info.getDistance());
        holder.imageUrl.setBackgroundResource(R.mipmap.test_info_iv);
        holder.date.setText(info.getDate());
        holder.des.setText(info.getDes());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    @Override
    public void onDiscoverItemClick(int position) {

    }

    //显示viewpager的Holder
    public class DiscoverHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageUrl;
        public TextView des;
        public TextView distance;
        public TextView date;
        public DiscoverHolder(View view) {
            super(view);
            imageUrl = ButterKnife.findById(view, R.id.iv_title);
            des = ButterKnife.findById(view, R.id.tv_title);
            distance = ButterKnife.findById(view, R.id.tv_distance);
            date = ButterKnife.findById(view, R.id.tv_date);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(discoverClicklistener != null){
                discoverClicklistener.onDiscoverItemClick(getPosition() - 1);
            }
        }
    }

    //设置条目点击监听器
    public void setDiscoverClicklistener(DiscoverClickListener listener){
        this.discoverClicklistener = listener;
    }
}
