package it.naturtalent.p2;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class P2Utils
{
	private final static String POOL_FAETURE_PATH = "/home/dieter/.p2/pool/features";
	private static String FEATUREXML = "feature.xml";
	
	private static Map<String,List<String>>mapFeaturePool;
	
	
	public static Map<String, List> findFeaturesforPlugins(String [] plugins)
	{
		Map<String, List>resultMap = new HashMap<String, List>();
		
		if(!ArrayUtils.isEmpty(plugins))
		{
			for(String plugin : plugins)
			{
				List<String>foundFeatures = findFeaturesforPlugin(plugin);
				resultMap.put(plugin,foundFeatures);
			}
		}
		return resultMap;
	}
	
	/**
	 * Sucht im Pool nach allen Features, die das Plugin 'plugin' beinhalten.
	 * 
	 * @param plugin
	 * @return
	 */
	public static List<String> findFeaturesforPlugin(String plugin)
	{
		if((mapFeaturePool == null) || (mapFeaturePool.isEmpty()))
			readFeaturePool();	
		
		List<String>listFeatures = new ArrayList<String>();
		for(String featureName : mapFeaturePool.keySet())
		{
			List<String>festurePluginsList = mapFeaturePool.get(featureName);
			if(festurePluginsList.contains(plugin))
				listFeatures.add(featureName);
		}
		
		return listFeatures;
	}
	
	public static Map<String,List<String>> getFeaturePool()
	{
		if((mapFeaturePool == null) || (mapFeaturePool.isEmpty()))
			readFeaturePool();			
		return mapFeaturePool;
	}

	public static void readFeaturePool()
	{
		mapFeaturePool = new HashMap<String, List<String>>();
		
		List<File>featureFiles = P2Utils.getFeatureDirectories();		
		for(File featureFile : featureFiles)
		{
			String featureName = featureFile.getName();  
			featureName = StringUtils.substring(featureName, 0, StringUtils.indexOf(featureName, '_'));
			
			List<String>plugins = getFeatureContent(featureFile);
			
			
			mapFeaturePool.put(featureName, plugins);
		}
	}
	
	public static List<String> getFeatureContent(File featureDirectory)
	{
		List<String>listPlugins = new ArrayList<String>();
		
		File featureXML = new File(featureDirectory,FEATUREXML);
		if(featureXML.exists())
		{
			try
			{
				Document document = new SAXBuilder().build(featureXML);				
				Element rootElement = document.getRootElement();
				List<Element>listPluginsElement = rootElement.getChildren("plugin");
				if((listPluginsElement != null) && (!listPluginsElement.isEmpty()))
				{
					for(Element pluginElement : listPluginsElement)
					{
						Attribute pluginAttribute = pluginElement.getAttribute("id");
						listPlugins.add(pluginAttribute.getValue());
					}
				}						
				
			} catch (JDOMException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return listPlugins;
	}

	/**
	 * @return
	 */
	public static List<File> getFeatureDirectories()
	{
		List<File>featureList = new ArrayList<File>();
		File poolDir = new File(POOL_FAETURE_PATH);
		File [] features = poolDir.listFiles(new FilenameFilter()
		{						
			@Override
			public boolean accept(File dir, String name)
			{	
				return (new File(dir, name).isDirectory());
			}
		});
		
		for(File feature : features)
			featureList.add(feature);
			
		return featureList;
	}

}
