/*
 * Copyright 2014-2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.atlas.core.validation

import com.netflix.atlas.core.util.SmallHashMap

/**
  * Helper for rules that can be checked using a single key and value pair.
  */
trait TagRule extends Rule {

  def validate(tags: SmallHashMap[String, String]): ValidationResult = {
    val iter = tags.entriesIterator
    while (iter.hasNext) {
      val result = validate(iter.key, iter.value)
      if (result != TagRule.Pass) return failure(result, tags)
      iter.nextEntry()
    }
    ValidationResult.Pass
  }

  /**
    * Check the key/value pair and return `null` if it is valid or a reason string if
    * there is a validation failure. The `null` type for success is used to avoid allocations
    * or other overhead since the validation checks tend to be a hot path.
    */
  def validate(k: String, v: String): String
}

object TagRule {

  /** Null string to make the code easier to follow when using null for a passing result. */
  val Pass: String = null
}
