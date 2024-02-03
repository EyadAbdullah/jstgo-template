package net.jstgo.repo.template.engine;

import com.google.gson.Gson;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.jstgo.repo.template.ast.node.AstArrayExpr;
import net.jstgo.repo.template.ast.node.AstAssignmentExpr;
import net.jstgo.repo.template.ast.node.AstBinaryExpr;
import net.jstgo.repo.template.ast.node.AstBlockStatementExpr;
import net.jstgo.repo.template.ast.node.AstBoolean;
import net.jstgo.repo.template.ast.node.AstForOfExpr;
import net.jstgo.repo.template.ast.node.AstFunctionCall;
import net.jstgo.repo.template.ast.node.AstIdentifier;
import net.jstgo.repo.template.ast.node.AstNode;
import net.jstgo.repo.template.ast.node.AstNumber;
import net.jstgo.repo.template.ast.node.AstObjectExpr;
import net.jstgo.repo.template.ast.node.AstObjectPropertyExpr;
import net.jstgo.repo.template.ast.node.AstProgram;
import net.jstgo.repo.template.ast.node.AstString;
import net.jstgo.repo.template.ast.node.AstVariableDeclarator;
import net.jstgo.repo.template.functions.TplFunction;
import net.jstgo.repo.template.TemplateUtils;
import net.jstgo.repo.template.ast.node.AstIfExpr;
import net.jstgo.repo.template.ast.node.AstMemberExpr;
import org.apache.commons.lang3.ArrayUtils;

public class TemplateInterpreter {

  private final Map<String, Object> variables = new HashMap<>();
  private final Map<String, Object> globalVariables = new HashMap<>();
  private final Map<String, TplFunction> functions = new HashMap<>();

  public TemplateInterpreter(List<TplFunction> functions, Map<String, Object> variables) {
    for (var func : functions) {
      this.functions.putIfAbsent(func.name(), func);
    }
    this.variables.putAll(variables);
  }

  public TemplateInterpreter(List<TplFunction> functions) {
    for (var func : functions) {
      this.functions.putIfAbsent(func.name(), func);
    }
  }

  public Map<String, Object> getGlobalVariables() {
    return globalVariables;
  }

  public List<Object> interpret(AstProgram node) {
    return node.getBody().stream().map(this::interpretNode)
        .collect(Collectors.toList());
  }

  private <T extends AstNode> Object interpretNode(T node) {
    if (node instanceof AstIdentifier) {
      var expr = ((AstIdentifier) node).getInit();
      // find if identifier already holds any values
      return variables.getOrDefault(expr, globalVariables.get(expr));
    } else if (node instanceof AstNumber) {
      return TemplateUtils.originate(((AstNumber) node).getValue());
    } else if (node instanceof AstBoolean) {
      return ((AstBoolean) node).getValue();
    } else if (node instanceof AstString) {
      return ((AstString) node).getValue();
    } else if (node instanceof AstVariableDeclarator) {
      var expr = (AstVariableDeclarator) node;
      // let = closure-scope, var = global-scope
      var type = expr.getId();
      var assignmentExpr = (AstAssignmentExpr) expr.getInit();
      var identifier = ((AstIdentifier) assignmentExpr.getLeft()).getInit();
      var value = interpretNode(assignmentExpr.getRight());
      // variables can not be defined more than once
      if (variables.containsKey(identifier) ||
          globalVariables.containsKey(identifier)) {
        throw new IllegalStateException(
            String.format("Error: variable '%s' is defined !%n%s", identifier,
                new Gson().toJson(node))
        );
      }
      if (type.equalsIgnoreCase("let")) {
        variables.putIfAbsent(identifier, value);
      } else if (type.equalsIgnoreCase("var")) {
        globalVariables.putIfAbsent(identifier, value);
      }
    } else if (node instanceof AstAssignmentExpr) {
      var expr = (AstAssignmentExpr) node;
      var identifier = ((AstIdentifier) expr.getLeft()).getInit();
      var value = interpretNode(expr.getRight());
      if (variables.containsKey(identifier)) {
        variables.put(identifier, value);
      } else if (globalVariables.containsKey(identifier)) {
        globalVariables.put(identifier, value);
      } else {
        throw new IllegalStateException(
            String.format("Error: variable '%s' is undefined !%n%s", identifier,
                new Gson().toJson(node))
        );
      }
      return value;
    } else if (node instanceof AstFunctionCall) {
      var expr = (AstFunctionCall) node;
      var identifier = ((AstIdentifier) expr.getFunc()).getInit();
      var args = expr.getArgs().stream().map(this::interpretNode).toList();
      if (!functions.containsKey(identifier)) {
        throw new IllegalStateException(
            String.format("Error: function is undefined '%s' !%n%s", identifier,
                new Gson().toJson(node))
        );
      }
      return functions.get(identifier).parse(args.toArray(new Object[0]));
    } else if (node instanceof AstBinaryExpr) {
      return TemplateUtils.originate(interpretBinary((AstBinaryExpr) node));
    } else if (node instanceof AstArrayExpr) {
      var expr = (AstArrayExpr) node;
      return expr.getElements().stream()
          .map(this::interpretNode)
          .collect(Collectors.toList());
    } else if (node instanceof AstMemberExpr) {
      var expr = (AstMemberExpr) node;
      var property = interpretNode(expr.getKey());
      return parseMember(property, expr.getValue());
    } else if (node instanceof AstObjectExpr) {
      var expr = (AstObjectExpr) node;
      return expr.getElements().stream()
          .filter(AstObjectPropertyExpr.class::isInstance)
          .map(AstObjectPropertyExpr.class::cast)
          .map(elem -> {
            var identifier = ((AstIdentifier) elem.getKey()).getInit();
            return new SimpleEntry<>(identifier, interpretNode(elem.getValue()));
          })
          .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    } else if (node instanceof AstBlockStatementExpr) {
      var expr = (AstBlockStatementExpr) node;
      return expr.getElements().stream()
          .map(this::interpretNode)
          .collect(Collectors.toList());
    } else if (node instanceof AstIfExpr) {
      var expr = (AstIfExpr) node;
      var condition = interpretNode(expr.getCondition());
      if (Boolean.TRUE.equals(condition)) {
        return interpretNode(expr.getConsequent());
      } else {
        return interpretNode(expr.getAlternative());
      }
    } else if (node instanceof AstForOfExpr) {
      interpretForOf(node);
    }
    return null;
  }

