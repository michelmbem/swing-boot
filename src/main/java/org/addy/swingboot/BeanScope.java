package org.addy.swingboot;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BeanScope {
    public final String SINGLETON =  "singleton";
    public final String PROTOTYPE =  "prototype";
    public final String REQUEST =  "request";
    public final String SESSION =  "session";
    public final String APPLICATION =  "application";
    public final String WEBSOCKET =  "websocket";

}
