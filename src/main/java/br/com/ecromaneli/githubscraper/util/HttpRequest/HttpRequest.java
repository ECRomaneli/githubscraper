package br.com.ecromaneli.githubscraper.util.HttpRequest;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * An jQuery.ajax like class, to provide methods to perform http requests.
 */
public class HttpRequest {
    private URL url;
    private HttpURLConnection con;

    @Getter @Setter
    private Integer connectTimeout;

    @Getter @Setter
    private Integer readTimeout;

    /**
     * Perform an GET request.
     * @param url Address.
     * @param header Request Header.
     * @return Response code and data.
     */
    public HttpResponse get(String url, HttpHeader header) {
        return send(HttpMethod.GET, url, header, null);
    }

    /**
     * Perform an GET request.
     * @param url Address.
     * @return Response code and data.
     */
    public HttpResponse get(String url) {
        return send(HttpMethod.GET, url, null, null);
    }

    /**
     * Generic request.
     * @param method Request method.
     * @param url Address.
     * @param header Request Header.
     * @param input Request Inputs.
     * @return Response code and data.
     */
    public HttpResponse send(HttpMethod method, String url, HttpHeader header, HttpInput input) {
        try {
            this.setURL(url);
            this.openConnection();
            this.setMethod(method);
            this.setHttpHeader(header);
            this.setHttpInput(input);
            this.setTimeout();
            return this.getResponse();
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpResponse(HttpStatus.BAD_REQUEST, null, e.getMessage());
        } finally {
            this.closeConnection();
        }
    }

    private void openConnection() throws IOException {
        con = (HttpURLConnection) url.openConnection();
    }

    private void closeConnection() {
        con.disconnect();
    }

    private void setMethod(HttpMethod method) throws ProtocolException {
        con.setRequestMethod(method.name());
    }

    private void setTimeout() {
        if (connectTimeout != null) { con.setConnectTimeout(connectTimeout); }
        if (readTimeout != null)    { con.setReadTimeout(readTimeout); }
    }

    private void setURL(String strUrl) throws MalformedURLException {
        url = new URL(strUrl);
    }

    private void setHttpHeader(HttpHeader header) {
        if (header == null) { return; }
        header.forEach((k, v) -> con.setRequestProperty(k, v));
    }

    private void setHttpInput(HttpInput input) throws IOException {
        if (input == null) { return; }
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(input));
        out.flush();
        out.close();
    }

    private HttpStatus getHttpStatus() throws IOException {
        return HttpStatus.valueOf(con.getResponseCode());
    }

    private byte[] readResponse() throws IOException {
        InputStream is = null;
        ByteArrayOutputStream out = null;
        try {
            is = new BufferedInputStream(con.getInputStream());
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
            while ((n = is.read(buf)) != -1) { out.write(buf,0, n); }
            return out.toByteArray();
        }
        catch (IOException e) { throw e; }
        finally {
            if (is != null) { is.close(); }
            if (out != null) { out.close(); }
        }
    }

//    private byte[] readResponse() throws IOException {
//        InputStream is = null;
//        ByteArrayOutputStream out = null;
//        try {
//            is = new BufferedInputStream(con.getInputStream());
//            out = new ByteArrayOutputStream();
//            byte[] buf = new byte[1024];
//            int n;
//            while ((n = is.read(buf)) != -1) { out.write(buf,0, n); }
//            return out.toByteArray();
//        }
//        catch (IOException e) { throw e; }
//        finally {
//            if (out != null) { out.close(); }
//            if (is != null) { is.close(); }
//        }
//    }

    private HttpResponse getResponse() throws IOException {
        HttpStatus status = getHttpStatus();
        byte[] bytes = readResponse();
        return new HttpResponse(status, bytes, null);
    }
}
