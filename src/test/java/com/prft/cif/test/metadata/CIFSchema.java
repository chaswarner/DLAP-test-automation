package com.prft.cif.test.metadata;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.Serializable;

public class CIFSchema implements Serializable, Comparable<com.prft.cif.test.metadata.CIFSchema> {


    private static final long serialVersionUID = -7268772673090041378L;
    private Integer id;
    private String name;
    private String description;
    private String type;
    private Boolean required;
    private Integer size;
    private Integer position;
    private String format;
    private String sensitive;
    private String rules;


    public CIFSchema() {
        //this(1,"","","",false,0,0,"","","");
    }

    public CIFSchema(Integer id, String name, String description, String type, Boolean required, Integer size, Integer position, String format, String sensitive, String rules) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.size = size;
        this.position = position;
        this.format = format;
        this.sensitive = sensitive;
        this.rules = rules;
    }

    public CIFSchema(String name, String description, String type, Boolean required, Integer size, Integer position, String format, String sensitive, String rules) {
        this(null, name, description, type, required, size, position, format, sensitive, rules);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSensitive() {
        return sensitive;
    }

    public void setSensitive(String sensitive) {
        this.sensitive = sensitive;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        com.prft.cif.test.metadata.CIFSchema schema = (com.prft.cif.test.metadata.CIFSchema) o;

        if (name != null ? !name.equals(schema.name) : schema.name != null) return false;
        if (description != null ? !description.equals(schema.description) : schema.description != null) return false;
        if (type != null ? !type.equals(schema.type) : schema.type != null) return false;
        if (required != null ? !required.equals(schema.required) : schema.required != null) return false;
        if (size != null ? !size.equals(schema.size) : schema.size != null) return false;
        if (position != null ? !position.equals(schema.position) : schema.position != null) return false;
        if (sensitive != null ? !sensitive.equals(schema.sensitive) : schema.sensitive !=null) return false;
        if (rules != null ? !rules.equals(schema.rules) : schema.rules !=null) return false;
        return format != null ? format.equals(schema.format) : schema.format == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (required != null ? required.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (sensitive != null ? sensitive.hashCode() : 0);
        result = 31 * result + (rules != null ? rules.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", required=" + required +
                ", size=" + size +
                ", position=" + position +
                ", format='" + format + '\'' +
                ", sensitive='" + sensitive + '\'' +
                ", rules='" + rules + '\'' +
                '}';
    }

    @JsonFilter("schemaFilter")
    public String toJSONSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("name");
        FilterProvider filters = new SimpleFilterProvider().addFilter("schemaFilter", theFilter);
        String json = "{}";

        json = mapper.writer(filters).writeValueAsString(this);

        return json;

    }

    @Override
    public int compareTo(com.prft.cif.test.metadata.CIFSchema compareTo) {
        if (this.id < compareTo.getId()) {
            return -1;
        } else {
            return 1;
        }
    }
}
