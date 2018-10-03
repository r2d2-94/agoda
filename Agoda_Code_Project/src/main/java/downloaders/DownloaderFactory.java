package downloaders;

import java.net.URI;
import java.net.URL;
import java.util.HashSet;

public class DownloaderFactory {

	static HashSet<String> aSet = new HashSet<>();
	static{
		aSet.add("http");
		aSet.add("ftp");
		aSet.add("https");
		aSet.add("sftp");
		aSet.add("ftps");
	}
	public synchronized static IDownloaderTask getInstance(URL url){
		String scheme =  url.getProtocol();
		if(!aSet.contains(scheme)){
			throw new SchemeNotSupportedException("Scheme/Protocol Not Supported");
		}
		if(scheme.equals("http")){
			return new HTTPDownloaderTask(url,false);
		}else if(scheme.equals("ftp")){
			return new FTPDownloaderTask(url,false);
		}
		else if(scheme.equals("https")){
			return new HTTPDownloaderTask(url,true);
		}
		else if(scheme.equals("ftps")) {
			return new FTPDownloaderTask(url, true);
		}
		else if(scheme.equals("sftp")){
			return new SFTPDownloaderTask();
		}
		return null;
	}
}
