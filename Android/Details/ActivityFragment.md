
## Activity

Lifecycle of Activity: 
![SSFlow](drawio/android_lifecycle_activity.drawio.png)


In the `onCreate()` method, perform basic application startup logic that happens only once for the entire life of the activity.

For example, your implementation of onCreate() might 
- bind data to lists
- associate the activity with a ViewModel,
- instantiate some class-scope variables. 

This method receives the parameter savedInstanceState, which is a Bundle object containing the activity's previously saved state. If the activity has never existed before, the value of the Bundle object is null. 

```kotlin
// This callback is called only when there is a saved instance previously saved using
// onSaveInstanceState(). Some state is restored in onCreate(). Other state can optionally
// be restored here, possibly usable after onStart() has completed.
// The savedInstanceState Bundle is same as the one used in onCreate().
override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    textView.text = savedInstanceState?.getString(TEXT_VIEW_KEY)
}

// Invoked when the activity might be temporarily destroyed; save the instance state here.
override fun onSaveInstanceState(outState: Bundle?) {
    outState?.run {
        putString(GAME_STATE_KEY, gameState)
        putString(TEXT_VIEW_KEY, textView.text.toString())
    }
    // Call superclass to save any view hierarchy.
    super.onSaveInstanceState(outState)
}
```

`Onstart` call makes the activity visible to the user as the app prepares for the activity to enter the foreground and become interactive. 

For example, this method is where the code that maintains the UI is initialized. 


In `OnResume`state : When the activity enters the Resumed state, it comes to the foreground, and the system invokes the onResume() callback. This is the state in which the app interacts with the user. The app stays in this state until something happens to take focus away from the app, such as 
- the device receiving a phone call
- the user navigating to another activity, or 
- the device screen turning off. 

>Note : In multi-window mode, however, your activity might be fully visible even when it is in the Paused state. For example, when the app is in multi-window mode and the user taps the window that does not contain your activity, your activity moves to the Paused state.

`onPause()`
The system calls this method as the first indication that the user is leaving your activity, though it does not always mean the activity is being destroyed. It indicates that the activity is no longer in the foreground, but it is still visible if the user is in multi-window mode.

ex 
In multi-window mode, only one app has focus at any time, and the system pauses all the other apps.
The opening of a new, semi-transparent activity, such as a dialog, pauses the activity it covers. As long as the activity is partially visible but not in focus, it remains paused. 

You can also use the onPause() method to `release system resources`, handles to sensors (like GPS), or any resources that affect battery life while your activity is Paused and the user does not need them.

onPause() execution is very brief and does not necessarily offer enough time to perform save operations. For this reason, `don't use onPause() to save application or user data, make network calls, or execute database transactions.` Such work might not complete before the method completes.

`onStop()`
When your activity is no longer visible to the user, it enters the Stopped state
This can occur when a newly launched activity covers the entire screen. 

 In the onStop() method, release or adjust resources that are not needed while the app is not visible to the user. For example, your app might pause animations or switch from fine-grained to coarse-grained location updates. Using onStop() instead of onPause() means that UI-related work continues, even when the user is viewing your activity in multi-window mode.

Also, use onStop() to perform relatively CPU-intensive shutdown operations. For example, if you can't find a `better time to save information to a database`, you might do so during onStop(). 

>Note: Once your activity is stopped, the system might destroy the process that contains the activity if the system needs to recover memory. Even if the system destroys the process while the activity is stopped, the system still retains the state of the View objects, such as text in an EditText widget, in a Bundle—a blob of key-value pairs—and restores them if the user navigates back to the activity.

`onDestroy()` is called before the activity is destroyed. The system invokes this callback for one of two reasons:

- The activity is finishing, due to the user completely dismissing the activity or due to finish() being called on the activity.
- The system is temporarily destroying the activity due to a configuration change, such as device rotation or entering multi-window mode.
 
Instead of putting logic in your Activity to determine why it is being destroyed, use a ViewModel object to contain the relevant view data for your Activity. If the Activity is recreated due to a configuration change, the ViewModel does not have to do anything, since it is preserved and given to the next Activity instance.

If the Activity isn't recreated, then the ViewModel has the onCleared() method called, where it can clean up any data it needs to before being destroyed. You can distinguish between these two scenarios with the isFinishing() method.

