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

package com.iqkv.foundation.audit.spi;

import com.iqkv.foundation.audit.model.event.AuditEvent;

/**
 * Interface for publishing audit events to external systems (e.g., RabbitMQ).
 */
public interface AuditEventPublisher {

  /**
   * Publishes an audit event.
   *
   * @param event the event to publish
   */
  void publish(AuditEvent event);
}
