
# Android

![Alt text](drawio/mindmap%20testing.svg)

# Classes
- Data
- Object
- Sealed
- open

# JetPack
- Room
- DataStore
- WorkManager
- Paging
- UI <a href="#jetpack-compose">Compose</a>

# Coroutines
- Suspend
- Blocking
- RunBlocking
- Structured Concurrency
- Scope
- Context
- Launch
- async
- delay
- Internal workings of coroutines (state machine and continuation).
- How coroutines handle structure concurrency?
- Differences between coroutines and threads.
- Scopes and builders for coroutines.

# Kotlin 
- <a href="Kotlin.md#flow">FLOW</a> 

# Basic Android Components 
- Activity Fragment Lifecycle
- <a href="#SSL-Pinning">SSL Pinning</a> 
- SQLITE
- Whats new in Android?
- MVP vs MVI vs <a href="MVP_vs_MVVM_Comparison.markdown">MVVM</a> : Differences and use cases.
- ViewModel survival during configuration changes: Understand ViewModel internals.
- RecyclerView view caching: How RecyclerView handles view caching internally.
- Android Handler, Looper, and message queue: Their roles in Android threading.
- Normal class vs data class: Differences in Kotlin.
- Activity and Fragment Lifecycle: Understand their lifecycle methods.
- Activity launch modes: Different launch modes in Android.
- Memory Management and solving memory leaks: Techniques to manage memory efficiently.
- StateFlow vs SharedFlow vs LiveData: Differences and when to use each.
- App performance improvement: Strategies to enhance app performance.
- Foreground service vs background service: Their distinctions and use cases.
- WorkManager vs AlarmManager: Choosing the right one for scheduling tasks.
- Benefits of Dependency Injection (Dagger and Hilt): Importance and alternatives to using DI frameworks.
- Ensuring app security: Best practices for securing Android applications.
- Encrypting and decrypting user data: Using RSA vs AES encryption algorithms.
- Networking and Data Persistence : Offline support. (Retrofit & Room)
Activity Lifecycle
- What is the difference between onCreate() and onStart()
- When only onDestroy is called for an activity without onPause() and onStop()?
    - when you put finish in oncreate :D
https://developer.android.com/guide/components/activities/activity-lifecycle

