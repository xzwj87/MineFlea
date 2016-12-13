package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

import com.github.xzwj87.mineflea.market.data.ResponseCode;
import com.github.xzwj87.mineflea.market.data.repository.DataRepository;
import com.github.xzwj87.mineflea.market.internal.di.PerActivity;
import com.github.xzwj87.mineflea.market.model.UserFollowInfo;
import com.github.xzwj87.mineflea.market.ui.BaseView;
import com.github.xzwj87.mineflea.market.ui.UserFollowView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jason on 10/31/16.
 */

@PerActivity
public class UserFolloweePresenterImpl extends UserFolloweePresenter{

    private static final String TAG = UserFolloweePresenterImpl.class.getSimpleName();

    private UserFollowView mView;
    @Inject
    DataRepository mRepo;
    private List<UserFollowInfo> mUserFollowList;

    @Inject
    public UserFolloweePresenterImpl(DataRepository repository){
        mRepo = repository;
    }

    @Override
    public void init() {
        mRepo.init();
        mRepo.registerCallBack(PRESENTER_FOLLOWEE,new FolloweePresenterCallback());
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        mUserFollowList = null;
        mRepo.unregisterCallback(PRESENTER_FOLLOWEE);
    }

    @Override
    public void setView(BaseView view) {
        mView = (UserFollowView)view;
    }

    @Override
    public int getUserFollowCount() {
        return mUserFollowList.size();
    }

    @Override
    public UserFollowInfo getUserFollowAtPos(int pos) {
        if(mUserFollowList != null) {
            return mUserFollowList.get(pos);
        }

        throw new IllegalAccessError("no following list still!!!");
    }

    @Override
    public void getUserFollowList(String userId) {
        mRepo.queryUserFolloweeListByUserId(userId);
    }

    private class FolloweePresenterCallback implements PresenterCallback {

        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Message message) {
            int what = message.what;
            switch (what) {
                case ResponseCode.RESP_QUERY_FOLLOWEES_SUCCESS:
                    if (message.obj == null) {
                        mView.showBlankPage();
                    } else {
                        mUserFollowList = (List<UserFollowInfo>) message.obj;
                        mView.renderView();
                    }
                    mView.showProgress(false);
                    break;
                case ResponseCode.RESP_QUERY_FOLLOWEES_ERROR:
                    break;
            }
        }

        @Override
        public void onNext(Message message) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}
