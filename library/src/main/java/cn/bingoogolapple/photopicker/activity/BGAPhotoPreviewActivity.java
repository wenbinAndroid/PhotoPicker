/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.bingoogolapple.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cn.bingoogolapple.photopicker.R;
import cn.bingoogolapple.photopicker.adapter.BGAPhotoPageAdapter;
import cn.bingoogolapple.photopicker.imageloader.BGAImage;
import cn.bingoogolapple.photopicker.imageloader.BGAImageLoader;
import cn.bingoogolapple.photopicker.pre.PhotoPreListener;
import cn.bingoogolapple.photopicker.pre.PhotoPreviewUtils;
import cn.bingoogolapple.photopicker.util.BGAAsyncTask;
import cn.bingoogolapple.photopicker.util.BGAPhotoPickerUtil;
import cn.bingoogolapple.photopicker.util.BGASavePhotoTask;
import cn.bingoogolapple.photopicker.widget.BGAHackyViewPager;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:59
 * 描述:图片预览界面
 */
public class BGAPhotoPreviewActivity extends BGAPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener, BGAAsyncTask.Callback<Void> {
    private static final String EXTRA_SAVE_PHOTO_DIR = "EXTRA_SAVE_PHOTO_DIR";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";

    private TextView mTitleTv;
    private ImageView mDownloadIv;
    private BGAHackyViewPager mContentHvp;
    private BGAPhotoPageAdapter mPhotoPageAdapter;


    private File mSavePhotoDir;

    private boolean mIsHidden = false;
    private BGASavePhotoTask mSavePhotoTask;

    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;
    private PhotoPreListener mPhotoPreListener;
    private TextView mTvCount;
    private ImageView mIvDown;
    private int mMaxCount;
    private int mCurrentPosition;
    private static final String TAG = "BGAPhotoPreviewActivity";

    public static class IntentBuilder {
        private Intent mIntent;

        public IntentBuilder(Context context) {
            mIntent = new Intent(context, BGAPhotoPreviewActivity.class);
        }

        /**
         * 保存图片的目录，如果传 null，则没有保存图片功能
         */
        public IntentBuilder saveImgDir(@Nullable File saveImgDir) {
            mIntent.putExtra(EXTRA_SAVE_PHOTO_DIR, saveImgDir);
            return this;
        }

        /* *//**
         * 当前预览的图片路径
         *//*
        public IntentBuilder previewPhoto(String photoPath) {
            mIntent.putStringArrayListExtra(EXTRA_PREVIEW_PHOTOS, new ArrayList<>(Arrays.asList(photoPath)));
            return this;
        }*/

        /**
         * 图片预览地址获取
         *
         * @param listener
         * @return
         */
        public IntentBuilder previewListener(PhotoPreListener listener) {
            PhotoPreviewUtils.getInstant().addListener(listener);
            return this;
        }
        /*
         *//**
         * 当前预览的图片路径集合
         *//*
        public IntentBuilder previewPhotos(ArrayList<String> previewPhotos) {
            mIntent.putStringArrayListExtra(EXTRA_PREVIEW_PHOTOS, previewPhotos);
            return this;
        }*/

        /**
         * 当前预览的图片索引
         */
        public IntentBuilder currentPosition(int currentPosition) {
            mIntent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
            return this;
        }

        public Intent build() {
            return mIntent;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setNoLinearContentView(R.layout.bga_pp_activity_photo_preview);
        mContentHvp = findViewById(R.id.hvp_photo_preview_content);
    }

    @Override
    protected void setListener() {
        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                changePositionText(position);
            }

        });
    }

    private void changePositionText(int position) {
        mCurrentPosition = position + 1;
        String currentPositionStr = mCurrentPosition + "";
        String maxPositionStr = mMaxCount + "";
        SpannableString spannableString = new SpannableString(currentPositionStr + " / " + maxPositionStr);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, currentPositionStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(2f), 0, currentPositionStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, currentPositionStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvCount.setText(spannableString);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mSavePhotoDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_PHOTO_DIR);
        if (mSavePhotoDir != null && !mSavePhotoDir.exists()) {
            mSavePhotoDir.mkdirs();
        }
        mIvDown = findViewById(R.id.iv_down);
        mTvCount = findViewById(R.id.tv_count);
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        mPhotoPreListener = PhotoPreviewUtils.getInstant().getListener();
        if (mPhotoPreListener != null) {
            mMaxCount = mPhotoPreListener.getCount();
            mPhotoPageAdapter = new BGAPhotoPageAdapter(this, mPhotoPreListener);
            mContentHvp.setAdapter(mPhotoPageAdapter);
            mContentHvp.setCurrentItem(currentPosition);
            changePositionText(currentPosition);
            mToolbar.setVisibility(View.GONE);
        }
        mIvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSavePhotoTask == null) {
                    savePic();
                }
            }
        });
    }


    @Override
    public void onViewTap(View view, float x, float y) {
        if (view == mIvDown) {
            savePic();
        } else {
            finish();
        }
    }

    private void showTitleBar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }
    }

    private void hiddenTitleBar() {
        if (mToolbar != null) {
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                }
            }).start();
        }
    }

    private synchronized void savePic() {
        if (mSavePhotoTask != null) {
            return;
        }

        final String url = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
        File file;
        if (url.startsWith("file")) {
            file = new File(url.replace("file://", ""));
            if (file.exists()) {
                BGAPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, file.getParentFile().getAbsolutePath()));
                return;
            }
        }

        // 通过MD5加密url生成文件名，避免多次保存同一张图片
        file = new File(mSavePhotoDir, BGAPhotoPickerUtil.md5(url) + ".png");
        if (file.exists()) {
            BGAPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, mSavePhotoDir.getAbsolutePath()));
            return;
        }

        mSavePhotoTask = new BGASavePhotoTask(this, this, file);
        BGAImage.download(url, new BGAImageLoader.DownloadDelegate() {
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                if (mSavePhotoTask != null) {
                    mSavePhotoTask.setBitmapAndPerform(bitmap);
                }
            }

            @Override
            public void onFailed(String url) {
                mSavePhotoTask = null;
                BGAPhotoPickerUtil.show(R.string.bga_pp_save_img_failure);
            }
        });
    }

    @Override
    public void onPostExecute(Void aVoid) {
        mSavePhotoTask = null;
    }

    @Override
    public void onTaskCancelled() {
        mSavePhotoTask = null;
    }

    @Override
    protected void onDestroy() {
        if (mSavePhotoTask != null) {
            mSavePhotoTask.cancelTask();
            mSavePhotoTask = null;
        }
        if (mPhotoPreListener != null) {
            PhotoPreviewUtils.getInstant().removeListener(mPhotoPreListener);
            mPhotoPreListener = null;
        }
        super.onDestroy();
    }
}