package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstIdentifier extends AstNode {

  private String init;

  public AstIdentifier(String init) {
    super(AstType.Identifier);
    this.init = init;
  }

  public String getInit() {
    return init;
  }

  public void setInit(String init) {
    this.init = init;
  }
}
