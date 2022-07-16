package com.formkiq.graalvm.processors;

import com.formkiq.graalvm.annotations.Reflectable;

/** Test3 class. */
@Reflectable
public class Test3 {

  /** Foo. */
  @Reflectable private String foo;

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
   * Dummy method.
   *
   * @param foobar {@link String}
   */
  @Reflectable
  public void foo(final String foobar) {
    // empty
  }
}
