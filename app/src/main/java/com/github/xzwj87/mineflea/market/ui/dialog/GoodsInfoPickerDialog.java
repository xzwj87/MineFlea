package com.github.xzwj87.mineflea.market.ui.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.model.PublishGoodsInfo;
import com.github.xzwj87.mineflea.market.ui.activity.GoodsDetailActivity;
import com.github.xzwj87.mineflea.utils.PicassoUtils;
import com.github.xzwj87.mineflea.utils.UserPrefsUtil;

/**
 * Created by jason on 12/12/16.
 */

public class GoodsInfoPickerDialog extends DialogFragment{

    public static GoodsInfoPickerDialog newInstance(PublishGoodsInfo goodsInfo){
        GoodsInfoPickerDialog dialog = new GoodsInfoPickerDialog();

        Bundle bundle = new Bundle();
        bundle.putString(PublishGoodsInfo.GOODS_ID,goodsInfo.getId());
        bundle.putString(PublishGoodsInfo.GOODS_NAME,goodsInfo.getName());
        bundle.putDouble(PublishGoodsInfo.GOODS_PRICE,goodsInfo.getPrice());
        bundle.putString(PublishGoodsInfo.GOODS_LOC_DETAIL,goodsInfo.getLocDetail());
        bundle.putDouble(PublishGoodsInfo.GOODS_LOC_LAT,goodsInfo.getLocation().latitude);
        bundle.putDouble(PublishGoodsInfo.GOODS_LOC_LONG,goodsInfo.getLocation().longitude);
        if(goodsInfo.getImageUri().size() != 0) {
            bundle.putString(PublishGoodsInfo.GOODS_IMAGES, goodsInfo.getImageUri().get(0));
        }
        bundle.putInt(PublishGoodsInfo.GOODS_LIKES,goodsInfo.getStars());

        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedState){
        View root = inflater.inflate(R.layout.nearby_fragment_goods_dialog,container,false);

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();


        Window window = getDialog().getWindow();

        if(window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lp.gravity = Gravity.BOTTOM;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            window.setWindowAnimations(R.style.dialogWindowAnim);
            // has to be set to be full screen width
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        renderView(getDialog());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedState){

        setStyle(STYLE_NO_FRAME,R.style.FullWidthDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog dialog = builder.setView(R.layout.nearby_fragment_goods_dialog)
                               .create();

        return dialog;
    }

    private void renderView(Dialog root){
        ImageView image = (ImageView) root.findViewById(R.id.iv_goods_image);
        TextView loc = (TextView) root.findViewById(R.id.tv_loc);
        TextView price = (TextView) root.findViewById(R.id.tv_goods_price);
        TextView name = (TextView) root.findViewById(R.id.tv_goods_name);
        TextView likes = (TextView) root.findViewById(R.id.tv_likes);
        Button check = (Button) root.findViewById(R.id.btn_check);

        final Bundle bundle = getArguments();

        String url = bundle.getString(PublishGoodsInfo.GOODS_IMAGES);
        if(!TextUtils.isEmpty(url)) {
            PicassoUtils.loadImage(getContext(), image, url);
        }else{
            PicassoUtils.loadImage(getContext(),image,R.mipmap.no_pictures);
        }

        LatLng latLng = UserPrefsUtil.getCurrentLocation();
        if(latLng == null){
            latLng = new LatLng(0,0);
        }
        String locDetail = bundle.getString(PublishGoodsInfo.GOODS_LOC_DETAIL);
        double lat = bundle.getDouble(PublishGoodsInfo.GOODS_LOC_LAT,latLng.latitude);
        double log = bundle.getDouble(PublishGoodsInfo.GOODS_LOC_LONG,latLng.longitude);
        LatLng latlng1 = new LatLng(lat,log);

        int dist = (int)(AMapUtils.calculateLineDistance(latLng,latlng1));
        String distGap = getString(R.string.distance_from_you);
        String units = getString(R.string.dist_units);
        String detail = locDetail + "," + distGap + " " + String.valueOf(dist) + units;
        loc.setText(detail);

        String nameStr = bundle.getString(PublishGoodsInfo.GOODS_NAME);
        name.setText(nameStr);

        String symbol = getString(R.string.currency_symbol);
        String p = symbol + String.valueOf(bundle.getDouble(PublishGoodsInfo.GOODS_PRICE,0));
        price.setText(p);

        likes.setText(String.valueOf(bundle.getInt(PublishGoodsInfo.GOODS_LIKES,0)));

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = bundle.getString(PublishGoodsInfo.GOODS_ID);
                checkDetail(id);
                dismiss();
            }
        });
    }

    private void checkDetail(String id){
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra(PublishGoodsInfo.GOODS_ID,id);

        startActivity(intent);
    }
}
