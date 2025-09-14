# Kotlin

- <a href ="#Structured Concurrency">Structured Concurrency</a>
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

**Definition:** Kotlin Coroutines are a framework for managing asynchronous programming, simplifying tasks like network calls, database operations, and background work in Android.

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


### Coroutine Scopes
CoroutineScope is like a parent supervising a group of workers (coroutines).
If the parent is fired or leaves the building (cancelled), all the workers must stop.

A CoroutineScope defines where coroutines live and how long they live.

- It ties a group of coroutines to a lifecycle (e.g., an Activity, ViewModel, or custom object).
- When the scope is cancelled, all its child coroutines are also cancelled.

- lifecycleScope : to cancle background task as soon as activity is destroyed
- ViewModelScope : to cancel background task as soon as viewmodel is destroyed


| Scope Name                 | Lifecycle Awareness | Blocks Thread? | Typical Use Case                        |
| -------------------------- | ------------------- | -------------- | --------------------------------------- |
| `runBlocking`              | ❌ No                | ✅ Yes          | Tests, `main()` functions               |
| `coroutineScope`           | ✅ Yes (Structured)  | ❌ No           | Suspend functions with children         |
| `supervisorScope`          | ✅ Yes (Structured)  | ❌ No           | Isolated child coroutine failure        |
| `GlobalScope`              | ❌ No                | ❌ No           | Rare, long-lived background jobs        |
| `viewModelScope`           | ✅ ViewModel         | ❌ No           | ViewModel-related async work            |
| `lifecycleScope`           | ✅ Activity/Fragment | ❌ No           | UI or network work bound to UI          |
| `rememberCoroutineScope()` | ✅ Compose           | ❌ No           | UI state work in Composables            |
| `Custom CoroutineScope`    | ✅ If managed        | ❌ No           | Utility classes, repositories, services |


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

In below code i want to call two apis in parllel if anyone fails it should return empty list. how can we do?

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

####  What distinct coroutines from threads?
- Executed within thread
- coroutines are suspendable that means you can pause and resume in between tasks
- They can switch context that means they can move from main thread to back thread easily


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


# Questions

1. **What do you mean by Lightweight threads in Coroutines?**
   - **Answer**: Lightweight threads are coroutines that run on a thread pool, using minimal resources vs. OS threads. They suspend/resume without blocking, ideal for async tasks.

    **Layman’s Terms:**

    Think of threads as workers in a factory (thread pool). Each worker can only handle one job at a time.

    Java Threads: A worker starts a job (e.g., fetching data) and stands idle, waiting for the server, holding tools (resources) and doing nothing. If you have 100 jobs, you need 100 workers, which is expensive.
    
    Coroutines: A worker starts a job, but when waiting for the server, they put the job aside (suspend) and go help another task. Later, any free worker picks up the paused job (resume). This needs fewer workers, saving resources.


    Java Threads: When using a thread pool (e.g., ExecutorService), a thread is assigned to a task and remains blocked during operations like network calls, holding onto OS resources (e.g., ~1MB stack) until the task completes. This wastes resources as the thread can’t be reused while waiting.
    
    Coroutines: Run on a thread pool (e.g., via Dispatchers.IO), but during a network call, the coroutine suspends (pauses) and releases the thread back to the pool, allowing other tasks to use it. When the network call finishes, the coroutine resumes on any available thread, using minimal memory (~few KB).


Analogy: Java threads are like chefs cooking one dish, waiting for the oven. Coroutines are chefs starting a dish, then cooking others while it bakes, using fewer chefs.


2. **What do you mean by Structured concurrency and how is it with scopes?**
   - **Answer**: Structured concurrency ensures coroutines complete within their scope, avoiding leaks. Scopes like `viewModelScope` tie tasks to lifecycle, cancel on destroy.

3. **What are delegates?**
   - **Answer**: Delegates in Kotlin delegate property behavior to another object (e.g., `by lazy`). Used in ViewModel for lazy initialization of data, saving resources.

4. **What is infix?**
   - **Answer**: Infix allows calling functions without dots/parentheses for readability (e.g., `1 shl 2`). Used in Kotlin for DSL-like syntax, like custom operators. 

