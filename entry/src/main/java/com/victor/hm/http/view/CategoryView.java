package com.victor.hm.http.view;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CategoryView
 * Author: Victor
 * Date: 2021/8/9 14:27
 * Description:
 * -----------------------------------------------------------------
 */

public interface CategoryView<T> {
    void OnCategoryInfo(T data, String msg);
}