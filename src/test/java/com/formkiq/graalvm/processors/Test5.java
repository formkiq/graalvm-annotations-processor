package com.formkiq.graalvm.processors;

/** Test5 class. */
public class Test5 {

  /** Test Inner Class. */
  public static class Test5Inner {
    /** Foo. */
    private String foo;

    /** Bar. */
    private String bar;

    /**
     * Get Foo.
     *
     * @return {@link String}
     */
    public String getFoo() {
      return this.foo;
    }

    /**
     * Set Foo.
     *
     * @param val {@link String}
     */
    public void setFoo(final String val) {
      this.foo = val;
    }

    /**
     * Get Bar.
     *
     * @return {@link String}
     */
    public String getBar() {
      return this.bar;
    }

    /**
     * Set Bar.
     *
     * @param val {@link String}
     */
    public void setBar(final String val) {
      this.bar = val;
    }

    /**
     * Dummy Method.
     *
     * @param foobar {@link String}
     */
    public void foo(final String foobar) {
      // empty
    }
  }
}
