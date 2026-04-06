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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link DataHolder}.
 */
class DataHolderTest {

    /**
     * Verifies that a new {@link DataHolder} starts with an empty list.
     */
    @Test
    void newInstanceHasEmptyList() {
        DataHolder holder = new DataHolder();
        assertNotNull(holder.getFoundInfo());
        assertTrue(holder.getFoundInfo().isEmpty());
    }

    /**
     * Verifies that {@link DataHolder#addFoundInfo(List)} appends classes.
     */
    @Test
    void addFoundInfoAppendsClasses() {
        DataHolder holder = new DataHolder();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(String.class);
        classes.add(Integer.class);

        holder.addFoundInfo(classes);

        assertEquals(2, holder.getFoundInfo().size());
        assertEquals(String.class, holder.getFoundInfo().get(0));
        assertEquals(Integer.class, holder.getFoundInfo().get(1));
    }

    /**
     * Verifies that multiple calls to {@link DataHolder#addFoundInfo(List)} accumulate entries.
     */
    @Test
    void addFoundInfoAccumulatesAcrossCalls() {
        DataHolder holder = new DataHolder();
        List<Class<?>> first = new ArrayList<>();
        first.add(String.class);
        holder.addFoundInfo(first);

        List<Class<?>> second = new ArrayList<>();
        second.add(Integer.class);
        holder.addFoundInfo(second);

        assertEquals(2, holder.getFoundInfo().size());
    }
}
