package com.victor.hm.http.view;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DownloadView
 * Author: Victor
 * Date: 2021/8/9 14:27
 * Description:
 * -----------------------------------------------------------------
 */

public interface DownloadView<T> {
    void OnDownload(T data, String msg);
}