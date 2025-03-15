
# Android

![Alt text](drawio/mindmap%20testing.svg)

# Classes
- Data
- Object
- Sealed
- open

# jetPack
- Room
- DataStore
- WorkManager
- Paging


# UI
- Compose

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
- <a href="#FLOW">FLOW</a> 

# Basic Android Components 
- Activity Fragment Lifecycle
- <a href="#SSL-Pinning">SSL Pinning</a> 
- SQLITE
- Whats new in Android?
- MVP vs MVI vs MVVM: Differences and use cases.
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



#### Tell all the Android application components

App components are the essential building blocks of an Android app. Each component serves a specific purpose and has its own lifecycle.

There are four types of app components:

• Activities: An activity is the entry point for user interaction. It represents a single screen with a user interface. For example, a login screen or a settings page is an Activity. Activities are managed in a stack, and you can navigate between them.
• Services: A Service is a component that runs in the background to perform long-running operations or handle tasks without a UI. For example, playing music or downloading a file can be managed by a Service. It doesn’t interact directly with the user but can communicate with other components.
• Broadcast receivers: A Broadcast Receiver listens for system-wide broadcast announcements or events, such as a low battery warning, a new SMS, or a custom event from another app.
• Content providers: It manages data sharing between different applications. Example: Sharing contacts or accessing media files from the gallery.

Kotlin

    val vs const val: Differences and usage scenarios.
    lateinit vs lazy: When to use each and how they work.
    Lambda and higher-order functions: Benefits and examples.
    object vs companion object: Usage and differences.
    Scope functions (let, apply, with, run): How they simplify code blocks.
    Extension functions: Implementing and utilizing extension functions effectively.
    == vs ===: Equality checks in Kotlin.
    Sealed class vs enum: Differences and use cases.
    Inline functions: Reasons for using inline, crossinline, and noinline functions.
    Synchronization in Kotlin: Understanding how it works.

## Mobile system desing (https://github.com/weeeBox/mobile-system-design/)

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

## FLOW

Flow is an asynchronous data stream(which generally comes from a task) that emits values to the collector and gets completed with or without an exception.



![SSFlow](drawio/StateFlowVsSharedFlow.drawio.png)





## StateFlow vs SharedFlow:

```kotlin
StateFlow = SharedFlow
            .withInitialValue(initialValue)
            .replay(count=1)
            .distinctUntilChanged()

```

```kotlin
val sharedFlow = MutableSharedFlow<Int>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
sharedFlow.emit(0) // initial value
val stateFlow = sharedFlow.distinctUntilChanged()
```


## Where we have to use which Flow

`example1` : we have a use case: Get the list of the users from the network and show them in the UI.

```kotlin
//ViewModel
val usersStateFlow = MutableStateFlow<UiState<List<User>>>(UiState.Loading)

//Activity
usersStateFlow.collect {
print(it)
}

//ViewModel
usersStateFlow.value = UiState.Success(usersFromNetwork)


```
Now, if orientation changes, the ViewModel gets retained, and our collector present in the Activity will re-subscribe to collect. The following will be collected:

usersStateFlow: List of users which was set from the network. (StateFlow keeps the last value).

Advantage: No need for a new network call.

if we have used used SharedFlow here
- as soon as activity is subscribed to collect, it will not get any value and we to explicity add loading
- Now, if orientation changes, the ViewModel gets retained, and our collector present in the Activity will resubscribe to collect. Nothing will get collected here as SharedFlow is used which does not store any data. We will have to make a new network call.

Disadvantage: Un-necessary network call as we were already having the data.


`example 2` : suppose we are doing a task, if that task gets failed, we have to show Snackbar.

```kotlin 
//Viewmodel
val showSnackbarSharedFlow = MutableSharedFlow<Boolean>()
//Activity
showSnackbarSharedFlow.collect {

}


```

