package com.github.xzwj87.mineflea.market.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.utils.FileManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/7/16.
 */

public class PublishGoodsImageAdapter extends RecyclerView.Adapter<PublishGoodsImageAdapter.ImageViewHolder>{

    private static int MAX_COLS = 3;
    private ArrayList<String> mImgPath;
    private Context mContext;
    private ItemClickListener mListener;
    private static Bitmap sAddBitmap = decodeAddBitmap();

    public PublishGoodsImageAdapter(Context context, ArrayList<String> filePath){
        mImgPath = filePath;
        mContext = context;
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
                .inflate(R.layout.item_publish_goods_image,parent,false);

        return new ImageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Log.v(PublishGoodsImageAdapter.class.getSimpleName(),
                "onBindViewHolder(): pos = " + position);
        String path = mImgPath.get(position);

        if(position != (getItemCount()-1)) {
            Uri imgUri = Uri.fromFile(new File(path));
            Picasso.with(mContext)
                    .load(imgUri)
                    .resize(512,512)
                    .centerCrop()
                    .into(holder.imgView);

            holder.mBtnRemove.setVisibility(View.VISIBLE);
        }else{
            holder.imgView.setImageBitmap(sAddBitmap);
            holder.mBtnRemove.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mImgPath.size();
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

    private static int getImgSize(){
        WindowManager winMgr = (WindowManager)AppGlobals.getAppContext().
                getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        winMgr.getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels/MAX_COLS;
    }


    private static Bitmap decodeAddBitmap(){
        Drawable drawable;
        if(Build.VERSION.SDK_INT >= 21){
            drawable = AppGlobals.getAppContext().getDrawable(R.drawable.ic_add_grey_100dp);
        }else{
            drawable = AppGlobals.getAppContext().
                    getResources().getDrawable(R.drawable.ic_add_grey_100dp);
        }

        int size = getImgSize();
        return FileManager.drawableToBitmap(drawable,size,size);
    }
}
