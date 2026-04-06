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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeShutdown;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

import java.util.ArrayList;
import java.util.List;

/**
 * CDI portable extension that collects information about discovered bean classes.
 *
 * <p>During bean discovery this extension records every class whose package starts
 * with {@code org.apache.deltaspike} or {@code org.os890}. It then programmatically
 * registers a singleton {@link DataHolder} bean that exposes the collected classes
 * at runtime.</p>
 *
 * <p>The {@link DataHolder} annotated type is vetoed so that the container does not
 * create a default managed bean for it; instead the extension registers a custom
 * bean instance via the CDI {@code BeanConfigurator} API.</p>
 */
public class DataCollectorExtension implements Extension {

    private List<Class<?>> foundInfo = new ArrayList<>();

    /**
     * Observes every annotated type during bean discovery.
     *
     * <p>Vetoes {@link DataHolder} so it can be registered programmatically later,
     * and collects class references for packages of interest.</p>
     *
     * @param processAnnotatedType the CDI discovery event for the current type
     */
    public void onProcessAnnotatedType(@Observes ProcessAnnotatedType<?> processAnnotatedType) {
        Class<?> beanClass = processAnnotatedType.getAnnotatedType().getJavaClass();
        if (beanClass.equals(DataHolder.class)) {
            processAnnotatedType.veto();
        } else if (beanClass.getName().startsWith("org.apache.deltaspike") || beanClass.getName().startsWith("org.os890")) {
            if (!beanClass.isAnnotation() && !beanClass.isEnum()) {
                foundInfo.add(beanClass);
            }
        }
    }

    /**
     * Registers a programmatic {@link DataHolder} bean after bean discovery completes.
     *
     * <p>The bean is application-scoped and pre-populated with the collected class
     * information.</p>
     *
     * @param adb         the after-bean-discovery event
     * @param beanManager the CDI bean manager
     */
    public void onAfterBeanDiscovery(@Observes AfterBeanDiscovery adb, BeanManager beanManager) {
        final DataHolder dataHolder = new DataHolder();
        dataHolder.addFoundInfo(foundInfo);
        adb.<DataHolder>addBean()
                .beanClass(DataHolder.class)
                .addType(DataHolder.class)
                .addType(Object.class)
                .scope(ApplicationScoped.class)
                .createWith(ctx -> dataHolder);
    }

    /**
     * Clears the collected class list when the CDI container shuts down.
     *
     * @param bs the before-shutdown event
     */
    public void onBeforeShutdown(@Observes BeforeShutdown bs) {
        foundInfo.clear(); //don't clear it in onAfterBeanDiscovery to check if it's just called once
    }
}
