package uff.ic.swlab.utils;

import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class Resource {

    public static String getAuthority(String[] urls) {
        String authority;
        Map<String, Integer> hist = new HashMap<>();
        for (String url : urls) {
            authority = getAuthority(url);
            hist.put(authority, hist.getOrDefault(authority, 0) + 1);
        }

        Comparator<Entry<String, Integer>> comparator = (Entry<String, Integer> o1, Entry<String, Integer> o2) -> o2.getValue().compareTo(o1.getValue());
        SortedSet<Entry<String, Integer>> authorities = new TreeSet<>(comparator);
        authorities.addAll(hist.entrySet());

        return authorities.size() > 0 ? authorities.first().getKey() : null;
    }

    private static String getAuthority(String urlString) {
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            String auth = url.getAuthority();
            return protocol + "://" + auth;
        } catch (Exception e) {
            return null;
        }
    }
}