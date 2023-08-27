package net.jstgo.template.tags;

public class ElseTag extends BaseTag {

  @Override
  public String openTag() {
    return "{else";
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
