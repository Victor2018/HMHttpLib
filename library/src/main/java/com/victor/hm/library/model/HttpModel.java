package com.victor.hm.library.model;

import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.interfaces.OnHttpListener;

public interface HttpModel <H,T> {
    void sendReuqest(String url, H header, T parm, FormInfo formInfo, OnHttpListener<T> listener);
}
