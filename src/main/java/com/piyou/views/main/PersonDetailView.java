package com.piyou.views.main;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

import com.piyou.backend.model.Intervention;
import com.piyou.backend.model.Person;
import com.piyou.backend.services.PersonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "users", layout = MainView.class)
@PageTitle("Gestion des utilisateur")
@CssImport("styles/views/main/suiviInter.css")
public class PersonDetailView extends Div implements AfterNavigationObserver {

	@Autowired
	PersonService personService ;
	
	List<Person> listPerson;
	private Grid<Person> gridPerson;
	
	TextField txtUserId = new TextField();
	TextField txtPersonName = new TextField();
	TextField txtPersonLogin = new TextField();
	
	Label labelPersonId = new Label();
	
	private Binder<Person> binder;

	private Person currentPerson;
	
    private Button cancel = new Button("Cancel");
    private Button btnSave = new Button("Save");
	
    
    Div panelDiv = new Div();

	private SplitLayout splitLayout;
	
	public PersonDetailView() {
		
		gridPerson = new Grid<>();
	    
		gridPerson.addClassName("my-grid");
	        
		gridPerson.getStyle().set("margin", "var(--lumo-space-m)");
	    
		gridPerson.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		gridPerson.setHeightFull();
		


        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        
        Column cUserId = gridPerson.addColumn(Person::getId);
        cUserId.setHeader("Id");
        
        Column cUserName = gridPerson.addColumn(Person::getFirstName).setHeader("Prénom");
        Column cUserLogin = gridPerson.addColumn(Person::getLogin).setHeader("Login");
        
        binder = new Binder<>(Person.class);


        binder.forField(txtUserId).withConverter(new StringToLongConverter("person id error")).bind( Person::getId, Person::setId);
        binder.forField(txtPersonName).bind(Person::getFirstName, Person::setFirstName);
        binder.forField(txtPersonLogin).bind(Person::getLogin, Person::setLogin);

        /**
         * GridLayout
         */
        Div toolbar = new Div();
    	toolbar.setWidthFull();
    	toolbar.getElement().getClassList().add("button-layout");
    	
    	HorizontalLayout hl = new HorizontalLayout();
//    	hl.setSpacing(true);
//    	hl.th
    	
   
    	hl.setWidthFull();
    	hl.setAlignItems(Alignment.END);
    	Button btnAdd = new Button(new Icon(VaadinIcon.PLUS));
    	btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    	hl.add(btnAdd);
    	toolbar.add(hl);
    	
    	btnAdd.addClickListener(e ->{
    		populateForm(null);
    		gridPerson.deselectAll();
    		gridPerson.asSingleSelect().clear();
    		splitLayout.getSecondaryComponent().setVisible(true);
    	});
    	
    	toolbar.getStyle().set("padding", "var(--lumo-space-s) var(--lumo-space-l)");
    	toolbar.getStyle().set("border-right", "10px");
    	toolbar.getStyle().set("border-color", "#66a8ff");
//        Div wrapper = new Div();
        VerticalLayout vl = new VerticalLayout();
        vl.add(toolbar);
        vl.add(gridPerson);
        vl.getStyle().set("padding", "0px");
        vl.getStyle().set("overflow-x", "hidden");

        

        splitLayout.addToPrimary(vl);
        
        createEditorLayout(splitLayout);
        
        add(splitLayout);
        this.setHeightFull();
	}
	
	
    private void populateForm(Person value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
        currentPerson = value;

        if(value != null) {
        	labelPersonId.setText("Utilisateur n° "+value.getId().toString());
        }else {
        	labelPersonId.setText("Nouvel utilisateur");
        }
//        user.setValue(value.getOwner()); 
//        createDate.setValue(value.getCreatedDate());

    }
    
    
    private void createButtonLayout(Div editorDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        
        HorizontalLayout headerLayout = new HorizontalLayout();
//        interId.setVisible(false);
        headerLayout.add(txtUserId);
//        System.out.println(txtUserId.getValue());
        if(txtUserId.getValue() != null) {
        	headerLayout.add(labelPersonId);
        }else {
        	headerLayout.add(new Label("Nouvelle intervention"));
        }
        headerLayout.getClassNames().add("editorHeader");

        txtUserId.setReadOnly(true);

        
        buttonLayout.getClassNames().add("button-layout");
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(headerLayout, cancel, btnSave);
//        headerLayout.add(buttonLayout);
        editorDiv.add(buttonLayout);
        
        btnSave.addClickListener(e -> {
        	try {
        		
        		if (currentPerson == null) {
        			currentPerson = new Person();
        		}
				binder.writeBean(currentPerson);
				
				boolean isNew = Objects.isNull(currentPerson.getId());
				
				personService.update(currentPerson);
				populateForm(currentPerson);
				if( !isNew) {
					gridPerson.getDataProvider().refreshItem(currentPerson);
				}else {
					listPerson.add(0,currentPerson);
					gridPerson.setItems(listPerson);
				}
			} catch (ValidationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        
        cancel.addClickListener(e -> {
        	gridPerson.asSingleSelect().clear();
//        	splitLayout.setSplitterPosition(100);
        	splitLayout.getSecondaryComponent().setVisible(false);
        	});
    }
    
    
 private void createEditorLayout(SplitLayout splitLayout) {
    	

    	createButtonLayout(panelDiv);

    	panelDiv.setId("editor-layout");
    	panelDiv.setWidthFull(); 	
    	
    	Div formDiv = new Div();
    
        FormLayout formLayout = new FormLayout();
        
        formLayout.getStyle().set("overflow", "hidden");
        formLayout.add(txtUserId);
        txtUserId.setLabel("ID");
        
        formLayout.add(txtPersonName);
        txtPersonName.setLabel("Nom");
        
         
//        panelDiv.add(user);
        formLayout.add(txtPersonLogin);
        txtPersonLogin.setLabel("Login");
        

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
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// TODO Auto-generated method stub
		listPerson = personService.getAll();
		
		gridPerson.setItems(listPerson);
		gridPerson.setVisible(true);
	}

}