-  Mobile system design (https://github.com/weeeBox/mobile-system-design/)
- Unit testing : 
https://medium.com/1mgofficial/unit-testing-in-mvvm-kotlin-databinding-ba3d4ea08f0e | [sample](https://github.com/niharika2810/UnitTesting-MVVM-Kotlin-Koin-Coroutines-Sample)

## Android Application Components

App components are the essential building blocks of an Android app. Each component serves a specific purpose and has its own lifecycle.

There are four types of app components:

✅  Activities: 

An activity is the entry point for user interaction. It represents a single screen with a user interface. For example, a login screen or a settings page is an Activity. Activities are managed in a stack, and you can navigate between them.

✅  Services: 

A Service is a component that runs in the background to perform long-running operations or handle tasks without a UI. For example, playing music or downloading a file can be managed by a Service. It doesn’t interact directly with the user but can communicate with other components.

✅  Broadcast receivers:

 A Broadcast Receiver listens for system-wide broadcast announcements or events, such as a low battery warning, a new SMS, or a custom event from another app.

✅  Content providers: 

It manages data sharing between different applications. Example: Sharing contacts or accessing media files from the gallery.

## Difference between AAR, JAR, DEX, APK in Android 

| Format    |   Contains	|   Used for	|   Can Include Resources?	|   Android-Specific?   |
|---|----|---|---|---|
|JAR|	.class files	|Java/Kotlin libraries                                  |	❌ No	|❌ No|
|AAR	| .class, res/, AndroidManifest.xml	| Android libraries (SDKs, UI components)     |	✅ Yes|	✅ Yes|
|DEX|	Compiled bytecode	|       Running apps on Android	                                        |❌ No	|✅ Yes|
|APK|	.dex, res/, AndroidManifest.xml	|Final Android application                       |	✅ Yes|	✅ Yes|


- Use JAR for pure Java/Kotlin libraries (e.g., Gson, OkHttp).
- Use AAR for Android-specific libraries (e.g., custom UI components, third-party SDKs).
- Use DEX indirectly (it’s generated automatically during compilation).
- Use APK as the final installable package for Android apps.

## Different ways to add dependency in android
- __implementation__ : The dependency is available at `compile-time` and `runtime`, but `not exposed` to other modules.
- __api__ : Works like implementation, but it `exposes` the dependency to other modules.Other modules that depend on your library can also access the transitive dependency. Use When you are creating a library module and want to expose dependencies to other modules.
- __compileOnly__ : The dependency is available at `compile-time` but not included in the APK. Used for annotations, provided dependencies, or optional APIs. Use When a library is needed only at compile-time but should not be packaged with the app.
- __runtimeOnly__ : The dependency is `not available at compile-time`, but included in the APK. Use When a library is needed only at runtime (e.g., logging, database drivers).





## SSL Pinning
#### What is SSL ?
SSL (Secure Sockets Layer) is a protocol for establishing authenticated and encrypted links between networked computers and servers.

#### How SSL Pinning will help to get rid of Middle Man attack ? 

SSL encrypts the data exchanged between our server and app, and the attacker middle man can’t view the actual data transmitted. Actual data exchanged will only be visible to our Server and App.

#### What is SSL pinning?
It is a process where we can check the authenticity of a HOST by checking its core X509 certificate. This X509 certificate is the integral part of SSL. we can find more about it here X509 certificate.
SSL pinning is a process which forces our client App to validate the server’s certificate against a known copy.


we have a known KEY of our server’s certificate stored in our app (like we can store in strings or constants in some file) and then when we try to establish a connection with our server (means hit any API call), first we try to check if the connection is secure by matching the KEY we have in our app with Server’s certificate’s KEY.

“If these both keys matches we are good to go our App connection is with our known(our own) Server”


we have these `3` certificates for every endpoint URL, and we can use any of these certificates’ KEY to check with our server’s certificate while making a connection.

> Root Certificate -> Intermediate Certificate -> leaf Certificate

When we pin one of these certficate there is pros and cons

#### Root Certificate : 
this is also known as Certificate Authority(CA), if we pin against this Root Certificate, that means we need to use the Certificate key of the Hosting service provider.

The benefit of using the Pinning to Root Certificate is it has a good long life (approx 10+ years) it will not expire soon hence we no need to worry to upload a new APK again and again just because our certificate expired and we got a new one with a new respective KEY.


#### Intermediate Certificate: 
If we use an Intermediate Certificate so we are relying on the company where our backend server is hosted. for eg. baltimore cybertrust, AWS, GoDaddy, etc.


#### Leaf Certificate : 
Leaf Certificate has a short expiry time (like approx 1 year) so If we pin this and use the KEY provided from this certificate in our App, then once it expires our backend Server gets a new Leaf Certificate hence new KEY, that time our app is blocked we need to update another APK of our app by adding this new KEY generated by newly issued Leaf Certificate.

#### Implementation

There are many ways to implement : 
- Retrofit with OkHttp using CertificatePinner.
- network_security_config.xml.

Using Retrofit
```kotlin
@Provides
@Singleton
fun provideRetrofit(){
    val API_BASE = "www.abc.com"
    val certificatePinner = CertificatePinner.Builder()
                .add(API_BASE,"Root Certficate Key")
                .build()

    val httpClient = OkhttpClient.newBuilder()
    httpClient.apply{
        certificatePinner(certificatePinner)
    }

    val converter = MoshiConvertorFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory.Build()).asLenient()

    return Retrofit.Builder()
                .baseUrl(API_BASE)
                .addConvertFactory(converter)
                .client(httpclient.build())
                .build()
} 
```

2nd way -  https://developer.android.com/privacy-and-security/security-config

create xml folder inside res directory. Create file `network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted = "false" >
        <domain includeSubdomains="true">example.com</domain>
        <pin-set expiration="2025-01-26">
            <pin digest = "SHA-256">Root Certificate Key</pin>
        </pin-set>
    </domain-config>
</network-security-config>
```
add this file to `AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest ... >
    <application android:networkSecurityConfig="@xml/network_security_config"
                    ... >
        ...
    </application>
</manifest>
```


## launch modes 
We use launchMode to give instructions to the Android operating system about how to launch the activity

4 types of launch modes

1. ✅ Standard (default)
    - A new instance of the activity is always created.
    - Multiple instances can exist in the same or different tasks.
    - Each time you startActivity(), a new object is created.

2. ✅ SingleTop
    - If the activity is already at the top of the stack, no new instance is created instead onNewIntent() is called.
    - If it's not on top, a new instance is created.
    - Use case: Notifications, deeplinks, home screen activity.
3. ✅  SingleTask
    - If an instance exists in any task, it will be brought to the front and existing activities above it are destroyed.
    - Only one instance exists across the system.
    - onNewIntent() is called with the new intent.
    - Use case: Login, splash, or activities that should not be duplicated.
    - Example :
        ```
         A > B > C > D > E
            C is single task 
            if you call C then stack will become like
            A > B > C 
            and C will get new Extras from `onNewIntent()`
        ```
4. ✅  SingleInstance
    - Like singleTask, but the entire task will only contain that activity — no other activities can be launched into that task.
    - Runs in its own separate task.
    - Not recommended unless you need complete isolation.
    - Use case: Video calls, special views, or secure viewer windows.




### Summary 
| Launch Mode      | New Instance? | Shares Task? | onNewIntent()? | Use Case                    |
| ---------------- | ------------- | ------------ | -------------- | --------------------------- |
| `standard`       | ✅ Always      | ✅ Yes        | ❌ No           | Normal screens              |
| `singleTop`      | ❌ If on top   | ✅ Yes        | ✅ Yes          | Notifications, toolbar taps |
| `singleTask`     | ❌ Reuses      | ✅ One only   | ✅ Yes          | Login, splash               |
| `singleInstance` | ❌ Reuses      | ❌ Own task   | ✅ Yes          | Isolated activities         |





# Dependency Injection (Hilt/Dagger) – Advanced Usage
✅ Hilt Components & Scopes

| Scope                     | Component                   | Lifetime                        |
| ------------------------- | --------------------------- | ------------------------------- |
| `@Singleton`              | `SingletonComponent`        | App-wide, tied to `Application` |
| `@ActivityRetainedScoped` | `ActivityRetainedComponent` | Survives config changes         |
| `@ActivityScoped`         | `ActivityComponent`         | Tied to Activity lifecycle      |
| `@ViewModelScoped`        | `ViewModelComponent`        | Scoped to `ViewModel`           |
| `@FragmentScoped`         | `FragmentComponent`         | Scoped to a single Fragment     |

✅ Modules vs EntryPoints

- Use @Module + @Provides when you don't own the class (e.g., Retrofit, Room).
- Use @Binds for interfaces to implementations.
- Use @EntryPoint only when you need to inject into non-Hilt-aware classes (e.g., WorkManager, BroadcastReceiver).

✅ Multi-Module Setup with Hilt
- Each module (e.g., core-network, feature-login) can define its own @Module.
- Annotate @InstallIn(SingletonComponent::class) or relevant scope.
- Avoid tight coupling: define interfaces in domain, implementations in data.

✅ Custom Qualifiers

Used when you have multiple bindings of the same type.

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Provides
@AuthRetrofit
fun provideAuthRetrofit(): Retrofit = ...

```

# NDK in Android

NDK (Native Development Kit) is a toolset that allows you to write portions of your app using native languages like C or C++, and call them from Kotlin/Java.

📌 Android apps usually run on the Java/Kotlin layer (Android Runtime), but with NDK you can:

- Run low-level, high-performance code
- Hide sensitive logic (like root detection, encryption)
- Use existing C/C++ libraries

📌 we use C++ for root detection because C++ code is harder to reverse engineer and it run closer to OS so it can inspect things which java cannot easily access.

✅ Works: Java ↔ C++ with JNI

The connection is made through JNI (Java Native Interface).

Load Native Lib

```kotlin
companion object {
    init {
        System.loadLibrary("security-lib")
    }
}
```
C++ Code (native-lib.cpp or security-lib.cpp):
```c
#include <jni.h>
#include <string>
#include <fstream>

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_yourpackage_yourclass_isDeviceRooted(JNIEnv *env, jobject thiz) {
    std::ifstream suFile("/system/xbin/su");
    return suFile.good() ? JNI_TRUE : JNI_FALSE;
}

```

our kotlin code calling c++ function
```kotlin
external fun isDeviceRooted(): Boolean
```

In Gradle 

```groovy
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "arm64-v8a"
        }
        externalNativeBuild {
            cmake {
                cppFlags ""
            }
        }
    }
    externalNativeBuild {
        cmake {
            path "src/main/cpp/CMakeLists.txt"
        }
    }
}
```


```
app/
 ├── src/
 │   ├── main/
 │   │   ├── java/
 │   │   │   └── com/yourpackage/
 │   │   │       └── RootCheck.kt
 │   │   ├── cpp/
 │   │   │   └── security-lib.cpp
 │   │   └── AndroidManifest.xml
 └── build.gradle

