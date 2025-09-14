# Jetpack Compose

- **Definition**: Jetpack Compose is Android’s modern, declarative UI toolkit for building native user interfaces, replacing XML-based layouts with Kotlin-based composable functions.

🔄 Declarative vs Imperative

- Imperative (XML): You tell how to build the UI step by step.
- Declarative (Compose): You describe what the UI should look like for a given state.

- **Key Characteristics**:
  - **Declarative**: UI is defined as `@Composable` functions that react to state changes.
  - **Reactive**: State-driven recomposition updates only affected UI components.
  - **Component-Based**: Modular, reusable composables for complex UIs.
  - **Jetpack Integration**: Seamlessly works with ViewModel, LiveData, StateFlow, Navigation, and Room.


## Core Concepts
1. ✅  **Composables**:
   - Functions annotated with `@Composable` that define UI elements (e.g., `Text`, `Button`, `LazyColumn`).
   - **Key Points**:
     - Composable lifecycle is tied to recomposition, triggered by state changes.
     - Use `Modifier` for styling, layout, and behavior (e.g., `Modifier.padding()`, `Modifier.clickable()`).
   - **Example**:
    ```kotlin
    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello, $name!")
    }
    ```

2. ✅ **State Management**: in Compose
   - **Local State**: Use `mutableStateOf` and `remember` for UI-driven state.
   - **ViewModel State**: Use `StateFlow` or `LiveData` for data-driven state, observed with `collectAsState()` or `observeAsState()`.
   - **Recomposition**: Only composables reading changed state are recomposed.
   - **Example**:
     ```kotlin
     @Composable
     fun Counter() {
         var count by remember { mutableStateOf(0) }
         Button(onClick = { count++ }) {
             Text("Count: $count")
         }
     }
     ```

3. ✅ Basic **Layouts**:

   - **Basic**: `Column`, `Row`, `Box` for simple arrangements.
   - **Advanced**: `ConstraintLayout` for complex positioning, `LazyColumn`/`LazyRow` for efficient lists.
   - **Example**:
     ```kotlin
     @Composable
     fun ProductList(products: List<Product>) {
         LazyColumn {
             items(products, key = { it.id }) { product ->
                 ProductCard(product = product, onClick = { /* Navigate */ })
             }
         }
     }
     ```

    | Layout       | Purpose                                 |
    | ------------ | --------------------------------------- |
    | `Column`     | Vertically stack children               |
    | `Row`        | Horizontally stack children             |
    | `Box`        | Overlapping children (like FrameLayout) |
    | `LazyColumn` | RecyclerView alternative                |
    | `Modifier`   | Used for padding, size, click, etc.     |


1. ✅ Recomposition
    - When state changes, the function recomposes and re-renders affected parts of the UI.
    - Compose tracks what changed and only updates that part.


1. ✅ Theming and Styling

    - Use `MaterialTheme` for consistent colors, typography, and shapes.
    - Customize with `CompositionLocalProvider` for dynamic theming.
    
    Compose uses MaterialTheme:

    ```kotlin
    MaterialTheme {
        Text("Hello", style = MaterialTheme.typography.h6)
    }

    ```
    Colors, typography, and shapes can be customized globally via Theme.kt.

1. ✅ **Navigation** in Compose
   - Use `NavHost` and `NavController` for screen navigation, supporting deep links and type-safe arguments.
   - **Example**:
     ```kotlin
     @Composable
     fun AppNavigation() {
         val navController = rememberNavController()
         NavHost(navController, startDestination = "home") {
             composable("home") { HomeScreen(navController) }
             composable("details/{id}") { backStackEntry ->
                 DetailsScreen(id = backStackEntry.arguments?.getString("id"))
             }
         }
     }
     ```

1. **Animation**:
   - APIs like `AnimatedVisibility`, `animateContentSize`, `animateFloatAsState` for smooth transitions.
   - **Example**:
     ```kotlin
     @Composable
     fun ExpandableCard(isExpanded: Boolean) {
         val size by animateDpAsState(if (isExpanded) 200.dp else 100.dp)
         Card(modifier = Modifier.size(size)) {
             Text("Expandable Content")
         }
     }
     ```

