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


-keep class androidx.lifecycle.LiveData { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.auto.service.AutoService
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Modifier
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.PackageElement
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.TypeParameterElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.ArrayType
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVariable
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.util.AbstractAnnotationValueVisitor8
-dontwarn javax.lang.model.util.AbstractTypeVisitor8
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.Elements
-dontwarn javax.lang.model.util.SimpleElementVisitor8
-dontwarn javax.lang.model.util.SimpleTypeVisitor7
-dontwarn javax.lang.model.util.SimpleTypeVisitor8
-dontwarn javax.lang.model.util.Types
-dontwarn javax.tools.Diagnostic$Kind



 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
  # kept. Suspend functions are wrapped in continuations where the type argument
  # is used.
  -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

  # R8 full mode strips generic signatures from return types if not kept.
  -if interface * { @retrofit2.http.* public *** *(...); }
  -keep,allowoptimization,allowshrinking,allowobfuscation class <3>

  # With R8 full mode generic signatures are stripped for classes that are not kept.
  -keep,allowobfuscation,allowshrinking class retrofit2.Response

  # Keep Moshi generated adapters
  -keep class com.app.feelweather.remote.**JsonAdapter { *; }

  # Keep data classes used with Moshi
  -keepclassmembers class com.app.feelweather.remote.** {
      <init>(...);
  }
  -keepclassmembers class com.app.feelweather.remote.** {
      @com.squareup.moshi.Json <fields>;
  }
  -keepclassmembers class com.app.feelweather.remote.** {
      @com.squareup.moshi.JsonClass <fields>;
  }

  # Don't strip reflective access needed by Moshi
  -keepclassmembers class * {
      @com.squareup.moshi.* <fields>;
  }
  -keep @com.squareup.moshi.JsonClass class com.app.feelweather.remote.** { *; }

  # Moshi KotlinJsonAdapterFactory needs reflection
  -keep class kotlin.Metadata { *; }
  -dontwarn kotlin.reflect.**