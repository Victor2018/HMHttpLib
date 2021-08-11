package com.victor.hm.http;

import com.victor.hm.library.module.HttpRequest;
import ohos.aafwk.ability.AbilityPackage;

public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();
        HttpRequest.get().init(this);
    }
}
