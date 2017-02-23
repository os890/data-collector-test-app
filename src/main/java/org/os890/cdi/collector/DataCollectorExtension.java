/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.cdi.collector;

import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.ContextualLifecycle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;
import java.util.List;

public class DataCollectorExtension implements Extension {
    private List<Class> foundInfo = new ArrayList<Class>();

    public void onProcessAnnotatedType(@Observes ProcessAnnotatedType processAnnotatedType) {
        Class<?> beanClass = processAnnotatedType.getAnnotatedType().getJavaClass();
        if (beanClass.equals(DataHolder.class)) {
            processAnnotatedType.veto();
        } else if (beanClass.getName().startsWith("org.apache.deltaspike") || beanClass.getName().startsWith("org.os890")) {
            if (!beanClass.isAnnotation()) {
                foundInfo.add(beanClass);
            }
        }
    }

    public void onAfterBeanDiscovery(@Observes AfterBeanDiscovery adb, BeanManager beanManager) {
        final DataHolder dataHolder = new DataHolder();
        dataHolder.addFoundInfo(foundInfo);
        adb.addBean(new BeanBuilder<DataHolder>(beanManager).beanClass(DataHolder.class)
                .beanLifecycle(new ContextualLifecycle<DataHolder>() {
                    @Override
                    public DataHolder create(Bean<DataHolder> bean, CreationalContext<DataHolder> creationalContext) {
                        return dataHolder;
                    }

                    @Override
                    public void destroy(Bean<DataHolder> bean, DataHolder dataHolder, CreationalContext<DataHolder> creationalContext) {

                    }
                }).scope(ApplicationScoped.class).create());
    }

    public void onBeforeShutdown(@Observes BeforeShutdown bs) {
        foundInfo.clear(); //don't clear it in onAfterBeanDiscovery to check if it's just called once
    }
}
