#### 在[BGAPhotoPicker-Android](https://github.com/bingoogolapple/BGAPhotoPicker-Android)基础上进行了修改


#### 为什么会有这个

#####  使用这个图库的时,发现九宫格和打开大图功能在项目上有点不太方便,后台的图片数据并不是单单String 格式,而是对象的形式,所以每次都需要提前遍历遍历循环出来,不太方便.所以就有了这个修改,使用泛型来解耦,通过回调的方式来获取图片路径,并增加设置默认地址,修改预览大图的样式
	
```java
public interface PhotoPreListener {

    String getPhotoPath(int position);

    int getCount();
}

```
##### 图片预览将通过PhotoPreviewUtils将Listener存储起来,在需要的地方通过获取最上层的Listener通过回调回调获取,在界面结束的时候在移除最顶层的Listener

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


##### RecyclerView中使用

###### 九宫格 
```java
//view初始化,PhotoData.Data为对象中包含图像列表格式
BGANinePhotoLayout<PhotoData.Data> layout = helper.getView(R.id.npl_item_moment_photos);
//显示
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
```
![photo](https://github.com/wenbinAndroid/photopicker/blob/master/image/%E5%88%97%E8%A1%A8.png)
###### 查看大图
```java
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

```

##### 设置默认缓存地址
```java
public class PreViewConfig {
    public static String sSaveDir;
    public static void setSaveDir(String saveDir) {
        sSaveDir = saveDir;
    }
}
```
![photo2](https://github.com/wenbinAndroid/photopicker/blob/master/image/%E5%A4%A7%E5%9B%BE.png)
#### Thanks [bingoogolapple](https://github.com/bingoogolapple)



