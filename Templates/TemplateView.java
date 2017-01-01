package it.naturtalent.tools;

import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.UISynchronize;
import org.osgi.service.prefs.BackingStoreException;

// Praeferenzen
@Inject
@Optional
private void connectWiFi(
		@Preference(nodePath = "it.naturtalent.technik", value = WIFI_HOST) String host,
		@Preference(nodePath = "it.naturtalent.technik", value = WIFI_PORT) String port)
{
	Host = host;
	Port = port;
	
	Host = "wifi-extension-v2";
	
	System.out.println("Start ConnectJob");
	connectJob();
}

@Inject
@Optional
public void trackUserSettings(
		@Preference(nodePath = "it.naturtalent.technik", value = WIFI_HOST) String user)
{
	user = (user == null) ? "wifi-extension-v1" : user;
	System.out.println("New user: " + user);
}


@Inject
public RemoteSocketsView(UISynchronize sync,
		@Preference(nodePath = "it.naturtalent.technik") IEclipsePreferences prefs)
{
	this.sync = Objects.requireNonNull(sync);
	this.prefs = prefs;

	try
	{
		String[] names = prefs.keys();
		if (ArrayUtils.isEmpty(names))
		{
			prefs.put(WIFI_HOST, WIFI_DEFAULT_HOST);
			prefs.put(WIFI_PORT, WIFI_DEFAULT_PORT);
			prefs.flush();
		}
	} catch (BackingStoreException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}