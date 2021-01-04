package com.piyou.views.components;

import com.piyou.backend.model.Displayable;
import com.vaadin.flow.component.Component;

public abstract class AbstractSuperDisplayableComponent<U extends Displayable> extends AbstractSuperComponent 
implements SuperComponentInterface<U, Component>{

}