1. ✅ Side Effects in Compose

    - **Definition**: Side effects are operations that interact with the outside world (e.g., API calls, navigation, toasts) and should be controlled to avoid unexpected behavior during recomposition.

    - **Key APIs**:
        1. **LaunchedEffect**:
            - Runs a coroutine-scoped side effect when a composable enters the composition or when specific keys change.
            - **Use Case**: Fetch data or trigger navigation on initial composition or key change.
            - **Example**:
                ```kotlin
                @Composable
                fun ProfileScreen(viewModel: ProfileViewModel, userId: String) {
                    LaunchedEffect(userId) {
                        viewModel.fetchProfile(userId)
                    }
                    val profile by viewModel.profile.collectAsState()
                    Text(profile?.name ?: "Loading...")
                }
                ```
        2. **DisposableEffect**:
            -  "Do something when I'm on screen, and undo it when I’m gone."
            - Manages side effects with cleanup (e.g., registering/unregistering listeners).
            - **Use Case**: Start/stop a service when a composable enters/leaves composition.
            - **Example**:
                ```kotlin
                @Composable
                fun LocationTracker() {
                    DisposableEffect(Unit) {
                        val tracker = LocationTracker().apply { start() }
                        onDispose { tracker.stop() }
                    }
                }
                ```
        3. **SideEffect**:
            - Executes non-coroutine side effects on every recomposition (use cautiously).
            - **Use Case**: Logging or analytics.
            - **Example**:
                ```kotlin
                @Composable
                fun AnalyticsScreen() {
                    SideEffect {
                        Analytics.logEvent("ScreenViewed")
                    }
                    Text("Analytics Tracked")
                }
                ```
        4. **rememberCoroutineScope**:
            - Provides a coroutine scope tied to the composable’s lifecycle for launching coroutines outside `LaunchedEffect`.
            - **Use Case**: Trigger side effects on user actions (e.g., button clicks).
            - **Example**:
                ```kotlin
                @Composable
                fun ToastButton() {
                    val scope = rememberCoroutineScope()
                    Button(onClick = {
                        scope.launch { Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show() }
                    }) {
                        Text("Show Toast")
                    }
                }
                ```
        5. **produceState**:
            - Converts non-Compose reactive streams (e.g., Flow) into Compose state.
            - **Use Case**: Fetch data reactively from a Flow.
            - **Example**:
                ```kotlin
                @Composable
                fun ProductList(viewModel: ProductViewModel) {
                    val products by produceState<List<Product>>(initialValue = emptyList()) {
                        viewModel.products.collect { value = it }
                    }
                    LazyColumn {
                        items(products) { product -> Text(product.name) }
                    }
                }
                ```

    - Use LaunchedEffect, SideEffect, or rememberCoroutineScope for side effects.
        ```kotlin
        LaunchedEffect(Unit) {
            // Called once on composition
            fetchData()
        }
        ```

        | Question                           | Answer Hint                                  |
        | ---------------------------------- | -------------------------------------------- |
        | What is `@Composable`?             | Marks a function that defines UI             |
        | How does state management work?    | `remember`, `mutableStateOf`, `State<T>`     |
        | What is recomposition?             | UI re-renders when observable state changes  |
        | How is navigation handled?         | `NavController`, `NavHost`, `composable`     |
        | How does Compose compare with XML? | Declarative vs imperative                    |
        | How do you test Compose UI?        | `ComposeTestRule`, semantics, assertions     |
        | Can you use ViewModel in Compose?  | Yes, with `hiltViewModel()` or `viewModel()` |
        | What is `rememberSaveable`?        | Like `remember`, but survives config changes |



    - **Custom Modifiers**:
        - Create reusable modifiers for consistent styling.
        - **Example**:
            ```kotlin
            fun Modifier.customBorder() = this.then(
                Modifier.border(2.dp, Color.Black).padding(8.dp)
            )

            @Composable
            fun BorderedText(text: String) {
                Text(text, modifier = Modifier.customBorder())
            }
            ```

## CompositionLocal
- **Definition**: Provides implicit data passing through the composition tree (e.g., for theming, context).
- **Use Case**: Access app-wide resources like `LocalContext` or custom providers.
- **Example**:
  ```kotlin
  val LocalAppConfig = compositionLocalOf { AppConfig() }

  @Composable
  fun MyApp() {
      CompositionLocalProvider(LocalAppConfig provides AppConfig(isDarkTheme = true)) {
          Content()
      }
  }

  @Composable
  fun Content() {
      val config = LocalAppConfig.current
      Text("Dark Theme: ${config.isDarkTheme}")
  }
  ```

## Testing in Depth
- **Tools**: Use `ComposeTestRule` with `createComposeRule()` for UI tests.
- **Testing Scenarios**:
  - Verify UI rendering (`onNodeWithText`).
  - Test user interactions (`performClick`, `performTextInput`).
  - Validate state changes and side effects.
