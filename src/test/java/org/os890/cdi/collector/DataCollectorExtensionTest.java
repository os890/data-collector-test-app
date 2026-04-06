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

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.os890.cdi.addon.dynamictestbean.EnableTestBeans;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CDI SE integration test for {@link DataCollectorExtension}.
 *
 * <p>Boots a CDI SE container via {@link EnableTestBeans} in whitelist mode
 * and uses {@link Instance} for {@link DataHolder} lookup to avoid ambiguous
 * resolution with the auto-mocked bean.</p>
 */
@EnableTestBeans(limitToTestBeans = true)
class DataCollectorExtensionTest {

    @Inject
    private Instance<DataHolder> dataHolders;

    /**
     * Verifies that the extension registers a {@link DataHolder} bean.
     */
    @Test
    void dataHolderIsRegistered() {
        assertFalse(dataHolders.isUnsatisfied(),
                "DataHolder should be registered by the extension");
    }

    /**
     * Verifies that the extension collects at least one class during discovery.
     */
    @Test
    void extensionCollectsClasses() {
        DataHolder dataHolder = findExtensionDataHolder();
        assertNotNull(dataHolder.getFoundInfo());
        assertFalse(dataHolder.getFoundInfo().isEmpty(),
                "Extension should have discovered at least one class");
    }

    /**
     * Verifies that the collected classes come from the expected packages.
     */
    @Test
    void collectedClassesAreFromExpectedPackages() {
        DataHolder dataHolder = findExtensionDataHolder();
        for (Class<?> clazz : dataHolder.getFoundInfo()) {
            String name = clazz.getName();
            assertTrue(name.startsWith("org.apache.deltaspike") || name.startsWith("org.os890"),
                    "Collected class should be from org.apache.deltaspike or org.os890 package: " + name);
        }
    }

    /**
     * Finds the real {@link DataHolder} populated by the extension (not a mock).
     *
     * @return the extension-registered data holder
     */
    private DataHolder findExtensionDataHolder() {
        for (DataHolder dh : dataHolders) {
            if (!dh.getFoundInfo().isEmpty()) {
                return dh;
            }
        }
        DataHolder first = dataHolders.iterator().next();
        assertNotNull(first, "At least one DataHolder should exist");
        return first;
    }
}
