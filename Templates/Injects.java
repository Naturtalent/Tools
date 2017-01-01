package it.naturtalent.technik.parts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;

import it.naturtalent.icons.core.Icon;
import it.naturtalent.icons.core.IconSize;
import it.naturtalent.technik.Messages;

public class RemoteSocketsView
{
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	
	private StructuredViewer viewer;
	
	public enum ViewActionID
	{
		ADD_TASK,
		EDIT_TASK,
		DELETE_TASK,
		SAVE_TASK,
	}
	private Map<ViewActionID, Action>actionRegistry = new HashMap<ViewActionID, Action>();
	
	// KontextMenues
	private Map<ViewActionID, MenuItem> menuRegistry = new HashMap<ViewActionID, MenuItem>();

	public RemoteSocketsView()
	{
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent)
	{
		parent.setLayout(new GridLayout(1, false));
		
		Section sctnRemoteSockets = formToolkit.createSection(parent, Section.TITLE_BAR);
		sctnRemoteSockets.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(sctnRemoteSockets);
		sctnRemoteSockets.setText(Messages.RemoteSocketsView_sctnRemoteSockets_text);
		
		Composite compositeClient = formToolkit.createComposite(sctnRemoteSockets, SWT.NONE);
		formToolkit.paintBordersFor(compositeClient);
		sctnRemoteSockets.setClient(compositeClient);
		compositeClient.setLayout(new GridLayout(1, false));
		
		
		createSectionToolbar(sctnRemoteSockets);
		createContextMenu();
		
	}
	
	private void createSectionToolbar(Section section)
	{
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(section);		
		section.setTextClient(toolbar);
		
		final Cursor handCursor = new Cursor(Display.getCurrent(),SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		// Cursor needs to be explicitly disposed
		toolbar.addDisposeListener(new DisposeListener()
		{
			public void widgetDisposed(DisposeEvent e)
			{
				if (handCursor.isDisposed() == false)				
					handCursor.dispose();				
			}
		});
	
		Action action;
		
		// ADD	
		action = new Action(){};
			
		toolBarManager.add(action);
		actionRegistry.put(ViewActionID.ADD_TASK, action);
		
		toolBarManager.update(true);		
	}
	
	private void createContextMenu()
	{
		Table table = ((TableViewer)viewer).getTable();
		Menu menu = new Menu(table);
		table.setMenu(menu);
		
		MenuItem menuItem;
		
		// ADD
		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				actionRegistry.get(ViewActionID.ADD_TASK).run();
			}
		});
		menuItem.setText("hinzufügen");
		menuItem.setImage(Icon.COMMAND_ADD.getImage(IconSize._16x16_DefaultIconSize));
		menuRegistry.put(ViewActionID.ADD_TASK, menuItem);
	}
	
	/*
	 * Enable-/Disablestatus der Aktion-und Menueeintraege aktualisieren
	 */
	private void updateWidgetStatus()
	{
		StructuredSelection selection = (StructuredSelection)viewer.getSelection();
		Object selObj = selection.getFirstElement();

		// zunaechst alle disablen
		Set<ViewActionID>keySet = menuRegistry.keySet();
		for(ViewActionID id : keySet)
		{
			actionRegistry.get(id).setEnabled(false);
			menuRegistry.get(id).setEnabled(false);			
		}
				
		for(ViewActionID id : keySet)
			actionRegistry.get(id).setEnabled(false);

		// Enable Logik
		for(ViewActionID id : keySet)
		{
			Action action = actionRegistry.get(id);	
			MenuItem menueItem = menuRegistry.get(id);
			
			switch (id)
				{
				
				}
		}
	}


	@PreDestroy
	public void dispose()
	{
	}

	@Focus
	public void setFocus()
	{
		// TODO	Set the focus to control
	}

}
