
package com.prft.cif.test.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CIFAuditType implements Serializable
{
    private String type = "";
    private Integer row = 0;
    private Integer startPos = 0;
    private Integer size = 0;
    private String field = "";
    private String rawCriteria = "";
    private String curateCriteria = "";
    private Integer rowLine = 0;
    private String hash = "";
    private final static long serialVersionUID = -5584571600306230241L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CIFAuditType() {
    }

    /**
     * 
     * @param field
     * @param startPos
     * @param type
     * @param row
     * @param size
     * @param rawCriteria
     * @param curateCriteria
     * @param rowLine
     * @param hash
     */
    public CIFAuditType(String type, Integer row, Integer startPos, Integer size, String field, String rawCriteria, String curateCriteria, Integer rowLine, String hash) {
        super();
        this.type = type;
        this.row = row;
        this.startPos = startPos;
        this.size = size;
        this.field = field;
        this.rawCriteria = rawCriteria;
        this.curateCriteria = curateCriteria;
        this.rowLine = rowLine;
        this.hash = hash;
    }


    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getRow() { return row; }
    public void setRow(Integer row) { this.row = row; }

    public Integer getStartPos() { return startPos; }
    public void setStartPos(Integer startPos) { this.startPos = startPos; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getRawCriteria() { return rawCriteria; }
    public void setRawCriteria(String rawCriteria) { this.rawCriteria = rawCriteria; }

    public String getCurateCriteria() { return curateCriteria; }
    public void setCurateCriteria(String curateCriteria) { this.curateCriteria = curateCriteria; }

    public Integer getRowLine() { return rowLine; }
    public void setRowLine(Integer rowLine) { this.rowLine = rowLine; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }


//    @Override
//    public String toString() {
//        return new ToStringBuilder(this)
//                .append("type", type)
//                .append("row", row)
//                .append("startPos", startPos)
//                .append("size", size)
//                .append("field", field)
//                .append("rawCriteria",rawCriteria)
//                .append("curateCriteria",curateCriteria)
//                .append("rowLine",rowLine)
//                .append("hash",hash)
//                .toString();
//    }


    @Override
    public String toString() {
        return "CIFAuditType{" +
                "type='" + type + '\'' +
                ", row=" + row +
                ", startPos=" + startPos +
                ", size=" + size +
                ", field='" + field + '\'' +
                ", rawCriteria='" + rawCriteria + '\'' +
                ", curateCriteria='" + curateCriteria + '\'' +
                ", rowLine=" + rowLine +
                ", hash='" + hash + '\'' +
                '}';
    }

    @JsonIgnore
    public String toJSONSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json  = mapper.writeValueAsString(this);
        return json;

    }

}
