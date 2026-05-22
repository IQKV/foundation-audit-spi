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

import java.util.Optional;
import java.util.UUID;

import com.iqkv.foundation.audit.model.event.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface for audit log persistence.
 */
public interface ActivityLogRepository {

  /**
   * Persists an audit event.
   *
   * @param event the event to persist
   */
  void save(AuditEvent event);

  /**
   * Finds an audit event by its ID.
   *
   * @param id event identifier
   * @return the event if found
   */
  Optional<AuditEvent> findById(UUID id);

  /**
   * Finds audit events with pagination and basic filtering.
   *
   * @param tenantKey optional tenant filter
   * @param pageable  pagination information
   * @return page of audit events
   */
  Page<AuditEvent> findAllByTenantKey(String tenantKey, Pageable pageable);
}
