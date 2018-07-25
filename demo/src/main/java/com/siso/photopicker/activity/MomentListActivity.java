package com.siso.photopicker.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.siso.photopicker.adapter.MomentListAdapter;
import com.siso.photopicker.info.PhotoData;
import com.siso.photopicker.model.Moment;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPPToolbarActivity;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.util.PreViewConfig;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 你自己项目里「可以不继承 BGAPPToolbarActivity」，我在这里继承 BGAPPToolbarActivity 只是为了方便写 Demo
 * BGAOnRVItemClickListener和BGAOnRVItemLongClickListener这两个接口是为了测试事件传递是否正确，你自己的项目里可以不实现这两个接口
 */
public class MomentListActivity extends BGAPPToolbarActivity implements EasyPermissions.PermissionCallbacks {
    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;

    private RecyclerView mMomentRv;
    private MomentListAdapter mMomentAdapter;

    /**
     * 设置图片预览时是否具有保存图片功能「测试接口用的」
     */


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_moment_list);
        PreViewConfig.setSaveDir(Environment.getExternalStorageDirectory() + "/BGAPhotoPickerDownload");
        mMomentRv = findViewById(R.id.rv_moment_list_moments);
    }

    @Override
    protected void setListener() {
        mMomentAdapter = new MomentListAdapter(new ArrayList<PhotoData>());
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("朋友圈列表");
        photoPreviewWrapper();
        mMomentRv.setLayoutManager(new LinearLayoutManager(this));
        mMomentRv.setAdapter(mMomentAdapter);

        addNetImageTestData();
    }

    /**
     * 添加网络图片测试数据
     */
    private void addNetImageTestData() {
        List<Moment> moments = new ArrayList<>();

        List<PhotoData> dataList = new ArrayList<>();
        List<PhotoData.Data> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PhotoData.Data info = new PhotoData.Data();
            for (int y = 0; y < 4; y++) {
                if (i % 2 == 0) {
                    info.data = "http://n.sinaimg.cn/translate/200/w1080h720/20180724/556j-hftenhz9015427.jpg";
                } else {
                    info.data = "http://n.sinaimg.cn/translate/200/w1080h720/20180724/1p_9-hftenhz9015467.jpg";
                }
                info.id = i;
                list.add(info);
            }
        }
        PhotoData photoData = new PhotoData();
        photoData.data = list;
        dataList.add(photoData);
        mMomentAdapter.setNewData(dataList);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.tv_moment_list_add) {
            startActivityForResult(new Intent(this, MomentAddActivity.class), RC_ADD_MOMENT);
        } else if (v.getId() == R.id.tv_moment_list_system) {
            startActivity(new Intent(this, SystemGalleryActivity.class));
        }
    }








    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        final String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PREVIEW) {
            Toast.makeText(this, "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

}