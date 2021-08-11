package com.victor.hm.http.slice;

import com.victor.hm.http.ResourceTable;
import com.victor.hm.http.WebConfig;
import com.victor.hm.http.data.CategoryInfo;
import com.victor.hm.http.data.LoginParm;
import com.victor.hm.http.data.LoginReq;
import com.victor.hm.http.impl.CategoryPresenterImpl;
import com.victor.hm.http.impl.DownloadPresenterImpl;
import com.victor.hm.http.impl.LoginPresenterImpl;
import com.victor.hm.http.impl.UploadPresenterImpl;
import com.victor.hm.http.view.CategoryView;
import com.victor.hm.http.view.DownloadView;
import com.victor.hm.http.view.LoginView;
import com.victor.hm.http.view.UploadView;
import com.victor.hm.library.annotation.BindView;
import com.victor.hm.library.annotation.OnClick;
import com.victor.hm.library.data.FormInfo;
import com.victor.hm.library.data.Response;
import com.victor.hm.library.inject.ViewInject;
import com.victor.hm.library.interfaces.OnHttpListener;
import com.victor.hm.library.module.HttpRequest;
import com.victor.hm.library.util.DeviceUtil;
import com.victor.hm.library.util.JsonUtils;
import com.victor.hm.library.util.Loger;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainAbilitySlice extends AbilitySlice implements CategoryView<List<CategoryInfo>>,
        LoginView<LoginReq>, DownloadView<Response>, UploadView<Response> {

    public static final int PERM_WRITE_STORAGE_REQ_CODE = 1001;

    @BindView(ResourceTable.Id_txt_result)
    Text mTxtResult;

    @BindView(ResourceTable.Id_btn_get)
    Button mBtnGet;

    @BindView(ResourceTable.Id_btn_post)
    Button mBtnPost;

    @BindView(ResourceTable.Id_btn_down)
    Button mBtnDownload;

    @BindView(ResourceTable.Id_btn_upload)
    Button mBtnUpload;

    private CategoryPresenterImpl categoryPresenter;
    private LoginPresenterImpl loginPresenter;
    private DownloadPresenterImpl downloadPresenter;
    private UploadPresenterImpl uploadPresenter;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initialize();
    }

    private void initialize () {
        ViewInject.injectView(this);
        categoryPresenter = new CategoryPresenterImpl(this);
        loginPresenter = new LoginPresenterImpl(this);
        downloadPresenter = new DownloadPresenterImpl(this);
        uploadPresenter = new UploadPresenterImpl(this);

        requestPermission(SystemPermission.WRITE_USER_STORAGE, PERM_WRITE_STORAGE_REQ_CODE);
    }

    @OnClick(ResourceTable.Id_btn_get)
    public void sendGetRequest () {
        String deviceId = DeviceUtil.getDeviceId(this);
        String deviceName = DeviceUtil.getDeviceName(this);

        String url = String.format(WebConfig.getRequestUrl(WebConfig.FIND_CATEGORIES_URL), deviceId,deviceName);

//        HttpRequestHelper.get().setResponseCls(CategoryInfo.class);
//        HttpRequestHelper.get().sendGetRequest(url,this);

        categoryPresenter.sendRequest(url,null,null,null);
    }

    @OnClick(ResourceTable.Id_btn_post)
    public void sendPostRequest() {
        String parms = "username=Victor423099&password=423099";
        loginPresenter.sendRequest(WebConfig.LOGIN_URL,null, parms,null);
    }

    @OnClick(ResourceTable.Id_btn_down)
    public void sendDownloadRequest() {
        String url = "https://imtt.dd.qq.com/16891/apk/96881CC7639E84F35E86421691CBBA5D.apk?fsname=com.sina.weibo_11.1.3_4842.apk&csr=3554";

        String path = getFilesDir().getPath() + "webo.apk";
        downloadPresenter.sendRequest(url,null,path,null);
    }

    @OnClick(ResourceTable.Id_btn_upload)
    public void sendUploadRequest () {
        String url = "input your upload url";

        FormInfo info = new FormInfo();
        info.file = new File(getFilesDir().getPath() + "webo.apk");
        info.fileKey = "you server fileKey";
        info.fileType = HttpRequest.FILE_TYPE_FILE;

        downloadPresenter.sendRequest(url,null,null,info);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void OnCategoryInfo(List<CategoryInfo> data, String msg) {
        CategoryInfo item = data.get(new Random().nextInt(data.size()));
        mTxtResult.setText(item.description);
    }

    @Override
    public void OnLogin(LoginReq data, String msg) {
        mTxtResult.setText(JsonUtils.toJSONString(data));
    }

    @Override
    public void OnDownload(Response data, String msg) {
        mTxtResult.setText(data.progress + "%");
    }

    private void requestPermission(String permission, int requestCode) {
        if (verifySelfPermission(permission) == IBundleManager.PERMISSION_GRANTED) {
            mTxtResult.setText("Permission already obtained");
            return;
        }
        if (!canRequestPermission(permission)) {
            mTxtResult.setText("Cannot request Permission");
            return;
        }
        requestPermissionsFromUser(new String[]{permission}, requestCode);
    }

    @Override
    public void OnUpload(Response data, String msg) {
        mTxtResult.setText(JsonUtils.toJSONString(data));
    }
}
