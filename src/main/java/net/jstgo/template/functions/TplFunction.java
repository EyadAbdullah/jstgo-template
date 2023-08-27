package net.jstgo.template.functions;

public interface TplFunction {

  String name();

  Object parse(Object... args);
}