```

| Term                       | What It Is                                            |
| -------------------------- | ----------------------------------------------------- |
| **NDK**                    | Toolset for C/C++ in Android                          |
| **JNI**                    | Bridge between Java/Kotlin ↔ C++                      |
| **CMake**                  | Build system for native code                          |
| **`System.loadLibrary()`** | Loads `.so` native library                            |
| **Use case**               | Hide root detection, protect IP, run fast native code |




# WorkManager, JobScheduler, AlarmManager, and Services

### ✅ Why It Matters

In Android, background tasks must be handled carefully and efficiently, especially under OS restrictions (Doze Mode, App Standby, Battery Saver). A Senior Android Engineer is expected to choose the right background execution tool based on:

- Timing
- Reliability
- Device constraints
- Execution window

### ✅ WorkManager (Recommended for most use cases)
For deferrable, guaranteed, background work — even after app/process/device restart.

Features:
- Lifecycle-aware
- Automatically chooses JobScheduler, AlarmManager, or FirebaseJobDispatcher
- Supports constraints (network, battery, charging)
- Retry + exponential backoff
- Chained + periodic work

📚 Use When: You want reliable work that must finish eventually, like syncing data.

### ✅ JobScheduler (API 21+)
System-level job scheduler for batch background tasks.

Pros:
- OS optimized
- Batching = battery efficient
- Schedules tasks even after reboot (with extra code)

Cons:
- API 21+
- Not lifecycle-aware
- Complex to cancel/update jobs dynamically

📚 Use in system apps or battery-sensitive jobs.

### ✅ AlarmManager
For precise timing-based tasks — triggers exact or inexact events, even when app is killed.

```kotlin
val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
alarmManager.setExact(
    AlarmManager.RTC_WAKEUP,
    triggerTime,
    pendingIntent
)

