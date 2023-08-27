package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstNumber extends AstNode {

  private Double value;

  public AstNumber(Double value) {
    super(AstType.Number);
    this.value = value;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }
}
