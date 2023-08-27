package net.jstgo.template.tags;

public class PrintTag extends BaseTag {

  @Override
  public String openTag() {
    return "{{";
  }

  @Override
  public String closeTag() {
    return "}}";
  }

  @Override
  public boolean isRemovable() {
    return false;
  }
}
