package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;
import java.util.List;

public class AstBlockStatementExpr extends AstNode {

  private List<AstNode> elements;

  public AstBlockStatementExpr(List<AstNode> elements) {
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
