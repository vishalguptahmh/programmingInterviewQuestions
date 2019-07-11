class BubbleSort{
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
}