- as soon as activty subscribe, activity will not get anything
- if task get failed then only value got into collector
- in orientation change also it will not send any values to collector as activity will re-subscribe


### Unit testing : 

https://medium.com/1mgofficial/unit-testing-in-mvvm-kotlin-databinding-ba3d4ea08f0e

[sample](https://github.com/niharika2810/UnitTesting-MVVM-Kotlin-Koin-Coroutines-Sample)



# [Coroutines](https://outcomeschool.com/blog/kotlin-coroutines)

- What distinct coroutines from threads?
    - Executed within thread
    - coroutines are suspendable that means you can pause and resume in between tasks
    - They can switch context that means they can move from main thread to back thread easily

GlobalScope - this will live till our application will live
```kotlin
GlobalScope.launch{
    Log.d("",""+ Thread.currentThread.name) // Default Dispactcher -worker
}
```

delay is same sleep 

Delay will only work on that thread not whole thread this is misconception

```kotlin
GlobalScope.launch{
    delay(5000)
    Log.d("",""+ Thread.currentThread.name) // Default Dispactcher -worker
}
```
if you kill application within 5 sec then main thread is killed so coroutine will kill its worker thread also

### Start coroutines
there are two functions in kotlin to start the coroutines 
- launch{} : fire and forget
- async{} : perform a task and return a result

The difference is that the launch{} returns a Job and does not carry any resulting value whereas the async{} returns an instance of Deferred<T>, which has an await() function that returns the result of the coroutine like we have future in Java in which we do future.get() to get the result.

```kotlin
val deferred = GlobalScope.async(Dispatchers.Default) {
    // do something and return result, for example 20 as a result
    return@async 20
}
val result = deferred.await() // result = 20
```

### suspend Function 
Suspend function is a function that could be started, paused, and resume.Suspend functions are only allowed to be called from a coroutine or another suspend function


Coroutine Context : 
- Default : used for long running caclulation that will block main thread ex. sorting
- unconfind : this will remain in thread where suspend function is used
- IO : used for network calls , database operations, read and write files

```kotlin
GlobalScope.launch(Dispactcher.IO){
   result = network.call()
   withContext(Dispactcher.main){ // here we are switching backthread to main thread
    textFiled.text = result
   }
}

```

withContext is a suspend function through which we can do a task by providing the Dispatchers on which we want the task to be done.

withContext does not create a new coroutine, it only shifts the context of the existing coroutine and it's a suspend function whereas launch and async create a new coroutine and they are not suspend functions.

As withContext is a suspend function, it can be only called from a suspend function or a coroutine. So, both the above functions are suspend functions.


### RunBlocking 
- it will block main thread and it is coroutine scope
- used to run suspend functino on main thread .
- it is synchronous
- used in testcases also
- Used to check how coroutines works

```kotlin
runblocking{delay(3000)}
```
example : 

```kotlin
Log.d("Before run blocking " )
runBlocking{
    launch(Dispacher.IO){
        delay(5000)
        Log.d("launch")
    }

    Log.d("Start run blocking ")
    Delay(5000)
    Log.d("End run blocking ")
}
Log.d("After run blocking " )
// Output 
//Before run blocking
//Start run blocking
//launch 1
//launch 2
//End run blocking
//After run blocking 


```

- Global scope is living till app dies what is time to live for runblocking as its working on main thread?


### Scopes
Scopes in Kotlin Coroutines are very useful because we need to cancel the background task as soon as the activity is destroyed

- lifecycleScope : to cancle background task as soon as activity is destroyed
- ViewModelScope : to cancel background task as soon as viewmodel is destroyed

### Exception handling
 we generally handle exceptions using try catch but in coroutines there is another way also

 ```kotlin
 // 1st method
 GlobalScope.launch(Dispatcher.Main){
    try{
        fetchFromDb()
    }catch(e: Exception){
        Log.d(TAG,e.message())
    }
 }

 // 2nd Method

val handler = CoroutineExceptionHandler { _, exception ->
    Log.d(TAG, "$exception handled !")
}
GlobalScope.launch(Dispatchers.Main + handler) {
    fetchFromDb() // do on IO thread and back to UI Thread
}

```

For async we have to use try catch only

IN below code i want to call two apis in parrlel if anyone fails it should return empty list. how can we do?

```kotlin
//Question
launch {
    val users = try {
        getUsers()
    } catch (e: Exception) {
        emptyList<User>()
    }
    val moreUsers = try {
        getMoreUsers()
    } catch (e: Exception) {
        emptyList<User>()
    }
}
```

```kotlin
// solution
launch {
    supervisorScope {
        val usersDeferred = async { getUsers() }
        val moreUsersDeferred = async { getMoreUsers() }
        val users = try {
            usersDeferred.await()
        } catch (e: Exception) {
            emptyList<User>()
        }
        val moreUsers = try {
            moreUsersDeferred.await()
        } catch (e: Exception) {
            emptyList<User>()
        }
    }
}

```
The major difference is that a coroutineScope will cancel whenever any of its children fail. If we want to continue with the other tasks even when one fails, we go with the supervisorScope. A supervisorScope won't cancel other children when one of them fails.


### suspending vs blocking 
the key difference lies in how coroutines handle waiting for asynchronous operations. 

Suspending allows a coroutine to yield control without blocking the underlying thread, enabling better concurrency and resource utilization. 

Blocking, on the other hand, involves the thread waiting until the operation completes, which can lead to inefficiencies in terms of resource usage. 

Coroutines are designed to be more efficient by leveraging suspending functions to avoid unnecessary blocking and allow other tasks to proceed in the meantime.

The main difference is that the runBlocking method blocks the current thread for waiting, while coroutineScope just suspends, releasing the underlying thread for other usages. Because of that difference, runBlocking is a regular function and coroutineScope is a suspending function.

example : 

```kotlin 
private val dispatcher = Executers.newSingleThreadExceutor().asCoroutineDispatcher()

// SUSPENDING

fun exSUSPENDING(){
     lifecycleScope.launch(dispacher){
            Log.d("before 1")
            timeTakingTask()
            Log.d("after 1")
    }
    lifecycleScope.launch(dispatcher){
            Log.d("before 2")
            timeTakingTask()
            Log.d("after 2")
    }
}
/**
before 1
before 2
after 1
after 2
**/

// blocking
fun exBlocking(){
    lifecycleScope.launch(dispacher){
        runBlocking{
            Log.d("before 1")
            timeTakingTask()
            Log.d("after 1")
        }
    }
    lifecycleScope.launch(dispatcher){
        runBlocking{
            Log.d("before 2")
            timeTakingTask()
            Log.d("after 2")
        }
    }
}

/**
Output : 
before 1
after 1
before 2
after 2
**/

fun timeTakingTask(){
    withContext(Dispatcher.IO){
        Thread.sleep(5000)
    }
}
```

### Jobs , Cancelation, timeouts







### References 

- https://nik-arora8059.medium.com/android-learning-resources-77a67a77d340
- https://readmedium.com
- https://readmedium.com/en/https:/medium.com/mobile-app-development-publication/dagger-2-and-koin-comparison-4223dae9725d
- [Android development best practices](https://github.com/niharika2810/android-development-best-practices)
- https://www.youtube.com/@StudyingWithAlex

github pages for full List 
- https://github.com/niharika2810/android-learning-resources
- https://github.com/vamsitallapudi/Android-Interview-Questions-And-Answers
- [Design Patterns](https://github.com/vishalguptahmh/java-design-patterns)
- [Android Interview Questions](https://github.com/niharika2810/android-interview-questions)
- [Android Learning Resource](https://nik-arora8059.medium.com/android-learning-resources-77a67a77d340)
- https://github.com/anitaa1990/Android-Cheat-sheet