```

📚 Use When:
- You need exact timing
- Wake up device (e.g., alarms, reminders)

❌ Avoid for frequent polling or long-running tasks.


### ✅ Services
📦 ForegroundService
-  Long-running work that must show a notification (e.g., music, download, GPS tracking)

📦 IntentService (deprecated in API 30)
- Background task with a queue, runs on a separate thread

📦 BoundService
- For communication between components (e.g., Activity ↔ Service)

📚 Use When:
- You need persistent operations tied to a UI or event
- Example: playing music, step counter, Bluetooth connections


| Use Case                              | Recommended Tool             |
| ------------------------------------- | ---------------------------- |
| Background sync, retryable task       | ✅ WorkManager                |
| Periodic data upload                  | ✅ WorkManager                |
| Exact alarm/reminder                  | ✅ AlarmManager               |
| Foreground playback (e.g., music)     | ✅ ForegroundService          |
| System app managing jobs              | ✅ JobScheduler               |
| One-off short background job (legacy) | ❌ IntentService (deprecated) |


## Jetpack Navigation

- Nav graph =  Information about all destinations
- Nav host =  container of fragments
- Nav Controller =  Control which need to show in nav Host


#### PROS

- Handling Fragment transactions
- Hanlding up & back navigation
- Animation and Transitions
- Deep Linking
- Nav Drawers and Bottom navigations
- Safe Args
- View Model support

----


### References 

- https://nik-arora8059.medium.com/android-learning-resources-77a67a77d340
- https://readmedium.com
- https://readmedium.com/en/https:/medium.com/mobile-app-development-publication/dagger-2-and-koin-comparison-4223dae9725d
- https://www.youtube.com/@StudyingWithAlex

Github pages for full List 

- [Android development best practices by niharika](https://github.com/niharika2810/android-development-best-practices)
- [Android learning resources  by niharika](https://github.com/niharika2810/android-learning-resources)
- [Android Interview Questions  by niharika](https://github.com/niharika2810/android-interview-questions)
- [Android Interview questions by Amit](https://github.com/amitshekhariitbhu/android-interview-questions)

- [Android Interview questions by vamsitallapudi](https://github.com/vamsitallapudi/Android-Interview-Questions-And-Answers)
- [Design Patterns by me](https://github.com/vishalguptahmh/java-design-patterns)

- [Android cheatsheet by Anita](https://github.com/anitaa1990/Android-Cheat-sheet)


