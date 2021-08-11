package com.victor.hm.library.model.impl;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: HttpModelImpl
 * Author: Victor
 * Date: 2021/8/9 10:43
 * Description:
 * -----------------------------------------------------------------
 */

import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.interfaces.OnHttpListener;
import com.victor.hm.library.model.HttpModel;
import com.victor.hm.library.module.HttpRequest;

import java.util.HashMap;

public class HttpModelImpl<H,T> implements HttpModel<H,T> {
    private String TAG = "HttpModelImpl";

    @Override
    public void sendReuqest(String url, H header, T parm, FormInfo formInfo, final OnHttpListener<T> listener) {
        HttpRequest.get().sendRequest(url,(HashMap<String, String>) header,
                parm == null ? "" : parm.toString(),formInfo, listener);
    }

}
