package com.github.peterbencze.serritor.internal;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.github.peterbencze.serritor.api.Browser;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.peterbencze.serritor.internal.WebDriverFactory.*;

public class WebDriverFactoryBlockTest {

    @Test
    public void testLine103() throws Exception {
        java.lang.String key = "binary";
        Object value = "/path";
        java.util.HashMap<String, Object> extraOptions = new java.util.HashMap<String, Object>() {

            {
                put("binary", "/path");
            }
        };
        ChromeOptions options = new ChromeOptions();
        if ("binary".equals(key)) {
            options.setBinary((String) extraOptions.get("binary"));
        } else if ("args".equals(key)) {
            options.addArguments((List<String>) value);
        } else if ("extensions".equals(key)) {
            options.addEncodedExtensions((List<String>) value);
        } else {
        }
        assertEquals("{browserName=chrome, goog:chromeOptions={args=[], binary=/path, extensions=[]}}", options.asMap().toString());
    }

    @Test
    public void testLine107() throws Exception {
        java.lang.String key = "args";
        Object value = new java.util.ArrayList<>(java.util.Arrays.asList("1", "2"));
        java.util.HashMap<String, Object> extraOptions = new java.util.HashMap<String, Object>();
        ChromeOptions options = new ChromeOptions();
        if ("binary".equals(key)) {
            options.setBinary((String) extraOptions.get("binary"));
        } else if ("args".equals(key)) {
            options.addArguments((List<String>) value);
        } else if ("extensions".equals(key)) {
            options.addEncodedExtensions((List<String>) value);
        } else {
        }
        assertEquals("{browserName=chrome, goog:chromeOptions={args=[1, 2], extensions=[]}}", options.asMap().toString());
    }

    @Test
    public void testLine111() throws Exception {
        java.lang.String key = "extensions";
        Object value = new java.util.ArrayList<>(java.util.Arrays.asList("1", "2"));
        java.util.HashMap<String, Object> extraOptions = new java.util.HashMap<String, Object>();
        ChromeOptions options = new ChromeOptions();
        if ("binary".equals(key)) {
            options.setBinary((String) extraOptions.get("binary"));
        } else if ("args".equals(key)) {
            options.addArguments((List<String>) value);
        } else if ("extensions".equals(key)) {
            options.addEncodedExtensions((List<String>) value);
        } else {
        }
        assertEquals("{browserName=chrome, goog:chromeOptions={args=[], extensions=[1, 2]}}", options.asMap().toString());
    }

    @Test
    public void testLine115() throws Exception {
        java.lang.String key = "foo";
        Object value = "bar";
        java.util.HashMap<String, Object> extraOptions = new java.util.HashMap<String, Object>();
        ChromeOptions options = new ChromeOptions();
        if ("binary".equals(key)) {
            options.setBinary((String) extraOptions.get("binary"));
        } else if ("args".equals(key)) {
            options.addArguments((List<String>) value);
        } else if ("extensions".equals(key)) {
            options.addEncodedExtensions((List<String>) value);
        } else {
        }
        assertEquals("{browserName=chrome, goog:chromeOptions={args=[], extensions=[], foo=bar}}", options.asMap().toString());
    }
}