  private void interpretForOf(AstNode node) {
    var expr = (AstForOfExpr) node;
    var variableDeclarator = (AstVariableDeclarator) expr.getDefinition();
    var body = (AstBlockStatementExpr) expr.getBody();

    var definition = (AstAssignmentExpr) variableDeclarator.getInit();
    var variable = ((AstIdentifier) definition.getLeft()).getInit();
    var values = interpretNode(definition.getRight());
    if (values instanceof List) {
      for (var value : (List<?>) values) {
        variables.put(variable, value);
        interpretNode(body);
      }
    } else if (values instanceof Object[]) {
      for (var value : (Object[]) values) {
        variables.put(variable, value);
        interpretNode(body);
      }
    } else if (values instanceof Map) {
      for (var value : ((Map<?, ?>) values).entrySet()) {
        variables.put(variable, Map.of(
            "key", value.getKey(),
            "value", value.getValue()
        ));
        interpretNode(body);
      }
    }
  }

  private Object parseMember(Object values, AstNode member) {
    if (member instanceof AstMemberExpr) {
      var memberKey = ((AstMemberExpr) member).getKey();
      var memberValue = parseMember(values, memberKey);

      var identifier = ((AstMemberExpr) member).getValue();
      return parseMember(memberValue, identifier);
    } else if (member instanceof AstIdentifier) {
      return getMember(values, ((AstIdentifier) member).getInit());
    }
    return getMember(values, interpretNode(member));
  }

  public String str(Object value) {
    return value == null ? null : String.valueOf(value);
  }

  public Double num(Object value) {
    return value == null ? null : Double.parseDouble(str(value));
  }

  public Boolean bool(Object value) {
    return value == null ? null : Boolean.parseBoolean(str(value));
  }

  public boolean containsStr(Object obj1, Object obj2) {
    return obj1 instanceof String || obj2 instanceof String;
  }

  public Object getMember(Object obj, Object member) {
    if (obj instanceof List) {
      return ((List<?>) obj).get(Integer.parseInt(member.toString()));
    } else if (obj instanceof Object[]) {
      ArrayUtils.get((Object[]) obj, Integer.parseInt(member.toString()));
    } else if (obj instanceof Map) {
      return ((Map<?, ?>) obj).get(member);
    }
    return null;
  }

  // TODO: implement
  private Map<String, Object> objectToMap(Object obj) {
    var map = new HashMap<String, Object>();
    for (var field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        map.put(field.getName(), field.get(obj));
      } catch (Exception e) {
        // ignore: field not found or something
      }
    }
    return map;
  }

  public Object interpretBinary(AstBinaryExpr node) {
    var left = interpretNode(node.getLeft());
    var right = interpretNode(node.getRight());
    if (left == null || right == null) {
      return null;
    }
    return switch (node.getOp()) {
      case "+" -> containsStr(left, right)
          ? str(left) + str(right)
          : num(left) + num(right);
      case "-" -> num(left) - num(right);
      case "*" -> num(left) * num(right);
      case "/" -> num(left) / num(right);
      case "%" -> num(left) % num(right);
      case "<" -> num(left) < num(right);
      case ">" -> num(left) > num(right);
      case "<=" -> num(left) <= num(right);
      case ">=" -> num(left) >= num(right);
      case "==" -> Objects.equals(left, right);
      case "!=" -> !Objects.equals(left, right);
      case "&&" -> bool(left) && bool(right);
      case "||" -> bool(left) || bool(right);
      case "." -> getMember(left, right);
      default -> null;
//      case "!": break;
//      case "&": break;
//      case "|": break;
//      case "+=": break;
//      case "-=": break;
//      case "*=": break;
//      case "/=": break;
    };
  }
}
