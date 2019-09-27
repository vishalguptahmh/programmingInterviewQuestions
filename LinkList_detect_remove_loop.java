// Floyd Cycle Detection algorithm to detect and remove the loop

  public static void detectLoop(Node head){
        Node slow=head;
        Node fast=head;
        while(slow!=null && fast!=null && fast.next!=null){
            slow=slow.next;
            fast=fast.next.next;
            if(slow==fast){
                //loop detected;
                System.out.println("slow : "+slow.data);
                removeLoop(slow,head);
                break;
            }
        }
    }
    
    public static void removeLoop(Node node,Node h){
        Node slow=node;
        Node head=h;
        while(slow.next!=head.next){
            slow=slow.next;
            head=head.next;
        }
         System.out.println("\n removeLoop: slow: "+slow.data+" head: "+head.data);
        slow.next=null;
    }


