package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstString extends AstNode {

  private String value;
  private String raw;

  public AstString(String raw) {
    super(AstType.String);
    this.raw = raw;
    this.value = raw.substring(1, raw.length() - 1);
  }

  public String getValue() {
    return value;
  }

  public String getRaw() {
    return raw;
  }
}
