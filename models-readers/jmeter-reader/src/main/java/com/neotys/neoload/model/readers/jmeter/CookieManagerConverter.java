package com.neotys.neoload.model.readers.jmeter;

import com.neotys.neoload.model.v3.project.userpath.Javascript;
import com.neotys.neoload.model.v3.project.userpath.Step;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CookieManagerConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieManagerConverter.class);
    private static String COOKIE_MANAGER = "CookieManager";

    CookieManagerConverter() {
    }

    static Step createCookie(HashTree hashTree, HTTPSamplerProxy  httpSamplerProxy) {
        Javascript javascript = null;
        for (Object o : hashTree.list()) {
            if (o instanceof CookieManager) {
                CookieManager cookieManager = (CookieManager) o;
                javascript = createJavacript(cookieManager, httpSamplerProxy);
            }
        }
        if (javascript != null) {
            LOGGER.info("CookieManager have been converted");
            EventListenerUtils.readSupportedAction(COOKIE_MANAGER);
        }
        return javascript;
    }

    private static Javascript createJavacript(CookieManager cookieManager, HTTPSamplerProxy httpSamplerProxy) {
        StringBuilder script = new StringBuilder();
        Javascript.Builder javascriptBuilder = Javascript.builder()
                .name(cookieManager.getName())
                .description(cookieManager.getComment());

        for (int i = 0; i < cookieManager.getCookieCount(); i++) {
            if (cookieManager.get(i).getDomain().equals(httpSamplerProxy.getDomain())
                && cookieManager.get(i).getPath().equals(httpSamplerProxy.getPath())){
                script.append("/* Creation of the cookie number: ").append(i).append("*/\n");
                script.append("context.currentVU.setCookieForServer(\"");
                script.append(cookieManager.get(i).getDomain()).append("\",\"");
                script.append(cookieManager.get(i).getName()).append("=");
                script.append(cookieManager.get(i).getValue()).append("; path=");
                script.append(cookieManager.get(i).getPath()).append("\")");
                script.append(("\n"));
            }
        }

        LOGGER.warn("We don't convert the Cookie Policy");
        EventListenerUtils.readUnsupportedParameter(COOKIE_MANAGER, "CookiePolicy", "Type of policy");
        EventListenerUtils.readUnsupportedParameter(COOKIE_MANAGER, "Option", "Clean for each iteration");
        javascriptBuilder.script(script.toString());
        return javascriptBuilder.build();
    }
}
