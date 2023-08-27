package net.jstgo.template.functions;

public class FnNewLine implements TplFunction {

  @Override
  public String name() {
    return "nl";
  }

  @Override
  public Object parse(Object... args) {
    return "\n";
  }
}
