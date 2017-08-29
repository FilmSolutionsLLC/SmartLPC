package com.fps.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Table(name = "admin_dropdown")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "admin_dropdown")
public class AdminDropdown implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @Column(name = "dropdown_name")
    private String dropdownName;
   
    @ManyToOne
    private User admin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getDropdownName() {
		return dropdownName;
	}

	public void setDropdownName(String dropdownName) {
		this.dropdownName = dropdownName;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public AdminDropdown(Long id,String dropdownName, User admin) {
		super();
		this.id = id;
		this.dropdownName = dropdownName;
		this.admin = admin;
	}

	public AdminDropdown() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Admin_Dropdown [dropdownName=" + dropdownName + ", admin=" + admin + "]";
	}

    

}
