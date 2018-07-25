package com.siso.photopicker.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.siso.photopicker.info.PhotoData;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.pre.PhotoPreListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * @author Mrz
 * @date 2018/7/24 23:38
 */
public class MomentListAdapter extends BaseQuickAdapter<PhotoData, BaseViewHolder> {
    private static final String TAG = "PhotoAdapter";

    public MomentListAdapter(@Nullable List<PhotoData> data) {
        super(R.layout.item_moment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PhotoData item) {
        BGANinePhotoLayout<PhotoData.Data> layout = helper.getView(R.id.npl_item_moment_photos);
        //图片预览
        layout.setDelegate(new BGANinePhotoLayout.Delegate<PhotoData.Data>() {
            @Override
            public void onClickNinePhotoItem(BGANinePhotoLayout bgaNinePhotoLayout, View view,
                                             int i, final PhotoData.Data o,
                                             final List<PhotoData.Data> list) {
                Intent intent = new BGAPhotoPreviewActivity.
                        IntentBuilder(mContext).currentPosition(i).
                        previewListener(new PhotoPreListener() {
                            @Override
                            public String getPhotoPath(int i) {
                                return o.data;
                            }
                            @Override
                            public int getCount() {
                                return list.size();
                            }
                        }).build();
                mContext.startActivity(intent);
            }
        });
        //图片显示
        layout.setData((ArrayList<PhotoData.Data>) item.data, new PhotoPreListener() {
            @Override
            public String getPhotoPath(int i) {
                String data = item.data.get(i).data;
                return data;
            }
            @Override
            public int getCount() {
                return item.data.size();
            }
        });
    }
}
