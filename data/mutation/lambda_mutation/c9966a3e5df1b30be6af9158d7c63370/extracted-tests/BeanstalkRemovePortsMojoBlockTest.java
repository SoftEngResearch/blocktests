package com.github.davidmoten.aws.maven;

import com.amazonaws.services.ec2.model.*;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClientBuilder;
import com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentResourcesRequest;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.davidmoten.aws.maven.BeanstalkRemovePortsMojo.*;

public class BeanstalkRemovePortsMojoBlockTest {

    @Test
    public void testLine105() throws Exception {
        SecurityGroupRule x = new SecurityGroupRule();
        Map<String, List<String>> securityGroupRules = new HashMap<>();
        x.setGroupId("foo");
        x.setSecurityGroupRuleId("bar");
        List<String> list = securityGroupRules.get(x.getGroupId());
        if (list == null) {
            list = new ArrayList<>();
            securityGroupRules.put(x.getGroupId(), list);
        }
        assertEquals("bar", securityGroupRules.get("foo").iterator().next());
    }

    @Test
    public void testLine109() throws Exception {
        SecurityGroupRule x = new SecurityGroupRule();
        Map<String, List<String>> securityGroupRules = new HashMap<>();
        x.setGroupId("key");
        x.setSecurityGroupRuleId("ccc");
        List<String> ll = new ArrayList<>();
        ll.add("aaa");
        ll.add("bbb");
        securityGroupRules.put("key", ll);
        List<String> list = securityGroupRules.get(x.getGroupId());
        if (list == null) {
            list = new ArrayList<>();
            securityGroupRules.put(x.getGroupId(), list);
        }
        assertEquals("[aaa, bbb, ccc]", securityGroupRules.get("key").toString());
        assertEquals(3, securityGroupRules.get("key").size());
    }
}
