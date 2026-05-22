# Firebase
-keep class com.firebase.** { *; }
-keep class com.google.firebase.** { *; }

# Hilt
-keep class * extends dagger.hilt.android.internal.managers.ServiceComponentManager { *; }
-keep class dagger.hilt.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * extends androidx.room.InvalidationTracker { *; }

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.example.hostelpro.**$$serializer { public static ** INSTANCE; }
-keepclasseswithmembers class com.example.hostelpro.** {
    kotlinx.serialization.KSerializer serializer(...);
}
