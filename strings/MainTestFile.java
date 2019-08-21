class MainTestFile {

  public static void logd(String str) {
    System.out.println(str);
  }
  public static void main(String[] args) {
    String testString = "helloolleh";
    StringUtils stringUtils = new StringUtils();
    logd(""+stringUtils.checkPallindrome(testString));
    logd(""+stringUtils.removeCharFromString(testString, 'l'));

  }

}