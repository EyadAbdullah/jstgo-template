package net.jstgo.template.tags;

public class EndForTag extends BaseTag {

  @Override
  public String openTag() {
    return "{endfor";
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
