	/*
	 * sucht in der Editorperspektive nach dem 'modelElement' - Editorfenster 
	 */
	private MPart findEditorPart(Object modelElement)
	{
		MApplication application = E4Workbench.getServiceContext().get(IWorkbench.class).getApplication();		
		EModelService modelService = (EModelService) application.getContext()
                .get(EModelService.class.getName());
		MPartStack editorPartStack = (MPartStack) modelService.find(EDITOR_PARTSTACK_ID, application);
		
		List<MStackElement>partElements = editorPartStack.getChildren();
		for(MStackElement partElement : partElements)
		{
			if(partElement instanceof MPart)
			{
				MPart part = (MPart) partElement;				
				if (((MPart) partElement).getContext().get(ECPE4Editor.INPUT) == modelElement)				
					return (MPart) partElement; 
			}
		}
		
		return null;
	}