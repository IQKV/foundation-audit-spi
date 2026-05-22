/*
 * Copyright 2026 IQKV Foundation Team.
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

package com.iqkv.foundation.audit.spi.context;

import com.iqkv.foundation.audit.model.event.AuditActor;

/**
 * Thread-local holder for the audit context.
 * Used to propagate client IP and User-Agent across the service layer.
 */
public final class AuditContextHolder {

  private static final ThreadLocal<AuditActor> CONTEXT = new ThreadLocal<>();

  private AuditContextHolder() {
    // Utility class
  }

  /**
   * Sets the audit actor for the current thread.
   *
   * @param actor the actor to set
   */
  public static void setContext(AuditActor actor) {
    CONTEXT.set(actor);
  }

  /**
   * Returns the audit actor for the current thread.
   *
   * @return the actor, or null if not set
   */
  public static AuditActor getContext() {
    return CONTEXT.get();
  }

  /**
   * Clears the context for the current thread.
   */
  public static void clearContext() {
    CONTEXT.remove();
  }
}
