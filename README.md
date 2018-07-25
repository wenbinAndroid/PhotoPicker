#### 在[BGAPhotoPicker-Android](https://github.com/bingoogolapple/BGAPhotoPicker-Android)基础上进行了修改


#### 为什么会有这个

#####  谢谢[bingoogolapple](https://github.com/bingoogolapple)大牛提供了这么方便的图片选择库,发现在项目上有点不太方便,后台的图片数据并不是单单String 格式,而是对象的形式,所以每次都需要提前遍历遍历循环出来,十分的麻烦,所以就有了修改,使用泛型来解耦,通过回调的方式来获取图片路径
	
```java
public interface PhotoPreListener {

    String getPhotoPath(int position);

    int getCount();
}

```
#### 在图片预览将通过Utils将listener存储起来,在需要的地方通过获取最上层的Listener通过回调回调获取,在界面结束的时候在移除最顶层的listener

```java
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
  //add
 public void addListener(PhotoPreListener listeners) {
        mPreListeners.add(listeners);
    }
 
 //remove
 public void removeListener(PhotoPreListener listener) {
        int size = mPreListeners.size();
        if (size >= 1) {
            mPreListeners.remove(size - 1);
        }
    }
```


#### 如何使用
```
allprojects {
	repositories {
	...
	maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {

	 implementation 'com.github.wenbinAndroid:photopicker:master'
	 
	}

```




#### Thanks [bingoogolapple](https://github.com/bingoogolapple)
