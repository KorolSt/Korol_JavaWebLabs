package com.example.hotel.model.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;
import java.util.Objects;

public class FormatDateTag extends TagSupport {
    private static final long serialVersionUID = -4300785666200801826L;

    private static final Logger logger = LogManager.getLogger(FormatDateTag.class);

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private transient Temporal value;
    private String pattern;
    private String locale;

    public void setValue(Temporal value) {
        this.value = value;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            String patternToSet = Objects.requireNonNullElse(pattern, DEFAULT_DATE_PATTERN);
            out.write(DateTimeFormatter.ofPattern(patternToSet, new Locale(locale)).withZone(ZoneId.systemDefault()).format(value));

        } catch (IOException e) {
            logger.info("Value is not formatted.", e);
        }

        return SKIP_BODY;
    }
}
