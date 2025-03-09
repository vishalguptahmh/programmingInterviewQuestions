```kotlin

class Node(var value: Int) {
    var next: Node? = null
}


class SinglyLinkList{

    var head: Node? = null

    // this will add the value at the end of the list
    fun add(value: Int){
        var iter = head
        val node = Node(value)
        if (head == null){
            head = node
        }
        else{
            while(iter?.next!=null){
                iter = iter.next
            }
            iter?.next = node
        }
    }

    // add at first
    fun addAtFirst(value: Int){
        val node = Node(value)
        node.next = head
        head= node
    }

    // add at nth position
    fun addAtN(n:Int,value : Int){
        var mynode = head
        val newNode = Node(value)
        if(n==1){
            newNode.next = head
            head = newNode
        }
        else{
            var count = n-1
            while(count>1){
                mynode = mynode?.next
                count--
            }
            println(mynode?.value)
            newNode.next = mynode?.next
            mynode?.next = newNode
        }
    }

    //
    fun deleteValue(value :Int){
        var node = head
        var previous = node
        while(node!=null){
            if(node.value.equals(value)){
                println("Deleted value $value")
                previous?.next = node.next
                break
            }
            previous = node
            node = node.next

        }
    }

    // deleteatN
    fun deleteAtN(postion:Int){
        var node = head
        var count = 1
        if( postion == 1 ){
            head = head?.next
        }
        else{
            while(count < postion-1){
                node = node?.next
                count++
            }
            val deleteNode = node?.next
            if(deleteNode?.next == null){
                node?.next = null
            }
            else{
                node?.next = deleteNode.next
            }
        }
    }

    fun print(){
        var i =1
        var current = head
        while (current != null){
            println("index : $i : "+current.value)
            i++
            current = current.next
        }
    }

    fun printWithRecursion(){
        var current = head
        recursionprint(current)
    }

   private fun recursionprint(head:Node?){
       if(head ==null){
              return
         }
         else{
              println(head.value)
              recursionprint(head.next)
       }
    }
}

fun main(){
    val linkList = SinglyLinkList()
    linkList.add(1)
    linkList.add(2)
    linkList.add(3)
    linkList.add(4)
    linkList.add(5)
//    linkList.printWithRecursion() // print with recursion
//    linkList.addAtFirst(10) // add at n th position
//    linkList.addAtN(4,10) // add at n th position
//    linkList.print()

//    linkList.deleteValue(2) // delete value
//    linkList.print()
//    linkList.deleteValue(5)
//    linkList.print()
//    linkList.deleteValue(1)

    linkList.deleteAtN(5) // Delete at nth position
    linkList.print()
}

main()

```