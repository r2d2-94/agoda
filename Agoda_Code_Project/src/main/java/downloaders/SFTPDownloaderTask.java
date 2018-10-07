package downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import download.Download;

public class SFTPDownloaderTask implements IDownloaderTask {
	URL url=null;
	String username=null;
	String password=null;
	public static final int PORT = 22;
	private static final String STRICTHOSTKEYCHECKING = "StrictHostKeyChecking";
	@Override
	public void run() {
		download();
		
	}

	@Override
	public void download() {
		Session aSession = null;
		Channel aChannel = null;
		ChannelSftp channelSftp = null;
			JSch jsch = new JSch();
			try {
				aSession = jsch.getSession(username, url.getHost(), PORT);
			aSession.setPassword(password);
			Properties config = new Properties();
			config.put("", STRICTHOSTKEYCHECKING);
			aSession.setConfig(config);
			aSession.connect();
			aChannel = aSession.openChannel("sftp");
			aChannel.connect();
			channelSftp = (ChannelSftp) aChannel;
			channelSftp.cd(url.getPath());
			File aFile = new File(Download.DOWNLOAD_LOCATION+url.getPath());
			channelSftp.put(new FileInputStream(aFile),aFile.getName());
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (SftpException e) {
				deleteFile(Download.DOWNLOAD_LOCATION+url.getPath());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				channelSftp.exit();
				aChannel.disconnect();
				aSession.disconnect();
			}
	}

	@Override
	public void setUrl(URL url) {
	this.url =  url;
		
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
		
	}

	@Override
	public void setPassword(String password) {
		this.password =  password;
		
	}

	@Override
	public void deleteFile(String incompleteFilePath) {
		File aFile = new File(incompleteFilePath);
		aFile.delete();
	}

}
