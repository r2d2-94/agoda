package downloaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import download.Download;

public class HTTPDownloaderTask implements IDownloaderTask {
	URL url=null;
	String username=null;
	String password=null;
	boolean enableSecureLayer = false;
	public HTTPDownloaderTask(URL url,boolean enableSecureLayer) {
		this.url=url;
		this.enableSecureLayer = enableSecureLayer;
	}
	@Override
	public void setUrl(URL url) {
		this.url = url;
	}
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public void run() {
		download();
	}

	@Override
	public void download() {
		try {
			HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
				if(enableSecureLayer){
					createSocketFactory();
				}
		if(username!=null && password!=null){
			httpUrl.setRequestProperty("Authorization","Basic "+createAuthHeader(username+":"+password));
		}
			httpUrl.setConnectTimeout(3000);
		ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		if(url.getPath().equals("")) {
			
		}
		File aFile = new File(Download.DOWNLOAD_LOCATION+url.getFile().replace("/", File.separator).replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
		FileOutputStream fos = new FileOutputStream(aFile);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
		} catch(SocketTimeoutException ste){
			System.out.println("Connection took long to response, quitting download");
			deleteFile(Download.DOWNLOAD_LOCATION+url.getPath());
		}catch (IOException e) {
			deleteFile(Download.DOWNLOAD_LOCATION+url.getPath());
			System.out.println("Problem downloading file at url : "+url.toString());
		}
	}
	private void createSocketFactory() {
		TrustManager[] trustAll = new TrustManager[]{
				new X509TrustManager() {
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;		
					}
					
				    @Override
					public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					}
					
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {	
					}
				}
		};
		try {
		SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAll, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteFile(String incompleteFile) {
		File aFile = new File(incompleteFile);
		aFile.delete();
	}
	private String createAuthHeader(String auth) {
		return("Basic "+Base64.getEncoder().encodeToString(auth.getBytes()));
		
	}


}
