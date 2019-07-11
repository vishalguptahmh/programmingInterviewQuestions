
import java.util.concurrent.ThreadLocalRandom;

/*
* Worst Case: The worst case occurs when the partition process always picks
   greatest or smallest element as pivot
    or we can say array is already sorted in increasing or decresing order 
    complexty : o(n^2) 
*
*
*/

class QuickSort{
  public static Integer[] sort(Integer[] list){
    return quick(list,0,list.length-1);
  }
  //partition
  public static int partition(Integer[] list,int low,int high){
    int i=low;
    int pivot=list[high];
    for(int j=low;j<high;j++){
      if(list[j]<=pivot){
        int temp=list[j];
        list[j]=list[i];
        list[i]=temp;
        i++;
      }
    }
    int temp2=list[i];
    list[i]=list[high];
    list[high]=temp2;
    return i;
  }
  //mainfunction
  public static Integer[]  quick(Integer[] list,int low,int high){
    if(low<=high){
      int partition=randomPartition(list,low,high);
      quick(list,low,partition-1);
      quick(list,partition+1,high);
    }
    return list;
  }
  public static int randomPartition(Integer[] list,int low,int high){
    if(high!=0 && low < high){
      int temppicot = ThreadLocalRandom.current().nextInt(low, high);
      int tempValue=list[temppicot];
      list[temppicot]=list[high];
      list[high]=tempValue;
    }
    return partition(list,low,high);
  }
}