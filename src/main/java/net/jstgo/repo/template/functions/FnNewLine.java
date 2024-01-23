package net.jstgo.repo.template.functions;

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
