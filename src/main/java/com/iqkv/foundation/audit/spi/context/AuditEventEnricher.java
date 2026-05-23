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
import com.iqkv.foundation.audit.model.event.AuditableEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for enriching domain events with audit context.
 */
public final class AuditEventEnricher {

  private static final Logger log = LoggerFactory.getLogger(AuditEventEnricher.class);

  private AuditEventEnricher() {
    // Utility class
  }

  /**
   * Enriches the given payload with the current audit context if it implements {@link AuditableEvent}.
   *
   * @param payload the event payload to enrich
   */
  public static void enrich(Object payload) {
    if (payload instanceof AuditableEvent auditable) {
      final AuditActor actor = AuditContextHolder.getContext();
      if (actor != null) {
        auditable.setActor(actor);
        log.debug("Enriched event with audit actor: ip={}, ua={}", actor.ipAddress(), actor.userAgent());
      }
    }
  }
}
