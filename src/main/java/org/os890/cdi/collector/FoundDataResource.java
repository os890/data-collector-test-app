/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.os890.cdi.collector;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

/**
 * JAX-RS resource that exposes the list of discovered bean classes as plain text.
 *
 * <p>Deployed as a stateless EJB so that it benefits from container-managed
 * transactions and thread pooling in a Jakarta EE application server.</p>
 */
@Stateless
@Path("found")
public class FoundDataResource {

    @Inject
    private DataHolder dataHolder;

    /**
     * Returns a plain-text listing of all classes discovered by
     * {@link DataCollectorExtension}.
     *
     * @return a human-readable report with a count header followed by
     *         fully-qualified class names, one per line
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String exposeFoundInfo() {
        List<Class<?>> foundInfo = dataHolder.getFoundInfo();
        String newLine = System.getProperty("line.separator");
        StringBuilder result = new StringBuilder("number of found entries: " + foundInfo.size() + newLine + newLine);

        for (Class<?> foundClass : foundInfo) {
            result.append(foundClass.getName()).append(newLine);
        }
        return result.toString();
    }
}
