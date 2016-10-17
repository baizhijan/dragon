package com.bzj.dragon.utils.net;

import com.bzj.dragon.utils.net.netEnum.ContentTypeEnum;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;


/**
 * Author: baizhijian
 * Date: 15/11/4 18:07
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final int CONNECTION_TIMEOUT = 20000;
    private static final int DEFAULT_INITIAL_BUFFER_SIZE = 4 * 1024; // 4 kB

    /**
     * GET请求
     *
     * @param url
     * @param queryString
     * @return
     */
    public static String httpGet(String url, String queryString) {
        HttpClient httpClient = new HttpClient();
        HttpResponseModel response = httpGetBodyBytes(httpClient, url, queryString);
        return response.getBodyString();
    }

    /**
     * POST请求
     *
     * @param url
     * @param queryString
     * @param contentType
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, String queryString, ContentTypeEnum contentType) throws Exception {
        String responseData = null;
        PostMethod httpPost = new PostMethod(url);
        HttpClient httpClient = new HttpClient();
        httpPost.addParameter("Content-Type", contentType.getContent());
        httpPost.getParams().setParameter("http.socket.timeout", new Integer(CONNECTION_TIMEOUT));
        if (queryString != null && !queryString.equals("")) {
            httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString.getBytes()));
        }
        try {
            int statusCode = httpClient.executeMethod(httpPost);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("HttpPost Method failed: " + httpPost.getStatusLine());
            }
            responseData = httpPost.getResponseBodyAsString();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            httpPost.releaseConnection();
            httpClient = null;
        }
        return responseData;
    }

    /**
     * 证书get请求
     * @param password
     * @param path
     * @param url
     * @return
     * @throws Exception
     */
    public static String SSLGet(String password, String path,String url,Map<String, String> parmMap) throws Exception {
        String resourceStr = "";
        CloseableHttpClient client = getSslClient(password, path);
        if(client != null){
            if(!parmMap.isEmpty()){
                String paramString = paramMapToString(parmMap);
                url += "?"+paramString;
            }
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            resourceStr = entity.toString();
        }
        return resourceStr;
    }

    /**
     * 证书post请求
     * @param password
     * @param path
     * @return
     * @throws Exception
     */
    public static String SSLPost(String password, String path,String url,String queryString,ContentTypeEnum contentType) {
        String resourceStr = "";
        CloseableHttpClient client = getSslClient(password, path);
        if(client == null){
            return resourceStr;
        }
        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(10000).setConnectTimeout(10000)
                .setSocketTimeout(10000).build();
        httpPost.setConfig(config);
        StringEntity stringEntity = new StringEntity(queryString, ContentType.create(contentType.getContent(), Consts.UTF_8));
        httpPost.setEntity(stringEntity);
        try {
            HttpResponse httpResponse = client.execute(httpPost);
            if(httpResponse.getStatusLine().equals(HttpStatus.SC_OK)){
                HttpEntity entity = httpResponse.getEntity();
                resourceStr = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            logger.error("post error",e);
        }
        return resourceStr;
    }

    /**
     * 请求参数转换字符串
     *
     * @param parmMap
     * @return
     */
    public static String paramMapToString(Map<String, String> parmMap) {
        StringBuffer params = new StringBuffer();
        for (Iterator iter = parmMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry element = (Map.Entry) iter.next();
            params.append(element.getKey().toString());
            params.append("=");
            try {
                params.append(URLEncoder.encode(element.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("map to String is error", e);
            }
            params.append("&");
        }

        if (params.length() > 0) {
            params = params.deleteCharAt(params.length() - 1);
        }
        return params.toString();
    }

    public static HttpResponseModel httpGetBodyBytes(HttpClient httpClient, String url, String queryString) {
        if (queryString != null && !queryString.equals("")) {
            url += "?" + queryString;
        }
        GetMethod method = new GetMethod(url);
        method.getParams().setParameter("http.socket.timeout", new Integer(CONNECTION_TIMEOUT));
        method.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        method.setRequestHeader("Accept", "*/*");
        method.setRequestHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");
        method.setRequestHeader("Upgrade-Insecure-Requests", "1");
        method.addRequestHeader("Connection", "keep-alive");
        method.addRequestHeader("Cache-Control", "max-age=0");
        try {
            int statusCode = httpClient.executeMethod(method);

            String contentType = method.getResponseHeader("Content-Type").toString();
            String charset = method.getResponseCharSet();
            byte[] responseBody = new byte[0];
            InputStream instream = method.getResponseBodyAsStream();
            if (instream != null) {
                long contentLength = method.getResponseContentLength();
                if (contentLength > Integer.MAX_VALUE) { //guard below cast from overflow
                    throw new IOException("Content too large to be buffered: " + contentLength + " bytes");
                }
                int limit = method.getParams().getIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, 1024 * 1024);
                if ((contentLength == -1) || (contentLength > limit)) {
                    ;
                }
                ByteArrayOutputStream outstream = new ByteArrayOutputStream(
                        contentLength > 0 ? (int) contentLength : DEFAULT_INITIAL_BUFFER_SIZE);
                byte[] buffer = new byte[4096];
                int len;
                while ((len = instream.read(buffer)) > 0) {
                    outstream.write(buffer, 0, len);
                }
                outstream.close();
                responseBody = outstream.toByteArray();
            }
            return new HttpResponseModel(statusCode, contentType, charset, responseBody);
        } catch (Exception e) {
            String exceptionStacktrace = ExceptionUtils.getStackTrace(e);
            if (!(e instanceof SocketTimeoutException) && !(e instanceof SocketException)) {
                logger.error(url + " " + exceptionStacktrace, e);
            }
            return new HttpResponseModel(e.getClass().getName(), exceptionStacktrace);
        } finally {
            method.releaseConnection();
        }
    }

    /**
     * 获取sslClient
     * @param password
     * @param path
     * @return
     * @throws Exception
     */
    public static CloseableHttpClient getSslClient(String password, String path){
        FileInputStream instream = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(path));
            trustStore.load(instream, password.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            return httpclient;
        } catch (KeyStoreException e) {
            logger.error("KeyStore getInstance is throw Exception",e);
        } catch (IOException e) {
            logger.error("certificate file is not find ",e);
        } catch (CertificateException e) {
            logger.error("certificate load exception ",e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("certificate load exception ",e);
        } catch (KeyManagementException e) {
            logger.error("certificate management exception ",e);
        } finally {
            if(instream != null){
                try {
                    instream.close();
                } catch (IOException e) {
                    logger.error("instream close is exception ",e);
                }
            }
        }
        return null;
    }
}
