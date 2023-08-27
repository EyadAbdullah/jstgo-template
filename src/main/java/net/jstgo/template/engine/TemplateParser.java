package net.jstgo.template.engine;

import com.google.gson.Gson;
import net.jstgo.template.ast.node.AstArrayExpr;
import net.jstgo.template.ast.node.AstAssignmentExpr;
import net.jstgo.template.ast.node.AstBinaryExpr;
import net.jstgo.template.ast.node.AstBlockStatementExpr;
import net.jstgo.template.ast.node.AstBoolean;
import net.jstgo.template.ast.node.AstForOfExpr;
import net.jstgo.template.ast.node.AstFunctionCall;
import net.jstgo.template.ast.node.AstIdentifier;
import net.jstgo.template.ast.node.AstIfExpr;
import net.jstgo.template.ast.node.AstMemberExpr;
import net.jstgo.template.ast.node.AstNode;
import net.jstgo.template.ast.node.AstNumber;
import net.jstgo.template.ast.node.AstObjectExpr;
import net.jstgo.template.ast.node.AstObjectPropertyExpr;
import net.jstgo.template.ast.node.AstProgram;
import net.jstgo.template.ast.node.AstString;
import net.jstgo.template.ast.node.AstVariableDeclarator;
import net.jstgo.template.tokenizer.TplToken;
import net.jstgo.template.tokenizer.TplTokenType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class TemplateParser {

  public static final Map<String, Integer> PRECEDENCE = new HashMap<>();
  public static final String[] OPERATOR = new String[]{
      "+", "-", "*", "/", "%",
      "<", ">", "<=", ">=", "==", "!=",
      "=", "+=", "-=", "*=", "/=",
      ".", ":", "?", "&", "|", "!", "&&", "||"
  };

  static {
    PRECEDENCE.putAll(Map.of(
        "%=", 0,
        "/=", 0,
        "*=", 0,
        "-=", 0,
        "+=", 0,
        "=", 0
    ));
    PRECEDENCE.putAll(Map.of(
        ":", 1,
        "?", 1,
        "||", 2,
        "&&", 3,
        "==", 4,
        "!=", 4
    ));
    PRECEDENCE.putAll(Map.of(
        "<", 5,
        "<=", 5,
        ">", 5,
        ">=", 5
    ));
    PRECEDENCE.putAll(Map.of(
        "+", 6,
        "-", 6,
        "*", 7,
        "/", 7,
        "%", 7
    ));
    PRECEDENCE.putAll(Map.of(
        "!", 8,
        "++", 8,
        "--", 8
    ));
    PRECEDENCE.putAll(Map.of(
        ".", 9
    ));
  }

  private Iterator<TplToken> tokens;
  private TplToken token;

  /*-----------------------------------
   *         AST PARSERS
   *----------------------------------- */

  public AstNode parse(List<TplToken> tokens) {
    this.tokens = tokens.iterator();
    var body = new ArrayList<AstNode>();
    while (this.tokens.hasNext()) {
      nextToken();
      if (isPun(";")) {
        continue;
      }
      body.add(maybeBinary(parseAtom(), 0));
    }
    return new AstProgram(body);
  }

  public AstNode parseAtom() {
    return parseAtom(0);
  }

  public AstNode parseAtom(int initialPrecedence) {
    if (isPun("(")) {
      skipPun("(");
      var node = maybeBinary(parseAtom(), initialPrecedence);
      skipPun(")");
      return node;
    }
    if (isPun("{")) {
      return parseObjectOrBlock();
    }
    if (isPun("[")) {
      return parseArray();
    }
    if (isType(TplTokenType.STRING)) {
      return parseString();
    }
    if (isType(TplTokenType.NUMBER)) {
      return parseNumber();
    }
    if (isType(TplTokenType.WORD)) {
      var id = parseIdentifier();
      nextToken();
      if (isPun("(")) {
        return maybeBinary(parseFunctionCalls(id), initialPrecedence);
      } else if (isPun("[")) {
        skipPun("[");
        var expr = parseMemberExpr(id, maybeBinary(parseAtom(), initialPrecedence));
        skipPun("]");
        return maybeBinary(expr, initialPrecedence);
      } else if (isPun(":")) {
        skipPun(":");
        return parseProperty(id, maybeBinary(parseAtom(), initialPrecedence));
      }
      return maybeBinary(id, initialPrecedence);
    }
    if (isKw("false", "true")) {
      return parseBoolean();
    }
    if (isKw("let", "var")) {
      var kw = this.token.getValue();
      skipKw();
      return parseVariable(kw, maybeBinary(parseAtom(), initialPrecedence));
    }
    if (isKw("if")) {
      skipKw();
      return parseIf();
    }
    if (isKw("for")) {
      skipKw();
      return parseForOf();
    }
    throw new IllegalStateException(
        String.format("Error: couldn't parse token: '%s'", new Gson().toJson(this.token)));
  }

  /**
   * let var = 'dummy value'
   */
  private AstNode parseVariable(String type, AstNode init) {
    return new AstVariableDeclarator(type, init);
  }

  /**
   * { console.log(); let var = 'dummy value'; }
   */
  private AstNode parseBlockStatement(List<AstNode> elements) {
    return new AstBlockStatementExpr(elements);
  }

  /**
   * { prop: 'value', prop2: false }
   */
  private AstNode parseObject(List<AstNode> properties) {
    return new AstObjectExpr(properties);
  }

  /**
   * prop: 'value'
   */
  private AstNode parseProperty(AstNode identifier, AstNode obj) {
    return new AstObjectPropertyExpr(identifier, obj);
  }


  private AstNode parseString() {
    var str = this.token.getValue();
    return new AstString(str);
  }

  private AstNode parseBoolean() {
    var bool = this.token.getValue();
    return new AstBoolean(Boolean.parseBoolean(bool));
  }

  private AstNode parseNumber() {
    var bool = this.token.getValue();
    return new AstNumber(Double.parseDouble(bool));
  }

  private AstNode parseIdentifier() {
    var init = this.token.getValue();
    return new AstIdentifier(init);
  }

  private AstNode parseFunctionCalls(AstNode identifier) {
    return new AstFunctionCall(identifier, delimited("(", ")", ","));
  }

  private AstNode parseMemberExpr(AstNode identifier, AstNode obj) {
    return new AstMemberExpr(identifier, obj);
  }

  private AstNode parseArray() {
    return new AstArrayExpr(delimited("[", "]", ","));
  }

  private AstNode parseIf() {
    skipPun("(");
    var cond = maybeBinary(parseAtom(), 0);
    skipPun(")");
    var body = parseBlockStatement(delimited("{", "}", ";"));
    // validate next else
    AstNode alternative = null;
    if (isKw("else")) {
      skipKw();
      if (isKw("if")) {
        skipKw();
        alternative = parseIf();
      } else {
        alternative = parseBlockStatement(delimited("{", "}", ";"));
      }
    }
    return new AstIfExpr(cond, (AstBlockStatementExpr) body, alternative);
  }

  private AstNode parseForOf() {
    skipPun("(");
    var head = maybeBinary(parseAtom(), 0);
    skipPun(")");
    var body = parseBlockStatement(delimited("{", "}", ";"));
    return new AstForOfExpr(head, body);
  }

  private AstNode maybeBinary(AstNode left, Integer initialPrecedence) {
    if (!isPun()) { // skips any node to check if it's followed with an operator
      nextToken();
    }
    if (isOP()) {
      var operator = token.getValue();
      var precedence = PRECEDENCE.get(operator);
      if (precedence >= initialPrecedence) {
        nextToken();
        var right = maybeBinary(parseAtom(precedence), precedence);
        if (StringUtils.equalsIgnoreCase(operator, "=")) { // it's Assign
          return new AstAssignmentExpr(operator, left, maybeBinary(right, initialPrecedence));
        } else if (StringUtils.equalsIgnoreCase(operator, ".")) { // member access
          return maybeBinary(new AstMemberExpr(left, right), initialPrecedence);
        } else { // it's Binary
          return maybeBinary(new AstBinaryExpr(operator, left, right), initialPrecedence);
        }
      }
    }
    return left;
  }

  private AstNode parseObjectOrBlock() {
    var list = new ArrayList<AstNode>();
    Boolean isObject = null; // means it's a Block
    skipPun("{");
    while (tokens.hasNext()) {
      if (isPun("}")) {
        break; // the delimited can have no parameters to separate
      }
      if ((isPun(";") || isPun(",")) && isObject == null) {
        isObject = isPun(",");
      }
      if (isObject != null && isObject && isPun(",")) {
        skipPun(",");
        continue;
      } else if (isObject != null && !isObject && isPun(";")) {
        skipPun(";");
        continue;
      }
      list.add(maybeBinary(parseAtom(), 0));
      if (isPun("}")) {
        break; // skip after array
      }
    }
    skipPun("}");
    if (isObject == null) {
      throw new IllegalStateException("unknown syntax!");
    }
    if (isObject) {
      return new AstObjectExpr(list);
    } else {
      return new AstBlockStatementExpr(list);
    }
  }

  /*-----------------------------------
   *         AST PARSERS UTILS
   *----------------------------------- */

  private List<AstNode> delimited(String start, String stop, String separator) {
    var list = new ArrayList<AstNode>();
    skipPun(start);
    while (tokens.hasNext()) {
      if (isPun(stop)) {
        break; // the delimited can have no parameters to separate
      } else if (isPun(separator)) { // skip separators
        skipPun(separator);
        continue;
      }
      list.add(maybeBinary(parseAtom(), 0));
      if (isPun(stop)) {
        break; // skip after array
      }
    }
    skipPun(stop);
    return list;
  }

  /*-----------------------------------
   *         AST TOKEN UTILS
   *----------------------------------- */

  private boolean isOP() {
    return token.getType() == TplTokenType.PUNCTUATION
        && ArrayUtils.contains(OPERATOR, token.getValue());
  }

  private boolean isPun() {
    return token.getType() == TplTokenType.PUNCTUATION;
  }

  private boolean isPun(String... pun) {
    return token.getType() == TplTokenType.PUNCTUATION
        && ArrayUtils.contains(pun, token.getValue());
  }

  private boolean isKw(String... keyword) {
    return token.getType() == TplTokenType.KEYWORD
        && ArrayUtils.contains(keyword, token.getValue());
  }

  private boolean isType(TplTokenType type) {
    return token.getType() == type;
  }

  private void skipKw() {
    if (token.getType() == TplTokenType.KEYWORD) {
      nextToken();
    }
  }

  private void skipPun(String... str) {
    if (token != null &&
        token.getType() == TplTokenType.PUNCTUATION &&
        ArrayUtils.contains(str, token.getValue())) {
      nextToken();
    }
  }

  private void nextToken() {
    if (this.tokens.hasNext()) {
      this.token = this.tokens.next();
    }
  }
}
