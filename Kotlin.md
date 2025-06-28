# Kotlin

- Structured Concurrency
- val vs const val: Differences and usage scenarios.
- lateinit vs lazy: When to use each and how they work.
- Lambda and higher-order functions: Benefits and examples.
- object vs companion object: Usage and differences.
- Scope functions (let, apply, with, run): How they simplify code blocks.
- Extension functions: Implementing and utilizing extension functions effectively.
- == vs ===: Equality checks in Kotlin.
- Sealed class vs enum: Differences and use cases.
- Inline functions: Reasons for using inline, crossinline, and noinline functions.
- Synchronization in Kotlin: Understanding how it works.

## Advantage of kotlin flow
- Simple API
- Structured Concurrency
- Backpressure handling
- suspending execution
- Nullablity

## Structured Concurrency
It is a combination of language features and best pratices that,when followed, help you keep track of all work running in coroutins
If the code is not perfect, it will lose track of a coroutine and cause a memory or a work leak.

So structured concurrency can greatly help us.
- To cancel work when it is no longer needed.
- To keep track of work while it's running.
-  the two signal errors when a coroutine fails.

## FLOW

Flow is an asynchronous data stream(which generally comes from a task) that emits values to the collector and gets completed with or without an exception.



![SSFlow](drawio/StateFlowVsSharedFlow.drawio.png)


### Basic flow operator
| Operator        | Purpose                           |
| --------------- | --------------------------------- |
| `map`           | Transform data                    |
| `filter`        | Filter emissions                  |
| `flatMapLatest` | Switch to latest Flow             |
| `catch`         | Error handling                    |
| `onEach`        | Side effects                      |
| `collect`       | Terminal operator (executes Flow) |



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
### StateFlow vs SharedFlow
| Feature          | `StateFlow`                    | `SharedFlow`             |
| ---------------- | ------------------------------ | ------------------------ |
| Holds value      | Yes (like LiveData)            | No (optional replay)     |
| Emits on collect | Immediately with current value | Only when emitted        |
| Use case         | UI state                       | Events, one-time actions |

✅ Use StateFlow for UI state

✅ Use SharedFlow for navigation, errors, toasts, etc.


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



# [Coroutines](https://outcomeschool.com/blog/kotlin-coroutines)


####  What distinct coroutines from threads?
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
- The key difference lies in how coroutines handle waiting for asynchronous operations. 
- Suspending allows a coroutine to yield control without blocking the underlying thread, enabling better concurrency and resource utilization. 
- Blocking, on the other hand, involves the thread waiting until the operation completes, which can lead to inefficiencies in terms of resource usage. 

✅ Coroutines are designed to be more efficient by leveraging suspending functions to avoid unnecessary blocking and allow other tasks to proceed in the meantime.

🚀 The main difference is that the runBlocking method blocks the current thread for waiting, while coroutineScope just suspends, releasing the underlying thread for other usages. Because of that difference, runBlocking is a regular function and coroutineScope is a suspending function.

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

### 🔧 Common Builders

| Builder       | Use For                                 |
| ------------- | --------------------------------------- |
| `launch`      | Fire-and-forget jobs (e.g., UI updates) |
| `async`       | Return results via `Deferred`           |
| `runBlocking` | Testing or bridging blocking code       |
| `withContext` | Change dispatcher (e.g., IO, Main)      |

### 🔧 Coroutine Dispatchers

| Dispatcher               | Used For                  |
| ------------------------ | ------------------------- |
| `Dispatchers.Main`       | UI work (LiveData, Views) |
| `Dispatchers.IO`         | Network, DB, disk I/O     |
| `Dispatchers.Default`    | CPU-intensive tasks       |
| `Dispatchers.Unconfined` | Special-case debugging    |

### 🔧 Coroutine Cancellation
- Automatically cancelled when the coroutine scope (e.g., Activity, ViewModel) is destroyed
- Use isActive, ensureActive(), or try/catch with CancellationException for cleanup

