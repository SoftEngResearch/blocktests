package io.fabric8.maven.docker;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import io.fabric8.maven.docker.access.DockerAccess;
import io.fabric8.maven.docker.access.DockerAccessException;
import io.fabric8.maven.docker.access.ExecException;
import io.fabric8.maven.docker.config.BuildImageConfiguration;
import io.fabric8.maven.docker.config.ConfigHelper;
import io.fabric8.maven.docker.config.DockerMachineConfiguration;
import io.fabric8.maven.docker.config.ImageConfiguration;
import io.fabric8.maven.docker.config.RegistryAuthConfiguration;
import io.fabric8.maven.docker.config.RunImageConfiguration;
import io.fabric8.maven.docker.config.VolumeConfiguration;
import io.fabric8.maven.docker.config.handler.ImageConfigResolver;
import io.fabric8.maven.docker.log.LogDispatcher;
import io.fabric8.maven.docker.log.LogOutputSpecFactory;
import io.fabric8.maven.docker.model.Container;
import io.fabric8.maven.docker.service.DockerAccessFactory;
import io.fabric8.maven.docker.service.ImagePullManager;
import io.fabric8.maven.docker.service.QueryService;
import io.fabric8.maven.docker.service.RegistryService;
import io.fabric8.maven.docker.service.RegistryService.RegistryConfig;
import io.fabric8.maven.docker.service.ServiceHub;
import io.fabric8.maven.docker.service.ServiceHubFactory;
import io.fabric8.maven.docker.util.AnsiLogger;
import io.fabric8.maven.docker.util.AuthConfigFactory;
import io.fabric8.maven.docker.util.EnvUtil;
import io.fabric8.maven.docker.util.GavLabel;
import io.fabric8.maven.docker.util.ImageNameFormatter;
import io.fabric8.maven.docker.util.MojoParameters;
import io.fabric8.maven.docker.util.NamePatternUtil;
import io.fabric8.maven.docker.util.ProjectPaths;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.apache.maven.shared.filtering.MavenFileFilter;
import org.apache.maven.shared.filtering.MavenReaderFilter;
import org.apache.maven.shared.utils.logging.MessageUtils;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.fusesource.jansi.Ansi;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.fabric8.maven.docker.AbstractDockerMojo.*;

public class AbstractDockerMojoBlockTest {

    @Test
    public void testLine503() throws Exception {
        ImageConfiguration config = new ImageConfiguration();
        java.lang.String alias = "foo";
        ImmutableList.Builder<ImageConfiguration> allImages = ImmutableList.builder();
        if (config.getAlias() == null) {
        }
        allImages.add(config);
        assertTrue(allImages.build().size() > 0);
    }

    @Test
    public void testLine505() throws Exception {
        ImageConfiguration config = new ImageConfiguration();
        java.lang.String alias = "foo";
        ImmutableList.Builder<ImageConfiguration> allImages = ImmutableList.builder();
        if (config.getAlias() == null) {
        }
        allImages.add(config);
        assertEquals("foo", allImages.build().iterator().next().getAlias());
    }

    @Test
    public void testLine507() throws Exception {
        ImageConfiguration config = new ImageConfiguration();
        java.lang.String alias = "foo";
        ImmutableList.Builder<ImageConfiguration> allImages = ImmutableList.builder();
        config.setAlias("bar");
        if (config.getAlias() == null) {
        }
        allImages.add(config);
        assertEquals("bar", allImages.build().iterator().next().getAlias());
    }
}
