package it.naturtalent.p2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;



public class deployPostProcessing
{

	// Verzeichnis der Deploy-Files  
	private static final String PRODUCTZIPDIR = "/home/dieter/Repositories2019/NaturtalentRCP/products/it.naturtalent.produkt/target/products";
	private static final String LINUX_PRODUCTZIPFILE = "RCP product-linux.gtk.x86_64.zip";
	private static final String WINDOWS_PRODUCTZIPFILE = "RCP product-win32.win32.x86_64.zip";
	private static final String TEMP_DESTINATION_DIR = "/home/dieter/deploy";
	public static final int EOF = (-1);
	
	private static final String LINUX_OS = "linux";
	private static final String WINDOWS_OS = "windows";
	
	// jre-Runtimes
	private static final String WIN_JAVA6 = "/home/dieter/Repositories2019/Tools/it.naturtalent.p2/JRE/java8/jre";

	private static final String WINDOWS_LAUNCHER = TEMP_DESTINATION_DIR + "/" + WINDOWS_OS + "/" + "NaturTalent.exe";
			
	//private static final String WINDOWS_LAUNCHER_IMAGE = "/home/dieter/Repositories-2019-032/Tools/it.naturtalent.tools/NaturtalentLauchIcons/Novak/ico/nt.ico";
	private static final String WINDOWS_LAUNCHER_IMAGE = "/home/dieter/Repositories2019/Tools/it.naturtalent.tools/Naturtalent Lauch Icons/Novak/nt.ico";
	
	private static File deployDir;
	
	public static void main(String[] args)
	{
		String OS = WINDOWS_OS;
		
		// entpacken des mit Maven erzeugten Produkt-Zip
		//unzipDeploy_Os(WINDOWS_OS);
		unzipDeploy_Os(OS);
		
		// ausgewaehlte Plugins entfernen
		List<String>toSelectPugin = new ArrayList<String>();
		toSelectPugin.add("org.eclipse.ui.themes");
		deleteSelectedPlugins(toSelectPugin);
		
		// weitere Manipulationen vornehmen
		//deployProcessing(WINDOWS_OS);
		deployProcessing(OS);
		
		// wieder in einem neuem Produkt-Zip packen
		//zipDeploy(WINDOWS_OS);
		zipDeploy(OS);
		
		System.out.println("Deployprocessing beendet");
	}
	
