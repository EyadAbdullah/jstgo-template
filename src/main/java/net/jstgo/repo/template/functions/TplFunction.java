package net.jstgo.repo.template.functions;

public interface TplFunction {

  String name();

  Object parse(Object... args);
}