5. **What is inline, outline, and crossinline (with example)?**
   - **Answer**: 
     - **Inline**: Copies function body at call site, reduces overhead (e.g., `inline fun log(block: () -> Unit) { block() }`).
     - **Outline**: Regular function, not inlined, separate call (e.g., `fun log(block: () -> Unit) { block() }`).
     - **Crossinline**: Prevents non-local returns in inline function’s lambda (e.g., `inline fun execute(crossinline block: () -> Unit) { launch { block() } }`).

        ### Example for Inline, Outline, Crossinline
        ```kotlin
        // Inline: Copies block at call site
        inline fun logInline(block: () -> Unit) { block() }

        // Outline: Regular function call
        fun logOutline(block: () -> Unit) { block() }

        // Crossinline: Prevents return in lambda
        inline fun execute(crossinline block: () -> Unit) {
            GlobalScope.launch { block() }
        }
        ```

1. **How does viewModelScope ensure structured concurrency?**

    **Answer:** viewModelScope ties coroutines to ViewModel lifecycle, auto-cancels on clear. Used for fetching data for LazyColumn.

1. **Why use structured concurrency in Android apps?**

    **Answer:** Prevents leaks, ensures tasks complete in scope. lifecycleScope cancels UI tasks on destroy, saving resources. 

1. **How do you handle cancellation in coroutine scopes?**

    **Answer:** Scopes like lifecycleScope auto-cancel on lifecycle events. Check isActive in suspend functions for cleanup. 

1. **What happens if a coroutine scope is not used properly?**

    **Answer:** Without scopes, coroutines may leak, running after UI destroy. viewModelScope prevents this for data tasks.

1. **val vs const val: Differences and usage scenarios**
   - **Answer**: `val` is immutable, set at runtime. `const val` is compile-time constant, only for primitives/top-level. Use `val` for variables, `const val` for constants like `API_KEY`. (116 chars)

2. **lateinit vs lazy: When to use each and how they work**
   - **Answer**: `lateinit`: For non-null vars, init later (e.g., ViewModel). `lazy`: Delays init until first access (e.g., cache). `lateinit` for DI, `lazy` for heavy objects. (114 chars)

3. **Lambda and higher-order functions: Benefits and examples**
   - **Answer**: Lambdas simplify code; higher-order functions take lambdas. Benefit: Concise event handling. E.g., `button.setOnClick { showToast() }` in Compose. (104 chars)

4. **object vs companion object: Usage and differences**
   - **Answer**: `object`: Singleton for one instance (e.g., `ApiClient`). `companion object`: Static-like in class (e.g., `Product.newInstance()`). Use for utilities vs. class-specific. (121 chars)

5. **Scope functions (let, apply, with, run): How they simplify code blocks**
   - **Answer**: Scope functions (`let`, `apply`, `with`, `run`) chain operations. E.g., `product?.let { updateUI(it) }` avoids null checks, simplifies ViewModel code. (108 chars)

6. **Extension functions: Implementing and utilizing effectively**
   - **Answer**: Add functions to classes (e.g., `fun String.formatPrice() = "$$this"`). Use for utilities, like formatting data for `LazyColumn` in Compose. (103 chars)

7. **== vs ===: Equality checks in Kotlin**
   - **Answer**: `==` checks value equality (e.g., `product1 == product2`). `===` checks reference equality (e.g., same object). Use `==` for data, `===` for identity. (108 chars)

8. **Sealed class vs enum: Differences and use cases**
   - **Answer**: Sealed class: Restricted hierarchy with data (e.g., `UiState.Success`). Enum: Fixed constants (e.g., `Status.ACTIVE`). Use sealed for UI states in MVVM. (111 chars)

9. **Inline functions: Reasons for using inline, crossinline, and noinline**
   - **Answer**: `inline`: Copies function body, reduces overhead (e.g., `inline fun log(block: () -> Unit)`). `crossinline`: Prevents lambda return. `noinline`: Avoids inlining. (113 chars)

10. **Synchronization in Kotlin: Understanding how it works**
    - **Answer**: Use `@Synchronized` or `synchronized` block for thread safety (e.g., `synchronized(lock) { cache.put(id, product) }`). Ensures safe access in multi-threaded apps. (117 chars)



# Differences Between Kotlin Scope Functions (`let`, `apply`, `with`, `run`)

