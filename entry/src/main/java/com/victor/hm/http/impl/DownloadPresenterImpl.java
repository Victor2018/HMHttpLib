package com.victor.hm.http.impl;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CategoryPresenterImpl
 * Author: Victor
 * Date: 2021/8/9 14:25
 * Description:
 * -----------------------------------------------------------------
 */

import com.victor.hm.http.view.DownloadView;
import com.victor.hm.library.annotation.HttpParms;
import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.data.Request;
import com.victor.hm.library.data.Response;
import com.victor.hm.library.inject.HttpInject;
import com.victor.hm.library.presenter.impl.BasePresenterImpl;

public class DownloadPresenterImpl<H,T> extends BasePresenterImpl<H,T> {
    /*Presenter作为中间层，持有View和Model的引用*/
    private DownloadView IView;

    public DownloadPresenterImpl(DownloadView IView) {
        this.IView = IView;
    }

    @Override
    public void onSuccess(T data) {
        if (IView == null) return;
        if (data == null) {
            IView.OnDownload(null,"no data response");
        } else {
            IView.OnDownload(data,"");
        }
    }

    @Override
    public void onError(String error) {
        if (IView == null) return;
        IView.OnDownload(null,error);
    }

    @Override
    public void detachView() {
        IView = null;
    }

    @HttpParms(method = Request.DOWNLOAD, responseCls = Response.class)
    @Override
    public void sendRequest(String url, H header,T parm, FormInfo formInfo) {
        HttpInject.inject(this);
        super.sendRequest(url,header,parm,formInfo);
    }
}