package net.jstgo.repo.template;

public class TemplateUtils {

  private TemplateUtils() {
  }

  public static Object str(Object obj) {
    return String.valueOf(obj);
  }

  public static Object originate(Object obj) {
    if (obj instanceof Double) {
      var val = (double) obj;
      if ((val - Math.floor(val)) == 0) { // value is int
        return ((Double) obj).intValue();
      } else {
        return val;
      }
    }
    return obj;
  }
}