If the activity is finishing, onDestroy() is the final lifecycle callback the activity receives. 

If onDestroy() is called as the result of a configuration change, the system immediately creates a new activity instance and then calls onCreate() on that new instance in the new configuration. 

The system never kills an activity directly to free up memory. Instead, it kills the process the activity runs in, destroying not only the activity but everything else running in the process as well.

>Note: onSaveInstanceState() is not called when the user explicitly closes the activity or in other cases when finish() is called.

if the system destroys the activity due to system constraints (such as a configuration change or memory pressure), then although the actual Activity instance is gone, the system remembers that it existed. If the user attempts to navigate back to the activity, the system creates a new instance of that activity using a set of saved data that describes the state of the activity when it was destroyed.

The saved data that the system uses to restore the previous state is called the instance state. It's a collection of key-value pairs stored in a Bundle object. By default, the system uses the Bundle instance state to save information about each View object in your activity layout, such as the text value entered into an EditText widget.

A Bundle object isn't appropriate for preserving more than a trivial amount of data, because it requires serialization on the main thread and consumes system-process memory. To preserve more than a very small amount of data, take a combined approach to preserving data, using persistent local storage, the onSaveInstanceState() method, and the ViewModel class, as outlined in Save UI states

Instead of restoring the state during onCreate(), you can choose to implement onRestoreInstanceState(), which the system calls after the onStart() method. The system calls onRestoreInstanceState() only if there is a saved state to restore, so you do not need to check whether the Bundle is null.

the order of operations that occur when Activity A starts Activity B:

    - Activity A's onPause() method executes.
    - Activity B's onCreate(), onStart(), and onResume() methods execute in sequence. Activity B now has user focus.
    - If Activity A is no longer visible on screen, its onStop() method executes.

- When only onDestroy is called for an activity without onPause() and onStop()?

    - when you call `finish()` in `OnCreate()` function. 
- Why do we need to call setContentView() in onCreate() of Activity class? 
    - because `OnCreate()` is called once.if we call it in another methods then UI will set again and again which is not efficient way.
