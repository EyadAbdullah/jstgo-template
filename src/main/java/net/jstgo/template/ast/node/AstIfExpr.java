package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstIfExpr extends AstNode {

  private AstNode condition;
  private AstBlockStatementExpr consequent;
  private AstNode alternative;

  public AstIfExpr(AstNode condition, AstBlockStatementExpr consequent) {
    super(AstType.IfStatement);
    this.condition = condition;
    this.consequent = consequent;
  }

  public AstIfExpr(AstNode condition, AstBlockStatementExpr consequent, AstNode alternative) {
    super(AstType.IfStatement);
    this.condition = condition;
    this.consequent = consequent;
    this.alternative = alternative;
  }

  public AstNode getCondition() {
    return condition;
  }

  public void setCondition(AstNode condition) {
    this.condition = condition;
  }

  public AstBlockStatementExpr getConsequent() {
    return consequent;
  }

  public void setConsequent(AstBlockStatementExpr consequent) {
    this.consequent = consequent;
  }

  public AstNode getAlternative() {
    return alternative;
  }

  public void setAlternative(AstNode alternative) {
    this.alternative = alternative;
  }
}
