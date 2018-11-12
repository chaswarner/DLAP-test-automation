package com.prft.cif.test.metadata;

import java.io.Serializable;

public class CIFName implements Serializable {

    private static final long serialVersionUID = 7926340208941135227L;
    protected String name;
    protected String namespace;
    protected String version;

    public CIFName(String namespace, String name, String version) {
        this.namespace = namespace;
        this.name = name;
        this.version = version;
    }

    public CIFName() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        com.prft.cif.test.metadata.CIFName name1 = (com.prft.cif.test.metadata.CIFName) o;

        return ((this.name == null ? name1.getName() == null : this.name.equals(name1.getName()))
                && (this.namespace == null ? name1.getNamespace() == null : this.namespace.equals(name1.getNamespace()))
                && (this.version == null ? name1.getVersion() == null : this.version.equals(name1.getVersion())));

//        if(name !=null )
//        if (name != null ? !name.equals(name1.name) : name1.name != null) return false;
//        if (namespace != null ? !namespace.equals(name1.namespace) : name1.namespace != null) return false;
//        return version != null ? version.equals(name1.version) : name1.version == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Name{" +
                "name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", version='" + version + '\'' +
                '}';
    }


}