- **Example**:
  ```kotlin
  @Test
  fun testSearchBar() {
      composeTestRule.setContent {
          SearchBar(query = "", onQueryChange = {})
      }
      composeTestRule.onNodeWithText("Search").assertExists()
      composeTestRule.onNodeWithTag("searchField").performTextInput("phone")
      composeTestRule.onNodeWithText("phone").assertExists()
  }
  ```

## Handling Complex UI States
- **State Management**: Use sealed classes for UI states (e.g., Loading, Success, Error) with ViewModel.
- **Example**:
  ```kotlin
  sealed class UiState<out T> {
      object Loading : UiState<Nothing>()
      data class Success<T>(val data: T) : UiState<T>()
      data class Error(val message: String) : UiState<Nothing>()
  }

  class ProfileViewModel(private val useCase: GetProfileUseCase) : ViewModel() {
      private val _uiState = MutableStateFlow<UiState<User>>(UiState.Loading)
      val uiState: StateFlow<UiState<User>> = _uiState.asStateFlow()

      fun fetchProfile(userId: String) {
          viewModelScope.launch {
              _uiState.value = UiState.Loading
              useCase(userId).onSuccess {
                  _uiState.value = UiState.Success(it)
              }.onFailure {
                  _uiState.value = UiState.Error(it.message ?: "Error")
              }
          }
      }
  }

  @Composable
  fun ProfileScreen(viewModel: ProfileViewModel, userId: String) {
      LaunchedEffect(userId) { viewModel.fetchProfile(userId) }
      val state by viewModel.uiState.collectAsState()
      when (state) {
          is UiState.Loading -> CircularProgressIndicator()
          is UiState.Success -> Text((state as UiState.Success<User>).data.name)
          is UiState.Error -> Text((state as UiState.Error).message)
      }
  }
  ```

## Performance Optimization
- **Techniques**:
  - Use `remember` to cache expensive objects.
  - Add `key` to `LazyColumn`/`LazyRow` for efficient list updates (as you noted with `LazyColumn`).
  - Hoist state to avoid unnecessary recompositions.
  - Use Android Studio’s **Profiler** (as you mentioned) to monitor recompositions and memory.
- **Example**:
  ```kotlin
  @Composable
  fun OptimizedList(products: List<Product>) {
      LazyColumn {
          items(products, key = { it.id }) { product ->
              Text(
                  product.name,
                  modifier = Modifier.remember { Modifier.padding(8.dp) }
              )
          }
      }
  }
  ```

## Accessibility
- Use `semantics` modifier for screen reader support.
- **Example**:
  ```kotlin
  @Composable
  fun AccessibleButton(onClick: () -> Unit) {
      Button(
          onClick = onClick,
          modifier = Modifier.semantics { contentDescription = "Submit button" }
      ) {
          Text("Submit")
      }
  }
  ```

## Integration with MVVM
- **ViewModel**: Exposes data via `StateFlow` or `LiveData`, observed in composables.
- **Example**:
  ```kotlin
  class ProductViewModel(private val useCase: GetProductsUseCase) : ViewModel() {
      private val _products = MutableStateFlow<List<Product>>(emptyList())
      val products: StateFlow<List<Product>> = _products.asStateFlow()

      fun fetchProducts() {
          viewModelScope.launch {
              _products.value = useCase()
          }
      }
  }

  @Composable
  fun ProductScreen(viewModel: ProductViewModel) {
      val products by viewModel.products.collectAsState()
      ProductList(products = products)
  }
  ```

## Common Interview Questions
1. **“How does Jetpack Compose differ from traditional View-based systems?”**
   - **Answer**: “Compose is declarative, using Kotlin composables to describe UI as a function of state, unlike View-based systems’ XML and imperative updates. For a product list, I used `LazyColumn` in Compose, eliminating RecyclerView’s adapter, reducing code significantly.”
2. **“How do you handle side effects in Jetpack Compose?”**
   - **Answer**: “I use `LaunchedEffect` for one-time data fetching, `rememberCoroutineScope` for user actions like toasts, and `DisposableEffect` for resource management. In a project, I used `LaunchedEffect` to fetch cart data on screen load.”
3. **“How do you optimize Compose performance?”**
   - **Answer**: “I use `remember`, `key` in `LazyColumn`, and state hoisting. In a list feature, I used Profiler to reduce recompositions by adding `key = { item.id }`, improving performance.”
4. **“How do you manage complex UI states in Compose?”**
   - **Answer**: “I use sealed classes (`Loading`, `Success`, `Error`) with `StateFlow` in ViewModel. For a profile screen, I displayed a loading indicator, data, or error based on state, ensuring a robust UX.”
