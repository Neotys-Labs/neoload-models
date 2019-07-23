package com.neotys.neoload.model.readers.jmeter.step.javascript;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.Javascript;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.control.CacheManager;
import org.apache.jmeter.protocol.http.control.Cookie;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class JavascriptConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCreateCookieNull(){
        HashTree hashTree = new HashTree();
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setDomain("kaldrogo.intranet.neotys.com");
        Step step = JavascriptConverter.createJavascript(hashTree, httpSamplerProxy);
        assertNull(step);
    }

    @Test
    public void testCreateCookieNotNull(){
        HashTree hashTree = new HashTree();
        Cookie cookie = new Cookie();
        cookie.setDomain("kaldrogo.intranet.neotys.com");
        cookie.setPath("/");
        cookie.setValue("fd");
        cookie.setName("HTTP Cookie Manager");

        Cookie cookie2 = new Cookie();
        cookie2.setDomain("kaldrogo.intranet.neotys.com");
        cookie2.setPath("/auth");
        cookie2.setValue("d");
        cookie2.setName("HTTP Cookie Manager");

        CookieManager cookieManager = new CookieManager();
        cookieManager.add(cookie);cookieManager.add(cookie2);

        hashTree.add(cookieManager);
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setDomain("kaldrogo.intranet.neotys.com");
        httpSamplerProxy.setPath("/");
        Step result = JavascriptConverter.createJavascript(hashTree, httpSamplerProxy);
        Step expected = Javascript.builder()
                .script("/* Creation of the cookie number: 0*/\n" +
                        "context.currentVU.setCookieForServer(\"kaldrogo.intranet.neotys.com\",\"HTTP Cookie Manager=fd; path=/\")\n" )
                .name("")
                .description("")
                .build();
        assertEquals(result,expected);
        verify(spy,times(1)).readUnsupportedParameter("CookieManager","CookiePolicy","Type of policy");
        verify(spy,times(1)).readUnsupportedParameter("CookieManager","Option","Clean for each iteration");

    }

    @Test
    public void testCreateCookie2(){
        HashTree hashTree = new HashTree();
        Cookie cookie = new Cookie();
        cookie.setDomain("kaldrogo.intranet.neotys.com");
        cookie.setPath("/");
        cookie.setValue("fd");
        cookie.setName("HTTP Cookie Manager");

        Cookie cookie2 = new Cookie();
        cookie2.setDomain("kaldrogo.intranet.neotys.com");
        cookie2.setPath("/auth");
        cookie2.setValue("d");
        cookie2.setName("HTTP Cookie Manager");

        CookieManager cookieManager = new CookieManager();
        cookieManager.setClearEachIteration(true);
        cookieManager.add(cookie);cookieManager.add(cookie2);

        hashTree.add(cookieManager);
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setDomain("kaldrogo.intranet.neotys.com");
        httpSamplerProxy.setPath("/auth");
        Step result = JavascriptConverter.createJavascript(hashTree, httpSamplerProxy);
        Step expected = Javascript.builder()
                .script("/* Creation of the cookie number: 1*/\n" +
                        "context.currentVU.setCookieForServer(\"kaldrogo.intranet.neotys.com\",\"HTTP Cookie Manager=d; path=/auth\")\n")
                .name("")
                .description("")
                .build();
        assertEquals(result,expected);
        verify(spy,times(1)).readUnsupportedParameter("CookieManager","CookiePolicy","Type of policy");
        verify(spy,times(1)).readUnsupportedParameter("CookieManager","Option","Clean for each iteration");
    }

    @Test
    public void testClearCache(){
        HashTree hashTree = new HashTree();
        CacheManager cacheManager = new CacheManager();
        cacheManager.setClearEachIteration(true);


        hashTree.add(cacheManager);
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();

        Step result = JavascriptConverter.createJavascript(hashTree, httpSamplerProxy);
        Step expected = Javascript.builder()
                .script("context.currentVU.clearCache()\n")
                .name("")
                .description("")
                .build();
        assertEquals(result,expected);
        verify(spy,times(1)).readSupportedFunction("CacheManager", "Javascript CacheManager",0);
    }
}