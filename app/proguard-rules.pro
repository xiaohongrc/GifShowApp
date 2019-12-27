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


-keep public class com.hongenit.gifshowapp.R$*{
public static final int *;
}

# eventbus 混淆
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
  <init>(java.lang.Throwable);
}


-keepclasseswithmembers class * implements com.hongenit.gifshowapp.bean.Model{*;}
-keepclasseswithmembers class * extends com.hongenit.gifshowapp.network.response.BaseResponse{*;}




-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class com.google.protobuf.** {*;}
-keep class com.google.protobuf.GeneratedMessage.** { *;}




# glide的混淆过滤
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}



-dontwarn com.umeng.**
-keep public class com.umeng.** {*;}

-keepclassmembers class **.R$* {
    public static <fields>;
}



-dontwarn android.**
-keep public class android.** {*;}

-dontwarn com.android.**
-keep class com.android.** {*;}