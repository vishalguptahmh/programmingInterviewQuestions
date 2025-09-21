# Overview of Clean Architecture
- **Definition**:

    **What-** Clean Architecture is a design philosophy 
    
    **Does-** that separates concerns into layers (Presentation, Domain, Data) 
    
    **Why-** to create modular, testable, and maintainable code, emphasizing independence from frameworks and external systems.
- **Core Principles**:
  - **Separation of Concerns**: Each layer has a specific responsibility, reducing coupling.
  - **Dependency Rule:** All concrete dependencies point inward.
  - **Testability**: Business logic (Domain) is isolated, making it easy to unit test.
  - **Framework Independence**: Code is decoupled from UI, database, or network frameworks.

![Clean Architecture](../images/CleanArchitecture.jpg)

## Layers in Clean Architecture
1. **Presentation Layer**:
   - Handles UI and user interactions (e.g., Activities, Fragments, Jetpack Compose).
   - Integrates with MVVM’s View and ViewModel .
   - **Example**: A `ProductScreen` composable displaying a product list.
2. **Domain Layer**:
   - Contains business logic, implemented as **use cases** (or Interactors)
   - Pure Kotlin, no Android dependencies, highly testable.
   - **Example**: `GetProductsUseCase` to fetch and validate product data.
3. **Data Layer**:
   - Manages data sources (e.g., API, Room database) and **mappers**  to convert API models to domain models.
   - Includes repositories that abstract data access for the Domain layer.
   - **Example**: `ProductRepository` fetching data from Retrofit or Room.

## Benefits (Pros)
1. **Modularity**: Layers are independent, making it easy to swap implementations (e.g., replace Retrofit with a mock API).
2. **Testability**: Domain layer’s use cases are pure Kotlin, enabling unit tests without Android dependencies.
3. **Maintainability**: Clear separation reduces code complexity, especially in large apps.
4. **Scalability**: Supports growing apps by isolating concerns, as you noted with decoupling in MVVM.
5. **Flexibility**: Adapts to changes in UI (e.g., XML to Compose) or data sources without affecting business logic.

## Drawbacks (Cons)
1. **Initial Complexity**: Requires more setup (e.g., use cases, repositories, mappers) than simpler architectures, potentially overkill for small apps.
2. **Boilerplate Code**: Additional classes for use cases and mappers increase code volume.
3. **Learning Curve**: Teams unfamiliar with Clean Architecture may find it challenging to adopt.
4. **Performance Overhead**: Extra layers can introduce minor overhead, though negligible with proper optimization (e.g., using Profiler, as you mentioned).

## Integration with MVVM
- **Presentation Layer**: Maps to MVVM’s View (Compose/Fragment) and ViewModel.
- **Domain Layer**: Contains MVVM’s Model (use cases), handling business logic.
- **Data Layer**: Part of MVVM’s Model, including repositories and mappers (API-to-domain mapping).
- **Example**:
  - **View**: Jetpack Compose `ProductScreen` observes `ProductViewModel`.
  - **ViewModel**: Calls `GetProductsUseCase` and maps domain models to UI models.
  - **Use Case**: `GetProductsUseCase` in Domain layer validates data.
  - **Repository**: `ProductRepository` in Data layer fetches from API/Room.
  - **Mapper**: `ProductMapper` in Data layer converts `ApiProduct` to `Product`.

## Code Example
```kotlin
// Data Layer: API Model
data class ApiProduct(val id: Int, val productName: String, val priceInCents: Int)

// Domain Layer: Domain Model
data class Product(val id: Int, val name: String, val price: Double)

// Data Layer: Mapper
object ProductMapper {
    fun toDomain(apiProduct: ApiProduct): Product {
        return Product(
            id = apiProduct.id,
            name = apiProduct.productName,
            price = apiProduct.priceInCents / 100.0
        )
    }
}

// Data Layer: Repository
interface ProductRepository {
    suspend fun getProducts(): List<Product>
}

class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return api.getProducts().map { ProductMapper.toDomain(it) }
    }
}

// Domain Layer: Use Case
class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): Result<List<Product>> {
        return try {
            Result.success(repository.getProducts().filter { it.price > 0 })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Presentation Layer: ViewModel
class ProductViewModel(private val useCase: GetProductsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState.asStateFlow()

    fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            useCase().onSuccess {
                _uiState.value = UiState.Success(it)
            }.onFailure {
                _uiState.value = UiState.Error(it.message ?: "Error")
            }
        }
    }
}

// Presentation Layer: UI (Jetpack Compose)
@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.fetchProducts() }
    when (state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> {
            LazyColumn {
                items((state as UiState.Success).data, key = { it.id }) { product ->
                    Text("${product.name} - $${product.price}")
                }
            }
        }
        is UiState.Error -> Text((state as UiState.Error).message)
    }
}
```

