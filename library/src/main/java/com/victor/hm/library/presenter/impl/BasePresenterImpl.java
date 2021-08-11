package com.victor.hm.library.presenter.impl;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: BasePresenterImpl
 * Author: Victor
 * Date: 2021/8/9 10:46
 * Description:
 * -----------------------------------------------------------------
 */

import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.interfaces.OnHttpListener;
import com.victor.hm.library.model.HttpModel;
import com.victor.hm.library.model.impl.HttpModelImpl;
import com.victor.hm.library.presenter.HttpPresenter;

public abstract class BasePresenterImpl<H,T> implements HttpPresenter<H,T>, OnHttpListener<T> {
    private HttpModel httpModelImpl;
    public abstract void detachView();

    public BasePresenterImpl () {
        httpModelImpl = new HttpModelImpl();
    }

    @Override
    public void sendRequest(String url, H header,T parm, FormInfo formInfo) {
        httpModelImpl.sendReuqest(url,header,parm,formInfo,this);
    }

}
