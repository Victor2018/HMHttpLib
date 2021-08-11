package com.victor.hm.http.view;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: UploadView
 * Author: Victor
 * Date: 2021/8/9 14:27
 * Description:
 * -----------------------------------------------------------------
 */

public interface UploadView<T> {
    void OnUpload(T data, String msg);
}