	/**
	 * Die von Maven erzeugte Produkt-Zip-Datei im Zielverzeichnis entpacken.
	 * 
	 * @param os
	 */
	public static void unzipDeploy_Os(String os)
	{
		String srcZipFile;
		
		 // in dieses Verzeichnis wird entpackt
		deployDir = new File(TEMP_DESTINATION_DIR);
		 
		 // von Maven erzeugte Produktzips (OS-abhaengig)
		 if(StringUtils.equals(os, LINUX_OS))
		 {
			 deployDir = new File(deployDir, LINUX_OS);
			 srcZipFile = PRODUCTZIPDIR+File.separator+LINUX_PRODUCTZIPFILE;
		 }		 	
		 else
		 {
			 deployDir = new File(deployDir, WINDOWS_OS);
			 srcZipFile = PRODUCTZIPDIR+File.separator+WINDOWS_PRODUCTZIPFILE;
		 }
		 
		// Zielvereichnis erstellen bzw. bestehendes loeschen 
		try
		{
			if(deployDir.exists() && deployDir.isDirectory())
			{
				// Zielverzeichnis loeschen 
				FileUtils.cleanDirectory(deployDir);
			}
			else
			{
				// ein neues Zielverzeichnis erstellen
				deployDir.mkdir();
			}
			
			// Produktzip entpacken
			unzipArchiv(srcZipFile, deployDir);
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
	}
	
	/**
	 * definierte Plugins entfernen
	 * 
	 * @param toSelectPlugins
	 */
	private static void deleteSelectedPlugins(List<String> toSelectPlugins)
	{
		if((deployDir != null) && (deployDir.isDirectory()))
		{
			File pluginDir = new File(deployDir, "plugins");
			
			IOFileFilter notFileFilter = FileFilterUtils.notFileFilter(FileFilterUtils.fileFileFilter());		
			Collection<File>plugins = FileUtils.listFilesAndDirs(pluginDir, notFileFilter, TrueFileFilter.INSTANCE);
			
			for(File plugin : plugins)
			{
				String pluginName = plugin.getName();
				for(String toSelectPlugin : toSelectPlugins)
				{
					if(StringUtils.startsWith(pluginName, toSelectPlugin))
					{
						try
						{
							FileUtils.deleteDirectory(plugin);
							System.out.println("Plugin: "+pluginName+" wurde entfernt");
							break;
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
				
	}
	
	public static void zipDeploy(String os)
	{
		String destZipFile;
		String srcFolder;
		
		 if(StringUtils.equals(os, LINUX_OS))
		 {
			 srcFolder = TEMP_DESTINATION_DIR+File.separator+LINUX_OS;
			 destZipFile = TEMP_DESTINATION_DIR+File.separator+LINUX_PRODUCTZIPFILE;
		 }
		 	
		 else
		 {
			 srcFolder =  TEMP_DESTINATION_DIR+File.separator+WINDOWS_OS;
			 destZipFile = TEMP_DESTINATION_DIR+File.separator+WINDOWS_PRODUCTZIPFILE;
		 }

		 zipFiles(srcFolder, destZipFile);
	}


	
	
	/*
	 * das Archiv 'srcZipFile' im Zielverzeichnis entpacken
	 */
	public static void unzipArchiv(String srcZipFile, File targetDir)
	{
		try
		{
			ZipFile zipFile = new ZipFile(srcZipFile);

			System.out.println("Program Start unzipping the given zipfile");
			for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e.hasMoreElements();)
			{
				ZipEntry zipEntry = (ZipEntry) e.nextElement();
				//System.out.print(zipEntry.getName() + " .");
				saveEntry(zipFile, zipEntry, targetDir);
				//System.out.println(". unpacked");
			}
			System.out.println("zipfile wurde erfolgreich entpackt");
			
		} catch (FileNotFoundException e)
		{
			System.out.println("zipfile not found");
		} catch (ZipException e)
		{
			System.out.println("zip error...");
		} catch (IOException e)
		{
			System.out.println("IO error...");
		}

	}
	
	/*
	 * ein einzelner Eintrag im Zielverzeichnis speichern
	 */
	public static void saveEntry(ZipFile zipFile, ZipEntry zipEntry, File targetDir) throws ZipException, IOException
	{
		File file = new File(targetDir, zipEntry.getName());

		if (zipEntry.isDirectory())
			file.mkdirs();
		
		else
		{
			InputStream is = zipFile.getInputStream(zipEntry);
			BufferedInputStream bis = new BufferedInputStream(is);
			File dir = new File(file.getParent());
			dir.mkdirs();
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			for (int c; (c = bis.read()) != EOF;) // oder schneller
				bos.write((byte) c);

			bos.close();
			fos.close();
		}
	}


	

    /**
     * Ein Archiv neu erstellen.
     * 
     * @param srcFolder
     * @param destZipFile
     * @return
     */
    public static boolean zipFiles(String srcFolder, String destZipFile) {
        boolean result = false;
        try {
            System.out.println("Program Start zipping the given files");
            /*
             * send to the zip procedure
             */
            zipFolder(srcFolder, destZipFile);
            result = true;
            System.out.println("Given files are successfully zipped");
        } catch (Exception e) {
            System.out.println("Some Errors happned during the zip process");
        } finally {
            return result;
        }
    }

    /*
     * zip the folders
     */
    private static void zipFolder(String srcFolder, String destZipFile) throws Exception {
        ZipOutputStream zip = null;
        FileOutputStream fileWriter = null;
        /*
         * create the output stream to zip file result
         */
        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);
        /*
         * add the folder to the zip
         */
        addFolderToZip("", srcFolder, zip);
        /*
         * close the zip objects
         */
        zip.flush();
        zip.close();
    }

    /*
	 * add folder to the zip file
	 */
	private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception
	{
		File folder = new File(srcFolder);
	
		/*
		 * check the empty folder
		 */
		if (folder.list().length == 0)
		{
			System.out.println("leeres Verzeichnis: "+folder.getName());
			addFileToZip(path, srcFolder, zip, true);
		}
		else
		{
			/*
			 * list the files in the folder
			 */
			for (String fileName : folder.list())
			{
				if (path.equals(""))
				{
					addFileToZip(folder.getName(), srcFolder + "/" + fileName,zip, false);
				}
				else
				{
					addFileToZip(path + "/" + folder.getName(),srcFolder + "/" + fileName, zip, false);
				}
			}
		}
	}

	/*
     * recursively add files to the zip files
     */
	private static void addFileToZip(String path, String srcFile,
			ZipOutputStream zip, boolean flag) throws Exception
	{
		/*
		 * create the file object for inputs
		 */
		File folder = new File(srcFile);

		/*
		 * if the folder is empty add empty folder to the Zip file
		 */
		if (flag == true)
		{
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
		}
		else
		{ /*
			 * if the current name is directory, recursively traverse it to get
			 * the files
			 */
			if (folder.isDirectory())
			{
				/*
				 * if folder is not empty
				 */
				addFolderToZip(path, srcFile, zip);
			}
			else
			{
				/*
				 * write the file to the output
				 */
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0)
				{
					/*
					 * Write the Result
					 */
					zip.write(buf, 0, len);
				}
			}
		}
	}
	
	/**
	 * Manipulationen am extrahierten Produkt-Zip Archiv
	 * @param os
	 */
	private static void deployProcessing(String os) 
	{
		 if(StringUtils.equals(os, WINDOWS_OS))
		 {
			// Java Runtime zum Archiv hinzufuegen
			File srcDir = new File(WIN_JAVA6);
			File destDir = new File(TEMP_DESTINATION_DIR+File.separator+WINDOWS_OS+File.separator+"jre"); //$NON-NLS-N$
			try
			{
				// JRE einbinden
				FileUtils.copyDirectory(srcDir, destDir);
				
				// Icons im launcher austauschen
				IconExe iconExe = new IconExe();
				iconExe.unloadICO(WINDOWS_LAUNCHER, WINDOWS_LAUNCHER_IMAGE);
				
				System.out.println("Ergänzungen am Archiv beendet");
				
			} catch (Exception e)
			{
				System.out.println("Error "+ e.getMessage());
			}
		 }
	}

}
