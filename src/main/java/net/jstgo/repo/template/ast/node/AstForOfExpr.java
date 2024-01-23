package net.jstgo.repo.template.ast.node;

import net.jstgo.repo.template.ast.AstType;

public class AstForOfExpr extends AstNode {

  private AstNode definition;
  private AstNode body;

  public AstForOfExpr(AstNode definition, AstNode body) {
    super(AstType.ForOfStatement);
    this.definition = definition;
    this.body = body;
  }

  public AstNode getDefinition() {
    return definition;
  }

  public void setDefinition(AstNode definition) {
    this.definition = definition;
  }

  public AstNode getBody() {
    return body;
  }

  public void setBody(AstNode body) {
    this.body = body;
  }
}
