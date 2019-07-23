package com.neotys.neoload.model.readers.jmeter.step.javascript;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.userpath.Javascript;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.control.CacheManager;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavascriptConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavascriptConverter.class);
    private static String cookieManager = "CookieManager";

    JavascriptConverter() {
    }

    public static Step createJavascript(HashTree hashTree, HTTPSamplerProxy httpSamplerProxy) {
        Javascript.Builder javascript = Javascript.builder();
        StringBuilder script = new StringBuilder();
        for (Object o : hashTree.list()) {
            if (o instanceof CookieManager) {
                CookieManager cookieManager = (CookieManager) o;
                script.append(createCookieScript(cookieManager, httpSamplerProxy));
                javascript.name(cookieManager.getName())
                        .description(cookieManager.getComment());
            }
            if (o instanceof CacheManager) {
                CacheManager cacheManager = (CacheManager) o;
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

    static StringBuilder createCacheScript(CacheManager cacheManager) {
        StringBuilder script = new StringBuilder();
        if (cacheManager.getClearEachIteration()) {
            script.append("context.currentVU.clearCache()\n");
            LOGGER.info("Clear cache option converted");
            EventListenerUtils.readSupportedFunction("CacheManager", "Javascript CacheManager");
        }
        return script;
    }

    static StringBuilder createCookieScript(CookieManager cookieManager, HTTPSamplerProxy httpSamplerProxy) {
        StringBuilder script = new StringBuilder();

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
        EventListenerUtils.readUnsupportedParameter(JavascriptConverter.cookieManager, "CookiePolicy", "Type of policy");
        EventListenerUtils.readUnsupportedParameter(JavascriptConverter.cookieManager, "Option", "Clean for each iteration");
        return script;
    }
}
