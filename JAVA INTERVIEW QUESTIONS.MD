# JAVA INTERVIEW QUESTIONS

### Difference between final , finally and finalize

#### Final
final is modifier applicable for classes , methods and variables

- if class is final then we cannot extend that class means we cannot create child for that class
- if a method declared as final then we cannot override that method in child class
- if a variable declared as final then it will become constant and we cannot perform re-assignment for that variable.

#### Finally

Finally is a block always associated with try-catch to maintain cleanup code.

```java
try{
// Risky code
}
catch(Exception: E){
// Handling code
}
finally{
// clean up code
}
```

#### Finalize()

finalize is a method which is always invoked by garbage collector just before destroying object to perform cleanup activites.

ex if there is object to which db and network connection is associated then garbage collector gives change by calling Finalize method to close these connections.


>NOTE : finally meant for cleanup activities related to try block where-as Finalize meant for cleanup activities releated to object.
