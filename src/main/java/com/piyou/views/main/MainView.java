package com.piyou.views.main;

import java.util.Arrays;
import java.util.Optional;

import com.piyou.views.about.AboutView;
import com.piyou.views.descriptors.InterventionDescriptor;
import com.piyou.views.descriptors.PersonDescriptor;
import com.piyou.views.descriptors.ProjectDescriptor;
import com.piyou.views.helloworld.HelloWorldView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@PWA(name = "SuiviInter", shortName = "SuiviInter",  enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/views/main/main-view.css")
public class MainView extends AppLayout implements RouterLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 11111111L;
	private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        layout.add(new Image("images/user.svg", "Avatar"));
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "SuiviInter logo"));
        logoLayout.add(new H1("SuiviInter"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
    	
    	Div menuDiv = new Div();
    	RouterLink rt = new RouterLink();
    	rt.setText("Projet");
    	menuDiv.add(rt);
    	menuDiv.add(new RouterLink());

    	
    	Div menuPersonDiv = new Div();
    	RouterLink rtPerson = new RouterLink();
    	rtPerson.setText("Users");
    	menuPersonDiv.add(rtPerson);
    	
    	Div menuInterDiv = new Div();
    	RouterLink rtInter = new RouterLink();
    	rtInter.setText("Interventions");
    	menuInterDiv.add(rtInter);
    	
    	menuInterDiv.addClickListener(c -> {
    		DetailViewFactory dvf = new DetailViewFactory(InterventionDescriptor.class);
    		dvf.show();
    	});
    	
    	menuPersonDiv.addClickListener(e -> {
    		DetailViewFactory dvf = new DetailViewFactory(PersonDescriptor.class);
    		dvf.show();
    	});
    	
    	
    	menuDiv.add(new RouterLink());
    	menuDiv.addClickListener(e -> {
    		DetailViewFactory dvf = new DetailViewFactory(ProjectDescriptor.class);
    		dvf.show();
    	});
    	
    	tabs.add(createTab(menuDiv), createTab(menuPersonDiv), createTab(menuInterDiv));


        return tabs;
    }

    private Component[] createMenuItems() {
    	
    	
        RouterLink[] links = new RouterLink[] {
            new RouterLink("Hello World", HelloWorldView.class),
            new RouterLink("About", AboutView.class),
            new RouterLink("Interventions", InterventionsDetailView.class),
//            new RouterLink("Utilisateurs", PersonDetailView.class), 
//            new RouterLink("bim", DetailView.class, ProjectDescriptor.class.getCanonicalName()), 
//            new RouterLink("bim", DetailView.class, PersonDescriptor.class.getCanonicalName()), 
            
//            new RouterLink("Test", dvf.dv.getClass())
        };
        return Arrays.stream(links).map(MainView::createTab).toArray(Tab[]::new);
    }

    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.add(content);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        updateChrome();
    }

    private void updateChrome() {
        getTabWithCurrentRoute().ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabWithCurrentRoute() {
        String currentRoute = RouteConfiguration.forSessionScope()
                .getUrl(getContent().getClass());
        return menu.getChildren().filter(tab -> hasLink(tab, currentRoute))
                .findFirst().map(Tab.class::cast);
    }

    private boolean hasLink(Component tab, String currentRoute) {
        return tab.getChildren().filter(RouterLink.class::isInstance)
                .map(RouterLink.class::cast).map(RouterLink::getHref)
                .anyMatch(currentRoute::equals);
    }

    private String getCurrentPageTitle() {
//    	return "";
    	
    	PageTitle pageTitle = getContent().getClass().getAnnotation(PageTitle.class);
//    	String staticPageTitle = value(); 
        if(pageTitle != null) {
        	return pageTitle.value();
        }
        else {
        	return ((SplitView)getContent()).getPageTitle();
        	
        }
    	 
    }
}
