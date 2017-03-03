
package com.sun.pdfview;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FileSelector {

    URL server;

    public FileSelector() {
       /// this.server = new URL("http://localhost");
    }

    public String fetchFile(String refCode) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/porta_print",
                "root",
                "");
        PreparedStatement getFileStmt = con.prepareStatement("SELECT fileName, fileUser FROM filedirectory WHERE fileCode ='"+refCode+"'");
        ResultSet res = getFileStmt.executeQuery();
        
        String filePath = "http://localhost/portaprint/uploads/";
        if(res.next()){
            MessageDigest md = MessageDigest.getInstance("MD5");
            filePath += MD5(res.getString("fileUser"))+"/" ;
            filePath += res.getString("fileName")+".pdf";
        }else{
            filePath = "FAILED";
        }
        
        return filePath;
    }
    
   public String MD5(String md5) {
   try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
       }
        return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
    }
    return null;
}
}
