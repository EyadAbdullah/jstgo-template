package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstObjectPropertyExpr extends AstNode {

  private AstNode key;
  private AstNode value;

  public AstObjectPropertyExpr(AstNode key, AstNode value) {
    super(AstType.ObjectExpression);
    this.key = key;
    this.value = value;
  }

  public AstNode getKey() {
    return key;
  }

  public void setKey(AstNode key) {
    this.key = key;
  }

  public AstNode getValue() {
    return value;
  }

  public void setValue(AstNode value) {
    this.value = value;
  }
}
