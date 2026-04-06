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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Holds the list of bean classes discovered by {@link DataCollectorExtension}.
 *
 * <p>Instances are created and populated by the extension during container
 * bootstrap and then registered as application-scoped CDI beans so that
 * other components (such as {@link FoundDataResource}) can inject and
 * query them.</p>
 */
public class DataHolder {

    private static final Logger LOG = Logger.getLogger(DataHolder.class.getName());

    private List<Class<?>> foundInfo = new ArrayList<>();

    /**
     * Returns the list of discovered bean classes.
     *
     * @return an unmodifiable view would be safer, but the original list is
     *         returned to preserve the existing API contract
     */
    public List<Class<?>> getFoundInfo() {
        return foundInfo;
    }

    /**
     * Appends the given classes to the internal list.
     *
     * @param foundInfo the classes discovered during CDI bean discovery
     */
    public void addFoundInfo(List<Class<?>> foundInfo) {
        LOG.info("adding found info - number of entries: " + foundInfo.size());
        this.foundInfo.addAll(foundInfo);
    }
}
