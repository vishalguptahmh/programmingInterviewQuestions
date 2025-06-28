# Android Questions with Answers

## Questions

###  Explain how dependency injection works in Android. What are the benefits of using Hilt over Dagger 2?

**Definition of Dependency Injection (DI):** It's more than just a "tool"; it's a design pattern where dependencies are provided rather than created inside a class.

**Why use DI?:** Improves modularity, testability, and separation of concerns.

Benefits of Hilt over Dagger:

- Less boilerplate (e.g., no need to write component interfaces).
- Predefined scopes (@ActivityScoped, @ViewModelScoped, etc.).
- Integrated with Jetpack components out of the box.
- Better error messages and lifecycle management.

Annotation support: Hilt provides prebuilt annotations that simplify DI setup in Activities, Fragments, ViewModels, Services, etc.

| Feature             | Dagger 2                               | Hilt                                        |
| ------------------- | -------------------------------------- | ------------------------------------------- |
| Boilerplate         | High — manual components/modules setup | Low — automated components & modules        |
| Lifecycle support   | Manual                                 | Built-in with lifecycle-aware scopes        |
| Jetpack integration | Manual setup                           | Built-in support for ViewModel, WorkManager |
| Learning curve      | Steep                                  | Beginner-friendly                           |
| Testing support     | Manual configuration                   | Simplified with `@TestInstallIn`, etc.      |

**Use Case:**
Use Hilt for Android apps unless you need the fine-grained control Dagger offers or you're working on a multi-platform project.




### Can you describe how the ViewModel survives configuration changes and how it's scoped?

ViewModel and Lifecycle: It's not tied to the Activity or Fragment lifecycle directly but is scoped to them through ViewModelProvider. It doesn’t get destroyed unless explicitly cleared, which happens when the associated Activity or Fragment is permanently destroyed.

ViewModel lifecycle: The ViewModel is created and scoped to the Activity/Fragment, and it’s automatically cleared when the lifecycle is permanently destroyed (like when the user navigates away or the app is killed).

Clearing ViewModel: ViewModel has an onCleared() method that’s called when the scope is destroyed.

ViewModel and SavedStateHandle: For data persistence across configuration changes, you can use SavedStateHandle to store state in a ViewModel.

| Aspect       | ViewModel                                                                  |
| ------------ | -------------------------------------------------------------------------- |
| Lifecycle    | Scoped to `Activity`/`Fragment` (using `ViewModelProvider`)                |
| Persistence  | Survives configuration changes (rotation, etc.)                            |
| Clearing     | Automatically cleared when `Activity`/`Fragment` is permanently destroyed  |
| Saving State | Can use `SavedStateHandle` to store and restore data across config changes |




### 4. What’s your approach to managing app modularization in a large-scale Android project?

| Aspect       | ViewModel                                                                  |
| ------------ | -------------------------------------------------------------------------- |
| Lifecycle    | Scoped to `Activity`/`Fragment` (using `ViewModelProvider`)                |
| Persistence  | Survives configuration changes (rotation, etc.)                            |
| Clearing     | Automatically cleared when `Activity`/`Fragment` is permanently destroyed  |
| Saving State | Can use `SavedStateHandle` to store and restore data across config changes |



### 5. Describe how Jetpack Compose handles recomposition. What causes recomposition and how can it be optimized?

#### How Recomposition Works in Jetpack Compose:

Recomposition is the process where Compose re-executes the code for a composable function when its state or data changes. It only re-renders parts of the UI that depend on the changed data, making it more efficient than traditional UI frameworks.

#### When does recomposition happen?

- State changes: When a State or MutableState changes (e.g., a button click updates the state).

- StateHoisting: When a state is hoisted (passed as a parameter), any change in the parent component triggers a recomposition.

- remember vs rememberSaveable: remember keeps values during recomposition, while rememberSaveable persists them across process death (e.g., screen rotation).

#### Optimizing Recomposition:

- Use remember: Avoid recomposing values unnecessarily by remembering them.

- Keying: Use key() when creating dynamic lists or items to help Compose understand when to trigger a recomposition.

- Avoid unnecessary state: If state is scoped too broadly, recomposition might be triggered unnecessarily.

- derivedStateOf: For computations derived from state, use derivedStateOf to avoid recomputing values on every recomposition.



| Concept            | Explanation                                                             |
| ------------------ | ----------------------------------------------------------------------- |
| Recomposition      | Triggered when state or data changes, re-executes composable functions  |
| `remember`         | Stores values across recompositions to avoid unnecessary recalculations |
| `rememberSaveable` | Saves values across process death (e.g., screen rotation)               |
| Optimizations      | Minimize state hoisting, use `derivedStateOf`, key items for lists      |




