package net.jstgo.template.tags;

public class ElseIfTag extends BaseTag {

  @Override
  public String openTag() {
    return "{elseif";
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
