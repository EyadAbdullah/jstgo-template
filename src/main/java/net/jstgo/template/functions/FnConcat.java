package net.jstgo.template.functions;

public class FnConcat implements TplFunction {

  @Override
  public String name() {
    return "concat";
  }

  @Override
  public Object parse(Object... args) {
    var result = new StringBuilder();
    for (var arg : args) {
      result.append(arg);
    }
    return result.toString();
  }
}
