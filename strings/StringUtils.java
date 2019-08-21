class StringUtils implements FunctionInterface {

  /* check string is pallendrome or not */
  public Boolean checkPallindrome(String testString) {
    int size = testString.length();
    System.out.println(size);
    int i = 0;
    int j = size - 1;
    boolean issame = true;
    while (i < j) {
      if (!(testString.charAt(i) == testString.charAt(j))) {
        issame = false;
      }
      i++;
      j--;
    }
    return issame;
  }

  @Override
  public String removeCharFromString(String str, Character character) {
  
    StringBuffer stringBuffer=new StringBuffer();
    char[] charaarry=str.toCharArray();
    for (Character value : charaarry) {
      if (value != character) {
        stringBuffer.append(value);
      }
    }
    return stringBuffer.toString();
  }


  


}