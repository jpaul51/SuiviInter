package com.piyou.views.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import org.vaadin.tinymce.TinyMce;

import com.piyou.backend.model.Person;
import com.piyou.backend.services.PersonService;
import com.piyou.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "hello", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Hello World")
@CssImport("./styles/views/helloworld/hello-world-view.css")
public class HelloWorldView extends Div {

    private Grid<Person> grid;

    private TextField firstName = new TextField();
    private TextField lastName = new TextField();
    private TextField email = new TextField();
    private Div details = new Div();
    private TextArea normalTextArea = new TextArea();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Person> binder;

    public HelloWorldView(@Autowired PersonService personService) {
        setId("hello-world-view");
        // Configure Grid
        grid = new Grid<>(Person.class);
        grid.setColumns("firstName", "lastName", "email");
//        grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> populateForm(event.getValue()));

        // Configure Form
        binder = new Binder<>(Person.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        // the grid valueChangeEvent will clear the form too
        cancel.addClickListener(e -> grid.asSingleSelect().clear());

        save.addClickListener(e -> {
            Notification.show("Not implemented");
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        addFormItem(editorDiv, formLayout, firstName, "First name");
        addFormItem(editorDiv, formLayout, lastName, "Last name");
        addFormItem(editorDiv, formLayout, email, "Email");
        
       
//		MainPage page = new MainPage();
//		page.setContent("hophop2");

        TinyMce t = new TinyMce();
//        editorDiv.add(tt);
//        test.addC
        
        formLayout.addFormItem(t, "Details");
        editorDiv.add(formLayout);
        t.getElement().getClassList().add("full-width");
        
        addFormItem(editorDiv, formLayout, normalTextArea, "normalTextArea");
        
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void addFormItem(Div wrapper, FormLayout formLayout, AbstractField field, String fieldName) {
        formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
    }

    private void populateForm(Person value) {
        // Value can be null as well, that clears the form
        binder.readBean(value);
    }
}