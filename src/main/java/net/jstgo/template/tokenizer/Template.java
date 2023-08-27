package net.jstgo.template.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jstgo.template.functions.TplFunction;
import net.jstgo.template.tags.TagResult;

public class Template {

  private String source;
  private String output;
  private List<TagResult> tags = new ArrayList<>();
  private List<TplFunction> functions = new ArrayList<>();
  private Map<String, Object> variables = new HashMap<>(); // global variables

  public Template() {
  }

  public Template(String source) {
    this.source = source;
  }

  public Template(String source, List<TagResult> tags) {
    this.source = source;
    this.tags = tags;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public List<TagResult> getTags() {
    return tags;
  }

  public void setTags(List<TagResult> tags) {
    this.tags = tags;
  }

  public List<TplFunction> getFunctions() {
    return functions;
  }

  public void setFunctions(List<TplFunction> functions) {
    this.functions = functions;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }
}
