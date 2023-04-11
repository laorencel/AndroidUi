# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#   混淆设置参数:
#-optimizationpasses 5                       # 代码混淆的压缩比例，值介于0-7，默认5
#-verbose                                    # 混淆时记录日志
#-dontoptimize                               # 不优化输入的类文件
#-dontshrink                                 # 关闭压缩
#-dontpreverify                              # 关闭预校验(作用于Java平台，Android不需要，去掉可加快混淆)
#-dontoptimize                               # 关闭代码优化
#-dontobfuscate                              # 关闭混淆
#-ignorewarnings                             # 忽略警告
#-dontwarn com.squareup.okhttp.**            # 指定类不输出警告信息
#-dontusemixedcaseclassnames                 # 混淆后类型都为小写
#-dontskipnonpubliclibraryclasses            # 不跳过非公共的库的类
#-printmapping mapping.txt                   # 生成原类名与混淆后类名的映射文件mapping.txt
#-useuniqueclassmembernames                  # 把混淆类中的方法名也混淆
#-allowaccessmodification                    # 优化时允许访问并修改有修饰符的类及类的成员
#-renamesourcefileattribute SourceFile       # 将源码中有意义的类名转换成SourceFile，用于混淆具体崩溃代码
#-keepattributes SourceFile,LineNumberTable  # 保留行号
#-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod # 避免混淆注解、内部类、泛型、匿名类
#-optimizations !code/simplification/cast,!field/ ,!class/merging/   # 指定混淆时采用的算法
#   保持命令：
#-keep                           # 防止类和类成员被移除或被混淆；
#-keepnames                      # 防止类和类成员被混淆；
#-keepclassmembers	            # 防止类成员被移除或被混淆；
#-keepclassmembernames           # 防止类成员被混淆；
#-keepclasseswithmembers         # 防止拥有该成员的类和类成员被移除或被混淆；
#-keepclasseswithmembernames     # 防止拥有该成员的类和类成员被混淆

#---------------------------------基本指令区----------------------------------
    # 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
    -optimizationpasses 5
    # 指定不去忽略非公共库的类
    -dontskipnonpubliclibraryclasses
    # 指定不去忽略非公共库的类成员
    -dontskipnonpubliclibraryclassmembers
    # 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
    -dontpreverify
    -printmapping proguardMapping.txt
    # 指定混淆时采用的算法，后面的参数是一个过滤器
    # 这个过滤器是谷歌推荐的算法，一般不做更改
    -optimizations !code/simplification/cast,!field/*,!class/merging/*
    # 保留Annotation不混淆
    -keepattributes *Annotation*,InnerClasses
    # 避免混淆泛型
    -keepattributes Signature
    # 抛出异常时保留代码行号
    -keepattributes SourceFile,LineNumberTable
    # 解决warning: Ignoring InnerClasses attribute for an anonymous inner class
    -keepattributes EnclosingMethod
    -ignorewarnings
    #----------------------------------------------------------------------------
    #---------------------------------默认保留区---------------------------------
    # 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
    # 因为这些子类都有可能被外部调用
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class * extends android.view.View
    -keep public class com.android.vending.licensing.ILicensingService
    # 使用androidx
    -keep class com.google.android.material.** {*;}
    -keep class com.google.android.flexbox.** {*;}
    -keep class androidx.** {*;}
    -keep public class * extends androidx.**{*;}
    -keep interface androidx.** {*;}
    -dontwarn com.google.android.material.**
    -dontnote com.google.android.material.**
    -dontwarn androidx.**
    # 保留support下的所有类及其内部类
    -keep class android.support.** {*;}
    # 保留继承的
    -keep public class * extends android.support.v4.**
    -keep public class * extends android.support.v7.**
    -keep public class * extends android.support.annotation.**
    -keep public class * implements java.lang.annotation.Annotation {}
    # 保留我们自定义控件（继承自View）不被混淆
    -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
    }
    # 保留本地native方法不被混淆
    -keepclasseswithmembers class * {
     native <methods>;
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
    }
    # 保留Parcelable序列化类不被混淆
    -keep class * implements android.os.Parcelable {
        public static final android.os.Parcelable$Creator *;
    }
    # 保留Serializable序列化的类不被混淆
    -keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
    }
    # 保留枚举类不被混淆
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
    # 保留R下面的资源
    -keep class **.R$* {
    *;
    }
    # 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
    -keepclassmembers class * {
     void *(**On*Event);
     void *(**On*Listener);
    }
    #----------------------------------------------------------------------------
    #---------------------------------webview------------------------------------
    # webView处理，项目中没有使用到webView忽略即可
    -keepclassmembers class fqcn.of.javascript.interface.for.Webview {
    public *;
    }
    -keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
     public boolean *(android.webkit.WebView, java.lang.String);
    }
    -keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, jav.lang.String);
    }
    #----------------------------------------------------------------------------
    #---------------------------------实体类---------------------------------
    -keepattributes JSONField
    -keepattributes ParamNames
    -keep class com.laorencel.lib_core.** { *; }
    -keep class com.laorencel.lib_widget.** { *; }
    -keep class com.laorencel.lib_common.** { *; }
    -keep class com.laorencel.lsg.bean.** { *; }
    -keep public class * extends com.laorencel.lib_common.bean.BaseBean{*;}
    #保留所有继承某一个类的代码以及它的变量和方法，不进行混淆。
    -keepclasseswithmembernames class com.laorencel.lib_common.bean.**{*;}
    -keepclasseswithmembernames class com.laorencel.lib_core.http.result.**{*;}

    # 实现此接口的类都不会混淆
#    -keep interface com.huage.ui.view.BaseView{*;}
#    -keep class * implements com.huage.ui.view.BaseView {
#    <methods>;
#    <fields>;
#    }
    #---------------------------------第三方包------------------------------