package com.rameshthumati.logbook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration;
import org.zalando.logbook.json.CompactingJsonBodyFilter;
import org.zalando.logbook.json.FastJsonHttpLogFormatter;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.json.PrettyPrintingJsonBodyFilter;

import java.io.IOException;

import static java.util.regex.Pattern.compile;
import static org.zalando.logbook.Conditions.contentType;
import static org.zalando.logbook.HeaderFilters.authorization;
import static org.zalando.logbook.HeaderFilters.eachHeader;
import static org.zalando.logbook.QueryFilters.accessToken;
import static org.zalando.logbook.QueryFilters.replaceQuery;
import static org.zalando.logbook.json.JsonPathBodyFilters.jsonPath;


@Configuration
public class LogBookConfig {
    private static ObjectMapper objectMapperForLogbookPrettyPrint = new ObjectMapper();

    @Bean
    public Logbook logbook(){
        Logbook logbook = Logbook.builder()
                .requestFilter(RequestFilters.replaceBody(message -> contentType("audio/*").test(message) ? "mmh mmh mmh mmh" : null))
                .responseFilter(ResponseFilters.replaceBody(message -> contentType("*/*-stream").test(message) ? "It just keeps going and going..." : null))
                .queryFilter(accessToken())
                .queryFilter(replaceQuery("password", "<secret>"))
                .headerFilter(authorization())
//                .headerFilter(eachHeader("X-Secret"::equalsIgnoreCase, "<secret>"))
//                .bodyFilter(jsonPath("$.active").replace("unknown"))
//                .bodyFilter(jsonPath("$.address").replace("X"))
//                .bodyFilter(jsonPath("$.name").replace(compile("^(\\w).+"), "$1."))
//                .bodyFilter(jsonPath("$.friends.*.name").replace(compile("^(\\w).+"), "$1."))
//                .bodyFilter(jsonPath("$.grades.*").replace(1.0))
//                .bodyFilter(jsonPath("$.studentName").delete())//remove a parameter from body
//                .correlationId(new CustomCorrelationId())
                .sink(new DefaultSink(
                        new PrincipalHttpLogFormatter(new JsonHttpLogFormatter()),
                        new StreamHttpLogWriter()
                ))
//                .bodyFilter(new CompactingJsonBodyFilter())
                .build();
        return logbook;
    }

//    private BodyFilter prettyPrintJson() {
//        return (contentType, body) -> {
//            if (body.length() > 0) {
//                if (contentType != null && contentType.toLowerCase().contains("json")) {
//                    try {
//                        Object json = objectMapperForLogbookPrettyPrint.readValue(body, Object.class);
//                        String indented = objectMapperForLogbookPrettyPrint.writerWithDefaultPrettyPrinter()
//                                .writeValueAsString(json);
//                        return (indented + "\n");
//                    } catch (IOException e) {
////                        if (logger.isErrorEnabled()) logger.error("error parsing body as json to pretty-print it", e);
//                    }
//                }
//            }
//            return (body);
//        };
//
//    }

//    @Autowired
//    private LogbookAutoConfiguration logbookAutoConfiguration;
//
//    @Bean
//    public BodyFilter bodyFilter() {
////        PrettyPrintingJsonBodyFilter
//        return BodyFilter.merge(prettyPrintJson(), logbookAutoConfiguration.bodyFilter());
//    }


}
