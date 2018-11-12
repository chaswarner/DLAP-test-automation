package com.prft.cif.test.metadata;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "filePattern",
        "dataset"
})
public class CIFFilePattern implements Serializable {

    /**
     * The Filepattern Schema
     * <p>
     *
     *
     */
    @JsonProperty("filePattern")
    private String filePattern = "";
    @JsonProperty("dataset")
    private List<String> dataset = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * The Filepattern Schema
     * <p>
     *
     *
     */
    @JsonProperty("filePattern")
    public String getFilePattern() {
        return filePattern;
    }

    /**
     * The Filepattern Schema
     * <p>
     *
     *
     */
    @JsonProperty("filePattern")
    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    @JsonProperty("dataset")
    public List<String> getDataset() {
        return dataset;
    }

    @JsonProperty("dataset")
    public void setDataset(List<String> dataset) {
        this.dataset = dataset;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}