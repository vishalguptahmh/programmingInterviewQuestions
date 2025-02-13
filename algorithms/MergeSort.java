class MergeSort{
  public static Integer[] sort(Integer[] list){
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
}