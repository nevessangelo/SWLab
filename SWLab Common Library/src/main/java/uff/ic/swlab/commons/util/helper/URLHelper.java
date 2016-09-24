package uff.ic.swlab.commons.util.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import org.apache.commons.lang3.StringUtils;
import uff.ic.swlab.commons.util.DCConf;

public abstract class URLHelper {

    private static String getContent(String url) throws MalformedURLException, IOException, URISyntaxException {
        URLConnection conn = (new URL(normalize(url))).openConnection();
        conn.setConnectTimeout(DCConf.HTTP_CONNECT_TIMEOUT);
        conn.setReadTimeout(DCConf.HTTP_READ_TIMEOUT);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            return response.toString();
        }
    }

    public static String normalize(String url) throws MalformedURLException, URISyntaxException {
        URL url_ = new URL(url.trim());
        URI uri_ = new URI(url_.getProtocol(), url_.getUserInfo(), url_.getHost().toLowerCase(), url_.getPort(), url_.getPath(), url_.getQuery(), url_.getRef());
        return uri_.toString();
    }

    public static boolean sameAs(String url1, String url2) throws UnknownHostException, MalformedURLException, URISyntaxException, IOException {
        URL url1_ = new URL(url1);
        URL url2_ = new URL(url2);
        InetAddress address1 = InetAddress.getByName(url1_.getHost());
        InetAddress address2 = InetAddress.getByName(url2_.getHost());
        if (!normalize(url1).equals(normalize(url2))) {
            if (address1.getHostAddress().equals(address2.getHostAddress()))
                if (StringUtils.getJaroWinklerDistance(url1, url2) > 0.9)
                    return true;
            return false;
        }
        return true;
    }

    public static boolean isHTML(String url) throws MalformedURLException, IOException {
        URLConnection conn = (new URL(url)).openConnection();
        conn.setConnectTimeout(DCConf.HTTP_CONNECT_TIMEOUT);
        conn.setReadTimeout(DCConf.HTTP_READ_TIMEOUT);
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            String doc = response.toString();
            return doc.contains("<html") && doc.contains("</html");
        }
    }

}
