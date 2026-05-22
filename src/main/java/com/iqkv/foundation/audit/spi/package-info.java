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

/**
 * Service Provider Interface (SPI) for the Audit subsystem.
 *
 * <p>This module defines the contracts and abstractions that allow different
 * audit log implementations to be plugged into the IQKV platform without
 * tight coupling.
 *
 * <h2>Core Philosophy</h2>
 * <p>The IQKV platform follows the principle of flexibility and "no vendor lock-in".
 * This SPI package provides the official contract that any audit provider
 * (default implementation, custom solution, or third-party integration)
 * must follow.
 *
 * <h2>Main Components</h2>
 * <ul>
 *   <li>{@link AuditLogService} — Primary interface for logging and querying activity</li>
 *   <li>{@link AuditEventPublisher} — Used by other services (IAM, Billing, etc.) to publish events</li>
 *   <li>{@link AuditProvider} — Marker interface for Spring Boot auto-configuration</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <p>Services should depend on this SPI module rather than any concrete implementation.
 * The actual provider is selected via configuration:
 * </p>
 * <pre>
 * iqkv:
 *   audit:
 *     provider: default    # or: elasticsearch, custom, none
 * </pre>
 *
 */
package com.iqkv.foundation.audit.spi;
