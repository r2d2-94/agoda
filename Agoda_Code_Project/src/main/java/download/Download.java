package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import downloaders.DownloaderFactory;
import downloaders.IDownloaderTask;

public class Download {
	public static  String DOWNLOAD_LOCATION = null;
	private static final String DEF_enc = System.getProperty(System.getProperty("file.encoding"));
	public static void main(String[] args){
		String userDirectory = System.getProperty("user.dir");
		System.out.println(userDirectory);
		try {
			FileReader fr = new FileReader(userDirectory+File.separator+"config.properties");
			Properties prop =  new Properties();
			prop.load(fr);
			DOWNLOAD_LOCATION = prop.getProperty("DownLoadLocation");
		} catch (FileNotFoundException e1) {
			System.out.println("Config.properties is a required file which stores the Download Location of the files to be downloaded."
					+ "Please create one");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ExecutorService pool = Executors.newFixedThreadPool(10);
		try( BufferedReader br = new BufferedReader(new FileReader(new File(userDirectory+File.separator+"urls")))){
			String aLine = br.readLine();
			while(aLine!=null){
				String tokens[] = aLine.split(" ");
				URL uri = new URL(tokens[0]);
				IDownloaderTask aTask = DownloaderFactory.getInstance(uri);
				if(uri.getUserInfo()!=null) {
					parseAuthenticationForNonHttp(uri.getUserInfo(),aTask);
				}
				if(tokens[1]!=null)
					aTask.setUsername(tokens[1]);
				if(tokens[2]!=null)
					aTask.setPassword(tokens[2]);
				pool.submit(aTask);
				aLine = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			pool.shutdown();
		}
		
	}
	private static void parseAuthenticationForNonHttp(String userinfo, IDownloaderTask aTask) {
		try {
			userinfo = URLDecoder.decode(userinfo,DEF_enc);
		
	        int delimiter = userinfo.indexOf(':');
	        if (delimiter == -1) {
	            aTask.setUsername(URLDecoder.decode(userinfo, DEF_enc));
	        } else {
	        	 aTask.setUsername(URLDecoder.decode(userinfo.substring(0, delimiter++)));
	            aTask.setPassword(URLDecoder.decode(userinfo.substring(delimiter)));
	        }
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
