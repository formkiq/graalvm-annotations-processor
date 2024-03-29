package com.formkiq.graalvm.processors;

import com.formkiq.graalvm.annotations.Reflectable;

/** Test6 class. */
@Reflectable
public class Test6 {

  /** Test Inner Class. */
  @Reflectable
  public static class Test6Inner {

    /** Test Nested Innter Class. */
    @Reflectable
    public static class Test6NestedInner {
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
}
