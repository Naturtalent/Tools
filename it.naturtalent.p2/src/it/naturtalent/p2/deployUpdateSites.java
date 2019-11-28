package it.naturtalent.p2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.eclipse.core.runtime.Path;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;




public class deployUpdateSites
{
	// das Projekt um das es geht
	private static String projectName = "NaturtalentBasis";
	//private static String projectName = "Libreoffice";
	//private static String projectName = "Office";

	// Struktur zur Aufnahme der Authentifizierungsdaten aus dem SettingFile 'settings.xml'
	private static class FTPSettings
	{
		String username;
		String password;
	}
	private static FTPSettings ftpSettings;

	// aktuelles lokales Repository
	private static String LOCAL_REPOSITORY_DIR = "Repositories2019";
	
	// Parameter der in '.m2' gespeichete Settings-Datei
	private static String SETTINGS_DIR = ".m2";
	private static String SETTINGS_FILE = "settings.xml";
	private static String SETTINS_NAMESPACE = "http://maven.apache.org/SETTINGS/1.0.0";
	
	// Naturtalent FTP Parameter
	private static String FTP_NATURTALENT_SERVER = "natur-talent";
	private static String NATURTALENT_UDATESITES_PATH = "/public/UpdateSites";
	
	// Fritz FTP Parameter
	private static String FTP_FRITZBOX_SERVER = "Fritz.Nas";
	private static String FRITZBOX_UDATESITES_PATH = "/Stickrepos/public/UpdateSites";
	
	//private static String FTP_FRITZBOX_SERVER = "Fritz.Nas";
	private static int FTP_PORT = 21;
	//private static String REMOTE_UDATESITES_PATH = "/Stickrepos/public/UpdateSites";
	private static FTPClient ftpClient;
	
	//private static String ftpServerName = FTP_NATURTALENT_SERVER;
	//private static String ftpUpdateSitesPath = NATURTALENT_UDATESITES_PATH;
	
