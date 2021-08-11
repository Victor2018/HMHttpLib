package com.victor.hm.library.module;

import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.data.Request;
import com.victor.hm.library.data.Response;
import com.victor.hm.library.interfaces.OnDownloadListener;
import com.victor.hm.library.util.*;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.HashMap;

public class HttpEventHandler extends EventHandler implements OnDownloadListener {
    private static final String TAG = "HttpEventHandler";

    private Context mContext;
    public static final int GET_REQUEST = 0x1000001;
    public static final int POST_REQUEST = 0x1000002;
    public static final int UPLOAD_REQUEST = 0x1000003;
    public static final int DOWNLOAD_REQUEST = 0x1000004;

    public HashMap<String, Request> requests = new HashMap<>();
    public String requestUrl = null;
    public HashMap<String,String> headers = new HashMap<>();
    public String parms = null;
    public FormInfo formInfo = null;
    public String path = null;

    public HttpEventHandler(EventRunner runner,Context context) {
        super(runner);
        mContext = context;
    }

    @Override
    protected void processEvent(InnerEvent event) {
        switch (event.eventId) {
            case GET_REQUEST:
                Loger.e(TAG,"start send get request");
                onReponse(requestUrl, HttpUtil.get(requestUrl,headers).data,true);
                break;
            case POST_REQUEST:
                Loger.e(TAG,"start send post request");
                onReponse(requestUrl, HttpUtil.post(requestUrl,headers,parms).data,true);
                break;
            case UPLOAD_REQUEST:
                Loger.e(TAG,"start send upload request");
                onReponse(requestUrl,HttpUtil.uploadFile(requestUrl,headers,parms,formInfo).data,true);
                break;
            case DOWNLOAD_REQUEST:
                Loger.e(TAG,"start send download request");
                HttpUtil.downloadFile(requestUrl,headers,path,this);
                break;
            default:
                break;
        }
    }

    private void onReponse(String url,String result,boolean requestComplete) {
        mContext.getUITaskDispatcher().asyncDispatch(() -> {
            try {
                Object reponse = parseReponse(url,result);
                requests.get(url).listener.onSuccess(reponse);
                if (requestComplete) {
                    requests.remove(url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Object parseReponse (String url,String result) {
        Object reponse = null;
        try {
            if (requests.get(url).responseCls.toString().contains("String")) {
                Loger.e(TAG,"reponse data is String");
                if (TextUtils.isEmpty(result)) {
                    return null;
                }
                return result;
            }
            if (result.startsWith("[")) {
                Loger.e(TAG,"reponse data is json JSONArray");
                reponse = JsonUtils.parseArray(result,requests.get(url).responseCls);
            } else if (result.startsWith("{")) {
                Loger.e(TAG,"reponse data is JSONObject");
                reponse = JsonUtils.parseObject(result,requests.get(url).responseCls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse;
    }

    @Override
    public void onProgress(Response response) {
        onReponse(requestUrl, JsonUtils.toJSONString(response),
                response.current == response.contentLength);
    }
}

