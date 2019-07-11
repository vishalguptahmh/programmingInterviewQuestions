class SelectionSort{
    public static void logd(String str){
        System.out.println(str);
    }
    public static void logd(int str){
        System.out.println(str);
    }
    public static Integer[] sort(Integer[] list){
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
}