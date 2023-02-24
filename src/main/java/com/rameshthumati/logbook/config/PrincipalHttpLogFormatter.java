package com.rameshthumati.logbook.config;

import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Precorrelation;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import java.io.IOException;
import java.util.Map;

final class PrincipalHttpLogFormatter implements HttpLogFormatter {

    private final JsonHttpLogFormatter delegate;

    PrincipalHttpLogFormatter(final JsonHttpLogFormatter delegate) {
        this.delegate = delegate;
    }

    @Override
    public String format(Precorrelation precorrelation, org.zalando.logbook.HttpRequest request) throws IOException {
        final Map<String, Object> content = delegate.prepare(precorrelation, request);
        content.remove("remote");
        content.remove("origin");
        content.remove("protocol");
        content.put("type", "REQUEST");
        return delegate.format(content);
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {
        final Map<String, Object> content = delegate.prepare(correlation, response);
        content.remove("origin");
        content.put("type", "RESPONSE");
        return delegate.format(content);
    }
}
