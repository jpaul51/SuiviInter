package com.piyou.views.main;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

import com.piyou.UserContextFactory;
import com.piyou.backend.model.Displayable;
import com.piyou.backend.model.Name;
import com.piyou.backend.services.DisplayableService;
import com.piyou.backend.services.ServiceProxy;
import com.piyou.backend.util.TranslationUtils;
import com.piyou.views.components.AbstractSimpleSuperComponent;
import com.piyou.views.components.AbstractSuperCustomField;
import com.piyou.views.components.AbstractSuperDisplayableComponent;
import com.piyou.views.components.DateTimeComponent;
import com.piyou.views.components.LabelComponent;
import com.piyou.views.components.RichTextEditorComponent;
import com.piyou.views.components.SelectComponent;
import com.piyou.views.components.SuperComponentInterface;
import com.piyou.views.components.TextAreaComponent;
import com.piyou.views.components.TextFieldComponent;
import com.piyou.views.components.TimeComponent;
import com.piyou.views.descriptors.EAppFieldsTranslation;
import com.piyou.views.descriptors.InvalidActionDescriptorException;
import com.piyou.views.descriptors.InvalidFieldDescriptorException;
import com.piyou.views.descriptors.MainEntity;
import com.piyou.views.model.Action;
import com.piyou.views.model.ActionType;
import com.piyou.views.model.Application;
import com.piyou.views.model.FieldDetail;
import com.piyou.views.model.Input;
import com.piyou.views.model.TableLayoutManager;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

@Route(value = "bim", layout = MainView.class)
public class SplitView extends AbstractView implements HasUrlParameter<String> {

	@Inject
	UserContextFactory userContextFactory;

	@Autowired
	ServiceProxy serviceProxy;

//	HashSet<Component> components;

	DisplayableService displayableService;

    Label labelId = new Label();

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 222222L;

	String pageTitle = "vide";

	Div a = new Div();
	Class<? extends Application> viewClazz;
	Class<? extends Displayable> modelClazz;
	Grid<Displayable> grid;

	private Binder<Displayable> binder;

	private Button cancel = new Button("Cancel");
	private Button btnSave = new Button("Save");
	private Button btnDelete = new Button("Delete");

	Application application;

	FormLayout formLayout = new FormLayout();
	List<SuperComponentInterface<?,? extends Component>> formComponents = new ArrayList<>();

	HashMap<FieldDetail, SuperComponentInterface<?,? extends Component>> componentsByField = new HashMap<>();

	SplitLayout splitLayout;
	Div toolbar;

	Displayable currentDisplayable;
	List<Displayable> displayables = new ArrayList<>();

	HashMap<FieldDetail, PropertyDescriptor> propDescriptorByField = new HashMap<>();

