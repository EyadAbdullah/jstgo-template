package net.jstgo.template.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jstgo.template.ast.node.AstNode;
import net.jstgo.template.tokenizer.TplToken;

public class TagResult {

  private BaseTag tag;
  private int start; // start position, including open tag
  private int end; // end position, including close tag
  private String input; // template from start to end
  private List<TplToken> tokens = new ArrayList<>();
  private List<TagResult> children = new ArrayList<>();
  private Map<String, Object> variables = new HashMap<>(); // global variables
  private List<Object> values = new ArrayList<>(); // global variables
  private AstNode node;

  public TagResult(BaseTag type) {
    this.tag = type;
  }

  public BaseTag getTag() {
    return tag;
  }

  public void setTag(BaseTag tag) {
    this.tag = tag;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public List<TplToken> getTokens() {
    return tokens;
  }

  public void setTokens(List<TplToken> tokens) {
    this.tokens = tokens;
  }

  public List<TagResult> getChildren() {
    return children;
  }

  public void setChildren(List<TagResult> children) {
    this.children = children;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }

  public List<Object> getValues() {
    return values;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }

  public AstNode getNode() {
    return node;
  }

  public void setNode(AstNode node) {
    this.node = node;
  }
}
