package downloaders;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import download.Download;

public class FTPDownloaderTask implements IDownloaderTask {
		URL url=null;
		String username=null;
		String password=null;
		boolean enableSecureLayer = false;
		public static final int PORT = 21;
	public FTPDownloaderTask(URL url, boolean enableSecureLayer) {
			setUrl(url);
			this.enableSecureLayer = enableSecureLayer;
			
		}

	@Override
	public void run() {
		download();
		
	}

	@Override
	public void download() {
		FTPClient aClient =  new FTPClient();
		if(enableSecureLayer) {
			aClient =  new FTPSClient();
		}
		try{
			aClient.connect(url.getHost(), url.getPort()==-1?PORT:url.getPort());
			aClient.login(username,password);
			aClient.enterLocalPassiveMode();
			aClient.setFileType(FTP.BINARY_FILE_TYPE);
			aClient.setDefaultTimeout(3000);
			OutputStream outputStream =  new BufferedOutputStream(new FileOutputStream(Download.DOWNLOAD_LOCATION+url.getFile().replace("/", File.separator)));
			InputStream inputStream = aClient.retrieveFileStream(url.getPath());
			byte[] bytesArray =  new byte[4096];
			int bytesRead = -1;
			while((bytesRead = inputStream.read(bytesArray)) != -1){
				outputStream.write(bytesArray, 0, bytesRead);
			}
			boolean success = aClient.completePendingCommand();
			outputStream.close();
			inputStream.close();
			if(!success){
				throw new IOException();
			}
			
		}catch (Exception e) {
			deleteFile(Download.DOWNLOAD_LOCATION+url.getPath());
		}
		finally{
			
		}
	}
	public void deleteFile(String incompleteFile) {
		File aFile = new File(incompleteFile);
		aFile.delete();
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
		this.password =  password;
		
	}

}
