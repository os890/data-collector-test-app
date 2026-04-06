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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link FoundDataResource}.
 *
 * <p>Tests the resource's formatting logic by manually injecting a
 * pre-populated {@link DataHolder} via reflection.</p>
 */
class FoundDataResourceTest {

    private FoundDataResource resource;

    /**
     * Creates a {@link FoundDataResource} with a manually injected {@link DataHolder}.
     *
     * @throws Exception if reflective access fails
     */
    @BeforeEach
    void setUp() throws Exception {
        DataHolder dataHolder = new DataHolder();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(String.class);
        classes.add(Integer.class);
        dataHolder.addFoundInfo(classes);

        resource = new FoundDataResource();
        Field field = FoundDataResource.class.getDeclaredField("dataHolder");
        field.setAccessible(true);
        field.set(resource, dataHolder);
    }

    /**
     * Verifies that the output starts with the expected count header.
     */
    @Test
    void exposeFoundInfoContainsCountHeader() {
        String result = resource.exposeFoundInfo();
        assertNotNull(result);
        assertTrue(result.startsWith("number of found entries: 2"),
                "Output should start with entry count header");
    }

    /**
     * Verifies that the output includes all class names.
     */
    @Test
    void exposeFoundInfoIncludesClassNames() {
        String result = resource.exposeFoundInfo();
        assertTrue(result.contains(String.class.getName()),
                "Output should contain java.lang.String");
        assertTrue(result.contains(Integer.class.getName()),
                "Output should contain java.lang.Integer");
    }
}
