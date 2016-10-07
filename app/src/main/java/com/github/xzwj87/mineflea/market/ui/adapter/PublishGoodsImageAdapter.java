package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.github.xzwj87.mineflea.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/7/16.
 */

public class PublishGoodsImageAdapter extends RecyclerView.Adapter<PublishGoodsImageAdapter.ImageViewHolder>{

    private static int MAX_IMG_COLS = 3;
    private ArrayList<String> mImgPath;
    private int mImgSize;
    private Context mContext;
    private ItemClickListener mListener;

    public PublishGoodsImageAdapter(Context context, ArrayList<String> filePath){
        mImgPath = filePath;
        mContext = context;
        setColumnNumber(context,MAX_IMG_COLS);
    }

    public interface ItemClickListener{
        void onItemClickListener(int pos);
        void onItemLongClickListener(int pos);
    }

    public void setClickListener(ItemClickListener listener){
        mListener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        View root = LayoutInflater.from(context)
                .inflate(R.layout.item_layout_publish_goods_image,parent,false);

        return new ImageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String path = mImgPath.get(position);
        Uri imgUri = Uri.fromFile(new File(path));

        Picasso.with(mContext)
                .load(imgUri)
                .resize(mImgSize,mImgSize)
                .into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return mImgPath.size();
    }

    public void setColumnNumber(Context context,int cols){
        WindowManager winMgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        winMgr.getDefaultDisplay().getMetrics(metrics);

        mImgSize = metrics.widthPixels/cols;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.goods_img) RoundedImageView imgView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // only used by the last item
                    if(getLayoutPosition() == getItemCount()-1){
                        mListener.onItemClickListener(getItemCount()-1);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(getLayoutPosition() == getItemCount()-1){
                        mListener.onItemLongClickListener(getItemCount()-1);
                    }
                    return true;
                }
            });
        }
    }
}