## `let` ( Chef checks ingredient, cooks dish if available.)
- **What**: Runs block on non-null object, returns block’s result. Syntax: `obj?.let { it.property }`.
- **Why**: Simplifies null-safe ops, avoids null checks, improves readability.
- **Where**: Use in ViewModel for safe data handling or Compose for `LazyColumn` UI.
- **Analogy**: Chef checks if ingredient exists, cooks dish (result) only if there.

## `apply` (Chef sets up ingredient, serves it.)
- **What**: Configures object, returns object. Syntax: `obj.apply { this.property = value }`.
- **Why**: Groups property settings, reduces repetitive `obj.` calls, cleaner init.
- **Where**: Use in ViewModel to set up data or Compose to config UI components.
- **Analogy**: Factory worker sets machine’s properties, returns machine.

## `with` ( Chef makes dish with tool, serves dish.)
- **What**: Runs block on object, returns block’s result. Syntax: `with(obj) { property }`.
- **Why**: Simplifies ops on non-null object, avoids `this.` repeats, clear code.
- **Where**: Use in ViewModel for data transforms, less common in Compose.
- **Analogy**: Chef uses tool (object) to make dish, serves result (block output).

## `run` (Chef transforms ingredient into dish.)
- **What**: Runs block on object, returns result. Syntax: `obj.run { property }` or standalone.
- **Why**: Combines access and computation, concise for data transforms.
- **Where**: Use in ViewModel for computation or Compose for `LazyColumn` logic.
- **Analogy**: Chef transforms ingredient into dish (result) with recipe.

## Example
```kotlin
data class Product(val id: Int, var name: String, var price: Double)

val product: Product? = Product(1, "Phone", 999.99)
// let: Null-safe name update
product?.let { it.name = "New Phone"; updateUI(it) }
// apply: Configure product
product.apply { price = 899.99 }
// with: Transform data
with(product) { "$name: $$price" }
// run: Compute result
val display = product.run { "$name: $$price" }
```

“`let` for null-safe ops, `apply` configs objects, `with`/`run` compute results. Used in ViewModel for data or Compose for `LazyColumn`. Like chefs prepping dishes.”

## Sealed Class vs. Enum: Deep Dive

#### Sealed Class
- **What**: A restricted class hierarchy where all subclasses are defined within the file, allowing complex states with data (e.g., `sealed class UiState`).
- **Why**: Enables modeling complex states (e.g., `Success(data)`, `Error(message)`) with properties, ideal for dynamic UI or logic in MVVM.
- **Where**: Use in ViewModel for UI state management (e.g., `LazyColumn` data) or Clean Architecture use cases for result handling.
- **Analogy**: Like a chef’s menu with specific dishes (subclasses), each with unique ingredients (data), prepared in one kitchen (file).

#### Enum
- **What**: A fixed set of named constants (e.g., `enum class Status { ACTIVE, INACTIVE }`), typically without additional data.
- **Why**: Simplifies representing fixed, simple states without data, reducing complexity for predefined options.
- **Where**: Use for static states (e.g., user status in a repository) where no extra data or hierarchy is needed.
- **Analogy**: Like a restaurant’s fixed drink list (e.g., Coffee, Tea), simple and unchanging, no extra customization.

#### Key Differences
- **Data Flexibility**:
  - **Sealed Class**: Supports subclasses with properties (e.g., `Success(val data: List<Product>)`), ideal for dynamic data.
  - **Enum**: Limited to constants, no properties (e.g., `Status.ACTIVE`), less flexible.
- **Hierarchy**:
  - **Sealed Class**: Defines a closed hierarchy in one file, allowing nested subclasses for complex logic.
  - **Enum**: Flat list of values, no hierarchy or subclassing.
- **Use Case Fit**:
  - **Sealed Class**: Best for modeling states with data or behavior (e.g., UI states in MVVM).
  - **Enum**: Best for simple, predefined states without data (e.g., status flags).
- **Type Safety**:
  - Both provide type safety, but sealed classes allow `when` expressions to be exhaustive without an `else` clause when all subclasses are covered.

#### When to Use Which and Why
- **Use Sealed Class**:
  - **When**: You need states with associated data or complex logic (e.g., UI states like `Loading`, `Success`, `Error` in MVVM).
  - **Why**: Offers flexibility to carry data (e.g., `Success(val products: List<Product>)`) and supports exhaustive `when` checks for safety.
  - **Example**: In a ViewModel, manage `LazyColumn` states: `sealed class UiState { object Loading; data class Success(val data: List<Product>) }`.
