package downloaders;

import java.net.URL;

public interface IDownloaderTask extends Runnable{

	public void setUrl(URL uri) ;
	public void setUsername(String username);
	public void setPassword(String password);
	public void download();
}
