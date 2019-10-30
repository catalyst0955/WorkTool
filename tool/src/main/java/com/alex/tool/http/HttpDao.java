package com.alex.tool.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public String doSoapPostRequest(String urlStr, String postData) throws Exception {
        return doSoapPostRequest(urlStr, postData, "utf-8", "utf-8");
    }

    public String doSoapPostRequest(String urlStr, String postData, String acceptEnc, String contentEnc)
            throws Exception {
        String[][] prop = {{"Accept-Charset", acceptEnc}, {"Content-type", "text/xml;charset=".concat(contentEnc)},
                {"Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7"}};
        return doPostRequest(urlStr, postData, prop);
    }

    public String doJsonPostRequest(String urlStr, String postData)
            throws Exception {
        String[][] prop = {{"Content-type", "application/json;charset=UTF-8"},
                {"Accept-Language", "zh-TW,zh;q=0.9,en-US;q=0.8,en;q=0.7"}};
        return doPostRequest(urlStr, postData, prop);
    }

    public String doPostRequestNoHeader(String urlStr, String postData) throws Exception {
        return doPostRequest(urlStr, postData, null);
    }

    public String doPostRequest(String urlStr, String postData, String[][] prop) throws Exception {

        String result = "";
        int timeout = 90;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000)
                .setStaleConnectionCheckEnabled(true).build();

        try (CloseableHttpClient client = createTrustAllHttpClientBuilder().setDefaultRequestConfig(config).build()) {
            // AP網址
            HttpPost post = new HttpPost(urlStr);
            // 必須用UTF-8 不然會有亂碼
            StringEntity entity = new StringEntity(postData, "UTF-8");
            post.setEntity(entity);
            if (prop != null) {
                for (int i = 0; i < prop.length; i++) {
                    post.setHeader(prop[i][0], prop[i][1]);
                }
            }
            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 必須用UTF-8 不然會有亂碼
                result = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
            } else if (statusCode == 404) {
                // 要回傳找不到給打API的人知道
                result = "404";
            } else {
                result = "";
            }
        } finally {
//            try {
//                client.close();
//            } catch (Exception e) {
//            }
        }
        return result;
    }

    public HttpClientBuilder createTrustAllHttpClientBuilder() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, (chain, authType) -> true);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(sslsf);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public String queryAPI(String ap, String linkName, String queryJson) throws Exception {

        //1 使用HTTP PATCH進行串接

        //設定TIMEOUT時間
        int timeOut = 90000;//90秒
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        // 對連線池設定SocketConfig物件
        connManager.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(timeOut).build());

        try (CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build()) {

            HttpPatch httpPatch = new HttpPatch(ap.concat(linkName));
            StringEntity entity = new StringEntity(queryJson, "UTF-8");
            httpPatch.addHeader("Charset", "UTF-8");
            httpPatch.addHeader("Accept-Charset", "UTF-8");
            httpPatch.addHeader("Content-Type", "application/json");
            httpPatch.addHeader("Accept", "application/json");

            httpPatch.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPatch);
            //2. 檢查是否收到正常回傳,回傳資訊。
            StatusLine sl = response.getStatusLine();
            if (sl.getStatusCode() == 200) {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            } else {
                logger.info("呼叫APS失敗!");
                throw new RuntimeException("呼叫APS Server失敗!!");
            }

        }


    }

    public String doPostRequestByFormData(String urlStr, String[][] postData) throws Exception {
        String result = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();


        try (CloseableHttpClient client = createTrustAllHttpClientBuilder().setDefaultRequestConfig(config).build()) {
            // AP網址
            HttpPost post = new HttpPost(urlStr);
            // 必須用UTF-8 不然會有亂碼
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            for (int i = 0; i < postData.length; i++) {
                form.add(new BasicNameValuePair(postData[i][0], postData[i][1]));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            post.setEntity(entity);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            CloseableHttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 必須用UTF-8 不然會有亂碼
                result = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                logger.info("post result: " + result);
            } else if (statusCode == 404) {
                // 要回傳找不到給打API的人知道
                result = "404";
            } else {
                result = "";
            }
        } finally {
//            try {
//                client.close();
//            } catch (Exception e) {
//            }
        }
        return result;
    }


    public String doPostByFile(String urlStr, Map<String, String> params, Map<String, File> files, String[][] prop) throws Exception {

        //使用工具类创建 httpClient
        String result = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();


        try (CloseableHttpClient client = createTrustAllHttpClientBuilder().setDefaultRequestConfig(config).build()) {
            // AP網址
            HttpPost httpPost = new HttpPost(urlStr);
            // 必須用UTF-8 不然會有亂碼

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setCharset(Charset.forName("UTF-8"));//设置请求的编码格式
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

            for (String key : files.keySet()) {
//				FileInputStream fileInputStream = new FileInputStream(files.get(key));
//
//
//				builder.addBinaryBody(key,fileInputStream, ContentType.create("application/zip"), key);
                builder.addBinaryBody(key, files.get(key));
            }
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addTextBody(key, params.get(key)); //设置请求参数
                }
            }

            HttpEntity httpEntity = builder.build();
            //将请求参数放入 HttpPost 请求体中
            //使用 httpEntity 后 Content-Type会自动被设置成 multipart/form-data
            httpPost.setEntity(httpEntity);
            if (prop != null) {
                for (int i = 0; i < prop.length; i++) {
                    httpPost.setHeader(prop[i][0], prop[i][1]);
                }
            }


            //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 必須用UTF-8 不然會有亂碼
                result = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                logger.info("post result: " + result);
            } else if (statusCode == 404) {
                // 要回傳找不到給打API的人知道
                result = "404";
            } else {
                result = "";
            }
        } finally {
//            try {
//                client.close();
//            } catch (Exception e) {
//            }
        }
        return result;


    }


    public String doPostByInpuStream(String urlStr, Map<String, String> params, Map<String, InputStream> files, String[][] prop) throws Exception {

        //使用工具类创建 httpClient
        String result = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();


        try (CloseableHttpClient client = createTrustAllHttpClientBuilder().setDefaultRequestConfig(config).build()) {
            // AP網址
            HttpPost httpPost = new HttpPost(urlStr);
            // 必須用UTF-8 不然會有亂碼

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setCharset(Charset.forName("UTF-8"));//设置请求的编码格式
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

            for (String key : files.keySet()) {
                builder.addBinaryBody(key, files.get(key), ContentType.create("application/zip"), key);
            }
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addTextBody(key, params.get(key)); //设置请求参数
                }
            }

            HttpEntity httpEntity = builder.build();
            //将请求参数放入 HttpPost 请求体中
            //使用 httpEntity 后 Content-Type会自动被设置成 multipart/form-data
            httpPost.setEntity(httpEntity);
            if (prop != null) {
                for (int i = 0; i < prop.length; i++) {
                    httpPost.setHeader(prop[i][0], prop[i][1]);
                }
            }


            //httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            CloseableHttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 必須用UTF-8 不然會有亂碼
                result = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                logger.info("post result: " + result);
            } else if (statusCode == 404) {
                // 要回傳找不到給打API的人知道
                result = "404";
            } else {
                result = "";
            }
        } finally {
//            try {
//                client.close();
//            } catch (Exception e) {
//            }
        }
        return result;


    }

}