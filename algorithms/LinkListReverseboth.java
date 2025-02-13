    /*
     * For your reference:
     *
     * SinglyLinkedListNode {
     *     int data;
     *     SinglyLinkedListNode next;
     * }
     *
     */

    static SinglyLinkedListNode reverse(SinglyLinkedListNode head) {
        //   return reverseViaIterative(head);
        return reverseViaRecursion(head);
    }
    public static SinglyLinkedListNode reverseViaRecursion(SinglyLinkedListNode head){
        if(head==null){
            return head;
        }
        if(head.next==null){
            return head;
        }
       // System.out.println("recursive call head :"+head.data);
        SinglyLinkedListNode newNode  =reverseViaRecursion(head.next);
        //System.out.println("new node comes head:"+head.data+" newnode: "+newNode.data);
        SinglyLinkedListNode nextnode=head.next;
        nextnode.next=head;
        head.next=null;
        return newNode;
    }

    public static SinglyLinkedListNode reverseViaIterative(SinglyLinkedListNode head){
        if(head==null){
            return head;
        }
        SinglyLinkedListNode node=head;
        SinglyLinkedListNode previous=null;
        SinglyLinkedListNode current=head;
        SinglyLinkedListNode nexxt=head.next;
        while(nexxt!=null){
            current.next=previous;
            previous=current;
            current=nexxt;
            nexxt=nexxt.next;
        }
        current.next=previous;
        head=current;
    return head;
    }

