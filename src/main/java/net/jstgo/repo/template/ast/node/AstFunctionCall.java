package net.jstgo.repo.template.ast.node;

import net.jstgo.repo.template.ast.AstType;
import java.util.List;

public class AstFunctionCall extends AstNode {

  private AstNode func;
  private List<AstNode> args;

  public AstFunctionCall(AstNode func, List<AstNode> args) {
    super(AstType.CallExpression);
    this.func = func;
    this.args = args;
  }

  public AstNode getFunc() {
    return func;
  }

  public void setFunc(AstNode func) {
    this.func = func;
  }

  public List<AstNode> getArgs() {
    return args;
  }

  public void setArgs(List<AstNode> args) {
    this.args = args;
  }
}
