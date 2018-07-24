package cn.bingoogolapple.photopicker.pre;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mrz
 * @date 2018/7/24 11:13
 */
public class PhotoPreviewUtils {
    private static PhotoPreviewUtils sPreviewUtils;
    private static List<PhotoPreListener> mPreListeners = new ArrayList<>();

    public static PhotoPreviewUtils getInstant() {
        if (sPreviewUtils == null) {
            synchronized (PhotoPreviewUtils.class) {
                if (sPreviewUtils == null) {
                    sPreviewUtils = new PhotoPreviewUtils();
                }
            }
        }
        return sPreviewUtils;
    }

    private PhotoPreviewUtils() {

    }

    public void addListener(PhotoPreListener listeners) {
        mPreListeners.add(listeners);
    }

    public void removeListener(PhotoPreListener listener) {
        int size = mPreListeners.size();
        for (int i = 0; i < size; i++) {
            PhotoPreListener preListener = mPreListeners.get(i);
            if (preListener == listener) {
                mPreListeners.remove(listener);
                break;
            }
        }
    }

    public PhotoPreListener getListener() {
        int size = mPreListeners.size();
        if (size > 0) {
            return mPreListeners.get(size - 1);
        }
        return null;
    }

}
