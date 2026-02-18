package org.blocktest;

import org.apache.maven.artifact.versioning.ComparableVersion;
import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.model.ConfigurationContainer;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Named
@Singleton
public class BlockTestExtension extends AbstractEventSpy {

    private static final String MOP_AGENT_STRING = "-javaagent:${settings.localRepository}/javamop-agent/" +
            "javamop-agent/1.0/javamop-agent-1.0.jar";

    private void updateConfig(Xpp3Dom config) {
        Xpp3Dom argLine = config.getChild("argLine");

        String jvmOptions = System.getenv("ARG_LINE") != null ? System.getenv("ARG_LINE") : "";
        boolean collectTraces = System.getenv("COLLECT_TRACES") != null && System.getenv("COLLECT_TRACES").equals("1");
        boolean ajcCache = System.getenv("AJC_CACHE") != null && System.getenv("AJC_CACHE").equals("1");
        if (collectTraces)
            jvmOptions = jvmOptions + " -Xmx500g -XX:-UseGCOverheadLimit";
        if (ajcCache)
            jvmOptions = jvmOptions + " -Daj.weaving.cache.enabled=true -Daj.weaving.cache.dir=/tmp/aspectj-cache/";

        String agentPath = System.getenv("MOP_AGENT_PATH") != null ? System.getenv("MOP_AGENT_PATH") : MOP_AGENT_STRING;

        if (argLine != null) {
            if (!jvmOptions.isEmpty()) {
                argLine.setValue(argLine.getValue() + " " + agentPath + jvmOptions);
            } else {
                argLine.setValue(argLine.getValue() + " " + agentPath);
            }
        } else {
            argLine = new Xpp3Dom("argLine");
            if (!jvmOptions.isEmpty()) {
                argLine.setValue(agentPath + jvmOptions);
            } else {
                argLine.setValue(agentPath);
            }
            config.addChild(argLine);
        }
    }

    private void updateSurefireVersion(Plugin plugin) {
        if (!plugin.getGroupId().equals("org.apache.maven.plugins") ||
                !plugin.getArtifactId().equals("maven-surefire-plugin")) {
            // Not Surefire
            return;
        }

        if (System.getenv("SUREFIRE_VERSION") != null && !System.getenv("SUREFIRE_VERSION").equals("default")) {
            plugin.setVersion(System.getenv("SUREFIRE_VERSION"));

            System.out.println("BlockTestExtension: changed surefire version to " + plugin.getVersion());
        } else if (System.getenv("SUREFIRE_VERSION") != null && System.getenv("SUREFIRE_VERSION").equals("default")) {
            // getVersion will return null for project romix/java-concurrent-hash-trie-map
            String pluginVersion = plugin.getVersion() == null ? "0" : plugin.getVersion();
            ComparableVersion surefireVersion = new ComparableVersion(pluginVersion);
            ComparableVersion reasonableVersion = new ComparableVersion("3.1.2");
            if (surefireVersion.compareTo(reasonableVersion) < 0) {
                // Surefire is outdated, update it to `reasonableVersion`
                plugin.setVersion("3.1.2");
            }

            System.out.println("BlockTestExtension: changed surefire version to " + plugin.getVersion());
        }
    }

    private void checkAndUpdateConfiguration(ConfigurationContainer container) {
        Xpp3Dom configNode = (Xpp3Dom) container.getConfiguration();
        if (configNode == null) {
            configNode = new Xpp3Dom("configuration");
            container.setConfiguration(configNode);
        }
        updateConfig(configNode);
    }

    private void updateSurefire(MavenProject project) {
        for (Plugin plugin : project.getBuildPlugins()) {
            if (plugin.getGroupId().equals("org.apache.maven.plugins") &&
                    plugin.getArtifactId().equals("maven-surefire-plugin")) {
                System.out.println("BlockTestExtension: checking surefire version...");
                updateSurefireVersion(plugin);

                if (System.getenv("ADD_AGENT") == null || !System.getenv("ADD_AGENT").equals("1"))
                    continue;

                System.out.println("BlockTestExtension: checking agent...");
                checkAndUpdateConfiguration(plugin);

                for (PluginExecution exe : plugin.getExecutions()) {
                    checkAndUpdateConfiguration(exe);
                }
            }
        }
    }

