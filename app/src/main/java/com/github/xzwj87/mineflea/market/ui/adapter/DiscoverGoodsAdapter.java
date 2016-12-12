package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.utils.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by seaice on 2016/8/22.
 */
public class DiscoverGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final long MIN_RESOLUTION = DateUtils.MINUTE_IN_MILLIS; // 1 minute
    private static final long TRANSITION_RESOLUTION = DateUtils.DAY_IN_MILLIS*3;

    private static String DIST_UNITS = AppGlobals.getAppContext().
            getString(R.string.dist_units);

    private Context mContext;
    //get data from presenter
    private LayoutInflater mInflate;
    private DiscoverGoodsCallback mCallback;
    // current location
    private LatLng mCurrent;
    private List<TextView> mTvLikesList;

    public DiscoverGoodsAdapter(Context context) {
        mInflate = LayoutInflater.from(context);
        mContext = context;
        mTvLikesList = new ArrayList<>();
    }

    public void setCallback(DiscoverGoodsCallback callback){
        mCallback = callback;
    }

    public interface DiscoverGoodsCallback {
        void onItemClick(int position);
        void onItemLongClick(View root, int pos);
        PublishGoodsInfo getItemAtPos(int pos);
        int getItemCount();
        String getPublisherNickName(int pos);
        void addToMyFavor(int pos);
    }

    public void setCurrentLoc(LatLng loc){
        mCurrent = loc;
    }

    public void updateLikesView(int pos,int stars){
        ((TextView)mTvLikesList.get(pos)).setText(String.valueOf(stars));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_discover_goods_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        PublishGoodsInfo info = mCallback.getItemAtPos(position);
        String userNickName = mCallback.getPublisherNickName(position);

        if(info != null) {
            ViewHolder holder = (ViewHolder) vh;

            if(info.getImageUri().size() > 0) {
                String imgUrl = info.getImageUri().get(0);
                PicassoUtils.loadImage(holder.goodsImg, imgUrl);
            }else{
                PicassoUtils.loadImage(holder.goodsImg,R.mipmap.no_pictures);
            }

            String p = mContext.getString(R.string.currency_symbol) + String.valueOf(info.getPrice());
            holder.price.setText(p);
            holder.likes.setText(String.valueOf(info.getStars()));
            mTvLikesList.add(holder.likes);
            holder.title.setText(info.getName());

            holder.nickName.setText(userNickName);

            long updatedTime = info.getReleasedDate().getTime();
/*            String time = DateUtils.getRelativeDateTimeString(mContext,updatedTime,
                    MIN_RESOLUTION,TRANSITION_RESOLUTION,DateUtils.FORMAT_SHOW_TIME).toString();*/
            String time = DateUtils.getRelativeTimeSpanString(updatedTime,System.currentTimeMillis(),
                    MIN_RESOLUTION).toString();
            String dateStr = (time.split(", "))[0];
            String updateAt = " " + mContext.getString(R.string.updated_at) + " " +  dateStr;

            holder.updatedTime.setText(updateAt);

            int dist = (int)AMapUtils.calculateLineDistance(info.getLocation(),mCurrent);
            String distGap = mContext.getString(R.string.distance_from_you);
            holder.loc.setText(info.getLocDetail() + "," + distGap + " " +
                    String.valueOf(dist)  + DIST_UNITS);
        }
    }

    @Override
    public int getItemCount() {
        return mCallback.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_goods_img) ImageView goodsImg;
        @BindView(R.id.tv_goods_price) TextView price;
        @BindView(R.id.tv_user_nick_name) TextView nickName;
        @BindView(R.id.tv_updated_time) TextView updatedTime;
        @BindView(R.id.tv_title) TextView title;
        @BindView(R.id.tv_likes) TextView likes;
        @BindView(R.id.tv_location) TextView loc;


        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this,view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onItemClick(getLayoutPosition());
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCallback.onItemLongClick(v,getLayoutPosition());
                    return true;
                }
            });

            likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.addToMyFavor(getLayoutPosition());
                }
            });
        }
    }
}
