package net.jstgo.repo.template.ast.node;

import java.util.List;
import net.jstgo.repo.template.ast.AstType;

public class AstArrayExpr extends AstNode {

  private List<AstNode> elements;

  public AstArrayExpr(List<AstNode> elements) {
    super(AstType.ArrayExpression);
    this.elements = elements;
  }

  public List<AstNode> getElements() {
    return elements;
  }

  public void setElements(List<AstNode> elements) {
    this.elements = elements;
  }
}
