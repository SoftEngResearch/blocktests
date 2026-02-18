package com.alibaba.compileflow.engine.common.extension;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.alibaba.compileflow.engine.common.DirectedGraph;
import com.alibaba.compileflow.engine.common.Lifecycle;
import com.alibaba.compileflow.engine.common.extension.annotation.Extension;
import com.alibaba.compileflow.engine.common.extension.annotation.PluginDependency;
import com.alibaba.compileflow.engine.common.extension.annotation.PluginDependsOn;
import com.alibaba.compileflow.engine.common.extension.config.PluginConfig;
import com.alibaba.compileflow.engine.common.extension.config.PluginDependencyConfig;
import com.alibaba.compileflow.engine.common.extension.constant.PluginStatus;
import com.alibaba.compileflow.engine.common.extension.descriptor.PluginDependencyDescriptor;
import com.alibaba.compileflow.engine.common.extension.exception.PluginException;
import com.alibaba.compileflow.engine.common.extension.helper.PropertyHelper;
import com.alibaba.compileflow.engine.common.util.ClassLoaderUtils;
import com.alibaba.compileflow.engine.common.util.ClassUtils;
import com.alibaba.compileflow.engine.common.util.PackageUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alibaba.compileflow.engine.common.extension.PluginManager.*;

public class PluginManagerBlockTest {

    @Test
    public void testThree() throws Exception {
        java.lang.String d = "foo";
        try {
            String[] dependencyConfig = d.split(":");
            if (ArrayUtils.isEmpty(dependencyConfig) || dependencyConfig.length != 2) {
                {
                    assertEquals(null, (null));
                    return;
                }
            }
            PluginDependencyConfig pluginDependencyConfig = new PluginDependencyConfig();
            pluginDependencyConfig.setPluginId(dependencyConfig[0]);
            {
                assertEquals(null, (pluginDependencyConfig));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testFour() throws Exception {
        java.lang.String d = "";
        try {
            String[] dependencyConfig = d.split(":");
            if (ArrayUtils.isEmpty(dependencyConfig) || dependencyConfig.length != 2) {
                {
                    assertEquals(null, (null));
                    return;
                }
            }
            PluginDependencyConfig pluginDependencyConfig = new PluginDependencyConfig();
            pluginDependencyConfig.setPluginId(dependencyConfig[0]);
            {
                assertEquals(null, (pluginDependencyConfig));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testOne() throws Exception {
        java.lang.String d = "foo:1.0.0";
        String[] dependencyConfig = d.split(":");
        PluginDependencyConfig pluginDependencyConfig = new PluginDependencyConfig();
        pluginDependencyConfig.setPluginId(dependencyConfig[0]);
    }

    @Test
    public void testTwo() throws Exception {
        java.lang.String d = "foo:bar";
        String[] dependencyConfig = d.split(":");
        PluginDependencyConfig pluginDependencyConfig = new PluginDependencyConfig();
        pluginDependencyConfig.setPluginId(dependencyConfig[0]);
    }

    @Test
    public void testLine193() throws Exception {
        PluginDependencyConfig config = new PluginDependencyConfig();
        config.setPluginId("pluginId");
        config.setPluginVersion("1.0.0");
        try {
            PluginDependencyDescriptor pluginDependencyDescriptor = new PluginDependencyDescriptor();
            pluginDependencyDescriptor.setPluginId(config.getPluginId());
            pluginDependencyDescriptor.setPluginVersion(config.getPluginVersion());
            {
                assertEquals("1.0.0", ((PluginDependencyDescriptor) (pluginDependencyDescriptor)).getPluginVersion());
                assertEquals("pluginId", ((PluginDependencyDescriptor) (pluginDependencyDescriptor)).getPluginId());
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine197() throws Exception {
        PluginDependencyConfig config = new PluginDependencyConfig();
        config.setPluginId("bye");
        config.setPluginVersion("2.0.0");
        try {
            PluginDependencyDescriptor pluginDependencyDescriptor = new PluginDependencyDescriptor();
            pluginDependencyDescriptor.setPluginId(config.getPluginId());
            pluginDependencyDescriptor.setPluginVersion(config.getPluginVersion());
            {
                assertEquals("2.0.0", ((PluginDependencyDescriptor) (pluginDependencyDescriptor)).getPluginVersion());
                assertEquals("bye", ((PluginDependencyDescriptor) (pluginDependencyDescriptor)).getPluginId());
                return;
            }
        } finally {
        }
    }
}
