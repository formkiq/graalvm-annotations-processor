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
