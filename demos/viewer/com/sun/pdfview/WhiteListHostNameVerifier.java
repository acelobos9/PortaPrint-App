/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sun.pdfview;

import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author acelo
 */
public abstract class WhiteListHostNameVerifier implements HostnameVerifier {
    private static final HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    private Set<String> trustedHosts;

    public WhiteListHostNameVerifier(Set<String> trustedHosts) {
        this.trustedHosts = trustedHosts;
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        if (trustedHosts.contains(hostname)) {
            return true;
        } else {
            return defaultHostnameVerifier.verify(hostname, session);
        }
    }
}
