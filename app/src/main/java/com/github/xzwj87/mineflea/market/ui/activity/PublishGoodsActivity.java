package com.github.xzwj87.mineflea.market.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.market.internal.di.HasComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.DaggerMarketComponent;
import com.github.xzwj87.mineflea.market.internal.di.component.MarketComponent;
import com.github.xzwj87.mineflea.market.model.GoodsModel;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenter;
import com.github.xzwj87.mineflea.market.presenter.PublishGoodsPresenterImpl;
import com.github.xzwj87.mineflea.market.ui.PublishGoodsView;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jason on 9/27/16.
 */

public class PublishGoodsActivity extends BaseActivity
        implements PublishGoodsView,HasComponent<MarketComponent> {
    public static final String TAG = PublishGoodsActivity.class.getSimpleName();

    @BindView(R.id.goods_icon) ImageButton mIbGoodsIcon;
    @BindView(R.id.et_goods_name) EditText mEtGoodsName;
    @BindView(R.id.et_goods_high_price) EditText mEtHighPrice;
    @BindView(R.id.et_goods_low_price) EditText mEtLowPrice;
    @BindView(R.id.et_depreciation_rate) EditText mEtDepRate;
    @BindView(R.id.et_note) EditText mEtNote;

    private GoodsModel mGoods;
    private PublishGoodsPresenter mPresenter;
    private MarketComponent mMarketComponent;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_publish_goods);

        init();
        initInjector();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        mPresenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_publish_goods,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();

        switch (id){
            case R.id.action_ok:
                getGoodsData();

                publishGoods();

                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @OnClick(R.id.ib_goods_icon)
    void uploadGoodsIcon(){
        Log.v(TAG,"uploadGoodsIcon()");

    }

    @Override
    public void publishGoods(){
        Log.v(TAG,"publishGoods()");

    }

    private void getGoodsData(){
        mGoods.setName(mEtGoodsName.getText().toString());

        mGoods.setDepreciationRate(Double.parseDouble(mEtDepRate.getText().toString()));

        mGoods.setHighPrice(Double.parseDouble(mEtHighPrice.getText().toString()));

        mGoods.setLowerPrice(Double.parseDouble(mEtLowPrice.getText().toString()));

        mGoods.setReleasedDate(new Date());
    }

    private void init(){
        mPresenter = new PublishGoodsPresenterImpl(this);
    }

    private void initInjector(){
        mMarketComponent = DaggerMarketComponent.builder()
                .activityModule(getActivityModule())
                .build();

        mMarketComponent.inject(this);
    }

    @Override
    public MarketComponent getComponent() {
        return mMarketComponent;
    }
}