```kotlin
viewModelScope.launch {
    try {
        fetchData()
    } catch (e: CancellationException) {
        // Handle cancellation
    }
}
```
### suspend vs Flow
| Aspect           | `suspend` function          | `Flow`                        |
| ---------------- | --------------------------- | ----------------------------- |
| Emits            | Single result               | Multiple or continuous values |
| Use case         | One-time calls (e.g., API)  | Stream of events/data         |
| Cold or Hot      | N/A                         | Usually cold                  |
| Cancelable       | Yes                         | Yes                           |
| Lifecycle-aware? | Needs `viewModelScope` etc. | Yes                           |


## Questions

### What are the key differences between LiveData and StateFlow, and when would you use one over the other?

Thread-safety and concurrency: StateFlow is thread-safe and works well with Kotlin Coroutines.

Backpressure handling: Not applicable to LiveData or StateFlow directly, but good to keep in mind when comparing flows in general.


| Concept           | LiveData                             | StateFlow                                       |
| ----------------- | ------------------------------------ | ----------------------------------------------- |
| Lifecycle-aware   | Yes (only emits to active observers) | No (requires manual lifecycle handling)         |
| Thread-safe       | Yes                                  | Yes                                             |
| Holds state       | Yes (but only for observers)         | Yes (re-emits the last value to new collectors) |
| Coroutine support | Limited                              | Native support (with `collect`/`collectLatest`) |
| Use Case          | UI-only events, one-time events      | Persistent state sharing (e.g., API data)       |


###  How do you handle error propagation in Kotlin coroutines?
CoroutineExceptionHandler: This is the main tool for handling uncaught exceptions in coroutines. You can define a CoroutineExceptionHandler that listens for errors in coroutines and handles them as needed. This handler is passed when launching the coroutine.

```kotlin
val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught $exception")
}

GlobalScope.launch(Dispatchers.IO + handler) {
    // Some code that may throw an exception
}
```

##### Scope-specific error handling:

try-catch within coroutines: You can catch errors directly inside a coroutine using a try-catch block. This is useful for local error handling.

supervisorScope: In cases where you want child coroutines to fail independently (without canceling the parent coroutine), you can use supervisorScope. This ensures that the failure of one child doesn’t propagate and cancel other child coroutines.

##### Exception handling in structured concurrency:

    If you're using structured concurrency (e.g., CoroutineScope), exceptions in one coroutine can be propagated up to the scope, canceling other coroutines in the same scope. To handle this, you can use supervisorScope or try-catch blocks to catch and handle errors at the appropriate levels.

##### Flow error handling:
In the case of Flow, you can handle exceptions within a flow using catch operator:

```kotlin
flow {
    emit("data")
    throw Exception("Error")
}.catch { e -> emit("Caught exception: $e") }
```

| Concept                     | Explanation                                                  |
| --------------------------- | ------------------------------------------------------------ |
| `CoroutineExceptionHandler` | Handles uncaught exceptions in coroutines                    |
| `try-catch` in coroutines   | Used for local error handling within a coroutine             |
| `supervisorScope`           | Ensures child coroutines do not cancel each other on failure |
| Flow error handling         | `catch` operator allows error handling in `Flow`             |



### What is the difference between suspend functions and Flow? When would you use one vs the other?

| Feature      | `suspend` function                     | `Flow`                              |
| ------------ | -------------------------------------- | ----------------------------------- |
| Purpose      | Executes a task and returns one result | Emits a stream of values over time  |
| Example      | Network call                           | Search text input, periodic updates |
| Cancellation | Cancels when coroutine is canceled     | Cancelable mid-stream               |
| Cold/Hot     | N/A                                    | `Flow` is cold by default           |
| Collection   | Called with a coroutine                | Collected using `collect()`         |

✅ Use suspend when you need one result (e.g., API call).

✅ Use Flow when you need continuous/emitted values over time (e.g., observe database or user input).


|Question | Answer|
|--|--|
|Kotlin flow is hotstream | ❌|
|Kotlin flow aims to replace Android live data | ❌|
|When we mark a function with the suspend keyword it means that it is going to run on a background thread| ❌|
|We are not able start a coroutine without attaching it to a coroutine scope| ✅|
| Structured concurrency enables us to keep track and cancel all of our coroutines and the work that is happening inside them | ✅|
