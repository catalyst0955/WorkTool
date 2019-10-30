package com.alex.tool.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

@Repository
public class FTPDao {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean upload(String serverAddress, int serverPort, String userId, String pwd, String ftppath, String filename, InputStream inputStream) {
		FTPClient ftpClient = new FTPClient();
		if (inputStream == null) //處理檔案為空             
			 return false;  
		try {   
			ftpClient.connect(serverAddress, serverPort);
	    	ftpClient.login(userId, pwd);
	    	ftpClient.enterLocalPassiveMode();
	    	ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

	    	if(ftppath != null) {
	    		ftpClient.changeWorkingDirectory("/".concat(ftppath)); 
		 	}
            boolean done = ftpClient.storeFile(filename, inputStream);
            
        	if (done) {
            	logger.info("The file "+filename+" is uploaded successfully.");
        	}
			return true;         
		}catch (IOException ex) {
	    	logger.error("Error: " + ex.getMessage());
	    	ex.printStackTrace();
	    	return false; 
		}finally {
			try {
		    	if (ftpClient.isConnected()) {
		        	ftpClient.logout();
		        	ftpClient.disconnect();
		    	}
			} catch (IOException ex) {
		            ex.printStackTrace();
			}
		}
    
	}


	public InputStream download(String serverAddress, int serverPort, String userId, String pwd, String ftppath, String fileName)  {
		FTPClient ftpClient = new FTPClient();
		//boolean result = false;
		InputStream inputStream = null;
		try {
			// ftpClient.setControlEncoding(encoding);
			ftpClient.connect(serverAddress, serverPort);
			ftpClient.login(userId, pwd);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			// 轉移到FTP伺服器目錄至指定的目錄下
			ftpClient.changeWorkingDirectory("/".concat(ftppath));
			// 獲取文件列表
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().contains(fileName)) {
					//File localFile = new File(localPath + "/" + ff.getName());
					//list.add(ff.getName());
					inputStream = ftpClient.retrieveFileStream(fileName);
					break;					
					//OutputStream is = new FileOutputStream(localFile);
					//ftpClient.retrieveFile(ff.getName(), is);
					//is.close();
				}
			}

			//ftpClient.logout();
			//result = true;
		} catch (IOException ex) {
			logger.error("Error: " + ex.getMessage());
			ex.printStackTrace();
			//return false;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return inputStream;
	}
}
