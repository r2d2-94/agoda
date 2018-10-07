Please input the urls of the to be downloaded in the "url" file.
Download Location can be configured in "config.properties" file.

For HTTP, if username and password are required, pass them as space separated values after the url
EX: url user password

For FTP,SFTP,FTPS  ftp://user@pass/ftpserver/path is accepted. Make sure that username and password are base64encoded
Username and password can also be given as "ftpurl user password"

for SFTP, I did not provide a provisioning of SSH private Keys in the interest of time but can be done if the input format can be made clear as to how it will be provided.

For Secure Layered Communications, by default all certificates are trusted.
There is no provisioning of Private Key but can