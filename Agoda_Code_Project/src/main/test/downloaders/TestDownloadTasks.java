package downloaders;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import download.Download;

public class TestDownloadTasks  {
	
	
	@BeforeClass
	public static void setUp() {
		Download.prepareDownLoadLocation();
	}
	@Test
	public void TestHTTPDownload() throws MalformedURLException, InterruptedException {
		URL aUrl = new URL("http://api.ipify.org/?format=json");
		IDownloaderTask aTask =  DownloaderFactory.getInstance(aUrl);
		Assert.assertTrue(aTask instanceof HTTPDownloaderTask);
		Thread t = new Thread(aTask);
		t.start();
		t.join();
		Assert.assertTrue(new File(Download.DOWNLOAD_LOCATION+aUrl.getFile().replace("/", File.separator).replaceAll("[^a-zA-Z0-9\\.\\-]", "_")).exists());
		aTask.deleteFile(Download.DOWNLOAD_LOCATION+aUrl.getFile().replace("/", File.separator));
	}
	
	@Test
	public void TestHTTPsDownload() throws MalformedURLException, InterruptedException {
		URL aUrl = new URL("https://api.ipify.org/?format=json");
		IDownloaderTask aTask =  DownloaderFactory.getInstance(aUrl);
		Assert.assertTrue(aTask instanceof HTTPDownloaderTask);
		Thread t = new Thread(aTask);
		t.start();
		t.join();
		Assert.assertTrue(new File(Download.DOWNLOAD_LOCATION+aUrl.getFile().replace("/", File.separator).replaceAll("[^a-zA-Z0-9\\.\\-]", "_")).exists());
		aTask.deleteFile(Download.DOWNLOAD_LOCATION+aUrl.getFile().replace("/", File.separator));
	}
	
	@Test
	public void TestFTPDownload() throws MalformedURLException, InterruptedException {
		URL aUrl = new URL("ftp://test.exavault.com/transfer.pdf");
		IDownloaderTask aTask =  DownloaderFactory.getInstance(aUrl);
		Assert.assertTrue(aTask instanceof FTPDownloaderTask);
		Thread t = new Thread(aTask);
		t.start();
		t.join();
		Assert.assertTrue(new File(Download.DOWNLOAD_LOCATION+aUrl.getPath().replace("/", File.separator)).exists());
		aTask.deleteFile(Download.DOWNLOAD_LOCATION+aUrl.getPath().replace("/", File.separator));
	}
}
