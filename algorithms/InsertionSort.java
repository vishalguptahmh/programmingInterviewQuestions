class InsertionSort{
  public static void logd(int str){
    System.out.println(str);
}
public static void logd(String str){
  System.out.println(str);
}
public static void logd(Integer[] lis)
{
  logd("Sorted List: "+lis);
 for(int i=0;i<lis.length;i++){
    logd(lis[i]);
 }
}

  public static Integer[] sort(Integer[] list){
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
}