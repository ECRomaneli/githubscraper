package br.com.ecromaneli.githubscraper.util.HttpRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;

@NoArgsConstructor @AllArgsConstructor
public class HttpResponse {
    @Getter private HttpStatus status;
    @Getter private byte[] bytes;
    private String cachedData;

    public String getData() {
        if (cachedData == null) {
            try {
                cachedData = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        return cachedData;
    }
}
