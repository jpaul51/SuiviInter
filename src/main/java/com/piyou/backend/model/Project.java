package com.piyou.backend.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Project implements Serializable, Displayable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4807833043624930166L;

	@Id
	@GeneratedValue
	@Name("id")
	private Long id;
	
	@Name("name")
	private String name;
	@Name("description")
	private String description;
	
	@ManyToOne
	private Person projectManager;
	
	public Project() {
		
	}
	
	public Project(String projectName) {
		this.name = projectName;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

	public Person getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(Person projectManager) {
		this.projectManager = projectManager;
	}

	

	@Override
	public String getLabel() {
		return this.getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}




	
	
	
	
}
