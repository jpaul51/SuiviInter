package com.piyou.views.main;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.piyou.backend.model.Displayable;
import com.piyou.backend.model.Intervention;
import com.piyou.backend.model.Person;
import com.piyou.backend.model.Project;
import com.piyou.backend.model.SeriazableFunctionCutom;
import com.piyou.backend.services.InterventionService;
import com.piyou.backend.services.PersonService;
import com.piyou.backend.services.ProjectService;
import com.piyou.views.components.RichTextEditorBuilder;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridSingleSelectionModel;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wontlost.ckeditor.VaadinCKEditor;

@Route(value = "interventions", layout = MainView.class)
@PageTitle("Suivi des interventions")
@CssImport("styles/views/main/suiviInter.css")
public class InterventionsDetailView extends Div implements AfterNavigationObserver {
    @Autowired
    private InterventionService service;
    @Autowired
    private PersonService personService;
    @Autowired
    private ProjectService projectService;

    private Grid<Intervention> interventions;

    private TextField interId = new TextField();
    private Select<Person> user = new Select<>();
    private Select<Project> project = new Select<>();
    private TextArea description = new TextArea();
    private VaadinCKEditor  ckDetails;
    private DateTimePicker createDate = new DateTimePicker();
    private DateTimePicker dateLastUpdate = new DateTimePicker();
    private TimePicker pickerDuration = new TimePicker();

    private Button cancel = new Button("Cancel");
    private Button btnSave = new Button("Save");

    private Binder<Intervention> binder;
    
    private List<Intervention> interList ;
    
    Intervention currentInter = new Intervention();
    Div panelDiv = new Div();
    
    Label labelId = new Label("Nouvelle intervention");

    
    public static <T, U> SeriazableFunctionCutom<T, U> makeSerializable(SeriazableFunctionCutom<T, U> function) {
        return function;
    }
    
    public InterventionsDetailView() {
        setId("master-detail-view");
        
        ckDetails = RichTextEditorBuilder.richTextEditor("hello");
        ckDetails.setMaxHeight("100px");
        ckDetails.getElement().getStyle().set("margin", "0px");
        // Configure Grid
        interventions = new Grid<>();
        
        interventions.addClassName("my-grid");
        
        interventions.getStyle().set("margin", "var(--lumo-space-m)");
    
        interventions.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        interventions.setHeightFull();
        
        Column c = interventions.addColumn(Intervention::getId);
        c.setVisible(false);
        Column cUser = interventions.addColumn(inter -> inter.getOwner().getFirstName() == null ? "" : inter.getOwner().getFirstName())
        .setHeader("User");
        cUser.setVisible(false);
        
        Column cDescription = interventions.addColumn(Intervention::getDescription).setHeader("Description");
        cDescription.setWidth("200px");

        Column cLastModifiedDate = interventions.addColumn(new LocalDateTimeRenderer<>(
                Intervention::getLastModifiedDate,
                "dd/MM/yyyy HH:mm"))
            .setHeader("Modifié le");
        
        cLastModifiedDate.setWidth("50px");
        Column cDuration = interventions.addColumn(i -> i.getDuration() == null ? 0 : i.getDuration().getMinute()).setHeader("Durée");
        cDuration.setWidth("50px");
        
        Column cCommentaire = interventions.addColumn(new ComponentRenderer<>(inter -> {
        		Html content = new Html(new StringBuilder("<div class='htmlContainer'>")
        								.append(inter.getCommentaire())
        								.append("</div>").toString());           		
                return content;
            
        })).setHeader("Commentaire");
        
        cCommentaire.setWidth("200px");
        createDate.setReadOnly(true);

        // Configure Form
        binder = new Binder<>(Intervention.class);
        
        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(user).bind(Intervention::getOwner, Intervention::setOwner);
        binder.forField(createDate).bind(Intervention::getCreatedDate, Intervention::setCreatedDate);
        binder.forField(dateLastUpdate).bind(Intervention::getLastModifiedDate, Intervention::setLastModifiedDate);
        binder.forField(interId).withConverter(new StringToLongConverter("inter id error")).bind( Intervention::getId, Intervention::setId);
        binder.forField(ckDetails).bind(Intervention::getCommentaire, Intervention::setCommentaire);
        binder.forField(project).bind(Intervention::getProject, Intervention::setProject);
        binder.forField(pickerDuration).bind(Intervention::getDuration, Intervention::setDuration);
        binder.bindInstanceFields(this);

//        binder.bind(Intervention::getDescription, description);
        // note that password field isn't bound since that property doesn't exist in
        // Employee

        
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        interventions.addSelectionListener(e->{
        	Optional<Intervention> oInter = e.getFirstSelectedItem();
        	if(oInter.isPresent()) {
        		e.getSource().select(oInter.get());
        	    populateForm(oInter.get());
            	splitLayout.getSecondaryComponent().setVisible(true);
            	splitLayout.setSplitterPosition(70);
        	}
        });
        
        GridSingleSelectionModel<Intervention> m = (GridSingleSelectionModel<Intervention>) interventions.getSelectionModel();
        m.setDeselectAllowed(false);

        add(splitLayout);
        
 
        cancel.addClickListener(e -> {
        	interventions.asSingleSelect().clear();
        	splitLayout.getSecondaryComponent().setVisible(false);
        	});

        btnSave.addClickListener(e -> {

        	try {
        		
        		LocalDateTime updatedDate = LocalDateTime.now();
        		if (currentInter == null) {
        			currentInter = new Intervention();
        			currentInter.setCreatedDate(updatedDate);
        			createDate.setValue(updatedDate);
        		}
        		binder.writeBean(currentInter);
        		
        		currentInter.setLastModifiedDate(updatedDate);
				
				boolean isNew = Objects.isNull(currentInter.getId());
				
				service.update(currentInter);
				populateForm(currentInter);
				if( !isNew) {
					interventions.getDataProvider().refreshItem(currentInter);
				}else {
					interList.add(0,currentInter);
					interventions.setItems(interList);
				}
			} catch (ValidationException e1) {
				e1.printStackTrace();
			}
   
        });

      
    }
    
 
    
