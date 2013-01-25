package org.springframework.data.simpledb.core.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.simpledb.annotation.Attributes;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformation;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformationSupport;

import java.util.Map;

public class SimpleDbSampleEntity {

    @Id
    private String itemName;


    @Attributes
    private Map<String, String> atts;


    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemName() {
		return itemName;
	}


    public void setAtts(Map<String, String> atts) {
        this.atts = atts;
    }
    public Map<String, String> getAtts() {
		return atts;
	}


    public static SimpleDbEntityInformation<SimpleDbSampleEntity, String> entityInformation(){
        return (SimpleDbEntityInformation<SimpleDbSampleEntity, String>) SimpleDbEntityInformationSupport.getMetadata(SimpleDbSampleEntity.class);
    }
}