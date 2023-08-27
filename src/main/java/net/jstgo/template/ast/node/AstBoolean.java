package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstBoolean extends AstNode {

  private Boolean value;

  public AstBoolean(Boolean value) {
    super(AstType.Boolean);
    this.value = value;
  }

  public Boolean getValue() {
    return value;
  }

  public void setValue(Boolean value) {
    this.value = value;
  }
}
