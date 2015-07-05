/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tuxlib.util.json;


final class JsonStringWriter extends JsonWriterBase<Json.Writer> implements Json.Writer {
  JsonStringWriter() {
    super(new StringBuilder());
  }
  
  /**
   * Completes this JSON writing session and returns the internal representation as a {@link String}.
   */
  public String write() {
    super.doneInternal();
    return appendable.toString();
  }
  
  /**
   * Used for testing.
   */
  // TODO(mmastrac): Expose this on the Json interface
  static String toString(Object value) {
    return new JsonStringWriter().value(value).write();
  }

}
