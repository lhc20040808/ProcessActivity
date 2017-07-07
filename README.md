# 通过Scheme隐式启动ACTIVITY


其他App或者Web页面通过Url scheme的方式，唤起原生页面，并传递相关参数。



## 页面中的格式

```javascript
<a href="[scheme]://[host]:[port]/[path]?[query]">启动应用程序</a>
```

| 字段     | 含义    |
| ------ | ----- |
| scheme | 协议名称  |
| host   | 协议作用域 |
| port   | 端口号   |
| path   | 指定页面  |
| query  | 传递参数  |

 

##  使用场景

1、Web页面调起Native中的页面

2、推送后调起Native中的页面

3、其他应用调起app中的页面



## 使用方法

这里采用创建一个过桥页，通过过桥页处理自定义的scheme请求并打开结果页。

当然也可以给所需要的页面都配置不同的host来实现跳转。

#### 定义Scheme

```Xml
        <activity
            android:name=".ProcessActivity"
            android:screenOrientation="portrait">

            <intent-filter>

                <data
                    android:host="com.lhc"
                    android:scheme="external" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <action android:name="android.intent.action.VIEW" />

            </intent-filter>
        </activity>
```

android.intent.category.DEFAULT 默认的category

android.intent.category.BROWSABLE   指定该Activity能被浏览器安全调用

 android.intent.action.VIEW 显示指定数据



#### 通过Web页面调Natvie页面

```html
<html>

  <body style="background-color:yellow">
      <p style="font-size:20px;text-align:center;">
        <h1>web跳转Activity</h1>
      </p>

      <p style="font-size:20px;text-align:center;" >
          <a href="external://com.lhc.main/com.lhc.TestActivity">修改host跳转</a>
      </p>

      <p style="font-size:20px;text-align:center;" >
        <a href="external://com.lhc/com.lhc.TestActivity">无参跳转</a>
      </p>

      <p style="font-size:20px;text-align:center;">
        <a href="external://com.lhc/com.lhc.TestActivity?productId=600001">一个参数跳转</a>
      </p>

      <p style="font-size:20px;text-align:center;">
        <a href="external://com.lhc/com.lhc.TestActivity?productId=600001&infomation=我是携带的信息">两个参数跳转</a>
      </p>


  </body>

</html>

```

 如果系统找不到能处理这个scheme的页面，则会在webview中进行跳转。



#### 通过Intent隐式启动

```Java
startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("external://com.lhc/com.lhc.TestActivity?productId=600001")));
```

如果系统找不到能处理这个scheme的页面，则会抛出android.content.ActivityNotFoundException异常



#### 处理方式

```java
   /**
     * 
     * @param uri 请求uri
     * @param path 结果页全路径
     */
    private void distribute(Uri uri, String path) {
        Bundle bundle = new Bundle();
        Set<String> keyNames = uri.getQueryParameterNames();
        for (String key : keyNames) {
            String value = uri.getQueryParameter(key);
            bundle.putString(key, value);
        }

        try {
            Class desPage = Class.forName(path);
            Intent intent = new Intent(this, desPage);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            finish();
        }
    }
```

关于处理方式这里就不再赘述，很多参考文章也写的很详细。只是这里我改成了通过反射的方式拿到class来启动页面，避免写过多的逻辑判断。实际业务中由于传递的参数不同，通过遍历key值得方式塞入参数，保证灵活性。



#### 扩展及大胆猜测

![淘宝分享](https://raw.githubusercontent.com/lhc20040808/Pictures/master/res/图片/taobao_share_pic.jpg)



经常淘宝购物的人应该知道，分享商品的时候分享出的是个链接，经过复制后打开App后会弹出一个dialog。大胆猜测可能是监听了剪切板拿到了复制信息，经过处理拿到scheme或者特定参数，然后进行了详情弹框的加载展示。





附上参考链接：

[酷炫的外部开启Activity新姿势](https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650823363&idx=2&sn=9b9e23a6bec12f661adb9e230d8b5d21&chksm=80b78e5db7c0074bc8ba44feaf93339112f7ebe8337f0590bf2547da60197c4e0a85a92245b4&mpshare=1&scene=23&srcid=0707hwKzh3gVlYlNEjqAMQfD#rd)

[android scheme链接打开本地应用](http://www.jianshu.com/p/45af72036e58)

