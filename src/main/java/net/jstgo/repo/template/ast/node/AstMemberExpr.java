package net.jstgo.repo.template.ast.node;

import net.jstgo.repo.template.ast.AstType;

public class AstMemberExpr extends AstNode {

  private AstNode key;
  private AstNode value;

  public AstMemberExpr(AstNode key, AstNode value) {
    super(AstType.MemberExpression);
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
