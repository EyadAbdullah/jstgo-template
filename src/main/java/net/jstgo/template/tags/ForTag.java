package net.jstgo.template.tags;

public class ForTag extends BaseTag {

  @Override
  public String openTag() {
    return "{for";
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
