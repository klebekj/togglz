/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.togglz.spring.boot2.autoconfigure.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.assertj.AssertableWebApplicationContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.togglz.spring.boot.autoconfigure.TogglzAutoConfiguration;
import org.togglz.spring.boot2.autoconfigure.BaseTest;

/**
 * Tests for Spring Boot 2 compatible {@link TogglzAutoConfiguration}.
 *
 * @author Marcel Overdijk
 * @author Rui Figueira
 */
public class TogglzManagementContextConfigurationTest extends BaseTest {

    @Test
    public void consoleWithCustomManagementContextPath() {
        // With TogglzManagementContextConfiguration responsible for creating the admin console servlet registration bean,
        // if a custom managememnt context path is provided it should be used as prefix.
        contextRunnerWithFeatureProviderConfig()
            .withPropertyValues("management.server.servlet.context-path: /manage")
            .run((context) -> {
                assertThat(getUrlMappings(context)).contains("/manage/togglz-console/*");
            });
    }

    @Test
    public void customConsolePath() {
        contextRunnerWithFeatureProviderConfig()
            .withPropertyValues("togglz.console.path: /custom")
            .run((context) -> {
                assertThat(getUrlMappings(context)).contains("/custom/*");
            });
    }

    @Test
    public void customConsolePathWithTrailingSlash() {
        contextRunnerWithFeatureProviderConfig()
            .withPropertyValues("togglz.console.path: /custom/")
            .run((context) -> {
                assertThat(getUrlMappings(context)).contains("/custom/*");
            });
    }

    @SuppressWarnings("unchecked")
    protected Collection<String> getUrlMappings(AssertableWebApplicationContext context) throws BeansException {
        assertEquals(1, context.getBeansOfType(ServletRegistrationBean.class).size());
        return context.getBean(ServletRegistrationBean.class).getUrlMappings();
    }
}