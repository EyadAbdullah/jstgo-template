package net.jstgo.template.tokenizer;

public class TplToken {

  private final int start;
  private final int end;
  private final String value;
  private final TplTokenType type;

  public TplToken(int start, int end, String value, TplTokenType type) {
    this.start = start;
    this.end = end;
    this.value = value;
    this.type = type;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public String getValue() {
    return value;
  }

  public TplTokenType getType() {
    return type;
  }

  @Override
  public String toString() {
    return type + ": " + value;
  }
}
