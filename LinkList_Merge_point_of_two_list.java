    /*
     * For your reference:
     *
     * SinglyLinkedListNode {
     *     int data;
     *     SinglyLinkedListNode next;
     * }
     *
     */
     public static int getLength(SinglyLinkedListNode node){
         int count=0;
         if(node==null){
             return 0;
         }
         while(node!=null){
            count++;
            System.out.print(" "+node.data);
            node=node.next;
         }
         System.out.println();
         return count;

     }
    static int findMergeNode(SinglyLinkedListNode head1, SinglyLinkedListNode head2) {

          return findMergeNodeWithIterative(head1,head2);
        // return findMergeWithHash(head1,head2);
    }

    static int findMergeWithHash(SinglyLinkedListNode head1, SinglyLinkedListNode head2){
            //int l1=getLength(head1);
            //int l2=getLength(head2);
            HashSet<SinglyLinkedListNode> set=new HashSet<SinglyLinkedListNode>();
            while(head1!=null){
                set.add(head1);
                System.out.println(head1.data);
                head1=head1.next;
            }
 System.out.println("\n");
            SinglyLinkedListNode temp=head2;
            while(temp!=null){
                System.out.println(temp.data);
                temp=temp.next;
            }
            while(head2!=null){
                if(set.contains(head2)){
                    return head2.data;
                }
                head2=head2.next;
            }
            return -1;
    }
    static int findMergeNodeWithIterative(SinglyLinkedListNode head1,SinglyLinkedListNode head2){
  int nodevalue=0;
            int l1=getLength(head1);
            int l2=getLength(head2);
            if(l1>l2){
                int diff=l1-l2;
                nodevalue=findintersection(diff,head1,head2);
            }
            else{
                int diff=l2-l1;
                nodevalue=findintersection(diff,head2,head1);    
            }
return nodevalue;
    }
   
    static int findintersection(int d,SinglyLinkedListNode head1, SinglyLinkedListNode head2){
        int value=-1;
        for(int i=0;i<d;i++){
            if(head1==null){
                return -1;
            }
            head1=head1.next;
        }

        while(head1!=null && head2!=null){
            if(head1==head2){
             return  value= head1.data;
            //    break;
            }
            head1=head1.next;
            head2=head2.next;
        }

return value;
    }

