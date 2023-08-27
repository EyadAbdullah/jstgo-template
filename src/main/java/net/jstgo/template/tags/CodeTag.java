package net.jstgo.template.tags;

public class CodeTag extends BaseTag {

  @Override
  public String openTag() {
    return "{%";
  }

  @Override
  public String closeTag() {
    return "%}";
  }

  @Override
  public boolean isRemovable() {
    return true;
  }
}
