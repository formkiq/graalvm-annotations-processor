package com.formkiq.graalvm.processors;

public class Test5 {

  public static class Test5Inner {
    /** Foo. */
    private String foo;

    /** Bar. */
    private String bar;

    public String getFoo() {
      return foo;
    }

    public void setFoo(final String val) {
      this.foo = val;
    }

    public String getBar() {
      return bar;
    }

    public void setBar(final String val) {
      this.bar = val;
    }

    public void foo(final String foobar) {
      // empty
    }
  }
}
