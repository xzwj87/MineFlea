package com.github.xzwj87.mineflea.market.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.utils.PicassoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by seaice on 2016/8/22.
 */
public class DiscoverGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final long MIN_RESOLUTION = DateUtils.MINUTE_IN_MILLIS; // 1 minute
    private static final long TRANSITION_RESOLUTION = 3*DateUtils.DAY_IN_MILLIS; // 3 days
    //get data from presenter
    //private List<DiscoverInfo> infoList;
    private LayoutInflater mInflate;
    private DiscoverGoodsCallback mCallback;

    public DiscoverGoodsAdapter() {
        mInflate = LayoutInflater.from(AppGlobals.getAppContext());
    }

    public void setCallback(DiscoverGoodsCallback callback){
        mCallback = callback;
    }

    public interface DiscoverGoodsCallback {
        void onItemClick(int position);
        void onItemLongClick(View root, int pos);
        PublishGoodsInfo getItemAtPos(int pos);
        int getItemCount();
        String getPublisherHeadIcon(int pos);
        String getPublisherNickName(int pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.item_discover_goods_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        PublishGoodsInfo info = mCallback.getItemAtPos(position);
        String userHeadIcon = mCallback.getPublisherHeadIcon(position);
        String userNickName = mCallback.getPublisherNickName(position);

        if(info != null) {
            ViewHolder holder = (ViewHolder) vh;

            if(info.getImageUri() != null && info.getImageUri().size() > 0) {
                String imgUrl = info.getImageUri().get(0);
                PicassoUtils.loadImage(holder.goodsImg, imgUrl);
            }
            holder.likes.setText(String.valueOf(info.getStars()));
            holder.title.setText(info.getName());
/*            if (!TextUtils.isEmpty(userHeadIcon)) {
                PicassoUtils.loadImage(holder.headIcon, userHeadIcon);
            }*/


            holder.userNickName.setText(userNickName);

            long updatedTime = info.getReleasedDate().getTime();
            holder.updatedTime.setText(DateUtils.getRelativeDateTimeString(
                    AppGlobals.getAppContext(),updatedTime, MIN_RESOLUTION,TRANSITION_RESOLUTION,0));
            // Todo: like time, we may want to get relative distance
            holder.loc.setText(info.getLocation());
            if(!TextUtils.isEmpty(info.getNote())){
                holder.note.setText(info.getNote());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCallback.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_goods_img) ImageView goodsImg;
        @BindView(R.id.tv_title) TextView title;
        @BindView(R.id.tv_likes) TextView likes;
        @BindView(R.id.tv_location) TextView loc;
        @BindView(R.id.tv_note) TextView note;
        //public CircleImageView headIcon;
        public TextView userNickName;
        public TextView updatedTime;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this,view);

            RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.layout_publisher);
            //headIcon = (CircleImageView) rl.findViewById(R.id.civ_user_header);
            userNickName = (TextView) rl.findViewById(R.id.tv_user_nick_name);
            updatedTime = (TextView) rl.findViewById(R.id.tv_updated_time);

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
        }
    }
}