	private static String ftpServerName = FTP_FRITZBOX_SERVER;
	private static String ftpUpdateSitesPath = FRITZBOX_UDATESITES_PATH;
	
	
	// Main
	public static void main(String[] args)
	{	
		String arg0 = args[0];
		
		if(StringUtils.isNotEmpty(arg0))
			projectName = arg0;
		
		// Auth-Daten lesen
		if(getFTPSettings())
		{
			// login beim FTP-Server
			ftpClient = getFTPCient();
			if (ftpClient != null)
			{
				// src - die lokale UpdateSite
				File localRepository = getLocalUpdateSiteDir(projectName);
				if(localRepository != null)
				{					
					try
					{
						// das Remoteverzeichnis
						String remoteDirPath = ftpUpdateSitesPath+File.separator+projectName+File.separator+"repository";
						
						ftpClient.changeWorkingDirectory(remoteDirPath);
					    int returnCode = ftpClient.getReplyCode();
					    if (returnCode == 550) 
					    {
					    	// UpdateSite Verzeichnis 'repository' anlegen
					    	if(!createDirectory(remoteDirPath))
					    		return;
					    }
					    else
					    {
					    	// Inhalt des bestehende Verzeichnisses loeschen
					    	removeDirectory(remoteDirPath, "");
					    	
					    	// UpdateSite Verzeichnis 'repository' anlegen
					    	if(!createDirectory(remoteDirPath))
					    		return;
					    }
						
					    // UpdateSite hochladen
						uploadUpdeteSite(remoteDirPath, localRepository.getPath(), "");
						
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
								
				try
				{
					ftpClient.logout();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Deployprocessing beendet");
	}
	
	/*
	 * die lokale UpdateSite ermitteln
	 */
	private static File getLocalUpdateSiteDir(String projectName)
	{
		File localRepository = new File(SystemUtils.getUserHome(),LOCAL_REPOSITORY_DIR);
		File projectDir = new File(localRepository, projectName);
		if(projectDir.exists() && projectDir.isDirectory())
		{
			File relengDir = new File(projectDir,"releng");
			
			//IOFileFilter dirFilter = FileFilterUtils.directoryFileFilter();
			IOFileFilter dirFilter = FileFilterUtils.directoryFileFilter();
			IOFileFilter nameFilter = FileFilterUtils.nameFileFilter("target");
	
			IOFileFilter targetFilter = FileFilterUtils.and(dirFilter,nameFilter);
			targetFilter = FileFilterUtils.makeDirectoryOnly(targetFilter);
			
			Collection<File>targetDir = FileUtils.listFilesAndDirs(relengDir, dirFilter, TrueFileFilter.INSTANCE);
			
			for(File file : targetDir)
			{
				Path path = new Path(file.getPath());
				if(StringUtils.equals(path.lastSegment(),"target"))	
				{
					File repository = new File(file,"repository");
					if(repository.exists())
						return repository;
				}
			}
		}
		System.out.println("Projektpfad: "+projectName+ " nicht gefunden");
		return null;
	}
	
	/**
	 * Mit dem FTP - Server verbinden
	 * 
	 * @return
	 */
	private static FTPClient getFTPCient()
	{
		try
		{
			FTPClient ftpClient = new FTPClient();
			//ftpClient.connect( FTP_UPDATESITE_SERVER, FTP_PORT );
			ftpClient.connect( ftpServerName, FTP_PORT );
			ftpClient.login(ftpSettings.username,ftpSettings.password);
			
			// use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();
            
			return ftpClient;
			
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Die Authentifizierungsdaten aus 'settings.xml' lesen.
	 * 
	 * @return
	 */
	private static boolean getFTPSettings()
	{
		// Namespace der xml-Definitionen		
		Namespace nameSpace = Namespace.getNamespace(SETTINS_NAMESPACE);
		
		// 'username' und 'password* auslesen
		File settingsFile = new File(FileUtils.getUserDirectory(), SETTINGS_DIR);
		settingsFile = new File(settingsFile, SETTINGS_FILE);
		if(settingsFile.exists())
		{			
			try
			{
				Document settingDoc = new SAXBuilder().build(settingsFile);
				Element rootElement = settingDoc.getRootElement();				
				Element servers = rootElement.getChild("servers",nameSpace);
				Element server = servers.getChild("server",nameSpace);
				List<Element>serverContents = server.getChildren();
				ftpSettings = new FTPSettings();
				for(Element serverElement : serverContents)
				{
					if(StringUtils.equals(serverElement.getName(), "username"))
						ftpSettings.username = serverElement.getTextTrim();
					if(StringUtils.equals(serverElement.getName(), "password"))
						ftpSettings.password = serverElement.getTextTrim();
				}						
				return true;
				
			} catch (JDOMException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(settingsFile);
		}
		
		return false;
	}
	
	private static void uploadUpdeteSite(String remoteDirPath, String localParentDir, String remoteParentDir)  throws IOException 
	{		
		System.out.println("LISTING directory: " + localParentDir);
		
		File localDir = new File(localParentDir);
		File[] subFiles = localDir.listFiles();
	    if (subFiles != null && subFiles.length > 0) 
	    {
	    	for (File item : subFiles) 
	    	{
	            String remoteFilePath = remoteDirPath + "/" + remoteParentDir + "/" + item.getName();
	            if (remoteParentDir.equals("")) 
	            {
	                remoteFilePath = remoteDirPath + "/" + item.getName();
	            }
	 	 
	            if (item.isFile()) 
	            {
	                // upload the file
	                String localFilePath = item.getAbsolutePath();
	                System.out.println("About to upload the file: " + localFilePath);
	                boolean uploaded = uploadSingleFile(localFilePath, remoteFilePath);
	                if (uploaded) 
	                {
	                    System.out.println("UPLOADED a file to: " + remoteFilePath);
	                } else 
	                {
	                    System.out.println("COULD NOT upload the file: " + localFilePath);
	                }
	            } else 
	            {
	                // create directory on the server
	                boolean created = ftpClient.makeDirectory(remoteFilePath);
	                if (created) 
	                {
	                    System.out.println("CREATED the directory: " + remoteFilePath);
	                } else 
	                {
	                    System.out.println("COULD NOT create the directory: " + remoteFilePath);
	                }
	 
	                // upload the sub directory
	                String parent = remoteParentDir + "/" + item.getName();
	                if (remoteParentDir.equals("")) 
	                {
	                    parent = item.getName();
	                }
	 
	                localParentDir = item.getAbsolutePath();
	                uploadUpdeteSite(remoteDirPath, localParentDir,parent);
	            }
	        }
	    }
	 }		
	
	/**
	 * Upload a single file to the FTP server.
	 *
	 * @param ftpClient
	 *            an instance of org.apache.commons.net.ftp.FTPClient class.
	 * @param localFilePath
	 *            Path of the file on local computer
	 * @param remoteFilePath
	 *            Path of the file on remote the server
	 * @return true if the file was uploaded successfully, false otherwise
	 * @throws IOException
	 *             if any network or IO error occurred.
	 */
	public static boolean uploadSingleFile(String localFilePath, String remoteFilePath) throws IOException
	{
		File localFile = new File(localFilePath);

		InputStream inputStream = new FileInputStream(localFile);
		try
		{
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			return ftpClient.storeFile(remoteFilePath, inputStream);
		} finally
		{
			inputStream.close();
		}
	}
	
	/**
     * Removes a non-empty directory by delete all its sub files and
     * sub directories recursively. And finally remove the directory.
     */
    public static void removeDirectory(String parentDir, String currentDir) throws IOException 
	{
		String dirToList = parentDir;
		if (!currentDir.equals(""))
		{
			dirToList += "/" + currentDir;
		}

		FTPFile[] subFiles = ftpClient.listFiles(dirToList);

		if (subFiles != null && subFiles.length > 0)
		{
			for (FTPFile aFile : subFiles)
			{
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals(".."))
				{
					// skip parent directory and the directory itself
					continue;
				}
				String filePath = parentDir + "/" + currentDir + "/"
						+ currentFileName;
				if (currentDir.equals(""))
				{
					filePath = parentDir + "/" + currentFileName;
				}

				if (aFile.isDirectory())
				{
					// remove the sub directory
					removeDirectory(dirToList, currentFileName);
				}
				else
				{
					// delete the file
					boolean deleted = ftpClient.deleteFile(filePath);
					if (deleted)
					{
						System.out.println("DELETED the file: " + filePath);
					}
					else
					{
						System.out.println("CANNOT delete the file: " + filePath);
					}
				}
			}

			// finally, remove the directory itself
			boolean removed = ftpClient.removeDirectory(dirToList);
			if (removed)
			{
				System.out.println("REMOVED the directory: " + dirToList);
			}
			else
			{
				System.out.println("CANNOT remove the directory: " + dirToList);
			}
		}
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
    
    private static boolean createDirectory(String remoteDirPath)
	{
        // Verzeichnis anlegen				           
        boolean success = false;
		try
		{
			success = ftpClient.makeDirectory(remoteDirPath);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        showServerReply(ftpClient);
        if (success) {
            System.out.println("Successfully created directory: " + remoteDirPath);
        } else {
            System.out.println("Failed to create directory. See server's reply.");
        }

        return success;
	}

}
