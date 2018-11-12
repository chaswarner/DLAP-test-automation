
package com.prft.cif.test.metadata;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.Serializable;
import java.util.List;


/**
 * The Root Schema
 * <p>
 * 
 * 
 */
@JsonFilter("auditFilter")
public class CIFAudit implements Serializable
{

    private String auditLocation = "";
    private String auditSuffix = "";
    private String auditFileIdentifier = "";
    private String auditFileIdentifierField = "";
    private String auditFileNamePattern = "";
    private Integer auditFileCount = 0;
    private List<CIFAuditType> auditTypes = null;

    private final static long serialVersionUID = 3586135528604603415L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CIFAudit() { }

    /**
     * 
     * @param auditTypes
     * @param auditFileIdentifier
     * @param auditFileCount
     * @param auditFileNamePattern
     * @param auditLocation
     * @param auditFileIdentifierField
     * @param auditSuffix
     */
    public CIFAudit(String auditLocation, String auditSuffix, String auditFileIdentifier, String auditFileIdentifierField, String auditFileNamePattern, Integer auditFileCount, List<CIFAuditType> auditTypes) {
        super();
        this.auditLocation = auditLocation;
        this.auditSuffix = auditSuffix;
        this.auditFileIdentifier = auditFileIdentifier;
        this.auditFileIdentifierField = auditFileIdentifierField;
        this.auditFileNamePattern = auditFileNamePattern;
        this.auditFileCount = auditFileCount;
        this.auditTypes = auditTypes;
    }


    public String getAuditLocation() {
        return auditLocation;
    }
    public void setAuditLocation(String auditLocation) {
        this.auditLocation = auditLocation;
    }


    public String getAuditSuffix() {
        return auditSuffix;
    }
    public void setAuditSuffix(String auditSuffix) {
        this.auditSuffix = auditSuffix;
    }

    public String getAuditFileIdentifier() {
        return auditFileIdentifier;
    }
    public void setAuditFileIdentifier(String auditFileIdentifier) {
        this.auditFileIdentifier = auditFileIdentifier;
    }

    public String getAuditFileIdentifierField() {
        return auditFileIdentifierField;
    }
    public void setAuditFileIdentifierField(String auditFileIdentifierField) { this.auditFileIdentifierField = auditFileIdentifierField; }

    public String getAuditFileNamePattern() {
        return auditFileNamePattern;
    }
    public void setAuditFileNamePattern(String auditFileNamePattern) { this.auditFileNamePattern = auditFileNamePattern; }

    public Integer getAuditFileCount() {
        return auditFileCount;
    }
    public void setAuditFileCount(Integer auditFileCount) {
        this.auditFileCount = auditFileCount;
    }

    public List<CIFAuditType> getAuditTypes() {
        return auditTypes;
    }
    public void setAuditTypes(List<CIFAuditType> auditTypes) {
        this.auditTypes = auditTypes;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this)
//                .append("auditLocation", auditLocation)
//                .append("auditSuffix", auditSuffix)
//                .append("auditFileIdentifier", auditFileIdentifier)
//                .append("auditFileIdentifierField", auditFileIdentifierField)
//                .append("auditFileNamePattern", auditFileNamePattern)
//                .append("auditFileCount", auditFileCount)
//                .append("auditTypes", auditTypes)
//                .toString();
//    }


    @Override
    public String toString() {
        return "CIFAudit{" +
                "auditLocation='" + auditLocation + '\'' +
                ", auditSuffix='" + auditSuffix + '\'' +
                ", auditFileIdentifier='" + auditFileIdentifier + '\'' +
                ", auditFileIdentifierField='" + auditFileIdentifierField + '\'' +
                ", auditFileNamePattern='" + auditFileNamePattern + '\'' +
                ", auditFileCount=" + auditFileCount +
                ", auditTypes=" + auditTypes +
                '}';
    }

    public String toJSONSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("auditTypes");
        FilterProvider filters = new SimpleFilterProvider().addFilter("auditFilter", theFilter);

        String json = "{}";

        json = mapper.writer(filters).writeValueAsString(this);

        return json;

    }


}
