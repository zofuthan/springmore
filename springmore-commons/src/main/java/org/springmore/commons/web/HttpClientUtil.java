package org.springmore.commons.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * HttpClientUtil
 * 
 * @author 唐延波
 * @date 2015-6-10
 */
public class HttpClientUtil {

	public static final String UTF_8 = "utf-8";
	
	public static final String GBK = "gbk";
	
	/**
	 * 连接超时时间
	 * 20s
	 */
	private static final int CONNECT_TIMEOUT = 20000;
	
	/**
	 * 请求超时时间
	 * 20s
	 */
	private static final int CONNECT_REQUEST_TIMEOUT = 20000;
	
	/**
	 * socket超时时间
	 * 20s
	 */
	private static final int SOCKET_TIMEOUT = 20000;
	
	/**
	 * 获取HttpClient
	 * 并配置初始化参数
	 * @return
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	private static CloseableHttpClient getHttpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT).build();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig).build();
		return httpclient;
	}
	
	/**
	 * 获取SSLHttpClient
	 * @return
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	private static CloseableHttpClient getSSLHttpClient() {	
		//设置超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT).build();
		//设置ssl
		SSLContext sslContext = SSLContexts.createSystemDefault();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		        sslContext,
		        NoopHostnameVerifier.INSTANCE);
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
                .setSSLSocketFactory(sslsf)
                .build();
		return httpclient;
	}
	
	/**
	 * 执行post或者get请求
	 * @param url
	 * @param encoding
	 * @param httpUriRequest
	 * @param httpclient
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	private static String execute(String url, String encoding,
			HttpUriRequest httpUriRequest, CloseableHttpClient httpClient)
			throws Exception {
		CloseableHttpResponse response = null;
		try {			
			response = httpClient.execute(httpUriRequest);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, encoding);
			return result;
		} finally {
			response.close();
			httpClient.close();
		}
	}
	
	/**
	 * http get或者https get
	 * 通用方法
	 * @param url
	 * @param encoding
	 * @param httpClient
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	private static String get(String url, String encoding,
			CloseableHttpClient httpClient) throws Exception {
		HttpGet httpget = new HttpGet(url);
		return execute(url, encoding, httpget, httpClient);
	}
	
	/**
	 * post
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @param httpClient
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	private static String post(String url, List<NameValuePair> nvps,
			String encoding, CloseableHttpClient httpClient) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
		return execute(url, encoding, httpPost, httpClient);
	}
	
	

	/**
	 * <pre>
	 * http get请求 
	 * 默认编码为utf-8
	 * </pre>
	 * @param url
	 * @param encoding 编码
	 * @return
	 * @author 唐延波
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @date 2015-6-10
	 */
	public static String get(String url, String encoding) throws Exception {
		CloseableHttpClient httpClient = getHttpClient();
		return get(url,encoding,httpClient);
	}
		

	/**
	 * http get请求
	 * 默认utf-8编码
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String get(String url) throws Exception {
		return get(url, UTF_8);
	}
	
	/**
	 * ssl get
	 * @param url
	 * @param encoding
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String getSSL(String url, String encoding) throws Exception {
		CloseableHttpClient httpClient = getSSLHttpClient();
		return get(url,encoding,httpClient);
	}
	
	/**
	 * ssl get
	 * 默认utf-8编码
	 * @param url
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String getSSL(String url) throws Exception {
		return getSSL(url,UTF_8);
	}
	
	
	

	/**
	 * post 请求
	  * <pre>
	 * List <NameValuePair> nvps = new ArrayList <NameValuePair>();
     * nvps.add(new BasicNameValuePair("user.userName", "哈哈"));
     * HttpClientUtil.doPost("http://localhost:8888/login/login!login.ac",nvps,"utf-8");
	 * </pre>
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String post(String url, List<NameValuePair> nvps,
			String encoding) throws Exception {
		CloseableHttpClient httpClient = getHttpClient();
		return post(url,nvps,encoding,httpClient);
	}

	/**
	 * post 请求
	 * <pre>
	 * List <NameValuePair> nvps = new ArrayList <NameValuePair>();
     * nvps.add(new BasicNameValuePair("user.userName", "哈哈"));
     * HttpClientUtil.doPost("http://localhost:8888/login/login!login.ac",nvps);
	 * </pre>
	 * @param url
	 * @param nvp
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String post(String url, List<NameValuePair> nvp)
			throws Exception {
		return post(url,nvp,UTF_8);
	}
	
	/**
	 * ssl post 请求
	 * <pre>
	 * List <NameValuePair> nvps = new ArrayList <NameValuePair>();
     * nvps.add(new BasicNameValuePair("user.userName", "哈哈"));
     * HttpClientUtil.doPost("http://localhost:8888/login/login!login.ac",nvps,"utf-8");
	 * </pre>
	 * @param url
	 * @param nvps
	 * @param encoding
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String postSSL(String url, List<NameValuePair> nvps,
			String encoding) throws Exception {
		CloseableHttpClient httpClient = getSSLHttpClient();
		return post(url,nvps,encoding,httpClient);
	}
	
	/**
	 * ssl post 请求
	 * <pre>
	 * List <NameValuePair> nvps = new ArrayList <NameValuePair>();
     * nvps.add(new BasicNameValuePair("user.userName", "哈哈"));
     * HttpClientUtil.doPost("http://localhost:8888/login/login!login.ac",nvps,"utf-8");
	 * </pre>
	 * @param url
	 * @param nvps
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String postSSL(String url, List<NameValuePair> nvps) throws Exception {
		return postSSL(url,nvps,UTF_8);
	}
	
	/**
	 * get 下载文件
	 * @param url
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static byte[] getFile(String url) throws Exception {
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		try {			
			HttpGet httpget = new HttpGet(url);
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toByteArray(entity);
		} finally {
			response.close();
			httpClient.close();
		}
	}
	
	/**
	 * post上传文件
	 * @param url
	 * @param fileName
	 * @param file
	 * @return
	 * @throws Exception
	 * @author 唐延波
	 * @date 2015-6-10
	 */
	public static String postFile(String url,String fileName,File file) throws Exception{
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost post = new HttpPost(url);		
		FileBody bin = new FileBody(file); 
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.addPart(fileName,bin);
		HttpEntity multiEntity = multipartEntityBuilder.build();
		post.setEntity(multiEntity);
		return HttpClientUtil.execute(url, UTF_8, post, httpClient);
	}
}
