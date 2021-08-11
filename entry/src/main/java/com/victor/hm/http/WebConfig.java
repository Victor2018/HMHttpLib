package com.victor.hm.http;

import ohos.utils.net.Uri;

public class WebConfig {
    public static final String EYEPETIZER_BASE_URL = "https://baobab.kaiyanapp.com/api/";
    public static final String CATEGORY_DETAIL_URL = "v4/categories/videoList?&id=%d&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//首页banner
    public static final String RELATED_VIDEO_URL = "v4/video/related?&id=%d&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//
    public static final String BANNER_URL = "v2/feed?num=1&udid=%s&deviceModel=%s";//首页banner
    public static final String DAILY_SELECTION_URL = "v4/tabs/selected";//每日精选
    public static final String FIND_FOLLOW_URL = "v4/tabs/follow?start=%d&num=%d&follow=false&startId=0&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//发现-关注
    public static final String FIND_CATEGORIES_URL = "v4/categories?udid=%s&deviceModel=%s";//发现-分类
    public static final String SEARCH_URL = "v1/search?&num=10&start=10&query=%s&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//搜索
    public static final String HOT_WEEKLY_URL = "v4/rankList/videos?strategy=weekly&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//热门-周排行
    public static final String HOT_MONTHLY_URL = "v4/rankList/videos?strategy=monthly&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//热门-月排行
    public static final String HOT_TOTAL_RANKING_URL = "v4/rankList/videos?strategy=historical&udid=d2807c895f0348a180148c9dfa6f2feeac0781b5&deviceModel=%s";//热门-总排行
    public static final String LOGIN_URL = "https://www.wanandroid.com/user/login";//wanandroid login


    public static String getServer() {
        return EYEPETIZER_BASE_URL;
    }
    public static String getServerIp() {
        Uri uri = Uri.parse(getServer());
        return uri.getDecodedHost();
    }

    public static String getRequestUrl(String api) {
        return getServer() + api;
    }
}
