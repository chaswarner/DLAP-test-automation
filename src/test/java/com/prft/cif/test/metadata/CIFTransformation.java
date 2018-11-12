package com.prft.cif.test.metadata;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.Serializable;
import java.util.List;


@JsonFilter("transformFilter")
public class CIFTransformation implements Serializable {

    private Integer id;
    private String name = "";
    private List<String> source;
    private String description;
    private String expression;

    private String criteria;
    private List<Column> columns;

    public CIFTransformation(Integer id, String name, List<String> source, String description, String expression, String criteria, List<Column> columns) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.description = description;
        this.expression = expression;
        this.criteria = criteria;
        this.columns = columns;
    }

    public CIFTransformation(){
        this(0,"",null,"","","",null);
    }

    @Override
    public String toString() {
        return "Transformation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", source=" + source +
                ", description='" + description + '\'' +
                ", expression='" + expression + '\'' +
                ", criteria='" + criteria + '\'' +
                ", columns=" + columns +
                '}';
    }

    public String toJSONSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("columns");
        FilterProvider filters = new SimpleFilterProvider().addFilter("transformFilter", theFilter);


        String json = "{}";

            json = mapper.writer(filters).writeValueAsString(this);

        return json;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Column {

        private Integer id;
        private List<String> source;
        private String target;
        private String expression;

        public Column(Integer id, List<String> source, String target, String expression) {
            this.id = id;
            this.source = source;
            this.target = target;
            this.expression = expression;
        }

        public Column(){
            this(0,null,"","");
        }

        public List<String> getSource() {
            return source;
        }

        public void setSource(List<String> source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Column{" +
                    "id=" + id +
                    ", source=" + source +
                    ", target='" + target + '\'' +
                    ", expression='" + expression + '\'' +
                    '}';
        }

        public String toJSONSchema() throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            String json = "{}";

                json = mapper.writeValueAsString(this);

            return json;
        }
    }

}
