package net.jstgo.repo.template.functions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FnDate implements TplFunction {

  @Override
  public String name() {
    return "getDate";
  }

  @Override
  public Object parse(Object... args) {
    var date = args[0];
    var format = args[1];
    if (date instanceof String || format instanceof String) {
      var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      var parse = LocalDate.parse(String.valueOf(date), formatter);
      return parse.toString();
    }
    return "";
  }
}
