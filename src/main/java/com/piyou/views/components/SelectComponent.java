package com.piyou.views.components;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.piyou.backend.model.Displayable;
import com.piyou.backend.services.ServiceProxy;
import com.piyou.backend.util.TranslationUtils;
import com.piyou.views.descriptors.InvalidFieldDescriptorException;
import com.piyou.views.descriptors.MainEntity;
import com.piyou.views.model.Application;
import com.piyou.views.model.FieldDetail;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.shared.Registration;

public class SelectComponent<V extends Displayable> extends AbstractSuperDisplayableComponent<V> {

	ComboBox<V> select = new ComboBox<>();

	Class<? extends Displayable> fieldMainEntity;

	public SelectComponent(FieldDetail field, Class<? extends Application> viewClazz)
			throws InvalidFieldDescriptorException {
		super();
		Class<? extends Application> descriptor = field.getEntityDescriptor();

		if (descriptor.getAnnotation(MainEntity.class) == null) {
			throw new InvalidFieldDescriptorException("La propriété entity est obligatoire pour un select");
		}

		fieldMainEntity = descriptor.getAnnotation(MainEntity.class).value();
		select.setItemLabelGenerator(i -> i.getLabel());

		try {
			Application appDescriptor = viewClazz.getConstructor().newInstance();
			Optional<FieldDetail> matchingField = appDescriptor.getAllFields().stream().filter(f -> f.equals(field))
					.findFirst();
			if (matchingField.isPresent()) {
				select.setLabel(TranslationUtils.translate(matchingField.get().getTranslationKey()));
			}

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		select.setLabel(label);
		select.addBlurListener(b -> {

		});

	}

	@Override
	public ComboBox<V> getComponent() {
		return select;
	}

	@Override
	public void initialize(ServiceProxy serviceProxy) {
		super.initialize(serviceProxy);
		select.setItems(serviceProxy.getInstance(fieldMainEntity).getAll());

		select.addCustomValueSetListener(l -> {
			System.out.println("gg");
		});

//			SelectComponent.this.fireEvent<2>(new ComponentEvent(select.getParent().get(),
//					Integer.parseInt(Long.toString(ComponentEvent.WINDOW_FOCUS_EVENT_MASK))));
//		});
	}

	@Override
	public void onClose() {
		super.onClose();
		select.blur();
//		select.blur();
//		select.setValue(select.getValue());
//		select.removeAll();
//		select.setReadOnly(true);
//		select.getItemRenderer().updateComponent(select, null);
//		select.getParent().
//		select.focus();
//		select.blur();
	}

	@Override
	public V getValue() {
		return select.getValue();
	}

	@Override
	public void setValue(V value) {
		select.setValue(value);
	}

	@Override
	public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<V>> listener) {
		// TODO Auto-generated method stub
		return select.addValueChangeListener(listener);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
//		isReadOnly = readOnly;
		this.select.setReadOnly(readOnly);

	}

	@Override
	public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
//		isRequired = true;
		select.setRequiredIndicatorVisible(requiredIndicatorVisible);
	}

	@Override
	public FieldDetail getFieldDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFieldDetail(FieldDetail field) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequiredIndicatorVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}
