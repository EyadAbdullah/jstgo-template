package net.jstgo.template.tags;

public class IfTag extends BaseTag {

  @Override
  public String openTag() {
    return "{if";
  }

  @Override
  public String closeTag() {
    return "}";
  }

  @Override
  public boolean isRemovable() {
    return false;
  }
}
