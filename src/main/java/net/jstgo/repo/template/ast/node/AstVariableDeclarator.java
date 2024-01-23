package net.jstgo.repo.template.ast.node;

import net.jstgo.repo.template.ast.AstType;

public class AstVariableDeclarator extends AstNode {

  private String id;
  private AstNode init;

  public AstVariableDeclarator(String id, AstNode init) {
    super(AstType.VariableDeclarator);
    this.id = id;
    this.init = init;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AstNode getInit() {
    return init;
  }

  public void setInit(AstNode init) {
    this.init = init;
  }
}