	public SplitView() throws InvalidFieldDescriptorException, InvalidActionDescriptorException {
		super();

		Class<?> cl = UserContextFactory.getCurrentUserContext().getCurrentClass();
		if (cl != null) {
			pageTitle = cl.getName();
			viewClazz = (Class<? extends Application>) cl;
			try {
				application = viewClazz.getConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		modelClazz = viewClazz.getAnnotation(MainEntity.class).value();
		Class<Displayable> clazz = (Class<Displayable>) modelClazz;
		binder = new Binder<>(clazz);
//		binder.wr

		grid = new Grid<>();
		grid.addClassName("my-grid");
		grid.getStyle().set("margin", "var(--lumo-space-m)");
		grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		grid.setHeight("300px");

		grid.setSizeFull();

		VerticalLayout vl = initToolbar();

		splitLayout = new SplitLayout();

		vl.add(grid);
		vl.getStyle().set("padding", "0px");
		vl.getStyle().set("overflow-x", "hidden");
		splitLayout.addToPrimary(vl);
		splitLayout.setSizeFull();
		createEditorLayout(splitLayout);

		TableLayoutManager tlManager = application.getTlManager();

		if (tlManager == null && !application.isNoTable()) {

			tlManager = new TableLayoutManager();
			tlManager.setColumns(application.getAllFields());

		}

		for (FieldDetail column : tlManager.getColumns()) {
			if(column.getName() == null) {
				throw new InvalidFieldDescriptorException("Field name is mandatory and must match a field");
			} 
			if(column.getTranslationKey() == null) {
				throw new InvalidFieldDescriptorException("Field translation key is mandatory");
			} 
			
			Column c;
			if(column.getType().equals(Input.TEXT_RICH)) {
				c = grid.addColumn(new ComponentRenderer<>(disp -> {
					
					StringBuilder stringcontent = new StringBuilder("<div class='htmlContainer'>");
					
					Optional<Object> valueProvided = createGridColumns(column, disp);
					
					
					stringcontent.append(valueProvided.isPresent() ? valueProvided.get() : "")
					
					
					.append("</div>");
					
	        		Html content = new Html(stringcontent.toString());           		
					return content;
					
					}));
			}else if(column.getType().equals(Input.DATE_TIME)) {
				c = grid.addColumn(new ComponentRenderer<>(disp -> {
	        		Div d = new Div();
//	        		LocalDateTime
	        		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
	        		formatter = formatter.localizedBy(TranslationUtils.locale);
	        		
	        		Optional<Object> providedValue = createGridColumns(column,disp);
	        		
	        		StringBuilder value;
	        		
	        		if(providedValue.isPresent()) {
	        			value =new StringBuilder().append(((LocalDateTime) providedValue.get() ).format(formatter));
	        		}else {
	        			value =new StringBuilder();
	        		}
	        		d.add(new Label((value.toString())));
					return d;					
					}));
			}else {
				c = grid.addColumn(createGridColumns(column));
			}
			
			c.setHeader(TranslationUtils.translate(column.getTranslationKey()));
			createBinder(column);

		}
		
		for(Action ac : application.getAction()) {
			if(ac.getActionType() == null) {
				throw new InvalidActionDescriptorException("Actiontype is mandatory");
			}
			//TODO: gérer les actions onchange et similaires
		}
		
		this.pageTitle = TranslationUtils.translate(this.application.getAppLabelKey());

		grid.addSelectionListener(e -> {
			Optional<Displayable> oDisplayable = e.getFirstSelectedItem();
			if (oDisplayable.isPresent()) {
				e.getSource().select(oDisplayable.get());
				populateForm(oDisplayable.get());
				splitLayout.getSecondaryComponent().setVisible(true);
				splitLayout.setSplitterPosition(70);
			}
		});

		GridSingleSelectionModel<Displayable> m = (GridSingleSelectionModel<Displayable>) grid.getSelectionModel();
		m.setDeselectAllowed(false);

//		splitLayout.getSecondaryComponent().setVisible(true);
		this.add(splitLayout);
	}

	private Optional<Object> createGridColumns(FieldDetail column, Displayable disp) {
		String fieldName = column.getName();

		Optional ret ;
		
		PropertyDescriptor pd = null;
		try {
		for (Field field : modelClazz.getDeclaredFields()) {
			if (field.getAnnotation(Name.class) != null) {
				String currfieldName = field.getAnnotation(Name.class).value();
				if (currfieldName.equals(fieldName)) {
					fieldName = currfieldName;
					pd = new PropertyDescriptor(currfieldName, modelClazz);
				}
			}
		}
		if (pd == null) {
			pd = new PropertyDescriptor(fieldName, modelClazz);
		}
		propDescriptorByField.put(column, pd);
		
		Object res = pd.getReadMethod().invoke(disp); 
		
		ret = Optional.ofNullable(res);
		
		
		
		
		return ret;
		}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private VerticalLayout initToolbar() {
		toolbar = new Div();
		toolbar.setWidthFull();
		toolbar.getElement().getClassList().add("header-layout");

		HorizontalLayout hl = new HorizontalLayout();

//		hl.setWidthFull();
//		hl.setAlignItems(Alignment.END);
		Button btnAdd = new Button(new Icon(VaadinIcon.PLUS));
		btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		hl.add(btnAdd);
		toolbar.add(hl);

		btnAdd.addClickListener(e -> {
			populateForm(null);
			grid.deselectAll();
			grid.asSingleSelect().clear();
			splitLayout.getSecondaryComponent().setVisible(true);
		});

		toolbar.getStyle().set("padding", "var(--lumo-space-s) var(--lumo-space-l)");
		toolbar.getStyle().set("border-right", "10px");
		toolbar.getStyle().set("border-color", "#66a8ff");
		VerticalLayout vl = new VerticalLayout();
		vl.add(toolbar);
		return vl;
	}

	private void populateForm(Displayable object) {
		if (object == null) {
			labelId.setText(EAppFieldsTranslation.APP_FIELDS_ADD.name().concat(" ").concat(pageTitle));
			currentDisplayable = null;
			for (SuperComponentInterface<?,? extends Component> disp : formComponents) {
				disp.setValue(null);
			}
		} else {

			this.currentDisplayable = object;
			labelId.setText(pageTitle.concat(" ").concat(currentDisplayable.getId().toString()));
			binder.readBean(object);
		}

	}

	private ValueProvider<Displayable, ?> createGridColumns(FieldDetail column) {
		return disp -> {
			try {
				String fieldName = column.getName();

				PropertyDescriptor pd = null;

				for (Field field : modelClazz.getDeclaredFields()) {
					if (field.getAnnotation(Name.class) != null) {
						String currfieldName = field.getAnnotation(Name.class).value();
						if (currfieldName.equals(fieldName)) {
							fieldName = currfieldName;
							pd = new PropertyDescriptor(currfieldName, modelClazz);
						}
					}
				}
				if (pd == null) {
					pd = new PropertyDescriptor(fieldName, modelClazz);
				}
				propDescriptorByField.put(column, pd);
				return pd.getReadMethod().invoke(disp);

			} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException
					| IntrospectionException e) {
				e.printStackTrace();
				return null;
			}
		};
	}


	private void createBinder(FieldDetail field) {
		
		String translatedFieldLabel = TranslationUtils.translate(field.getTranslationKey());

		String fieldType = field.getType();
		if(fieldType == null) {
			fieldType = Input.TEXT_INPUT;
		}
		
		SuperComponentInterface<?,?> component;
		 
		switch (fieldType) {

		case Input.TEXT_RICH:
//			LazyVerticalLayout layout = new LazyVerticalLayout();

			
			component = new RichTextEditorComponent(field);
			break;
		
		case Input.TEXT_AREA:
//			SuperComponentInterface<? extends Component, String> component;
			 
			component = new TextAreaComponent(field, translatedFieldLabel);
			break;
		case Input.SELECT:
//			SuperComponentInterface<?> component;
			try {
				component = new SelectComponent<>( field, viewClazz);
			}catch(InvalidFieldDescriptorException e) {
				e.printStackTrace();
				component = new LabelComponent(field, e.getLocalizedMessage());
			}
			break;
			
		case Input.DATE_TIME:
			try {
				component = new DateTimeComponent(field);
			}catch(InvalidFieldDescriptorException e) {
				e.printStackTrace();
				component = new LabelComponent(field, e.getLocalizedMessage());
			}
			break;
		case Input.DATE_TIME_ONLY:
			component = new TimeComponent(field);
			break;
		case Input.TEXT_INPUT:
		default:
//			SuperComponentInterface<? extends Component,String> component;
			 
			component = new TextFieldComponent(field, translatedFieldLabel);
			break;

		}
		component.setFieldDetail(field);
		component.setReadOnly(field.getReadOnly());

		binder.forField(component).bind(field.getName());
		componentsByField.put(field, component);
		formComponents.add(component);
		formLayout.add(component.getComponent());
	}

	private void createEditorLayout(SplitLayout splitLayout) {
		Div panelDiv = new Div();
		createButtonLayout(panelDiv);
		panelDiv.setId("editor-layout");
		panelDiv.setWidthFull();

		Div formDiv = new Div();

		formLayout.getStyle().set("overflow", "hidden");

		panelDiv.setVisible(false);
		formDiv.add(formLayout);
		panelDiv.add(formDiv);
		formDiv.getStyle().set("overflow-y", "auto");
//	        formDiv.getStyle().set("background-color", "#294365 !important");

		formDiv.getClassNames().add("scrollable-div");
		panelDiv.getStyle().set("overflow", "hidden");
		formDiv.getStyle().set("padding", "var(--lumo-space-m)");
		panelDiv.getStyle().set("padding", "0px");
		splitLayout.addToSecondary(panelDiv);

	}

	private void createButtonLayout(Div editorDiv) {
		HorizontalLayout headerLayout = new HorizontalLayout();

		HorizontalLayout labelLayout = new HorizontalLayout();
//	        labelId.setVisible(false);
	        labelLayout.add(labelId);
	       
		labelLayout.getClassNames().add("editorHeader");

		headerLayout.getClassNames().add("header-layout");
		headerLayout.setId("header-layout");
		headerLayout.setWidthFull();
		headerLayout.setSpacing(true);
		
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		
		HorizontalLayout buttonLayout = new HorizontalLayout(); 
		buttonLayout .add(btnDelete, cancel, btnSave);
		buttonLayout.getClassNames().add("button-layout"); 
//		labelLayout.setAlignItems(Alignment.START);
		labelLayout.getClassNames().add("label-layout");
		
		btnDelete.addClickListener(c -> {
			deleteDisplayable();
		});
		
		cancel.addClickShortcut(Key.ESCAPE);
		btnSave.addClickShortcut(Key.F2);
		btnDelete.addClickShortcut(Key.KEY_S, KeyModifier.ALT);
		
		cancel.addClickListener(e -> {
			grid.asSingleSelect().clear();
			cancel.clickInClient();
//        	splitLayout.setSplitterPosition(100);
			splitLayout.getSecondaryComponent().setVisible(false);
			grid.focus();
			
		});

		btnSave.addClickListener(e -> {
			saveDisplayable();

		});
		headerLayout.add(labelLayout, buttonLayout);
//	        headerLayout.add(buttonLayout);
		editorDiv.add(headerLayout);
	}

	private void deleteDisplayable() {
		if(currentDisplayable != null) {
			displayableService.delete(currentDisplayable);
			
			displayables.remove(currentDisplayable);
			grid.setItems(displayables);
			cancel.clickInClient();
			cancel.click();
		}
	}

	private void saveDisplayable() {
		boolean isNew = false;
		if (currentDisplayable == null) {
			isNew = true;
			try {
				currentDisplayable = modelClazz.getConstructor().newInstance();

				Iterator<Entry<FieldDetail, PropertyDescriptor>> it = propDescriptorByField.entrySet().iterator();

				while (it.hasNext()) {
					Entry<FieldDetail, PropertyDescriptor> entry = it.next();
					FieldDetail fd = entry.getKey();
					PropertyDescriptor pd = entry.getValue();

					pd.getWriteMethod().invoke(currentDisplayable, componentsByField.get(fd).getValue());
				}

			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {

			application.getAction().stream().filter(a -> a.getActionType().equals(ActionType.SUBMIT)).forEach(a ->{
				Iterator<Entry<FieldDetail, Object>> updates = a.getUpdates().entrySet().iterator();
				
				while(updates.hasNext()) {
					Entry<FieldDetail, Object> entry = updates.next();
					FieldDetail field = entry.getKey();
					Object updatedValue = field.getDefaultValue();
					
//					Class<?> clazz = componentsByField.get(field).getType(); 
					
					SuperComponentInterface<?, ?> component = componentsByField.get(field);
					if(component instanceof AbstractSimpleSuperComponent) {
						((AbstractSimpleSuperComponent<Object>)componentsByField.get(field)).setValue((updatedValue));
					}else if(component instanceof AbstractSuperCustomField) {
						((AbstractSuperCustomField)componentsByField.get(field)).setValue((String) (updatedValue));
					}else if(component instanceof AbstractSuperDisplayableComponent) {
						((AbstractSuperDisplayableComponent)componentsByField.get(field)).setValue((updatedValue));

					}
				}
				
			});
			
			binder.writeBean(currentDisplayable);

			
			displayableService.update(currentDisplayable);
			populateForm(currentDisplayable);
			if (!isNew) {
				grid.getDataProvider().refreshItem(currentDisplayable);
			} else {
				displayables.add(0, currentDisplayable);
				grid.setItems(displayables);
			}

		} catch (ValidationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// TODO Auto-generated method stub
		Location location = event.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();

		displayableService = serviceProxy.getInstance(modelClazz);
		displayables = displayableService.getAll();

		this.grid.setItems(displayables);
		
		
		findEntityFieldsComponents().stream().forEach(s ->{
			s.initialize(serviceProxy);
		});

		if(application.getAppLabelKey() == null) {
			pageTitle = "UNDEFINED";
		}else {
			pageTitle = application.getAppLabelKey();
		}

	}

	private List<SuperComponentInterface<?,? extends Component>> findEntityFieldsComponents() {
		return componentsByField.entrySet().stream().filter(e -> e.getKey().getEntityDescriptor() != null)
				.map(m -> m.getValue())
		        .collect(Collectors.toList());
	}
	
	
	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		if (parameter != null) {

		}
	}

	@Override
	public String getPageTitle() {
		return pageTitle;
	}

}
