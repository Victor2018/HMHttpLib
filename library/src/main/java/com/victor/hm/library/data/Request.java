package com.victor.hm.library.data;

import com.victor.hm.library.interfaces.OnHttpListener;

public class Request {
     public static final int GET = 0;
     public static final int POST = 1;
     public static final int UPLOAD = 2;
     public static final int DOWNLOAD = 3;

     public OnHttpListener listener;
     public Class responseCls;

     public Request(Class responseCls, OnHttpListener listener) {
          this.responseCls = responseCls;
          this.listener = listener;
     }
}