- What is onSaveInstanceState() and onRestoreInstanceState() in activity?
    - already described above or check this [link](https://developer.android.com/guide/components/activities/activity-lifecycle)

## Fragment
![fragmentLifecycle](images/fragment-view-lifecycle.png)


Fragments live inside activities, and each activity can host many fragments. 
 , unlike activities, they are not top-level application components. 
A Fragment represents a reusable portion of your app's UI. A fragment defines and manages its own layout, has its own lifecycle, and can handle its own input events. Fragments can't live on their own. They must be hosted by an activity or another fragment. 

`Advantages of fragments` include code reuse and modularity (e.g., using the same list view in many activities), including the ability to build multi-pane interfaces (mostly useful on tablets). 

The main `disadvantage` is (some) added complexity. You can generally achieve the same thing with (custom) views in a non-standard and less robust way. 

fragments can be added, replaced, or removed. And you can keep a record of these changes in a back stack that is managed by the activity, so that the changes can be reversed.

The Fragment library also provides more specialized fragment base classes:
    - DialogFragment
    - PreferenceFragmentCompat

FragmentActivity is the base class for AppCompatActivity

There are two ways to add Fragment 
- the fragment in your activity's layout file
- fragment container in your activity's layout file and then programmatically adding the fragment from within your activity.

In either case, you need to add a FragmentContainerView that defines the location where the fragment should be placed within the activity's view hierarchy. It is strongly recommended to always use a FragmentContainerView as the container for fragments, as FragmentContainerView includes fixes specific to fragments that other view groups such as FrameLayout do not provide.

[`FragmentManager`](https://developer.android.com/guide/fragments/fragmentmanager) is the class responsible for performing actions on your app's fragments, such as adding, removing, or replacing them and adding them to the back stack.
You might never interact with FragmentManager directly if you're using the Jetpack Navigation library, as it works with the FragmentManager on your behalf.
Fragments can host one or more child fragments. Inside a fragment, you can get a reference to the FragmentManager that manages the fragment's children through getChildFragmentManager(). If you need to access its host FragmentManager, you can use getParentFragmentManager(). [more Detail](https://developer.android.com/guide/fragments/fragmentmanager#access)

[Child fragments] (https://developer.android.com/guide/fragments/fragmentmanager#child)
usecase : Jetpack Navigation uses child fragments as individual destinations. An activity hosts a single parent NavHostFragment and fills its space with different child destination fragments as users navigate through your app.

As an alternative to using a LifecycleObserver, the Fragment class includes callback methods that correspond to each of the changes in a fragment's lifecycle. These include onCreate(), onStart(), onResume(), onPause(), onStop(), and onDestroy().

A fragment's view has a separate Lifecycle that is managed independently from that of the fragment's Lifecycle. Fragments maintain a LifecycleOwner for their view, which can be accessed using getViewLifecycleOwner() or getViewLifecycleOwnerLiveData().

When a fragment is instantiated, it begins in the INITIALIZED state. For a fragment to transition through the rest of its lifecycle, it must be added to a FragmentManager. The FragmentManager is responsible for determining what state its fragment should be in and then moving them into that state.

Beyond the fragment lifecycle, FragmentManager is also responsible for attaching fragments to their host activity and detaching them when the fragment is no longer in use. The Fragment class has two callback methods, onAttach() and onDetach(), that you can override to perform work when either of these events occur.

The onAttach() callback is invoked when the fragment has been added to a FragmentManager and is attached to its host activity. At this point, the fragment is active, and the FragmentManager is managing its lifecycle state. At this point, FragmentManager methods such as findFragmentById() return this fragment.

onAttach() is always called before any Lifecycle state changes.

The onDetach() callback is invoked when the fragment has been removed from a FragmentManager and is detached from its host activity. The fragment is no longer active and can no longer be retrieved using findFragmentById().

onDetach() is always called after any Lifecycle state changes.

Note that these callbacks are unrelated to the FragmentTransaction methods attach() and detach(). 

>Caution: Avoid using the <fragment> tag to add a fragment using XML, as the <fragment> tag allows a fragment to move beyond the state of its FragmentManager. Instead, always use FragmentContainerView for adding a fragment using XML.

Created : 

When your fragment reaches the CREATED state, it has been added to a FragmentManager and the onAttach() method has already been called.
This transition invokes the onCreate() callback. The callback also receives a savedInstanceState Bundle argument containing any state previously saved by onSaveInstanceState(). Note that savedInstanceState has a null value the first time the fragment is created, but it is always non-null for subsequent recreations, even if you do not override onSaveInstanceState()



- What is the difference between FragmentPagerAdapter vs FragmentStatePagerAdapter?
    - FragmentPagerAdapter: Each fragment visited by the user will be stored in the memory but the view will be destroyed. When the page is revisited, then the view will be created not the instance of the fragment.
    FragmentStatePagerAdapter: Here, the fragment instance will be destroyed when it is not visible to the user, except the saved state of the fragment.


- What is difference between add and replace fragment ? 
    - replace removes the existing fragment and adds a new fragment. This means when you press back button the fragment that got replaced will be created with its onCreateView being invoked. Whereas add retains the existing fragments and adds a new fragment that means existing fragment will be active and they wont be in 'paused' state hence when a back button is pressed onCreateView is not called for the existing fragment(the fragment which was there before new fragment was added).
    
    - In terms of fragment's life cycle events onPause, onResume, onCreateView and other life cycle events will be invoked in case of replace but they wont be invoked in case of add. 

- What is retained Fragment?
    - A retained fragment in Android refers to a fragment that persists across configuration changes, such as screen rotations, without being destroyed and recreated.The fragment instance is retained across activity recreation, ensuring that its non-UI data remains intact. However, the fragment's views are still destroyed and recreated during configuration changes
    
    - onDestroy() is not called, but onDetach() is still executed because the fragment detaches from its previous activity
    - onCreate(Bundle) is skipped since the fragment isn't re-created
    - Methods like onAttach(Activity) and onActivityCreated(Bundle) are still invoked

- What is the purpose of addToBackStack() while commiting fragment transaction?
    - [link](https://stackoverflow.com/questions/22984950/what-is-the-meaning-of-addtobackstack-with-null-parameter)

- How Android Image Loading library optimizes memory usage ?
    - To optimize memory usage and use less memory, Glide ,Fresco does downsampling.Downsampling means scaling the bitmap(image) to a smaller size which is actually required by the view.Assume that we have an image of size 4000*4000, but the view size is 200*200. So why load an image of 4000*4000, Glide down-samples the bitmap to 200*200, and then show it into the view.