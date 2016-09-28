package com.github.xzwj87.mineflea.data.repository;

import com.github.xzwj87.mineflea.data.RepoResponseCode;
import com.github.xzwj87.mineflea.model.PublisherModel;

import java.util.List;

import rx.Observable;

/**
 * Created by jason on 9/26/16.
 */

public interface PublisherRepository {

    /**
     * get the detail of a publisher
     */
    Observable<PublisherModel> getPublisherDetail(long id);

    /**
     * get a favor publisher detail
     */
    Observable<PublisherModel> getFavorPublisherDetail(long id);

    /**
     * get the favorite publisher list
     */
    Observable<List<PublisherModel>> getFavorPublisherList();

    /**
     * follow a publisher
     */
    Observable<RepoResponseCode> followPublisher(PublisherModel publisher);
}
