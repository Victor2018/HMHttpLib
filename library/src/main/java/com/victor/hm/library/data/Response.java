package com.victor.hm.library.data;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2020-2080, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: Response
 * Author: Victor
 * Date: 2021/8/11 10:37
 * Description:
 * -----------------------------------------------------------------
 */

import java.io.InputStream;

public class Response {
    public InputStream inputStream;
    public InputStream errorStream;
    public int code;
    public int current;
    public long contentLength;
    public double progress;
    public Exception exception;

    public String data;
}
