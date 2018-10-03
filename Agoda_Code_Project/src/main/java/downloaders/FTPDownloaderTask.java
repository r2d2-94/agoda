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

import download.Download;

public class FTPDownloaderTask implements IDownloaderTask {
		URL url=null;
		String username=null;
		String password=null;
		boolean enableSecureLayer = false;
	@Override
	public void run() {
		download();
		
	}

	@Override
	public void download() {
		FTPClient ftpClient =  new FTPClient();
		try{
			ftpClient.connect(url.getHost(), url.getPort());
			ftpClient.login(username,password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setDefaultTimeout(3000);
			OutputStream outputStream =  new BufferedOutputStream(new FileOutputStream(Download.DOWNLOAD_LOCATION+url.getPath()));
			InputStream inputStream = ftpClient.retrieveFileStream(url.getPath());
			byte[] bytesArray =  new byte[4096];
			int bytesRead = -1;
			while((bytesRead = inputStream.read(bytesArray)) != -1){
				outputStream.write(bytesArray, 0, bytesRead);
			}
			boolean success = ftpClient.completePendingCommand();
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
	private void deleteFile(String incompleteFile) {
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
