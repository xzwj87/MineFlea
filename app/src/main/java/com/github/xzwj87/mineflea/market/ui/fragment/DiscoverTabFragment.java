package com.github.xzwj87.mineflea.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.xzwj87.mineflea.R;
import com.github.xzwj87.mineflea.app.AppGlobals;
import com.github.xzwj87.mineflea.market.model.DiscoverInfo;
import com.github.xzwj87.mineflea.market.ui.DiscoverClickListener;
import com.github.xzwj87.mineflea.market.ui.activity.DiscoverGoodsActivity;
import com.github.xzwj87.mineflea.market.ui.adapter.DiscoverRecylerViewAdapter;
import com.github.xzwj87.mineflea.utils.DiscoverProtocol;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jason on 10/9/16.
 */

public class DiscoverTabFragment extends BaseFragment implements DiscoverClickListener{
    public static final String TAG = DiscoverTabFragment.class.getSimpleName();

    private DiscoverRecylerViewAdapter recylerViewAdapter;
    private List<DiscoverInfo> disInfolist;


    @BindView(R.id.discover_recycler_view)
    RecyclerView discoverRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    public DiscoverTabFragment() {
    }

    public static DiscoverTabFragment newInstance() {
        DiscoverTabFragment fragment = new DiscoverTabFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedSate) {
        View root = inflater.inflate(R.layout.fragment_discover_tab, container, false);
        ButterKnife.bind(this, root);
        init();
        return root;
    }

    private void init() {
        DiscoverProtocol protocol = new DiscoverProtocol();
        disInfolist = protocol.loadDiscoverData();
        recylerViewAdapter = new DiscoverRecylerViewAdapter(disInfolist);
        recylerViewAdapter.setDiscoverClicklistener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppGlobals.getAppContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        discoverRecyclerView.setLayoutManager(layoutManager);
        discoverRecyclerView.setAdapter(recylerViewAdapter);
        setSwipeLayout();
    }

    //设置下拉刷新
    private void setSwipeLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AppGlobals.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(AppGlobals.getAppContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onDiscoverItemClick(int position) {
        Toast.makeText(AppGlobals.getAppContext(), "显示", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DiscoverGoodsActivity.class);
        startActivity(intent);
    }
}