	public  <V extends String, Intervention> Column<Intervention> addColumn(Grid<Intervention> grid, String header, 
			ValueProvider<Intervention, String> valueProvider) 
			throws ClassCastException{
    	
    		
		ValueProvider<Intervention, Html> newValueProvider = new ValueProvider<Intervention, Html>() {

			@Override
			public Html apply(Intervention source) {
				String value = valueProvider.apply(source);
				if(value == null)
					value="";
				return new Html(new StringBuilder("<div class='htmlContainer'><textarea rows='5' class='transparenttextarea'>")
						.append(value)
						.append("</textarea></div>").toString());
			}
		};
		
//    	interList.addColumn(new ComponentRenderer<>(e -> {
//            String value = (String) e.getProject().getName();
//            return new Html(value);
//        }));
    	
    	return grid.addComponentColumn(newValueProvider);
    	
    
    }
  
    
     SerializableFunction<Object, Html> contentGen( ) {
    	return new SerializableFunction<Object, Html>() {

			@Override
			public Html apply(Object t) {
				return new Html(new StringBuilder("<div class='htmlContainer'><textarea class='transparenttextarea'>")
						.append(this.apply(t))
						.append("</textarea></div>").toString());
			
			}

		
		};
    }

    private void createEditorLayout(SplitLayout splitLayout) {
    	

    	
//    	panelDiv.setWidthFull();
//    	this.getUI().ifPresent(e->e.);
    	createButtonLayout(panelDiv);
//        Div editorDiv = new Div();
    	panelDiv.setId("editor-layout");
    	panelDiv.setWidthFull(); 	
    	
    	Div formDiv = new Div();
    
        FormLayout formLayout = new FormLayout();
        
        formLayout.getStyle().set("overflow", "hidden");
        formLayout.add(user);
        user.setLabel("User");
        
        formLayout.add(project);
        project.setLabel("Projet");
        
         
//        panelDiv.add(user);
        formLayout.add(description);
        description.setLabel("Description");
        
        formLayout.setColspan(description, 2);
        
        formLayout.add(dateLastUpdate);
//        panelDiv.add(createDate);
        dateLastUpdate.setLabel("Date de mise à jour");
        dateLastUpdate.setReadOnly(true);
        
        pickerDuration.setLabel("Durée");
        
        formLayout.add(pickerDuration);
        

//        formLayout.add(dur);
//        project.setLabel("Projet");

//        interId.setVisible(false);
//        formLayout.add(interId);
        Div commDiv = new Div();
        commDiv.add(ckDetails);

        ckDetails.setLabel("Détails");
        
        formLayout.add(commDiv, 2);
        formLayout.setColspan(commDiv, 2);

//        formLayout.add(editorDiv);
//        formLayout.add(commDiv);
        
//        panelDiv.add(formLayout);
        panelDiv.setVisible(false);
        formDiv.add(formLayout);
        panelDiv.add(formDiv);
        formDiv.getStyle().set("overflow-y", "auto");
//        formDiv.getStyle().set("background-color", "#294365 !important");
        
      
        formDiv.getClassNames().add("scrollable-div");
        panelDiv.getStyle().set("overflow", "hidden");
        formDiv.getStyle().set("padding", "var(--lumo-space-m)");
        panelDiv.getStyle().set("padding", "0px");
        splitLayout.addToSecondary(panelDiv);

    }
 
