package com.github.xzwj87.mineflea.market.presenter;

import android.os.Message;

/**
 * Created by jason on 11/7/16.
 */

public interface PresenterCallback {
    void onComplete(Message message);

    void onNext(Message message);

    void onError(Throwable e);
}
