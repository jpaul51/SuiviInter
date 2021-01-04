package com.piyou.views.main;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.piyou.Config;
import com.piyou.UserContextFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.router.RouteConfiguration;

public class DetailViewFactory {

	
	@Inject
	UserContextFactory userContextFactory;
	
	ArrayList<Column> columns = new ArrayList<Grid.Column>();
	SplitView dv ;
	Class clazz ;	
	


	public DetailViewFactory(Class pclazz) {

		
		clazz = pclazz;
		
		
		
		
//		context.set
//		BeanAttributes<Class> b;
//		Bean<Class> bean;
//		beanManager.createBean(null, a.getClass(), null);
//		bean .
//		beanManager.getBeans(clazz.getCanonicalName()).add(a);
		
		RouteConfiguration configuration =
	            RouteConfiguration.forSessionScope();

//	        configuration.setRoute("bim", DetailView.class,
//	                MainView.class);

	 
//	        
//	        this.getUI().ifPresent(ui -> ui.navigate("view", queryParameters));
	        
//		clazz = this.getClass().getTypeParameters()[0].getClass();
		
//        this.genericType = ((Class) ((this<T>) getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[0]);
	
//		Field[] fields = p.getClass().getDeclaredFields();
//		ArrayList<Field> fieldsArray = new ArrayList<Field>(Arrays.asList(fields));
// p.getClass().neC
//		ParameterizedType aType = (ParameterizedType) p;
//		aType.getClass().getMethods();
//		
//		ArrayList<Method> methods = new ArrayList<>();
//		
//		for(Method m : methods) {
//			System.out.println(m.getName());
//		}
		
//		  PropertyInfo propInfo = ((Object)p).GetType().GetProperty("Items"); //this returns null
//	        object itemValue = propInfo.GetValue(obj, null);

//		for(Field f: fieldsArray) {
//			
////			f.getType()
//			Column c =  grid.addColumn(i -> i.getClass().);
//		
//			
//		}

	}
	
	public void show() {
//		Map<String, String> mapParams = new HashMap<>();
//        mapParams.put("clazz", clazz.getCanonicalName());
//        QueryParameters params =  QueryParameters.simple(mapParams);
		
//		AnnotationConfigApplicationContext context = 
//				new AnnotationConfigApplicationContext(Config.class);
//		userContextFactory = new UserContextFactory();
		UserContextFactory.getCurrentUserContext().setCurrentClass(clazz);
        UI.getCurrent().navigate("bim");
	}

}
