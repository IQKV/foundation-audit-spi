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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.iqkv.foundation.audit.model.event.AuditActor;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Spring Web interceptor that populates the {@link AuditContextHolder} from
 * incoming X-Audit headers.
 */
public class AuditContextInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler) {
    final String ipAddress = request.getHeader("X-Audit-IP");
    final String userAgent = request.getHeader("X-Audit-UA");

    if (ipAddress != null || userAgent != null) {
      final AuditActor actor = new AuditActor(
          null, // id will be populated from security context if needed
          null, // type
          null, // email
          ipAddress,
          userAgent,
          null  // impersonatorId
      );
      AuditContextHolder.setContext(actor);
    }

    return true;
  }

  @Override
  public void afterCompletion(@NonNull HttpServletRequest request,
                              @NonNull HttpServletResponse response,
                              @NonNull Object handler,
                              Exception ex) {
    AuditContextHolder.clearContext();
  }
}
