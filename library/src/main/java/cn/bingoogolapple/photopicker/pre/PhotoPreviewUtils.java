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

    public void addListener(PhotoPreListener listeners) {
        mPreListeners.add(listeners);
    }

    private PhotoPreviewUtils() {

    }


    public void removeListener(PhotoPreListener listener) {
        int size = mPreListeners.size();
        if (size >= 1) {
            mPreListeners.remove(size - 1);
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
