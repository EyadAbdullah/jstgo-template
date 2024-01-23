package net.jstgo.repo.template.ast.node;

import net.jstgo.repo.template.ast.AstType;

public class AstNode {

  private AstType type;

  public AstNode(AstType type) {
    this.type = type;
  }

  public AstType getType() {
    return type;
  }

  public void setType(AstType type) {
    this.type = type;
  }
}