### 8. Explain how WorkManager differs from JobScheduler, AlarmManager, and Services.
| Component        | Purpose                                              | API Level |
| ---------------- | ---------------------------------------------------- | --------- |
| **WorkManager**  | Background deferrable tasks (even after reboot)      | 14+       |
| **JobScheduler** | Schedule jobs with conditions (charging, idle, etc.) | 21+       |
| **AlarmManager** | Trigger actions at exact times (clock/time-based)    | 1+        |
| **Service**      | Run tasks without UI (foreground or background)      | 1+        |

✅ Use WorkManager for reliable deferrable tasks (e.g., upload logs).

✅ Use AlarmManager for exact-time events (e.g., daily reminder).

✅ Use Services for ongoing tasks (e.g., music player).

✅ Avoid JobScheduler unless targeting only Android 5.0+.



### 9. How would you secure sensitive data (like tokens) in an Android application?

##### Tools & Techniques:

- EncryptedSharedPreferences (Jetpack Security)
- SQLCipher for encrypted Room DB
- KeyStore/TEE (Trusted Execution Environment)
- Use MasterKey to encrypt symmetric keys for preferences/db
- Stores encryption keys in hardware-backed security

```kotlin
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val sharedPreferences = EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)

```


### 10. What steps do you take when diagnosing a memory leak in an Android app?

##### Use Android Studio Memory Profiler:

- Run the app with profiling.
- Monitor heap size and object allocations.
- Take heap dumps to analyze what objects are still in memory and why.

##### LeakCanary:

- Automatically detects and reports memory leaks.

- Shows a reference chain to the leaking object.

- Best used in debug builds.

##### Common leak causes to investigate:

- Context leaks (e.g., holding onto Activity/Fragment reference in a static field).

- Unregistered listeners/broadcast receivers.

- Long-lived singleton or retained objects.

- Non-cancelled coroutines or RxJava subscriptions.

- Manual heap dump analysis (if needed) using MAT (Memory Analyzer Tool) for large-scale inspection.

| Tool/Step               | Purpose                                                    |
| ----------------------- | ---------------------------------------------------------- |
| Android Studio Profiler | Visualize memory usage, take heap dumps                    |
| LeakCanary              | Detect leaks with reference chains at runtime              |
| Common leak sources     | Static references, retained `Activity`, unclosed resources |
| Best practices          | Weak references, cancel coroutines, unregister listeners   |



### 11. Describe how the Navigation Component simplifies fragment transactions. What are its limitations?

✅ What It Solves:

- No more manual FragmentTransactions
- Type-safe argument passing (with SafeArgs)
- Built-in back stack and deep link handling

🧱 Key Features:
- NavController, NavHostFragment
- Handles up/back actions automatically
- Integrates with Jetpack libraries

❌ Limitations:
- Learning curve
- Navigation Graph file can get large
- Difficult for highly dynamic navigation

### 12. How do you implement offline-first architecture in your app?
Offline-first architecture ensures the app works without internet and syncs data later.

#### Key components:

- Local cache/database (e.g., Room)
- Repository pattern to coordinate local/remote sources
- NetworkBoundResource pattern or manual logic:
    - Load from DB
    - Emit loading
    - Fetch remote (if needed)
    - Save result to DB
    - Emit updated data from DB

🧱 Tools/Techniques:

- Room + Retrofit + Kotlin Flow
- WorkManager for syncing changes in background
- Detect connectivity via ConnectivityManager



### 13. In Compose, what are remember, rememberSaveable, and derivedStateOf used for?

| Function           | Purpose                                               |
| ------------------ | ----------------------------------------------------- |
| `remember`         | Caches value during recomposition                     |
| `rememberSaveable` | Same as `remember` but survives configuration changes |
| `derivedStateOf`   | Memoizes derived/calculated state for performance     |

```kotlin
val count = remember { mutableStateOf(0) }
val username = rememberSaveable { mutableStateOf("") }
val isUserAdult = derivedStateOf { user.age >= 18 }

```


### 14. How do you write and organize unit tests for your repository layer?

- Use a mocking framework like MockK or Mockito to mock data sources (e.g., DAO, API).
- Use JUnit + Coroutine Test Library:
    - runTest, TestDispatcher, etc.

```kotlin
//example
RepositoryTest/
├── getUserFromNetwork_success()
├── getUserFromDb_whenOffline()
├── syncData_fails_gracefully()

```
Tools:
- JUnit 5
- MockK
- Coroutine Test
- Truth/Hamcrest/AssertJ

### 15  How would you structure your architecture (layers/modules/patterns) in a large-scale Android application?

✅ Recommended App Layer Structure:

