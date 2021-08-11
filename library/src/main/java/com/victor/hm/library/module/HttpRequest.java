package com.victor.hm.library.module;

import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.data.Request;
import com.victor.hm.library.interfaces.OnHttpListener;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.HashMap;

import static com.victor.hm.library.module.HttpEventHandler.*;

public class HttpRequest {
    private static final String TAG = "HttpRequest";

    private HttpEventHandler mHttpEventHandler;
    private EventRunner mHttpEventRunner;

    private int requestMethod = Request.GET;
    private Class responseCls= null;

    public static final String FILE_TYPE_FILE = "file/*";
    public static final String FILE_TYPE_IMAGE = "image/*";
    public static final String FILE_TYPE_AUDIO = "audio/*";
    public static final String FILE_TYPE_VIDEO = "video/*";

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setResponseCls(Class responseCls) {
        this.responseCls = responseCls;
    }

    private static HttpRequest instance;

    public static HttpRequest get () {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }

    private HttpRequest() {
    }

    public void init(Context context) {
        mHttpEventRunner = EventRunner.create("HttpRequestTask");
        mHttpEventHandler = new HttpEventHandler(mHttpEventRunner,context);
    }

    public void sendGetRequest(String url, OnHttpListener listener) {
       sendGetRequestDelay(url,listener,0);
    }

    public void sendGetRequestDelay(String url, OnHttpListener listener,long delayTime) {
        mHttpEventHandler.requests.put(url, new Request(responseCls,listener));
        mHttpEventHandler.requestUrl = url;

        InnerEvent event = InnerEvent.get(GET_REQUEST);
        mHttpEventHandler.sendEvent(event, delayTime, EventHandler.Priority.IMMEDIATE);
    }

    public void sendPostRequest(String url,
                                HashMap<String,String> headers,
                                String parms,
                                OnHttpListener listener) {
        mHttpEventHandler.requests.put(url, new Request(responseCls,listener));
        mHttpEventHandler.requestUrl = url;
        mHttpEventHandler.headers = headers;
        mHttpEventHandler.parms = parms;

        InnerEvent event = InnerEvent.get(POST_REQUEST);
        mHttpEventHandler.sendEvent(event, EventHandler.Priority.IMMEDIATE);
    }

    public void sendUploadRequest(String url,
                                HashMap<String,String> headers,
                                String parms,
                                FormInfo formInfo,
                                OnHttpListener listener) {
        mHttpEventHandler.requests.put(url, new Request(responseCls,listener));
        mHttpEventHandler.requestUrl = url;
        mHttpEventHandler.headers = headers;
        mHttpEventHandler.parms = parms;
        mHttpEventHandler.formInfo = formInfo;

        InnerEvent event = InnerEvent.get(UPLOAD_REQUEST);
        mHttpEventHandler.sendEvent(event, EventHandler.Priority.IMMEDIATE);
    }

    public void sendDownloadRequest(String url,HashMap<String,String> headers,
                                    String path, OnHttpListener listener) {
        mHttpEventHandler.requests.put(url, new Request(responseCls,listener));
        mHttpEventHandler.requestUrl = url;
        mHttpEventHandler.headers = headers;
        mHttpEventHandler.path = path;

        InnerEvent event = InnerEvent.get(DOWNLOAD_REQUEST);
        mHttpEventHandler.sendEvent(event, EventHandler.Priority.IMMEDIATE);
    }

    public <T> void sendRequest (String url, HashMap<String,String> headers,
                                 String parm,FormInfo formInfo, OnHttpListener<T> httpListener) {
        switch (requestMethod) {
            case Request.GET:
                sendGetRequest(url,httpListener);
                break;
            case Request.POST:
                sendPostRequest(url,headers,parm,httpListener);
                break;
            case Request.UPLOAD:
                sendUploadRequest(url,headers,parm,formInfo,httpListener);
                break;
            case Request.DOWNLOAD:
                sendDownloadRequest(url,headers,parm,httpListener);
                break;
        }
    }

    public void onDestroy (){
        if (mHttpEventRunner != null) {
            mHttpEventRunner = null;
        }
        if (mHttpEventHandler != null) {
            mHttpEventHandler.removeAllEvent();
            mHttpEventHandler = null;
        }
    }
}
