package com.victor.hm.library.interfaces;

import com.victor.hm.library.data.Response;

public interface OnHttpListener<T> {
    void onSuccess(T data);
    void onError(String error);
}
