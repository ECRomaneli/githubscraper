package br.com.ecromaneli.githubscraper.util.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ParameterStringBuilder {
    public static String getParamsString(HttpInput params) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(utf8Encode(entry.getKey()));
            result.append("=");
            result.append(utf8Encode(entry.getValue()));
            result.append("&");
        }
        String resultString = result.toString();

        return resultString.length() > 0 ?
                resultString.substring(0, resultString.length() - 1) :
                resultString;
    }

    private static String utf8Encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return value;
        }
    }
}
