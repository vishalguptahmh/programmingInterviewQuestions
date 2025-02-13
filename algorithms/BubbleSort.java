class BubbleSort{
    //In every case it will sort with 0(n^2) complexity
    public static Integer[] sort(Integer[] list){
      for(int i=0;i<list.length;i++){
        for(int j=0;j<list.length-1;j++){
          if(list[j]>list[j+1]){
            int temp=list[j];
            list[j]=list[j+1];
            list[j+1]=temp;
          }
        }
      }
      return list;
    }
    
    //if array is sorted then this will sort with 0(n) complexity
    public static Integer[] sortWithMinialComplexity(Integer[] list){
        for(int i=0;i<list.length;i++){
        boolean swaped=false;
          for(int j=0;j<list.length-i-1;j++){
              if(list[j]>list[j+1]){
               //swap
               int v=list[j];
               list[j]=list[j+1];
               list[j+1]=v;
               swaped=true;
               }  
          }
         if(!swaped){
         break;
         }
      }
   return list;  
 }
