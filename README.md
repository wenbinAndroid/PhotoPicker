#### 在[BGAPhotoPicker-Android](https://github.com/bingoogolapple/BGAPhotoPicker-Android)基础上进行了修改


#### 为什么会有这个

#####  谢谢[bingoogolapple](https://github.com/bingoogolapple)大牛提供了这么方便的图片选择库,发现在项目上有点不太方便,后台的图片数据并不是List<String> 格式,所以每次都需要提前遍历遍历循环出来,十分的麻烦,所以就有了修改,使用泛型来解耦,通过回调的方式来获取图片路径
	
```java
public interface PhotoPreListener {

    String getPhotoPath(int position);

    int getCount();
}

```java
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
