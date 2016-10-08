package com.github.xzwj87.mineflea.market.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.xzwj87.mineflea.BuildConfig;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.utils.FileManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jason on 10/7/16.
 */

public class PublishGoodsImageAdapter extends RecyclerView.Adapter<PublishGoodsImageAdapter.ImageViewHolder>{

    private static int MAX_IMG_COLS = 3;
    private int mItemSize;
    private ArrayList<String> mImgPath;
    private int mImgSize;
    private Context mContext;
    private ItemClickListener mListener;

    public PublishGoodsImageAdapter(Context context, ArrayList<String> filePath){
        mImgPath = filePath;
        mContext = context;
        mItemSize = mImgPath.size();
        setColumnNumber(context,MAX_IMG_COLS);
    }

    public interface ItemClickListener{
        void onItemClickListener(int pos);
        void onItemLongClickListener(int pos);
        void onButtonRemoveClickListener(int pos);
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

        if(path != null) {
            Uri imgUri = Uri.fromFile(new File(path));
            Picasso.with(mContext)
                    .load(imgUri)
                    .resize(mImgSize, mImgSize)
                    .into(holder.imgView);

            holder.mBtnRemove.setVisibility(View.VISIBLE);
        }else{
            Drawable drawable;
            if(Build.VERSION.SDK_INT >= 21){
                drawable = mContext.getDrawable(R.drawable.ic_add_grey_100dp);
            }else{
                drawable = mContext.
                        getResources().getDrawable(R.drawable.ic_add_grey_100dp);
            }

            Bitmap bitmap = FileManager.drawableToBitmap(drawable,mImgSize,mImgSize);
            if(bitmap != null) {
                holder.imgView.setImageBitmap(bitmap);
            }
            holder.imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
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
        @BindView(R.id.btn_remove) ImageButton mBtnRemove;

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

            mBtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onButtonRemoveClickListener(getLayoutPosition());
                }
            });
        }
    }
}
