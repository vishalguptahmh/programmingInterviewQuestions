import java.util.*;
import java.lang.*;
class TestFile{
 
  public static void main(String args[])
    {
        Integer[] lis={3,2,6,7,8,1,5,4};
        Integer[] sorted= 
        // SelectionSort.sort(lis);
       // BubbleSort.so rt(lis);
        // InsertionSort.sort(lis);
        // MergeSort.sort(lis);
        QuickSort.sort(lis);
        logd(sorted);
      
    }
  public static void logd(String str){
      System.out.println(str);
  }
  public static void logd(int str){
      System.out.println(str);
  }
  public static void logd(Integer[] lis)
     {
       logd("Sorted List: ");
      for(int i=0;i<lis.length;i++){
         logd(lis[i]);
      }
  }
  
}