## Applying SOLID Principles
- **Single Responsibility**: Each layer/class (e.g., use case, repository) has one responsibility.
- **Open/Closed**: Use cases and repositories are open for extension (e.g., new data sources) but closed for modification.
- **Liskov Substitution**: Repository implementations can be swapped without affecting use cases.
- **Dependency Inversion**: High-level modules (use cases) depend on abstractions (repository interfaces), not implementations.

## Performance Considerations
- Use Android Studio’s **Profiler**  to monitor performance across layers.
- Optimize data fetching with coroutines (e.g., `viewModelScope`) to avoid blocking the UI thread.
- Cache data in the repository to reduce network calls, improving app responsiveness.

## Common Questions
1. **“How do you implement Clean Architecture in an Android app?”**
   - **Answer**: I structure the app into Presentation (ViewModel, Compose), Domain (use cases), and Data (repositories, mappers) layers. For a product list, I used `GetProductsUseCase` to handle business logic, `ProductRepository` to fetch data, and a ViewModel to update a Compose UI, ensuring modularity.
2. **“Why use Clean Architecture with MVVM?”**
   - **Answer**: “Clean Architecture enhances MVVM by isolating business logic in the Domain layer’s use cases, making it testable and decoupled. In an e-commerce app, I used Clean Architecture to swap a mock API for testing without changing the ViewModel or UI.”
3. **“How do you handle mappers in Clean Architecture?”**
   - **Answer**: “Mappers reside in the Data layer to convert API models to domain models, as I did with `ProductMapper` to transform `ApiProduct` to `Product`. This decouples the Domain layer from external data structures.”

4. **How clean Architecture differsfrom MVVM architecture**
     - **Answer**:
        | Feature  | MVVM    | Clean Architecture   |
        | -------- | ------------------------- | ----------------------------------- |
        | Scope               | UI-level pattern          | Full app-level layered architecture |
        | Layers              | View, ViewModel, Model    | Presentation, Domain, Data          |
        | Use Cases           | ❌ Usually absent          | ✅ Central to domain logic           |
        | Dependency Rule     | Flexible                  | Strict inward dependencies only     |
        | Domain Isolation    | ❌ Mixed logic             | ✅ Completely separate (testable)    |
        | Android-free domain | ❌ Often Android-dependent | ✅ Pure Kotlin (no Android imports)  |
        | Testing Ease        | Moderate                  | High (pure, testable components)    |
        | Complexity          | Low to Medium             | Medium to High                      |


        In fact, most real-world scalable Android projects use Clean Architecture with MVVM in the presentation layer.

5. **Clean Architecture and SOLID Principals?**
    - **Answer:**
        | Concept    | Description  |
        | ---------------------- | --------------- |
        | **Clean Architecture** | Blueprint for structuring app into layers with strict separation of concerns          |
        | **SOLID**              | Guidelines for writing maintainable, scalable code inside and across layers           |
        | **Relationship**       | Clean Architecture **enforces** SOLID principles in how layers depend and communicate |


6. **Which layers knows about which Layer?**
    | Layer            | Knows About             | Doesn’t Know About |
    | ---------------- | ----------------------- | ------------------ |
    | **Presentation** | Domain                  | Data               |
    | **Domain**       | Nothing outside         | Presentation, Data |
    | **Data**         | Domain (via interfaces) | Presentation       |
    
    - The Domain layer defines the interfaces, and the Data layer implements them.
    - All concrete dependencies point inward.
    - Abstractions (interfaces) are defined in the domain layer.
    - The domain is completely isolated — it can be reused in a CLI app, backend service, etc.


