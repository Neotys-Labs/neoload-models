package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.v3.project.userpath.Javascript;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.control.Cookie;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jorphan.collections.HashTree;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class CookieManagerConverterTest {

    private TestEventListener spy;

    @Before
    public void before()   {
        spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testCreateCookieNull(){
        HashTree hashTree = new HashTree();
        assertNull(CookieManagerConverter.createCookie(hashTree));
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
        Step result = CookieManagerConverter.createCookie(hashTree);
        Step expected = Javascript.builder()
                .script("/* Creation of the cookie number: 0*/\n" +
                        "context.currentVU.setCookieForServer(\"kaldrogo.intranet.neotys.com\",\"HTTP Cookie Manager=fd; path=/\")\n" +
                        "/* Creation of the cookie number: 1*/\n" +
                        "context.currentVU.setCookieForServer(\"kaldrogo.intranet.neotys.com\",\"HTTP Cookie Manager=d; path=/auth\")\n")
                .name("")
                .description("")
                .build();
        assertEquals(result,expected);
        verify(spy,times(1)).readUnsupportedParameter("CookieManager","CookiePolicy","Type of policy");
        verify(spy,times(1)).readUnsupportedParameter("CookieManager","Option","Clean for each iteration");

    }
}