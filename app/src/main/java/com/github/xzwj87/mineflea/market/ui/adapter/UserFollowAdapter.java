package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.UserFollowInfo;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jason on 10/31/16.
 */

public class UserFollowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private UserFollowCallback mCb;
    private Context mContext;


    public UserFollowAdapter(){}

    public interface UserFollowCallback{
        int getUserFollowCount();
        UserFollowInfo getUserFollowAtPos(int pos);
    }

    public void setCallback(UserFollowCallback cb){
        mCb = cb;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View root = LayoutInflater.from(mContext)
                      .inflate(R.layout.item_user_followers,parent,false);

        return new UserFollowViewHolder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserFollowInfo info = mCb.getUserFollowAtPos(position);

        UserFollowViewHolder vh = (UserFollowViewHolder)holder;
        vh.tvNickName.setText(info.nickName);
        vh.tvEmail.setText(info.eMail);

        if(URLUtil.isNetworkUrl(info.headIconUrl)) {
            Picasso.with(mContext)
                   .load(Uri.parse(info.headIconUrl))
                   .resize(1024,1024)
                   .centerCrop()
                   .into(vh.civHead);
        }else{
            Picasso.with(mContext)
                    .load(Uri.fromFile(new File(info.headIconUrl)))
                    .resize(1024,1024)
                    .centerCrop()
                    .into(vh.civHead);
        }

    }

    @Override
    public int getItemCount() {
        return mCb.getUserFollowCount();
    }


    public class UserFollowViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.civ_user_header)
        CircleImageView civHead;
        @BindView(R.id.tv_user_nick_name)
        TextView tvNickName;
        @BindView(R.id.tv_user_email)
        TextView tvEmail;

        public UserFollowViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }
    }
}
