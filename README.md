## 员工版APP ***Android*** 项目

> 中台项目

-----------------------------

### 开发环境【要求统一】

[下载开发工具](http://m-file.ds.gome.com.cn/?p=/soft/android&mode=list)

### AS开发插件【建议使用】

[下载开发插件](http://m-file.ds.gome.com.cn/?p=/soft/android/plugin&mode=list)

### 代码样式模板【统一编码风格】

[下载样式模板](http://m-file.ds.gome.com.cn/?p=/soft/android/plugin&mode=list)

### 签名配置【环境变量】

mac

~~~bash
export KEY_STORE="/xxxx/GOME.keystore"
export KEY_STORE_PASSWORD="xxxx"
export KEY_ALIAS="xxxx"
export KEY_ALIAS_PASSWORD="xxxx"
~~~

> 页面开发模式，采用mvp，【禁止使用 mvvm，databinding 等开发模式】
>
> 为了统一开发规范，建议使用AS插件内的代码模板
>
> AS插件还提供了findViewById代码生成工具，及其json串自动生成java实体工具，建议使用

------------------------------

### kotlin支持

1.子项目build.gradle添加插件

~~~groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
~~~

2.子项目build.gradle添加kotlin依赖

~~~groovy
compile deps.SKotlin
~~~

### 开发规范

开发前，请详细阅读

包命名规范
=========

> 1.包名全部小写，连续的单词只是简单地连接起来，不使用下划线；
>
> 2.采用反域名命名规则，一级包名为com【常规】，二级包名为gome（可以是公司域名或者个人命名），三级包名根据应用进行命名，四级包名为模块名或层级名;

应用包名

~~~bash
cn.gome.staff
~~~

基础模块

~~~bash
com.gome.mobile.frame.core

#图片模块
com.gome.mobile.frame.img

#网络模块
com.gome.mobile.frame.http

#IO模块
com.gome.mobile.frame.io

#工具模块
com.gome.mobile.frame.utils
~~~

业务模块

> 1.每个业务模块独立 Module 开发
>
> 2.每个独立 Module 的包名 为如下包名
>
> 3.maven定义【cn.gome.staff.buss:share:1.0.0】

~~~bash
cn.gome.staff.buss

#业务模块-购物
cn.gome.staff.buss.shop

#业务模块-社交
cn.gome.staff.buss.im

#业务模块-分享
cn.gome.staff.buss.share
~~~

业务开发

> 指开发具体的业务需求

如下需求开发以 cn.gome.staff.buss.home 为根目录

~~~bash
# 实体
bean
# http请求参数
bean.request
# http返回参数
bean.response

# Api定义【retrofit】
api
# GTask使用【GTask和retrofit采用一个】
task

# 工具类
utils

# activity
ui.activity
# fragment
ui.fragment

# 列表适配器
adapter

# 自定义控件【模块级别，全局性的请放到核心控件库】
view
~~~

JAVA类命名规范
=============

> 1.采用大驼峰式命名法，尽量避免缩写，除非该缩写是众所周知的，比如HTML，URL;
>
> 2.如果类名称包含单词缩写，则单词缩写的每个字母均应大写;

~~~bash
# 实体
Product
# 实体管理器
ProductManager
# Activity
ProductListActivity
# adapter
ProductListAdapter
# fragment
ProductListFragment
~~~

接口命名规范
==========

> 1.常规接口,命名规则与类一样采用大驼峰命名法，以ible结尾，以I开头
>
> 2.监听类接口，以Listener结尾,以On开头

~~~java
//常规接口
interface IHttpResultible
//监听接口
interface OnClickListener
~~~

成员变量命名规范
=============

> 采用小驼峰命名法，以 m 为前缀

~~~java
//常规变量
public String mUserName;
//控件变量
public Button mBtnLogin;
~~~

临时变量命名
==========

> 使用标准的Java命名方法，不使用Google的m命名法

~~~java
String userName;
//而不推荐使用
String mUserName;//错误命名法
~~~

常量命名
=======

> 常量使用全大写字母加下划线的方式命名

~~~java
public static final String TAG = "tag";
public static final String PARAMS_ID = "paramId";
public static final String APP_URL_LOGIN = "appUrlLogin";
~~~

控件实例命名
==========

> 类中控件名称必须与xml布局id保持命名一致

布局ID命名

> 1.全小写
>
> 2.下划线命名法
>
> 3. {控件类型简写}_{控件功能名称}

~~~xml
android:id="@+id/btn_pay"
~~~

代码控件

> m开头，驼峰

~~~java
private Button mBtnPay;
~~~

方法命名规范
==========

动词或动名词，采用小驼峰命名法

~~~java
run();
onCreate();
syncProducts();
~~~

布局文件(Layout)命名规范
=====================

> 全部小写，采用下划线命名法
>
> 其中{module_name}为业务模块或是功能模块等模块化的名称或简称【*即前缀*】。

~~~bash
# activity layout： {module_name}_activity_{名称} 例如：
gm_activity_main.xml | gm_activity_shopping.xml

# fragment layout:{module_name}_fragment_{名称} 例如：
gm_fragment_main.xml | gm_fragment_shopping.xml

# Dialog layout: {module_name}_dialog_{名称} 例如：
gm_dialog_loading.xml

# 列表项布局命名：{module_name}_list_item_{名称} 例如：
gm_list_item_customer.xml

# adapter的子布局： {module_name}_item_{名称} 例如：
gm_item_order.xml

# widget layout： {module_name}_widget_{名称} 例如：
gm_widget_shopping_detail.xml
~~~

资源id命名规范
============

> 命名模式为：{view缩写}_{module_name}_{view的逻辑名称}

我的国美头部 LinearLayout 的布局id –> ll_gm_content

我的国美logo ImageView 的布局id –> iv_gm_logo


图片资源文件命名规范
================

> 图标命名：{module_name}_ic_{名称}

~~~java
gm_ic_app.png
~~~

> 背景图片命名： {module_name}_bg_{名称} 例如：

~~~java
gm_bg_navbar_highlight_normal.9.png
~~~

> 按钮Button命名： {module_name}_btn_{名称} 例如：

~~~java
gm_btn_login_normal.9.png
~~~

> 按钮checkbox图片命名：{module_name}_checkbox_{名称} 例如：

~~~java
gm_checkbox_cart_true.png
~~~

> 其他图片命名：{module_name}_icon_{名称} 例如：

~~~java
gm_icon_blue_circle.png
~~~

资源文件命名规范

> 加前缀！！！！！！！
>
> 图片等非xml文件，必须加前缀；除了layout文件外，其他xml文件 可不加前缀
>
> assets内文件要求加前缀

application工程 开发注意事项

> 禁止在项目中使用switch处理【资源ID】

拓展module注意事项

> 子module与application工程，包名不能相同
>
> 代码包 符合 cn.gome.staff.<模块名称> 规范
>
> 子module与application工程，资源前缀不能相同

反射类使用注意事项

> android.support.annotation.Keep;
>
> 在被反射调用的类上面加 @Keep 注解