    private void createButtonLayout(Div editorDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        
        HorizontalLayout headerLayout = new HorizontalLayout();
        interId.setVisible(false);
        headerLayout.add(interId);
        System.out.println(interId.getValue());
        if(interId.getValue() != null) {
        	headerLayout.add(labelId);
        }else {
        	headerLayout.add(new Label("Nouvelle intervention"));
        }
        headerLayout.getClassNames().add("editorHeader");
//        headerLayout.setAlignItems(Alignment.START);
//        headerLayout.setWidth("50%");
//        interId.addThemeVariants(TextFieldVariant.LUMO_SMALL);
//        interId.getStyle().set("width", "auto");
//        interId.getElement().getStyle().set("width", "auto");
        interId.setReadOnly(true);
//        interId.getClassNames().add("itemIdNum");
//        interId.getStyle().set("border", "none");
        
        buttonLayout.getClassNames().add("button-layout");
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(headerLayout, cancel, btnSave);
//        headerLayout.add(buttonLayout);
        editorDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
    	
    	
    	Div toolbar = new Div();
    	toolbar.setWidthFull();
    	toolbar.getElement().getClassList().add("button-layout");
    	
    	HorizontalLayout hl = new HorizontalLayout();
   
    	hl.setWidthFull();
    	hl.setAlignItems(Alignment.END);
    	Button btnAdd = new Button(new Icon(VaadinIcon.PLUS));
    	btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	hl.add(btnAdd);
    	toolbar.add(hl);
    	
    	btnAdd.addClickListener(e ->{
    		populateForm(null);
    		interventions.deselectAll();
    		interventions.asSingleSelect().clear();
    		splitLayout.getSecondaryComponent().setVisible(true);
    	});
    	
    	toolbar.getStyle().set("padding", "var(--lumo-space-s) var(--lumo-space-l)");
    	toolbar.getStyle().set("border-right", "10px");
    	toolbar.getStyle().set("border-color", "#66a8ff");
//        Div wrapper = new Div();
        VerticalLayout vl = new VerticalLayout();
        vl.add(toolbar);
        vl.add(interventions);
        vl.getStyle().set("padding", "0px");
        vl.getStyle().set("overflow-x", "hidden");

        splitLayout.addToPrimary(vl);
    }

  
    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Lazy init of the grid items, happens only when we are sure the view will be
        // shown to the user
    	
    	interList = service.getAll();
    	List<Displayable> personList = personService.getAll();

//    	user.setItems(personList);
    	user.setItemLabelGenerator(Person::getFirstName);
    	
    	List<? extends Displayable> projects = projectService.getAll();
    	project.setItems((Collection<Project>) projects);
    	project.setItemLabelGenerator(Project::getName);
    	
        interventions.setItems(interList);
        
//        interventions.setItemLabelGenerator
//        user.seti
    }
//
    private void populateForm(Intervention value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
        currentInter = value;

        if(value != null) {
        	labelId.setText("Intervention n° "+value.getId().toString());
        }else {
        	labelId.setText("Nouvelle intervention");
        }
//        user.setValue(value.getOwner()); 
//        createDate.setValue(value.getCreatedDate());

    }
}
    
