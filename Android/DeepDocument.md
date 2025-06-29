# How ViewModel Survives Configuration Changes in Android

ViewModel survives configuration changes (e.g., screen rotation) because it's stored in a ViewModelStore, which is preserved by the Android system using ActivityThread and onRetainNonConfigurationInstance().

| Component                            | Role                                                       |
| ------------------------------------ | ---------------------------------------------------------- |
| `ViewModel`                          | Holds UI-related data and survives configuration changes   |
| `ViewModelStore`                     | Stores ViewModel instances (like a map)                    |
| `ComponentActivity`                  | Base class that manages `ViewModelStore` lifecycle         |
| `onRetainNonConfigurationInstance()` | Hook for saving objects during config change               |
| `ActivityThread`                     | Android system component that manages Activity lifecycles  |
| `ActivityClientRecord`               | Internal data holder for each Activity in `ActivityThread` |


## 🔁 Full Flow: Step-by-Step

✅ When config change occurs (e.g., screen rotation):
- 🔁 Android detects a config change (e.g., orientation).
- 📲 ActivityThread prepares to recreate the Activity.
- 🧠 ActivityThread calls:
```kotlin
    activity.onRetainNonConfigurationInstance()
```
- 🧰 In ComponentActivity, this returns a NonConfigurationInstances object that contains:
    - ViewModelStore
    - Any custom data (via onRetainCustomNonConfigurationInstance())
    - 🗂 ActivityThread stores this object inside an ActivityClientRecord.

✅ When new Activity is created:

- 🔄 ActivityThread creates a new Activity instance.

- 🎯 Before calling onCreate(), Android gives it access to the previously retained NonConfigurationInstances.

- 💾 ComponentActivity.getLastNonConfigurationInstance() returns the saved object.

- 📦 The ViewModelStore is restored in the new Activity.

- 🧠 ViewModelProvider reuses the same ViewModelStore, so existing ViewModel instances are reused.


🧪 How to check if it's a config change:

```kotlin
override fun onDestroy() {
    super.onDestroy()
    if (isChangingConfigurations) {
        Log.d("DEBUG", "It's a config change")
    } else {
        Log.d("DEBUG", "Activity is finishing")
    }
}


```