📦 Presentation Layer:
- UI (Compose or XML)
- ViewModels for business/UI logic

📦 Domain Layer (Optional but highly recommended in clean architecture):

- Use cases (business rules)
- Interfaces for repository/data access

📦 Data Layer:

- Repositories

- Network sources (Retrofit, etc.)

- Local sources (Room DB, etc.)

✅ Modularization Tips:

- Split modules by features: login, profile, payments
- Create core/shared modules: core-ui, core-network, core-database
- Use Gradle’s api vs implementation smartly to reduce build times


| Pattern    | Where Used                         |
| ---------- | ---------------------------------- |
| Singleton  | For shared instances (e.g., DI)    |
| Factory    | For ViewModel or object creation   |
| Repository | For separating data sources        |
| Observer   | For reacting to data/state changes |

| Layer             | Responsibility                               |
| ----------------- | -------------------------------------------- |
| Presentation      | UI logic (Activities, Fragments, ViewModels) |
| Domain (optional) | Business rules, UseCases                     |
| Data              | Local/remote data sources, Repositories      |

or 

| Module Type     | Purpose                                               |
| --------------- | ----------------------------------------------------- |
| `app`           | Main application entry point.                         |
| `core`          | Shared utility classes (network, DI setup, database). |
| `feature-*`     | Independent features (e.g., login, onboarding).       |
| `domain`        | Business logic, UseCases, interfaces.                 |
| `data`          | API clients, repositories, local DB logic.            |
| `design-system` | Shared UI components and themes.                      |

### 🚀 Why Modularization Matters 

Modularization is key for scaling Android apps, isolating features, boosting build performance, and enabling team collaboration. Interviewers expect you to not only know how to split a project into modules but also why and how each type of module interacts.

🧱 What Is Modularization?

Modularization is the process of breaking a monolithic Android app into smaller, independent modules that can be developed, tested, and maintained separately.

🧱 Separation of Concerns

- Use interface contracts between modules to reduce tight coupling.
- Feature modules should not directly depend on each other.
- Keep core modules framework-agnostic (no Android SDK if possible).


🧱 example : 
```kotlin
MyBankApp/
│
├── app/                         → Main app module
├── core/
│   ├── core-network/           → Retrofit, API clients
│   ├── core-database/          → Room, DB setup
│   └── core-utils/             → Shared helpers, extensions
│
├── domain/                     → UseCases, interfaces (no Android code)
│
├── data/                       → Repositories, model mappers
│
├── feature-login/             → Login feature (UI + ViewModel)
├── feature-dashboard/         → Dashboard feature (UI + ViewModel)
│
├── design-system/             → Shared UI components, theme, icons
└── buildSrc/                  → Centralized dependency versions
```


settings.gradle.kts

```kotlin
include(":app", ":core:core-network", 
                ":core:core-database", 
                ":core:core-utils",
                ":domain", ":data",
                ":feature-login", 
                ":feature-dashboard",
                ":design-system")

```

feature-login Gradle Setup (build.gradle.kts)

```kotlin
dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core:core-utils"))
    implementation(project(":design-system"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.lifecycle.viewmodel)
}

```

🧱 Inter-Module Communication

Use interfaces in domain and data to keep feature modules decoupled.

Step 1: Define contract in domain
```kotlin
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}

```

Step 2: Implement in data

```kotlin
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<User> {
        return safeApiCall { api.login(username, password) }
    }
}
```

Step 3: Use in feature-login
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun login(username: String, password: String) = viewModelScope.launch {
        val result = authRepository.login(username, password)
        // Handle result
    }
}

```
✅ Testing a Feature Module in Isolation
- Use mockk or Mockito to mock AuthRepository.
- Only test the feature module's UI and ViewModel.
- Integration tests can be written in data module.


# Common Interview Questions with Answers

1. **How would you design an Android app for scalability?**
    
    **Answer:** Use MVVM with Clean Architecture, modular layers (Presentation, Domain, Data). Cache data with HashMap, paginate LazyColumn.

2. How do you optimize an Android app’s performance?
    
    **Answer:** Use coroutines for async tasks, cache in HashMap, monitor with Profiler. Paginate LazyColumn for lists, as I did in an e-commerce app.

3. **How do you ensure modularity in an Android app?**

    **Answer:** Apply Clean Architecture, use Hilt for DI. Separate use cases, repositories, and Compose UI for loose coupling, easy testing.

1. **How would you handle offline data in an Android app?**

    **Answer:** Use Room for local storage, sync with API via repository. Show cached data in LazyColumn when offline, update when online.

1. **How do you design a robust data flow in an Android app?**

    **Answer:** Unidirectional flow: API → Repository → Use Case → ViewModel → Compose. Use StateFlow for reactive UI updates.