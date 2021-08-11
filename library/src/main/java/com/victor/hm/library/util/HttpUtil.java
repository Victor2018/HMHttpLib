package com.victor.hm.library.util;

import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.data.Response;
import com.victor.hm.library.data.UploadParm;
import com.victor.hm.library.interfaces.OnDownloadListener;
import com.victor.hm.library.interfaces.OnHttpListener;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpUtil {
    private static String TAG = "HttpUtil";
    private static int CONNECT_TIME_OUT = 10000;
    private static int READ_TIME_OUT = 15000;
    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63";
    private static String ENCODE = "utf-8";

    private static final String BOUNDARY = UUID.randomUUID().toString();
    private static final String TWO_HYPHENS = "--";
    private static final String LINE_END = "\r\n";

    public static Response get(String requestUrl,HashMap<String,String> headers){
        Loger.e(TAG, "get-requestUrl=" + requestUrl);
        Response response = null;
        HttpsURLConnection conn = null;
        try {
            conn = getHttpsURLConnection(requestUrl,"GET","application/json");
            conn.setDoInput(true);

            setHeaders(conn,headers);

            conn.connect();
            response = getResponse(conn,null);
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = getResponse(conn,e);
        }

        Loger.e(TAG, "get-result=" + response.data);
        return response;
    }

    public synchronized static Response post(String requestUrl, HashMap<String,String> headers, String parms) {
        Loger.e(TAG, "post-requestUrl=" + requestUrl);
        Loger.e(TAG, "post-parms=" + parms);

        Response response = null;
        HttpsURLConnection conn = null;
        try {
            conn = getHttpsURLConnection(requestUrl,"POST",null);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            setHeaders(conn,headers);

            conn.connect();

            setParms(conn,parms);

            response = getResponse(conn,null);

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = getResponse(conn,e);
        }
        Loger.e(TAG, "post-result=" + response.data);
        return response;
    }

    public static Response uploadFile(String requestUrl, HashMap<String,String> headers, String parms, FormInfo formInfo) {
        Loger.e(TAG, "post-requestUrl=" + requestUrl);
        Loger.e(TAG, "post-parms=" + parms);

        Response response = null;
        HttpsURLConnection conn = null;
        try {
            conn = getHttpsURLConnection(requestUrl,"POST",null);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type","multipart/form-data; BOUNDARY=" + BOUNDARY);

            setHeaders(conn,headers);

            conn.connect();

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            setParms(conn,parms);

            if(formInfo != null) {
                if (formInfo.file != null) {
                    writeFile(formInfo, dos);//上传文件
                }
            }

            byte[] endData = (LINE_END + TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END).getBytes();//写结束标记位
            dos.write(endData);
            dos.flush();

            response = getResponse(conn,null);

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = getResponse(conn,e);
        }
        Loger.e(TAG, "post-result=" + response.data);
        return response;
    }

    public static void downloadFile (String requestUrl, HashMap<String,String> headers, String path, OnDownloadListener listener) {
        Loger.e(TAG, "downloadFile-requestUrl=" + requestUrl);
        if (TextUtils.isEmpty(path)) {
            Loger.e(TAG, "downloadFile-path = " + path);
            return;
        }
        Response response = null;
        HttpsURLConnection conn = null;
        try {
            conn = getHttpsURLConnection(requestUrl,"GET",null);
            conn.setDoInput(true);

            setHeaders(conn,headers);

            conn.connect();

            int current = 0;
            int total = conn.getContentLength();
            InputStream is = conn.getInputStream();
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            response = new Response();
            response.contentLength = total;

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 2];
            int size = is.read(buffer);
            while (size != -1) {
                current += size;
                fos.write(buffer, 0, size);
                size = is.read(buffer);
                double progress = getRoundUpDouble(current * 100.0f / total);
                // 时刻将当前进度更新给onProgressUpdate方法
                Loger.e(TAG,"--------------------------" + progress + "%" + "--------------------------");
                response.current = current;
                response.progress = progress;

                if (listener != null) {
                    listener.onProgress(response);
                }
            }
            fos.close();
            response = getResponse(conn,null);
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response = getResponse(conn,e);
        }

        Loger.e(TAG, "get-result=" + response.data);
    }

    private static void setHeaders (HttpsURLConnection conn,HashMap<String,String> headers) {
        if (conn == null) return;
        if (headers == null) return;
        for (Map.Entry<String,String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void setParms (HttpsURLConnection conn,String parms) {
        if (conn == null) return;
        if (TextUtils.isEmpty(parms)) return;
        try {
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(parms.getBytes(StandardCharsets.UTF_8));
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseResponse (InputStream is) {
        String result = "";
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 2];
            int size = is.read(buffer);
            while (size != -1) {
                bos.write(buffer, 0, size);
                size = is.read(buffer);
            }
            result = new String(bos.toByteArray(), ENCODE);
            is.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 当正常返回时，得到Response对象
     */
    private static Response getResponse(HttpsURLConnection conn,Exception exception) {
        Response response = new Response();
        try {
            response.code = conn.getResponseCode();
            response.contentLength = conn.getContentLength();
            response.inputStream = conn.getInputStream();
            response.errorStream = conn.getErrorStream();
            response.exception = exception;

            if (response.code == HttpURLConnection.HTTP_OK) {
                response.data = parseResponse(response.inputStream);
            } else {
                if (response.inputStream != null) {
                    response.data = parseResponse(response.inputStream);
                } else if (response.errorStream != null) {
                    response.data = parseResponse(response.errorStream);
                } else if (response.exception != null) {
                    response.data = response.exception.getMessage();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpsURLConnection getHttpsURLConnection(String requestURL,String requestMethod,String contentType) {
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(requestURL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setRequestMethod(requestMethod);

            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            conn.setRequestProperty("Accept-Charset", ENCODE);
            if (!TextUtils.isEmpty(contentType)) {
                conn.setRequestProperty("Content-type", contentType);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * 上传文件时写文件
     */
    private static void writeFile(FormInfo formInfo, DataOutputStream dos) {
        try {
            dos.write(getFileParamsString(formInfo.file, formInfo.fileKey, formInfo.fileType).getBytes());
            dos.flush();

            FileInputStream fis = new FileInputStream(formInfo.file);
            final long total = formInfo.file.length();
            Loger.e(TAG,"upload-total = " + total);

            int current = 0;
            byte[] buffer = new byte[1024 * 2];
            int size = fis.read(buffer);
            while (size != -1) {
                dos.write(buffer, 0, size);
                size = fis.read(buffer);

                current += size;
                dos.write(buffer, 0, size);
                size = fis.read(buffer);
                double progress = getRoundUpDouble(current * 100.0f / total);

                Loger.e(TAG,"upload-progress = " + progress);
            }

            dos.flush();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传文件时得到一定格式的拼接字符串
     */
    private static String getFileParamsString(File file, String fileKey, String fileType) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(LINE_END);
        strBuf.append(TWO_HYPHENS);
        strBuf.append(BOUNDARY);
        strBuf.append(LINE_END);
        strBuf.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"");
        strBuf.append(LINE_END);
        strBuf.append("Content-Type: " + fileType );
        strBuf.append(LINE_END);
        strBuf.append("Content-Length: "+file.length());
        strBuf.append(LINE_END);
        strBuf.append(LINE_END);
        return strBuf.toString();
    }

    private static double getRoundUpDouble (double value) {
        double result = 0.0;
        BigDecimal bd = new BigDecimal(value);
        try {
            result = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
