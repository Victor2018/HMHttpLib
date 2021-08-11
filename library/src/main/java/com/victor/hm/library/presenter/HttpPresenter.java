package com.victor.hm.library.presenter;

import com.victor.hm.library.data.FormInfo;

public interface HttpPresenter<H,T> {
    void sendRequest(String url, H header, T parm, FormInfo formInfo);
}
