package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;
import java.util.List;

public class AstProgram extends AstNode {

  private List<AstNode> body;

  public AstProgram(List<AstNode> body) {
    super(AstType.Program);
    this.body = body;
  }

  public List<AstNode> getBody() {
    return body;
  }

  public void setBody(List<AstNode> body) {
    this.body = body;
  }
}
