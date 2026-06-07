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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# Keep Kotlin metadata
-keepattributes *Annotation*, InnerClasses
-dontnote kotlin.random.Random

# Hilt
-keep class dagger.hilt.** { *; }
-keep class hilt_aggregated_deps.** { *; }
-keepclasseswithmembernames class * {
    @dagger.hilt.* <fields>;
}
-keepclasseswithmembernames class * {
    @dagger.hilt.* <methods>;
}

# Firebase
-keep class com.firebase.** { *; }
-keep class com.google.firebase.** { *; }
-keepclassmembers class ** {
    @com.google.firebase.database.Exclude <fields>;
}

# Retrofit
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.**

# Coil
-keep class coil.** { *; }

# Google Play Services
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Timber
-dontwarn timber.log.**

# Lottie
-keep class com.airbnb.lottie.** { *; }

# View Binding
-keepclasseswithmembers class * {
    android.view.View *(android.content.Context, android.util.AttributeSet);
}

# Application classes that will be serialized/deserialized over Gson
-keep class com.example.tourismguide.domain.model.** { *; }
-keep class com.example.tourismguide.data.remote.dto.** { *; }
-keep class com.example.tourismguide.data.local.entity.** { *; }

# Keep constructors for data classes
-keepclassmembers class com.example.tourismguide.** {
    *** <init>(...);
}

# Avoid shrinking AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Material Design
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# Suppress warnings for libraries
-dontnote com.google.**
-dontwarn com.google.common.**
-dontwarn android.support.**
-dontwarn androidx.test.**