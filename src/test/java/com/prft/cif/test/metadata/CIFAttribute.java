package com.prft.cif.test.metadata;

import java.io.Serializable;

public class CIFAttribute implements Serializable {

    private static final long serialVersionUID = 9174922383639510238L;
    public String key;
    private String value;

    public CIFAttribute() {}

    public CIFAttribute(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        com.prft.cif.test.metadata.CIFAttribute attribute = (com.prft.cif.test.metadata.CIFAttribute) o;

        return key != null ? key.equals(attribute.key) : attribute.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }


}
