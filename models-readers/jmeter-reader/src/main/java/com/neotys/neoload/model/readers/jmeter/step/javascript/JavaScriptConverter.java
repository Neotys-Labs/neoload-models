package com.neotys.neoload.model.readers.jmeter.step.javascript;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.JavaScript;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.control.CacheManager;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manage all the different javascript to write
 */
public class JavaScriptConverter {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaScriptConverter.class);
    private static final String cookieManager = "CookieManager";

    //Constructor
    JavaScriptConverter() {
    }

    //Methods
    public static Step createJavascript(final HashTree hashTree, final HTTPSamplerProxy httpSamplerProxy) {
        final JavaScript.Builder javascript = JavaScript.builder();
        final StringBuilder script = new StringBuilder();
        for (Object o : hashTree.list()) {
            if (o instanceof CookieManager) {
                final CookieManager cookieManager = (CookieManager) o;
                script.append(createCookieScript(cookieManager, httpSamplerProxy));
                javascript.name(cookieManager.getName())
                        .description(cookieManager.getComment());
            }
            if (o instanceof CacheManager) {
                final CacheManager cacheManager = (CacheManager) o;
                script.append(createCacheScript(cacheManager));
                javascript.name(cacheManager.getName())
                        .description(cacheManager.getComment());
            }
        }
        if (script.toString().isEmpty()) {
            return null;
        } else {
            javascript.script(script.toString());
            return javascript.build();
        }
    }

    static StringBuilder createCacheScript(final CacheManager cacheManager) {
        final StringBuilder script = new StringBuilder();
        if (cacheManager.getClearEachIteration()) {
            script.append("context.currentVU.clearCache()\n");
            LOGGER.info("Clear cache option converted");
            EventListenerUtils.readSupportedFunction("CacheManager", "Javascript CacheManager");
        }
        return script;
    }

    static StringBuilder createCookieScript(final CookieManager cookieManager, final HTTPSamplerProxy httpSamplerProxy) {
        final StringBuilder script = new StringBuilder();

        for (int i = 0; i < cookieManager.getCookieCount(); i++) {
            if (cookieManager.get(i).getDomain().equals(httpSamplerProxy.getDomain())
                    && cookieManager.get(i).getPath().equals(httpSamplerProxy.getPath())) {
                script.append("/* Creation of the cookie number: ").append(i).append("*/\n");
                script.append("context.currentVU.setCookieForServer(\"");
                script.append(cookieManager.get(i).getDomain()).append("\",\"");
                script.append(cookieManager.get(i).getName()).append("=");
                script.append(cookieManager.get(i).getValue()).append("; path=");
                script.append(cookieManager.get(i).getPath()).append("\")");
                script.append(("\n"));
                LOGGER.info("Cookie Converted");
                EventListenerUtils.readSupportedFunction("CookieManager", "Javascript CookieManager");
            }
        }

        LOGGER.warn("We don't convert the Cookie Policy");
        EventListenerUtils.readUnsupportedParameter(JavaScriptConverter.cookieManager, "CookiePolicy", "Type of policy");
        EventListenerUtils.readUnsupportedParameter(JavaScriptConverter.cookieManager, "Option", "Clean for each iteration");
        return script;
    }
}
