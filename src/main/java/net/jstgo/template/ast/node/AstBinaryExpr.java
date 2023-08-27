package net.jstgo.template.ast.node;

import net.jstgo.template.ast.AstType;

public class AstBinaryExpr extends AstNode {

  private String op;
  private AstNode left;
  private AstNode right;

  public AstBinaryExpr(String op, AstNode left, AstNode right) {
    super(AstType.BinaryExpression);
    this.op = op;
    this.left = left;
    this.right = right;
  }

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public AstNode getLeft() {
    return left;
  }

  public void setLeft(AstNode left) {
    this.left = left;
  }

  public AstNode getRight() {
    return right;
  }

  public void setRight(AstNode right) {
    this.right = right;
  }
}