    private void addJUnit(MavenProject project) {
        if (System.getenv("ADD_JUNIT") == null || !System.getenv("ADD_JUNIT").equals("1"))
            return;

        boolean foundJUnit = false;
        for (Plugin plugin : project.getBuildPlugins()) {
            if (plugin.getGroupId().equals("junit") ||
                    plugin.getGroupId().equals("org.junit.jupiter")) {
                foundJUnit = true;
            }
        }
        if (foundJUnit) {
            return;
        }

        System.out.println("BlockTestExtension: adding junit dependency");

        List<Dependency> dependencies = project.getDependencies();
        Dependency junit = new Dependency();
        junit.setGroupId("junit");
        junit.setArtifactId("junit");
        junit.setVersion("4.13.2");
        junit.setScope("test");
        dependencies.add(junit);
    }
    
    private void addJaCoCoPlugin(MavenProject project) {
        if (System.getenv("ADD_JACOCO") == null || !System.getenv("ADD_JACOCO").equals("1"))
            return;
        
        Plugin plugin = new Plugin();
        plugin.setGroupId("org.jacoco");
        plugin.setArtifactId("jacoco-maven-plugin");
        plugin.setVersion("0.8.11");

        List<PluginExecution> executions = new ArrayList<>();
        PluginExecution prepare_execution = new PluginExecution();
        prepare_execution.addGoal("prepare-agent");
        prepare_execution.setId("prepare");
        executions.add(prepare_execution);
        PluginExecution report_execution = new PluginExecution();
        report_execution.addGoal("report");
        report_execution.setId("report");
        report_execution.setPhase("test");
        executions.add(report_execution);
        plugin.setExecutions(executions);
        
        Xpp3Dom configNode = (Xpp3Dom) plugin.getConfiguration();
        if (configNode == null) {
            configNode = new Xpp3Dom("configuration");
            plugin.setConfiguration(configNode);
        }
        
        Xpp3Dom sourceDirectories = new Xpp3Dom("sourceDirectories");
        Xpp3Dom sourceDirectory1 = new Xpp3Dom("sourceDirectory");
        sourceDirectory1.setValue("${project.build.sourceDirectory}");
        Xpp3Dom sourceDirectory2 = new Xpp3Dom("sourceDirectory");
        sourceDirectory2.setValue("${project.build.sourceDirectory}");
        sourceDirectories.addChild(sourceDirectory1);
        sourceDirectories.addChild(sourceDirectory2);
        configNode.addChild(sourceDirectories);

        Xpp3Dom classDirectories = new Xpp3Dom("classDirectories");
        Xpp3Dom classDirectory1 = new Xpp3Dom("classDirectory");
        classDirectory1.setValue("${project.build.sourceDirectory}");
        Xpp3Dom classDirectory2 = new Xpp3Dom("classDirectory");
        classDirectory1.setValue("${project.build.sourceDirectory}");
        classDirectories.addChild(classDirectory1);
        classDirectories.addChild(classDirectory2);
        configNode.addChild(classDirectories);

        Plugin oldJaCoCo = null;
        for (Plugin p : project.getBuild().getPlugins()) {
            if (p.getGroupId().equals("org.jacoco") && p.getArtifactId().equals("jacoco-maven-plugin")) {
                oldJaCoCo = p;
            }
        }

        if (oldJaCoCo != null) {
            // Remove old JaCoCo plugin
            project.getBuild().removePlugin(oldJaCoCo);
        }

        System.out.println("BlockTestExtension: adding JaCoCo dependency");
        
        // Add new JaCoCo plugin
        project.getBuild().addPlugin(plugin);
    }

    private void addBlockTest(MavenProject project) {
        System.out.println("BlockTestExtension: adding blocktest dependency");

        List<Dependency> dependencies = project.getDependencies();
        Dependency bt = new Dependency();
        bt.setGroupId("org.blocktest");
        bt.setArtifactId("blocktest");
        bt.setVersion("1.0");
        dependencies.add(bt);
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof ExecutionEvent) {
            ExecutionEvent e = (ExecutionEvent) event;
            if (e.getType() == ExecutionEvent.Type.SessionStarted) {
                List<MavenProject> sortedProjects = e.getSession().getProjectDependencyGraph().getSortedProjects();
                for (MavenProject project : sortedProjects) {
                    addBlockTest(project);

                    addJUnit(project);
                    
                    addJaCoCoPlugin(project);

                    updateSurefire(project);
                }
            }
        }
    }
}
