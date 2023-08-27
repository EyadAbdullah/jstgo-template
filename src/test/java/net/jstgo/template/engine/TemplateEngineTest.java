package net.jstgo.template.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.jstgo.template.functions.FnConcat;
import net.jstgo.template.functions.FnDate;
import net.jstgo.template.functions.FnNewLine;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class TemplateEngineTest {

  public static final String MD_EXA = "examples/markdown-example.md";
  public static final String MD_EXA_OUT = "output/markdown-example.md";

  private String inputString = "";

  @BeforeAll
  void init() throws IOException {
    // READ TEMPLATE FILE
    var templateStream = TemplateEngineTest.class.getClassLoader().getResourceAsStream(MD_EXA);
    inputString = IOUtils.toString(templateStream, StandardCharsets.UTF_8);
  }

  @Test
  void processTemplate() {
    // HANDLE TEMPLATE FILE
    var engine = new TemplateEngine();
    // ADDING YOUR FUNCTIONS
    engine.addFunction(new FnConcat());
    engine.addFunction(new FnDate());
    engine.addFunction(new FnNewLine());
    System.out.println("start processing");
    // PASS CUSTOM VARIABLES AND RETURN RESULT OUTPUT
    var result = engine.processTemplate(inputString, customVariables());
    System.out.println("finish processing ^_0");
    // WRITE OUTPUT INTO FILE
    Assertions.assertDoesNotThrow(() -> createFile(MD_EXA_OUT, result.getOutput()));
  }

  private static void createFile(String name, String content) throws IOException {
    var file = new File(name);
    var outputStream = new FileOutputStream(file);
    IOUtils.write(content, outputStream);
    outputStream.close();
  }

  private static Map<String, Object> customVariables() {
    return new HashMap<>(Map.of(
        "randomUUID", UUID.randomUUID().toString(),
        "object", Map.of("strProperty", "some value :) ")
    ));
  }
}