package it.naturtalent.p2;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FindFeature
{

	
	
	public static void main(String[] args)
	{
	
		/*
		String [] plugins = {
				
				"org.eclipse.emf.ecp.edit",
				"org.eclipse.emf.ecp.edit.swt",
				"org.eclipse.emf.ecp.ui.view.swt",
				"org.eclipse.emf.ecp.view.control.multireference",
				"org.eclipse.emf.ecp.view.core.swt",
				"org.eclipse.emf.ecp.template.model",
				"org.eclipse.emf.ecp.view.util.swt",
				"org.eclipse.emf.emfstore.client",
				"org.eclipse.emfforms.core.services.editsupport",
				"org.eclipse.emfforms.localization",
				"org.eclipse.emfforms.swt.core.di",				
		};
		*/
		
		/*
		String [] plugins = {
				
				"org.eclipse.emf.ecp.emfstore.core",
				"org.eclipse.emf.ecp.ui.e4",
				"org.eclipse.emf.emfstore.client.ui",
				"org.eclipse.emf.ecp.editor.e3",				
				"org.eclipse.emf.ecore.edit",
				"org.eclipse.emf.ecp.view.horizontal.model",
				"org.eclipse.emf.ecp.view.vertical.model",
		};
		*/

		/*
		String [] plugins = {
				
				"org.eclipse.emf.ecp.ui.e3",
				"org.eclipse.emf.ecp.editor.e3",
				"org.eclipse.emf.ecp.view.template.model.edit",
				"org.eclipse.emf.ecp.ide.util",
				"org.eclipse.emf.emfstore.migration",
				"org.eclipse.emf.emfstore.server",
				"org.eclipse.emf.emfstore.client.model.edit",
				"org.eclipse.emf.emfstore.server.model",
				"org.eclipse.emf.emfstore.server.model.edit",
		};
		*/

		/*
		String [] plugins = {
				
				"org.eclipse.emf.ecp.ui.e3",
				"org.eclipse.emf.ecp.editor.e3",
				"org.eclipse.emf.ecp.ide.util",
				"org.eclipse.emf.emfstore.common.model.edit",
				"org.eclipse.emf.emfstore.common.model",
				"org.eclipse.emf.emfstore.common",
				
		};
		*/
		
		
		String [] plugins = {

				/*
				"org.eclipse.equinox.p2.core",
				"org.eclipse.equinox.p2.engine",
				"org.eclipse.equinox.p2.metadata.repository",
				"org.eclipse.equinox.p2.operations",
				"org.eclipse.equinox.p2.transport.ecf",
				"org.eclipse.equinox.p2.ui",
				
				"org.eclipse.emf.common.ui",
				"org.eclipse.emf.databinding",
				"org.eclipse.emf.databinding.edit",
				"org.eclipse.emf.edit",
				*/
				
				
				"org.eclipse.equinox.concurrent",	// kein Feature gefunden
				"org.eclipse.equinox.security",		// org.eclipse.platform, org.eclipse.equinox.p2.core.feature,  org.eclipse.help
				"org.eclipse.equinox.security.ui",	// org.eclipse.platform, org.eclipse.equinox.p2.rcp.feature
			

				
				"org.eclipse.core.filesystem",	// org.eclipse.platform
				"org.eclipse.core.net",			// org.eclipse.platform
				"org.eclipse.core.resources",	// org.eclipse.platform
			
				
				
				"org.eclipse.emfforms.swt.control.multiattribute", // org.eclipse.emfforms.swt.control.multiattribute.feature
				"org.eclipse.net4j.util",
				"org.eclipse.ui.forms",		// org.eclipse.platform
				"org.eclipse.ui.views",		// org.eclipse.platform
				


				
				
		};
	



		
		Map<String, List>resultMap = P2Utils.findFeaturesforPlugins(plugins);
		for(String checkPlugin : resultMap.keySet())
		{
			List<String>features = resultMap.get(checkPlugin);
			if(features.isEmpty())
				features.add("--- kein Feature gefunden ---");
			
			for(String feature : features)
			{
				System.out.println('\n');
				System.out.println("Plugin: "+checkPlugin+" ist definiert in Feature: "+feature);
			}
		}
		
		/*
		List<String>features = P2Utils.findFeaturesforPlugin("org.eclipse.equinox.p2.director.app");		
		for(String feature : features)
		{
			System.out.println('\n');
			System.out.println("Plugin definiert in Feature: "+feature);
		}
		*/
	}

}
