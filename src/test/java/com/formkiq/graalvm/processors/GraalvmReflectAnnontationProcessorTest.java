/**
 * Copyright [2020] FormKiQ Inc. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.formkiq.graalvm.processors;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import org.junit.Test;

/** Unit Test for {@link GraalvmReflectAnnontationProcessor}. */
public class GraalvmReflectAnnontationProcessorTest {

  /** {@link Gson}. */
  private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  /**
   * testReflectableImport01.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testReflectableImport01() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@ReflectableImport(classes=com.formkiq.graalvm.processors.Test3.class)\n"
                        + "public class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "com.formkiq.graalvm.processors");

    assertEquals(1, map.size());
    assertEquals("com.formkiq.graalvm.processors.Test3", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.FALSE, fields.get(0).get("allowWrite"));

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(1, methods.size());
    assertEquals("foo", methods.get(0).get("name"));
    assertEquals(1, ((List<String>) methods.get(0).get("parameterTypes")).size());
    assertEquals("java.lang.String", ((List<String>) methods.get(0).get("parameterTypes")).get(0));
  }

  /**
   * testReflectableClasses01.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testReflectableClasses01() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@ReflectableClasses({@ReflectableClass("
                        + "className=com.formkiq.graalvm.processors.Test4.class,"
                        + "allDeclaredConstructors=false,\n"
                        + "    fields = {@ReflectableField(allowWrite = true, name = \"foo\")},\n"
                        + "    methods = {@ReflectableMethod(name = \"bar\", "
                        + "parameterTypes = {\"java.lang.String\"})})})\n"
                        + "public class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "com.formkiq.graalvm.processors");

    assertEquals(1, map.size());
    assertEquals("com.formkiq.graalvm.processors.Test4", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.TRUE, fields.get(0).get("allowWrite"));

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(1, methods.size());
    assertEquals("bar", methods.get(0).get("name"));
    assertEquals(1, ((List<String>) methods.get(0).get("parameterTypes")).size());
    assertEquals("java.lang.String", ((List<String>) methods.get(0).get("parameterTypes")).get(0));
  }

  /**
   * testReflectableClass01.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testReflectableClass01() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@ReflectableClass("
                        + "className=com.formkiq.graalvm.processors.Test4.class,"
                        + "allDeclaredConstructors=false,\n"
                        + "    fields = {@ReflectableField(allowWrite = true, name = \"foo\")},\n"
                        + "    methods = {@ReflectableMethod(name = \"bar\", "
                        + "parameterTypes = {\"java.lang.String\"})})\n"
                        + "public class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "com.formkiq.graalvm.processors");

    assertEquals(1, map.size());
    assertEquals("com.formkiq.graalvm.processors.Test4", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.TRUE, fields.get(0).get("allowWrite"));
    assertNull(fields.get(0).get("allowUnsafeAccess"));

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(1, methods.size());
    assertEquals("bar", methods.get(0).get("name"));
    assertEquals(1, ((List<String>) methods.get(0).get("parameterTypes")).size());
    assertEquals("java.lang.String", ((List<String>) methods.get(0).get("parameterTypes")).get(0));
  }

  /**
   * Test allowUnsafeAccess.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testReflectableClass02() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@ReflectableClass("
                        + "className=com.formkiq.graalvm.processors.Test4.class,"
                        + "allDeclaredConstructors=false,\n"
                        + "    fields = {@ReflectableField(allowUnsafeAccess = true, "
                        + "allowWrite = true, name = \"foo\")},\n"
                        + "    methods = {@ReflectableMethod(name = \"bar\", "
                        + "parameterTypes = {\"java.lang.String\"})})\n"
                        + "public class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "com.formkiq.graalvm.processors");

    assertEquals(1, map.size());

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.TRUE, fields.get(0).get("allowWrite"));
    assertEquals(Boolean.TRUE, fields.get(0).get("allowUnsafeAccess"));
  }

  /**
   * testReflectableClass03.
   *
   * @throws IOException IOException
   */
  @Test
  public void testReflectableClass03() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@ReflectableClass("
                        + "className=com.formkiq.graalvm.processors.Test5.Test5Inner.class"
                        + ")\n"
                        + "public class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "com.formkiq.graalvm.processors");

    assertEquals(1, map.size());
    assertEquals("com.formkiq.graalvm.processors.Test5$Test5Inner", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));
  }

  /**
   * test multiple @ReflectableClass.
   *
   * @throws IOException IOException
   */
  @Test
  public void testReflectableClass04() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@ReflectableClass("
                        + "className=com.formkiq.graalvm.processors.Test5.Test5Inner.class"
                        + ")\n"
                        + "@ReflectableClass("
                        + "className=com.formkiq.graalvm.processors.Test4.class"
                        + ")\n"
                        + "public class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "com.formkiq.graalvm.processors");

    assertEquals(2, map.size());
    assertEquals("com.formkiq.graalvm.processors.Test4", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    assertEquals("com.formkiq.graalvm.processors.Test5$Test5Inner", map.get(1).get("name"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(1).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(1).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(1).get("allDeclaredFields"));
  }

  /**
   * testReflectableImportFile01.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testReflectableImportFile01() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.*;\n"
                        + "@Reflectable\n"
                        + "@ReflectableImport(files=\"test.json\")\n"
                        + "final class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "test");

    assertEquals(2, map.size());
    assertEquals("sample.Test", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertNull(map.get(0).get("allPublicFields"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertNull(map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertNull(fields.get(0).get("allowWrite"));

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(1, methods.size());
    assertEquals("bar", methods.get(0).get("name"));
    assertEquals(1, ((List<String>) methods.get(0).get("parameterTypes")).size());
    assertEquals("int", ((List<String>) methods.get(0).get("parameterTypes")).get(0));

    assertEquals("test.Test", map.get(1).get("name"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(1).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(1).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(1).get("allDeclaredFields"));

    assertNull(map.get(1).get("fields"));
    assertNull(map.get(1).get("methods"));
  }

  /** Test with no annontations. */
  @Test
  public void testNoOpWithNoAnnotations() {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(JavaFileObjects.forSourceString("Test", "final class Test {}\n"));
    assertThat(compilation).succeeded();
  }

  /**
   * testClassAnnotation.
   *
   * @throws IOException IOException
   */
  @Test
  public void testClassAnnotation() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "\n"
                        + "@Reflectable\n"
                        + "final class Test { }\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "test");
    assertEquals(1, map.size());
    assertEquals("test.Test", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    assertNull(map.get(0).get("fields"));
    assertNull(map.get(0).get("methods"));
  }

  /**
   * testInnerClassAnnotation.
   *
   * @throws IOException IOException
   */
  @Test
  public void testInnerClassAnnotation() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "package test;\n"
                        + "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "\n"
                        + "@Reflectable\n"
                        + "final class Test6 {\n"
                        + "@Reflectable\n"
                        + "  public static final class Data {}\n"
                        + "}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "test");
    assertEquals(2, map.size());

    int i = 0;
    assertEquals("test.Test6", map.get(i).get("name"));
    assertEquals(Boolean.TRUE, map.get(i).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(i).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(i).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(i).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(i).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(i).get("allDeclaredFields"));
    assertNull(map.get(i).get("fields"));
    assertNull(map.get(i++).get("methods"));

    assertEquals("test.Test6$Data", map.get(i).get("name"));
    assertEquals(Boolean.TRUE, map.get(i).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(i).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(i).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(i).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(i).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(i).get("allDeclaredFields"));
    assertNull(map.get(i).get("fields"));
    assertNull(map.get(i++).get("methods"));
  }

  @SuppressWarnings({"unchecked", "resource"})
  private List<Map<String, Object>> getReflectConf(
      final Compilation compilation, final String filename)
      throws JsonSyntaxException, JsonIOException, IOException {
    Optional<JavaFileObject> file =
        compilation.generatedFile(
            StandardLocation.CLASS_OUTPUT,
            "META-INF/native-image/" + filename + "/reflect-config.json");

    List<Map<String, Object>> list = this.gson.fromJson(file.get().openReader(false), List.class);

    Collections.sort(
        list,
        new Comparator<Map<String, Object>>() {
          @Override
          public int compare(final Map<String, Object> o1, final Map<String, Object> o2) {
            return o1.get("name").toString().compareTo(o2.get("name").toString());
          }
        });

    return list;
  }

  /**
   * testClassAnnotationWithSettings.
   *
   * @throws IOException IOException
   */
  @Test
  public void testClassAnnotationWithSettings() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "\n"
                        + "@Reflectable(\n"
                        + "  allPublicConstructors = false,\n"
                        + "  allPublicMethods = false,\n"
                        + "  allPublicFields = false,\n"
                        + "  allDeclaredConstructors = true,\n"
                        + "  allDeclaredMethods = true,\n"
                        + "  allDeclaredFields = true)\n"
                        + "final class Test {}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("Test", map.get(0).get("name"));
    assertEquals(Boolean.FALSE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.FALSE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.FALSE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    assertNull(map.get(0).get("fields"));
    assertNull(map.get(0).get("methods"));
  }

  /**
   * testConstructorAnnotations.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testConstructorAnnotations() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "\n"
                        + "final class Test {\n"
                        + "  @Reflectable\n"
                        + "  public Test() {}\n"
                        + "  @Reflectable\n"
                        + "  public Test(int foo, List<String> bar) {}\n"
                        + "}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("Test", map.get(0).get("name"));
    assertNull(map.get(0).get("allPublicConstructors"));
    assertNull(map.get(0).get("allPublicMethods"));
    assertNull(map.get(0).get("allPublicFields"));
    assertNull(map.get(0).get("allDeclaredConstructors"));
    assertNull(map.get(0).get("allDeclaredMethods"));
    assertNull(map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(0, fields.size());

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(2, methods.size());
    assertEquals("<init>", methods.get(0).get("name"));
    assertTrue(((List<String>) methods.get(0).get("parameterTypes")).isEmpty());
    assertEquals("<init>", methods.get(1).get("name"));
    assertEquals(2, ((List<String>) methods.get(1).get("parameterTypes")).size());
    assertEquals("int", ((List<String>) methods.get(1).get("parameterTypes")).get(0));
    assertEquals(
        "java.util.List<java.lang.String>",
        ((List<String>) methods.get(1).get("parameterTypes")).get(1));
  }

  /**
   * testMethodAnnotations.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testMethodAnnotations() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "\n"
                        + "final class Test {\n"
                        + "  @Reflectable\n"
                        + "  public void test() {}\n"
                        + "  @Reflectable\n"
                        + "  public void testParameters(int foo, List<String> bar) {}\n"
                        + "}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("Test", map.get(0).get("name"));
    assertNull(map.get(0).get("allPublicConstructors"));
    assertNull(map.get(0).get("allPublicMethods"));
    assertNull(map.get(0).get("allPublicFields"));
    assertNull(map.get(0).get("allDeclaredConstructors"));
    assertNull(map.get(0).get("allDeclaredMethods"));
    assertNull(map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(0, fields.size());

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(2, methods.size());
    assertEquals("test", methods.get(0).get("name"));
    assertTrue(((List<String>) methods.get(0).get("parameterTypes")).isEmpty());
    assertEquals("testParameters", methods.get(1).get("name"));
    assertEquals(2, ((List<String>) methods.get(1).get("parameterTypes")).size());
    assertEquals("int", ((List<String>) methods.get(1).get("parameterTypes")).get(0));
    assertEquals(
        "java.util.List<java.lang.String>",
        ((List<String>) methods.get(1).get("parameterTypes")).get(1));
  }

  /**
   * testFieldAnnotations.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testFieldAnnotations() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "@Reflectable\n"
                        + "final class Test {\n"
                        + "  @Reflectable private List<String> foo;\n"
                        + "  @Reflectable int bar;\n"
                        + "}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("Test", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(2, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.FALSE, fields.get(0).get("allowWrite"));
    assertEquals("bar", fields.get(1).get("name"));
    assertEquals(Boolean.FALSE, fields.get(1).get("allowWrite"));
  }

  /**
   * testFieldAnnotationsWithSettings.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testFieldAnnotationsWithSettings() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "\n"
                        + "final class Test {\n"
                        + "  @Reflectable(allowWrite = true) List<String> foo;\n"
                        + "  @Reflectable int bar;\n"
                        + "}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("Test", map.get(0).get("name"));
    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(2, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.TRUE, fields.get(0).get("allowWrite"));
    assertEquals("bar", fields.get(1).get("name"));
    assertEquals(Boolean.FALSE, fields.get(1).get("allowWrite"));
  }

  /**
   * testMixedAnnotations.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testMixedAnnotations() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "Test",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "\n"
                        + "@Reflectable\n"
                        + "final class Test {\n"
                        + "  @Reflectable List<String> foo;\n"
                        + "  @Reflectable\n"
                        + "  public void test() {}\n"
                        + "  public void testParameters(int foo, List<String> bar) {}\n"
                        + "}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("Test", map.get(0).get("name"));
    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.FALSE, fields.get(0).get("allowWrite"));
    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(1, methods.size());
    assertEquals("test", methods.get(0).get("name"));
    assertTrue(((List<String>) methods.get(0).get("parameterTypes")).isEmpty());
  }

  /**
   * testMultipleSourceFiles.
   *
   * @throws IOException IOException
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testMultipleSourceFiles() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "TestOne",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "@Reflectable\n"
                        + "final class TestOne {\n"
                        + "  @Reflectable List<String> foo;\n"
                        + "  @Reflectable\n"
                        + "  public void test() {}\n"
                        + "  public void testParameters(int foo, List<String> bar) {}\n"
                        + "}\n"),
                JavaFileObjects.forSourceString(
                    "TestTwo",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "import java.util.*;\n"
                        + "@Reflectable\n"
                        + "final class TestTwo {}\n"),
                JavaFileObjects.forSourceString("TestThree", "final class TestThree {}\n"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(2, map.size());
    assertEquals("TestOne", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));

    List<Map<String, String>> fields = (List<Map<String, String>>) map.get(0).get("fields");
    assertEquals(1, fields.size());
    assertEquals("foo", fields.get(0).get("name"));
    assertEquals(Boolean.FALSE, fields.get(0).get("allowWrite"));

    List<Map<String, Object>> methods = (List<Map<String, Object>>) map.get(0).get("methods");
    assertEquals(1, methods.size());
    assertEquals("test", methods.get(0).get("name"));
    assertTrue(((List<String>) methods.get(0).get("parameterTypes")).isEmpty());

    assertEquals("TestTwo", map.get(1).get("name"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(1).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(1).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(1).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(1).get("allDeclaredFields"));
    assertNull(map.get(1).get("fields"));
    assertNull(map.get(1).get("methods"));
  }

  /**
   * testEnumAnnotations.
   *
   * @throws IOException IOException
   */
  @Test
  public void testEnumAnnotations() throws IOException {
    Compilation compilation =
        javac()
            .withProcessors(new GraalvmReflectAnnontationProcessor())
            .compile(
                JavaFileObjects.forSourceString(
                    "TestEnum",
                    "import com.formkiq.graalvm.annotations.Reflectable;\n"
                        + "@Reflectable\n"
                        + "public enum TestEnum {\n"
                        + "  REFLECTED_ENUM_ONE,\n"
                        + "  REFLECTED_ENUM_TWO\n"
                        + "}"));

    List<Map<String, Object>> map = getReflectConf(compilation, "default");

    assertEquals(1, map.size());
    assertEquals("TestEnum", map.get(0).get("name"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allPublicFields"));
    assertEquals(Boolean.FALSE, map.get(0).get("allDeclaredConstructors"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredMethods"));
    assertEquals(Boolean.TRUE, map.get(0).get("allDeclaredFields"));
  }

  /** Test Reflect Config Path. */
  @Test
  public void testGenerateReflectConfigPath01() {
    GraalvmReflectAnnontationProcessor processor = new GraalvmReflectAnnontationProcessor();
    Set<String> keys =
        Set.of("com.formkiq.graalvm.processors.Test3", "com.formkiq.graalvm.processors.Test4");
    assertEquals("com.formkiq.graalvm.processors", processor.generateReflectConfigPath(keys));
  }

  /** Test Reflect Config Path. */
  @Test
  public void testGenerateReflectConfigPath02() {
    GraalvmReflectAnnontationProcessor processor = new GraalvmReflectAnnontationProcessor();
    Set<String> keys =
        Set.of(
            "com.formkiq.graalvm.processors.Test3",
            "com.formkiq.graalvm.processors.ocr.Test5",
            "com.formkiq.graalvm.processors.Test4");
    assertEquals("com.formkiq.graalvm.processors", processor.generateReflectConfigPath(keys));
  }

  /** Test Reflect Config Path. */
  @Test
  public void testGenerateReflectConfigPath03() {
    GraalvmReflectAnnontationProcessor processor = new GraalvmReflectAnnontationProcessor();
    Set<String> keys = Set.of("com.formkiq.graalvm.processors.Test5.Test5Inner");
    assertEquals("com.formkiq.graalvm.processors", processor.generateReflectConfigPath(keys));
  }
}
