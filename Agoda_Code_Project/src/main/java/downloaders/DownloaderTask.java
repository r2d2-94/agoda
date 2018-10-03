package downloaders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class DownloaderTask implements IDownloaderTask {

	URI url;
	public DownloaderTask(URI url) {
       this.url = url;
    }
	@Override
	public void run() {
		File aFile =  new File("C:\\Users\\bajaj\\workspace\\Agoda_Code_Project\\Downloads\\"+url.getPath());
		try{
		FileUtils.copyURLToFile(url.toURL(), aFile , 3000, 3000);
		}
		catch (MalformedURLException me){
			System.out.println(me.getMessage());
		}
		catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
	@Override
	public void download() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUrl(URL uri) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}

}
