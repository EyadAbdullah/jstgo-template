package net.jstgo.repo.template.ast.node;

import net.jstgo.repo.template.ast.AstType;
import java.util.List;

public class AstObjectExpr extends AstNode {

  private List<AstNode> properties;

  public AstObjectExpr(List<AstNode> properties) {
    super(AstType.ObjectExpression);
    this.properties = properties;
  }

  public List<AstNode> getElements() {
    return properties;
  }

  public void setElements(List<AstNode> properties) {
    this.properties = properties;
  }
}