5. **“How does the Modifier system work?”**
   - **Answer**: “Modifiers customize composables in order (e.g., `padding`, `clickable`). I created a `customBorder` modifier for consistent styling across buttons in an app.”
6. **"Why Should i choose Compose instead of XML?"**
    - **Answer**: 
    1. **Declarative UI**:
    - Compose uses Kotlin-based `@Composable` functions to describe UI as a function of state, making it intuitive and reducing imperative boilerplate compared to XML’s static layouts.
    - Example: A dynamic UI updates automatically when state changes, unlike XML requiring manual View updates.

    2. **Less Boilerplate**:
    - Eliminates XML layouts, adapters, and view holders. For example, you mentioned using `LazyColumn` to replace RecyclerView, significantly reducing code for lists.
    - Example: A list in Compose requires just `LazyColumn` and `items`, vs. XML’s RecyclerView, adapter, and view holder classes.

    3. **Type Safety**:
    - Kotlin-based Compose avoids XML’s string-based errors (e.g., mismatched IDs), catching issues at compile time.
    - Example: `Modifier.clickable()` is type-safe, unlike XML’s `android:onClick` string references.

    4. **Reactive UI Updates**:
    - State-driven recomposition (using `StateFlow` or `LiveData`) automatically updates UI, unlike XML’s manual `findViewById` and View updates.
    - Example: Observing a ViewModel’s `StateFlow` in Compose updates the UI without manual calls.

    5. **Faster Development**:
    - `@Preview` annotation enables real-time UI previews in Android Studio, speeding up iteration compared to XML’s slower design preview.
    - Example: Previewing a `ProductCard` composable instantly vs. running the app to test XML layouts.

    6. **Simplified List Implementation**:
    - As you noted, `LazyColumn` simplifies list creation compared to RecyclerView’s complex setup (adapters, view holders, XML layouts).
    - Example: `LazyColumn { items(products) { Text(it.name) } }` vs. multiple classes for RecyclerView.

    7. **Better Maintainability**:
    - Composable functions are modular and reusable, making UI code easier to refactor than XML’s nested layouts.
    - Example: A reusable `ProductCard` composable can be shared across screens, unlike XML layouts duplicated per View.

    8. **Seamless Jetpack Integration**:
    - Compose integrates natively with ViewModel, Navigation, and StateFlow, aligning with modern Android architecture, unlike XML’s older View system.
    - Example: Observing `StateFlow` in a composable is simpler than updating XML Views via ViewModel.

    9. **Animation Support**:
    - Built-in APIs like `AnimatedVisibility` and `animateFloatAsState` simplify animations compared to XML’s View animations or Property Animators.
    - Example: Animating a card’s size with `animateDpAsState` is more straightforward than XML’s AnimatorSet.

    10. **Improved Testing**:
        - Compose’s `ComposeTestRule` enables easier UI testing (e.g., `onNodeWithText`) compared to XML’s Espresso tests, which require complex View interactions.
        - Example: Testing a button click in Compose is simpler with `performClick()` vs. Espresso’s View matchers.

    ## Drawbacks of XML (Why Compose is Preferred)
    1. **Verbose Setup**:
    - XML requires separate layout files, View binding, and manual updates, increasing complexity (e.g., RecyclerView setup vs. `LazyColumn`).
    2. **Error-Prone**:
    - String-based references (e.g., `findViewById`, `android:id`) lead to runtime errors, unlike Compose’s compile-time safety.
    3. **Limited Reactivity**:
    - XML Views require manual updates (e.g., `setText()`), whereas Compose reacts to state changes automatically.
    4. **Complex Animations**:
    - View-based animations require verbose XML or code (e.g., Animator resources), while Compose simplifies with declarative APIs.
    5. **Legacy Approach**:
    - XML is tied to older Android patterns, less aligned with Jetpack and modern development compared to Compose.
## Practice Question
You’re building a shopping cart screen in Jetpack Compose that displays a list of cart items, a total price, and a checkout button. Describe how you would implement it, including:
- The composable structure (e.g., `LazyColumn`, `Text`, `Button`).
- How you’d handle side effects (e.g., fetching cart data, navigating on checkout).
- How you’d manage state (e.g., ViewModel with `StateFlow`, sealed UI states).
- Any performance optimizations, accessibility, or testing considerations.
Provide a brief code snippet to illustrate your approach.

- Explain when to use each side effect API and why
**Sample Answer**: “I use `LaunchedEffect` for one-time data fetching, like loading a user profile when a screen loads. For user actions like showing a toast, I use `rememberCoroutineScope` to launch coroutines on button clicks. In a recent project, I used `DisposableEffect` to manage a location service, ensuring cleanup when the composable was removed.”