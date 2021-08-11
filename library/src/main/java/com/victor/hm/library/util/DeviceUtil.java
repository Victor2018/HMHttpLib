package com.victor.hm.library.util;

import ohos.app.Context;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;

public class DeviceUtil {
    public static String getDeviceId (Context context) {
        String deviceId = KvManagerFactory.getInstance().createKvManager
                (new KvManagerConfig(context)).getLocalDeviceInfo().getId();
        return deviceId;
    }

    public static String getDeviceName (Context context) {
        String deviceId = KvManagerFactory.getInstance().createKvManager
                (new KvManagerConfig(context)).getLocalDeviceInfo().getName();
        return deviceId;
    }
}
