package org.addy.swingboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HtmlWrapper {
    public static final String PREFIX = "<font face='sans-serif' size='4'>";
    public static final String SUFFIX = "</font>";

    public String wrap(String text) {
        return PREFIX + text + SUFFIX;
    }

    public String unwrap(String html) {
        return html.startsWith(PREFIX) && html.endsWith(SUFFIX)
                ? html.substring(PREFIX.length(), html.length() - SUFFIX.length())
                : html;
    }
}