- **Use Enum**:
  - **When**: You need a fixed set of simple, constant states without data (e.g., user roles, status).
  - **Why**: Simpler, less overhead for static options, easier to maintain for predefined values.
  - **Example**: In a repository, define `enum class UserStatus { ACTIVE, INACTIVE }` for user state.

**Interview Answer**: “Sealed classes model complex states with data, like `UiState.Success` for `LazyColumn`. Enums are for fixed constants like `Status.ACTIVE`. Use sealed for MVVM states, enums for simple flags.” 

### Example
```kotlin
// Sealed Class for UI state in MVVM
sealed class UiState {
    object Loading : UiState()
    data class Success(val products: List<Product>) : UiState()
    data class Error(val message: String) : UiState()
}

// Enum for fixed states
enum class UserStatus { ACTIVE, INACTIVE }

// Usage in ViewModel
class ProductViewModel {
    val state = MutableStateFlow<UiState>(UiState.Loading)
    fun fetchProducts() {
        // Use sealed class for dynamic states
        state.value = UiState.Success(listOf(Product(1, "Phone", 999.99)))
    }
}

// Usage in Repository
fun isUserActive(status: UserStatus): Boolean {
    return status == UserStatus.ACTIVE // Enum for simple check
}
```

**Analogy for Both**: In a restaurant:
- **Sealed Class**: A chef’s special menu with dishes (states) like “Pasta with Sauce” (data), customized per order, prepared in one kitchen.
- **Enum**: A fixed drink menu (e.g., Coffee, Tea), no customization, just pick one.


## Inline vs NoInline vs CrossInline

Imagine a **construction site** building a house (your app’s code):
- **Noinline**: Like ordering a pre-made wall (normal function, like in Java/Kotlin). Workers fetch it from a factory (separate call), which is slower but keeps the house plan simple and small. Example: A regular function call in a ViewModel.
- **Inline**: Instead of fetching a wall, workers copy the wall’s instructions (function’s code) into the house plan (call site). It’s faster because there’s no fetching, but the plan gets bigger (bulkier code). Example: Copying a small function’s code to speed up a `LazyColumn` update.
- **Crossinline**: Workers copy the wall’s instructions (inline), but they lock the instructions so they can’t be used outside the house (no non-local returns, like `return` jumping out of the function). The instructions are still copied from the function (not from “outside”), but they’re restricted to stay inside the house’s plan. Example: Using a lambda in a ViewModel but ensuring it doesn’t exit early.

| **Concept** | **What** | **Why** | **Where** | **Analogy** |
|-------------|----------|---------|-----------|-------------|
| **Inline** | Copies function’s code at call site. Syntax: `inline fun log(block: () -> Unit)`. | Reduces call overhead, speeds up small functions. | Use in ViewModel for small utilities or Compose for `LazyColumn` logic. | Workers copy wall’s blueprint into house plan, faster but bulkier. |
| **Crossinline** | Inline with restricted lambda, no non-local returns. Syntax: `inline fun execute(crossinline block: () -> Unit)`. | Prevents lambda from exiting outer function, ensures safety. | Use in ViewModel with coroutines for safe lambdas. | Workers lock blueprint to stay in house plan, no early exit. |
| **Noinline** | Prevents lambda inlining in inline function. Syntax: `inline fun log(noinline block: () -> Unit)`. | Keeps code light, avoids inlining large lambdas. | Use in complex ViewModel logic to reduce code size. | Workers fetch pre-made wall, slower but simpler plan. |

### Example
```kotlin
// Inline: Copies block into call site
inline fun logInline(block: () -> Unit) { block() }

// Crossinline: Prevents non-local return in coroutine
inline fun execute(crossinline block: () -> Unit) {
    viewModelScope.launch { block() }
}

// Noinline: Avoids inlining lambda
inline fun logComplex(noinline block: () -> Unit) { block() }
```

**Sample Interview Answer**: “Inline copies code for speed, crossinline blocks early returns, noinline keeps code light. Used in ViewModel for `LazyColumn` logic, like a construction site.”