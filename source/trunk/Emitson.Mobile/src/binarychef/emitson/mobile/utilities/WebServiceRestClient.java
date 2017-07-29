package binarychef.emitson.mobile.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class WebServiceRestClient {
	
	public WebServiceRestClient(
			String webServiceBaseUrl,
			int connectionTimeout,
			int socketTimeout){
		_webServiceBaseUrl = webServiceBaseUrl;
		_connectionTimeout = connectionTimeout;
		_socketTimeout = socketTimeout;
	}
	
	private static final String TAG = "SDSynchronize";
	private static final int CONNECTION_TEST_CONNECTION_TIMEOUT = 10000;
	private static final int CONNECTION_TEST_SOCKET_TIMEOUT = 10000;
	
	private String _webServiceBaseUrl;
	private int _connectionTimeout = 60000;
	private int _socketTimeout = 60000;
	
	public String getWebServiceUrl(){
		return _webServiceBaseUrl;
	}
	
	public void setWebServiceUrl(String webServiceUrl){
		_webServiceBaseUrl = webServiceUrl;
	}
	
	public int getConnectionTimeout(){
		return _connectionTimeout;
	}
	
	public void setConnectionTimeout(int connectionTimeout){
		_connectionTimeout = connectionTimeout;
	}
	
	public int getSocketTimeout(){
		return _socketTimeout;
	}
	
	public void setSocketTimeout(int socketTimeout){
		_socketTimeout = socketTimeout;
	}
	
	private HttpParams getHttpParams(){
		HttpParams result = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(result, _connectionTimeout);
		HttpConnectionParams.setSoTimeout(result, _socketTimeout);
		return result;
	}
	
	private HttpParams getConnectionTestHttpParams(){
		HttpParams result = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(result, CONNECTION_TEST_CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(result, CONNECTION_TEST_SOCKET_TIMEOUT);
		return result;
	}
	
	private String getUrlString(String queryString){
		if(queryString == null || queryString.equals("")){
			return _webServiceBaseUrl;
		}
		String result = String.format("%s/%s", _webServiceBaseUrl,queryString);
		return result;
	}
	
	public void connectionTest() throws Exception{
		HttpClient httpClient = new DefaultHttpClient(getConnectionTestHttpParams());
		String serviceUrl = getUrlString(null);
		
		HttpGet httpGet = new HttpGet(serviceUrl);
		HttpResponse response = httpClient.execute(httpGet);
		if(response.getStatusLine().getStatusCode() != 200){
			throw new Exception(Integer.toString(response.getStatusLine().getStatusCode()) + ": " + response.getStatusLine().getReasonPhrase());
		}
	}
	
	private HttpResponse getResponse(WebServiceTaskInstruction instruction) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient(getHttpParams());
		HttpResponse result = null;
		String serviceUrl = getUrlString(instruction.getQueryString());

		switch (instruction.getWebRequestVerb()) {
		case Post:
			HttpPost httpPost = new HttpPost(serviceUrl);
			if(instruction.getPostContentType() != null){
				httpPost.setHeader("Content-type", instruction.getPostContentType());
			}
			if(instruction.getPostContent() != null){
				StringEntity se = new StringEntity(instruction.getPostContent(), "UTF-8");
				se.setContentType("application/json; charset=UTF-8");
				httpPost.setEntity(se);
			}
			result = httpClient.execute(httpPost);
			break;
		case Get:
			HttpGet httpGet = new HttpGet(serviceUrl);
			result = httpClient.execute(httpGet);
			break;
		case Delete:
			HttpDelete httpDelete = new HttpDelete(serviceUrl);
			result = httpClient.execute(httpDelete);
			break;
		case Put:
			HttpPut httpPut = new HttpPut(serviceUrl);
			if(instruction.getPostContentType() != null){
				httpPut.setHeader("Content-type", instruction.getPostContentType());
			}
			if(instruction.getPostContent() != null){
				StringEntity se = new StringEntity(instruction.getPostContent(), "UTF-8");
				se.setContentType("application/json; charset=UTF-8");
				httpPut.setEntity(se);
			}
			result = httpClient.execute(httpPut);
			break;
		default:
			break;
		}
		return result;
	}
	
	private String inputStreamToString(InputStream inputStream) throws IOException{
		String line = "";
		StringBuilder result = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		while((line = bufferedReader.readLine()) != null){
			result.append(line);
		}
		return result.toString();
	}
	
	public WebServiceTaskResult callWebService(
			WebServiceTaskInstruction instruction, 
			boolean performConnectionTest) throws Exception {
		if(performConnectionTest){
			connectionTest();
		}
		HttpResponse response = getResponse(instruction);
		if(response == null){
			throw new Exception("No response");
		}
		if(response.getStatusLine().getStatusCode() != 200){
			throw new Exception(Integer.toString(response.getStatusLine().getStatusCode()) + ": " + response.getStatusLine().getReasonPhrase());
		}
		String resultContents = inputStreamToString(response.getEntity().getContent());
		return new WebServiceTaskResult(instruction, resultContents);
	}
}