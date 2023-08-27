package net.jstgo.template.tokenizer;

import java.util.ArrayList;
import java.util.List;
import net.jstgo.template.tags.BaseTag;
import net.jstgo.template.tags.CodeTag;
import net.jstgo.template.tags.ElseIfTag;
import net.jstgo.template.tags.ElseTag;
import net.jstgo.template.tags.EndForTag;
import net.jstgo.template.tags.ForTag;
import net.jstgo.template.tags.IfTag;
import net.jstgo.template.tags.PrintTag;
import net.jstgo.template.tags.TagResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

public class TemplateTokenizer {

  private static final String[] PUNCTUATION = new String[]{
      "+", "-", "*", "/", "%",
      "<", ">", "<=", ">=", "==", "!=",
      "=", "+=", "-=", "*=", "/=",
      "&", "|", "!", "&&", "||",
      "{", "}", "[", "]", "(", ")", ".", ",", "?", ":", ";", "_"
  };

  private static final String[] KEYWORDS = new String[]{
      "let", "var", "if", "else", "for", "while", "true", "false", "of"
  };

  private static final List<BaseTag> tags = new ArrayList<>();

  static {
    // add standard Tags
    tags.addAll(List.of(
        new PrintTag(),
        new CodeTag(),
        new IfTag(),
        new ElseIfTag(),
        new ElseTag(),
        new ForTag(),
        new EndForTag()
    ));
  }

  private TemplateTokenizer() {
  }

  public static Template handleTemplate(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    var req = new Template(str);
    int charAt = 0;
    while (inRange(str, charAt)) {
      var tagResult = findTag(str, charAt);
      if (tagResult == null) {
        charAt++;
        continue;
      }
      // set start position
      tagResult.setStart(charAt);
      // set pointer after the opening tag
      charAt += tagResult.getTag().openTag().length();
      // find and set tokens of the current tag
      findTokens(str, charAt, tagResult);
      // add tag result to the list
      req.getTags().add(tagResult);
    }
    return req;
  }

  private static void findTokens(String str, int charAt, TagResult tagResult) {
    while (inRange(str, charAt)) {
      var ch = str.charAt(charAt);
      TplToken token;
      if (isBreaker(ch)) { // word separator. new line is acceptable
        charAt++;
        continue;
      } else if (isNumber(ch)) {
        var key = getNumber(str, charAt);
        token = new TplToken(charAt, charAt + key.length(), key, TplTokenType.NUMBER);
      } else if (isString(ch)) {
        var key = getString(str, charAt);
        token = new TplToken(charAt, charAt + key.length(), key, TplTokenType.STRING);
      } else if (isWord(ch)) {
        var key = getWord(str, charAt);
        if (isKeyword(key)) {
          token = new TplToken(charAt, charAt + key.length(), key, TplTokenType.KEYWORD);
        } else {
          token = new TplToken(charAt, charAt + key.length(), key, TplTokenType.WORD);
        }
      } else if (hasTag(str, tagResult.getTag().closeTag(), charAt)) {
        // set pointer at after the close tag
        charAt += tagResult.getTag().closeTag().length();
        // set result
        tagResult.setEnd(charAt);
        tagResult.setInput(str.substring(tagResult.getStart(), tagResult.getEnd()));
        break;
      } else if (isPunctuation(String.valueOf(ch))) {
        var key = getPunctuation(str, charAt);
        token = new TplToken(charAt, charAt + key.length(), key, TplTokenType.PUNCTUATION);
      } else {
        throw new IllegalArgumentException(
            String.format("Error: Unexpected token '%s' at '%s'", ch, charAt)
        );
      }
      tagResult.getTokens().add(token);
      charAt = token.getEnd();
    }
  }

  private static TagResult findTag(String str, int charAt) {
    for (var tag : tags) {
      // validate tag
      if (hasTag(str, tag.openTag(), charAt)) {
        return new TagResult(tag);
      }
    }
    return null;
  }

  private static boolean hasTag(String str, String tag, int charAt) {
    var end = charAt + tag.length();
    return inRange(str, end) && StringUtils.equals(str.substring(charAt, end), tag);
  }

  private static boolean isNumber(char ch) {
    return isNumber(CharUtils.toString(ch));
  }

  private static boolean isNumber(String str) {
    try {
      Double.parseDouble(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private static boolean isBreaker(char ch) {
    return Character.isWhitespace(ch);
  }

  private static boolean isString(char ch) {
    return CharUtils.toString(ch).matches("[\"']");
  }

  private static boolean isWord(char ch) {
    return CharUtils.toString(ch).matches("[a-zA-Z_]");
  }

  private static boolean isKeyword(String str) {
    return ArrayUtils.contains(KEYWORDS, str);
  }

  private static boolean isWordOrNumber(char ch) {
    return isNumber(ch) || isWord(ch);
  }

  private static boolean isPunctuation(String ch) {
    return ArrayUtils.contains(PUNCTUATION, ch);
  }

  private static boolean inRange(String str, int charAt) {
    return !StringUtils.isBlank(str) && str.length() >= charAt;
  }

  private static String getNumber(String str, int charAt) {
    var number = new StringBuilder();
    while (inRange(str, charAt) && isNumber(number.toString() + str.charAt(charAt))) {
      number.append(str.charAt(charAt));
      charAt++;
    }
    return number.toString().trim();
  }

  private static String getString(String str, int charAt) {
    var string = new StringBuilder();
    if (inRange(str, charAt)) {
      string.append(str.charAt(charAt));
      charAt++;
    }
    while (inRange(str, charAt) && !isString(str.charAt(charAt))) {
      string.append(str.charAt(charAt));
      charAt++;
    }
    if (inRange(str, charAt)) {
      string.append(str.charAt(charAt));
    }
    return string.toString();
  }

  private static String getWord(String str, int charAt) {
    var word = new StringBuilder();
    // starts with word and is (word number dot) and ends with noneWord
    if (inRange(str, charAt) && isWord(str.charAt(charAt))) {
      word.append(str.charAt(charAt));
      charAt++;
    }
    while (inRange(str, charAt) && isWordOrNumber(str.charAt(charAt))) {
      word.append(str.charAt(charAt));
      charAt++;
    }
    return word.toString();
  }

  private static String getPunctuation(String str, int charAt) {
    var punctuation = new StringBuilder(String.valueOf(str.charAt(charAt)));
    charAt++;
    while (inRange(str, charAt) &&
        isPunctuation(punctuation.toString() + str.charAt(charAt))) {
      // if this is + and the next is = then its += with is in the list
      punctuation.append(str.charAt(charAt));
      charAt++;
    }
    return punctuation.toString();
  }

}
