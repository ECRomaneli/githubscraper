package br.com.ecromaneli.githubscraper.utils.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpRequest {
    private URL url;
    private HttpURLConnection con;

    private void openConnection(URL url) throws IOException {
        con = (HttpURLConnection) url.openConnection();
    }

    private void closeConnection() {
        con.disconnect();
    }

    private void setMethod(HttpMethod method) throws ProtocolException {
        con.setRequestMethod(method.name());
    }

    private void setTimeout(Integer connectTimeout, Integer readTimeout) {
        if (connectTimeout != null) { con.setConnectTimeout(connectTimeout); }
        if (readTimeout != null)    { con.setReadTimeout(readTimeout); }
    }

    private void setURL(String strUrl) throws MalformedURLException {
        url = new URL(strUrl);
    }

    private int send() throws IOException {
        return con.getResponseCode();
    }

    private String readResponse() throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return inputLine;
        } catch (IOException e) { throw e; }
        finally { if (in != null) { in.close(); } }
    }

    public void setHttpHeader(HttpHeader header) {
        header.forEach((k, v) -> con.setRequestProperty(k, v));
    }

    public void setHttpInput(HttpInput input) throws IOException {
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(input));
        out.flush();
        out.close();
    }
}
