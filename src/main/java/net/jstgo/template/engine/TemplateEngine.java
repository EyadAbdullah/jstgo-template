package net.jstgo.template.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jstgo.template.TemplateUtils;
import net.jstgo.template.functions.TplFunction;
import net.jstgo.template.ast.node.AstNode;
import net.jstgo.template.ast.node.AstProgram;
import net.jstgo.template.tags.CodeTag;
import net.jstgo.template.tags.PrintTag;
import net.jstgo.template.tags.TagResult;
import net.jstgo.template.tokenizer.Template;
import net.jstgo.template.tokenizer.TemplateTokenizer;
import net.jstgo.template.tokenizer.TplToken;

public class TemplateEngine {

  private final List<TplFunction> functions = new ArrayList<>();

  public TemplateEngine(List<TplFunction> fns) {
    if (!fns.isEmpty()) {
      fns.forEach(this::addFunction);
    }
  }

  public TemplateEngine() {
  }

  public void addFunction(TplFunction function) {
    functions.add(function);
  }

  public Template processTemplate(String str) {
    return processTemplate(str, functions, new HashMap<>());
  }

  public Template processTemplate(String str, Map<String, Object> variable) {
    return processTemplate(str, functions, variable);
  }

  public Template processTemplate(String str, List<TplFunction> functions,
      Map<String, Object> variables) {
    var template = TemplateTokenizer.handleTemplate(str);
    if (template == null || template.getTags().isEmpty()) {
      return null;
    }
    template.setVariables(variables);
    template.setFunctions(functions);
    // analyze tokens
    template.getTags().forEach(tag -> tag.setNode(findAST(tag.getTokens())));
    // execute code Tags to finalize variables, and save template global variables
    template.getTags().stream()
        .filter(tag -> tag.getTag() instanceof CodeTag)
        .forEach(tag -> interpret(tag, template));
    // execute printable expressions
    template.getTags().stream()
        .filter(tag -> tag.getTag() instanceof PrintTag)
        .forEach(tag -> interpret(tag, template));

    var output = str;
    // write values from bottom page to up
    var reversed = new ArrayList<>(template.getTags());
    Collections.reverse(reversed);
    for (var tag : reversed) {
      // this kind of tags does NOT replace from variables
      if (tag.getTag().isRemovable() && tag.getTag() instanceof CodeTag) {
        output = replaceStr(output, "", tag.getStart(), tag.getEnd());
      } else // this Type is replaceable from global variables
        if (!tag.getTag().isRemovable() && tag.getTag() instanceof PrintTag) {
          // printable expressions allow a single value
          var value = tag.getValues().get(0);
          output = replaceStr(output, TemplateUtils.originate(value), tag.getStart(),
              tag.getEnd());
        }
    }
    template.setOutput(output);
    return template;
  }

  private String replaceStr(String original, Object replaced, int start, int end) {
    return original.substring(0, start) + TemplateUtils.str(replaced) + original.substring(end);
  }

  private AstNode findAST(List<TplToken> tokens) {
    return new TemplateParser().parse(tokens);
  }

  private void interpret(TagResult tag, Template template) {
    var interpreter = new TemplateInterpreter(template.getFunctions(), template.getVariables());
    if (tag.getNode() instanceof AstProgram) {
      var values = interpreter.interpret((AstProgram) tag.getNode());
      tag.setValues(values);
      tag.setVariables(interpreter.getGlobalVariables());
      template.getVariables().putAll(interpreter.getGlobalVariables());
    }
  }
}