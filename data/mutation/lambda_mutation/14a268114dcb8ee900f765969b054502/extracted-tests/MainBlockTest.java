package com.heroku.deployer;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import com.heroku.deployer.resolver.ApiKeyResolver;
import com.heroku.deployer.util.*;
import com.heroku.deployer.deployment.Deployer;
import com.heroku.deployer.deployment.DeploymentDescriptor;
import com.heroku.deployer.resolver.WebappRunnerResolver;
import com.heroku.deployer.sourceblob.SourceBlobDescriptor;
import com.heroku.deployer.sourceblob.SourceBlobPackager;
import org.apache.commons.codec.digest.DigestUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.heroku.deployer.Main.*;

public class MainBlockTest {

    @Test
    public void testLine98() throws Exception {
        Path projectDirectory = Paths.get(System.getProperty("user.dir"));
        Path path = Paths.get(Paths.get(System.getProperty("user.dir")).toString(), "foo", "baz", "..", "bar");
        SourceBlobDescriptor sourceBlobDescriptor = new SourceBlobDescriptor();
        Path includedPath = Paths.get(Paths.get(System.getProperty("user.dir")).toString(), "foo");
        Optional<Path> normalizedPath = PathUtils.normalize(projectDirectory, path);
        if (normalizedPath.isPresent()) {
            sourceBlobDescriptor.addLocalPath(normalizedPath.get(), path, false);
        } else {
            System.err.printf("Error: can't include path '%s': normalization failed!\n", includedPath);
            System.exit(-1);
        }
        assertTrue(sourceBlobDescriptor.containsPath(Paths.get("foo/bar")));
        assertTrue(sourceBlobDescriptor.getContents().containsKey(Paths.get("foo/bar")));
    }

    @Test
    public void testLine1() throws Exception {
        Path projectDirectory = Paths.get("/abc");
        Path path = Paths.get("/def");
        SourceBlobDescriptor sourceBlobDescriptor = new SourceBlobDescriptor();
        Path includedPath = Paths.get("bar");
        Optional<Path> normalizedPath = PathUtils.normalize(projectDirectory, path);
        if (normalizedPath.isPresent()) {
            sourceBlobDescriptor.addLocalPath(normalizedPath.get(), path, false);
        } else {
            System.err.printf("Error: can't include path '%s': normalization failed!\n", includedPath);
        }
        assertFalse(sourceBlobDescriptor.containsPath(Paths.get("foo/bar")));
        assertFalse(sourceBlobDescriptor.getContents().containsKey(Paths.get("foo/bar")));
    }
}
