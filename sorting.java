 
//counting sort
public static int[] sortFromCountingSort(int[] arr){
	//complexity o(n)
        int n=arr.length;
       int count[] = new int[256]; 
       int output[] =new int[n];
        for (int i=0; i<256; ++i) 
            count[i] = 0;
        
        for(int i=0;i<n;i++){
            count[arr[i]]++;
        }
        
        for(int i=0;i<10;i++){
            count[i+1]+=count[i];
        }
        
        for(int i=0;i<n;i++){
            output[count[arr[i]]-1] =arr[i];
            count[arr[i]]--;
        }
        
      
        return output;
    }

//selection sort
 public static Integer[] sortFromSelectionSort(Integer[] list){
        int size=list.length;    
        for(int i=0;i<size;i++){
            int min=i;
            for(int j=i;j<size;j++){
                if(list[j]<list[min]){
                    min=j;
                }
            }
            int temp=list[i];
            list[i]=list[min];
            list[min]=temp;
        }
        return list;
    }

//insertion sort
 public static Integer[] sortFromInsertionSort(Integer[] list){
    int hole=0;  //Integer[] lis={4,3,2,6,5,1,3};
    int holevalue=0;
    for(int i=1;i<list.length;i++){
      hole=i;
      holevalue=list[i];
      while(hole>0 && list[hole-1]>holevalue){
        list[hole]=list[hole-1];
        hole=hole-1;
      }
     list[hole]=holevalue;
    }
    return list;
  }

//bubble sort
    public static Integer[] sortFromBubbleSort(Integer[] list){
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

//quick sort
 public static Integer[] sortFromQuickSort(Integer[] list){
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



//merge sort

public static Integer[] sortFromMergeSort(Integer[] list){
    //divide and conqure
    return mergeSort(list);
  }
  public static Integer[] mergeSort(Integer[] list)
  {
    int size=list.length;
    int mid=size/2;
    logd("size: "+size +" mid:"+mid);
    if(mid<1){return list;} //return if single element is there
    Integer[] firstList=new Integer[mid]; //divide list into two arrays and filling it
    Integer[] secondList=new Integer[size-mid];
    for(int i=0;i<size;i++){
      if(i<mid){
        logd("firstList:"+i+"  value:"+list[i]);
        firstList[i]=list[i];
      }
      else{ 
        logd("second:"+(i-mid)+"  value:"+list[i]);
        secondList[i-mid]=list[i];
      }
    }

    //calling again same function 
   mergeSort(firstList);
   mergeSort(secondList);
   //merge elements
  return merge(firstList,secondList,list);
  }

  public static Integer[] merge( Integer[] firstList, Integer[] secondList,Integer[] mainList){
    int i=0,j=0,k=0;
    //find min value from both list and merge into main list;
    while(i<firstList.length && j<secondList.length){
      if(firstList[i]<secondList[j]){
        mainList[k]=firstList[i];
        k++;
        i++;
      }
      else{
        mainList[k]=secondList[j];
        k++;
        j++;
      }
    }
    //if second list end then put all elements of first list into main list
    while(i<firstList.length){
      mainList[k]=firstList[i];
      k++;
      i++;
    }
    //if first list end then put all elements of second list into main list;
    while(j<secondList.length){
      mainList[k]=secondList[j];
      k++;
      j++;
    }
    return mainList;
  }
public static void logd(String str){
    System.out.println(str);
}
public static void logd(int str){
    System.out.println(str);
}





