package com.prft.cif.test.metadata;


import com.prft.cif.test.metadata.CIFAttribute;
import com.prft.cif.test.metadata.CIFAudit;
import com.prft.cif.test.metadata.CIFName;
import com.prft.cif.test.metadata.CIFSchedule;
import com.prft.cif.test.metadata.CIFSchema;
import com.prft.cif.test.metadata.CIFTransformation;

import java.util.List;

public class CIFDataset extends CIFName {


    private static final long serialVersionUID = 8075270990766233031L;
    private String description="";

    private String format="";
    private String delimiter="";
    private String encoding="";
    private String compression="";
    private String fileNameFormat="";
    private Boolean header=false;
    private Integer headerLineCount=0;
    private String headerFormat="";
    private Boolean footer=false;
    private Integer footerLineCount=0;

    private String footerFormat="";
    private Integer interval=0;
    private String unit="";
    private String location="";

    private Boolean lock;

    private String startTime ="";
    private String multiRecInd ="";
    private String multiRecPos ="";
    private List<CIFName> sources;

    private String contactName="";
    private String contactEmail="";
    private List<String> roles;
    private List<String> tags;
    private List<String> partitionKeys;
    private List<String> primaryKeys;
    private String ingestType="";
    private List<CIFAttribute> attributes;
    private Boolean sensitiveData = false;
    private List<CIFSchema> schemas;

    private List<CIFTransformation> trsfms;

    private String zone="";

    private String businessDomain = "";
    private String sourceSystemCode = "";

    private String copyBook="";
    private String deleteFlag="";

    private CIFSchedule schedule;

    private Boolean auditFlag = false;
    private List<CIFAudit> audit;

    public CIFSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(CIFSchedule schedule) {
        this.schedule = schedule;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCopyBook() {
        return copyBook;
    }

    public void setCopyBook(String copyBook) {
        this.copyBook = copyBook;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getIngestType() {
        return ingestType;
    }

    public void setIngestType(String ingestType) {
        this.ingestType = ingestType;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(boolean sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public String getFileNameFormat() {
        return fileNameFormat;
    }

    public void setFileNameFormat(String fileNameFormat) {
        this.fileNameFormat = fileNameFormat;
    }

    public Boolean getHeader() {
        return header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }

    public Integer getHeaderLineCount() {
        return headerLineCount;
    }

    public void setHeaderLineCount(Integer headerLineCount) {
        this.headerLineCount = headerLineCount;
    }

    public String getHeaderFormat() {
        return headerFormat;
    }

    public void setHeaderFormat(String headerFormat) {
        this.headerFormat = headerFormat;
    }

    public Boolean getFooter() {
        return footer;
    }

    public void setFooter(Boolean footer) {
        this.footer = footer;
    }

    public Integer getFooterLineCount() {
        return footerLineCount;
    }

    public void setFooterLineCount(Integer footerLineCount) {
        this.footerLineCount = footerLineCount;
    }

    public String getFooterFormat() {
        return footerFormat;
    }

    public void setFooterFormat(String footerFormat) {
        this.footerFormat = footerFormat;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMultiRecInd() {
        return multiRecInd;
    }

    public void setMultiRecInd(String multiRecInd) {
        this.multiRecInd = multiRecInd;
    }

    public String getMultiRecPos() {
        return multiRecPos;
    }

    public void setMultiRecPos(String multiRecPos) {
        this.multiRecPos = multiRecPos;
    }

    public List<CIFName> getSources() {
        return sources;
    }

    public void setSources(List<CIFName> sources) {
        this.sources = sources;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPartitionKeys() {
        return partitionKeys;
    }

    public void setPartitionKeys(List<String> partitionKeys) {
        this.partitionKeys = partitionKeys;
    }

    public List<CIFAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<CIFAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<CIFSchema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<CIFSchema> schemas) {
        this.schemas = schemas;
    }

    public List<CIFTransformation> getTrsfms() {
        return trsfms;
    }

    public void setTrsfms(List<CIFTransformation> trsfms) { this.trsfms = trsfms; }

    public String getBusinessDomain() {
        return businessDomain;
    }

    public void setBusinessDomain(String businessDomain) {
        this.businessDomain = businessDomain;
    }

    public String getSourceSystemCode() {
        return sourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) { this.sourceSystemCode = sourceSystemCode; }

    public Boolean getAuditFlag() { return auditFlag; }

    public void setAuditFlag (Boolean auditFlag) {
        this.auditFlag = auditFlag;
    }

    public List<CIFAudit> getAudit() {
        return audit;
    }

    public void setAudit(List<CIFAudit> audit) { this.audit = audit; }

    @Override
    public String toString() {
        return "CIFDataset{" +
                "description='" + description + '\'' +
                ", format='" + format + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", encoding='" + encoding + '\'' +
                ", compression='" + compression + '\'' +
                ", fileNameFormat='" + fileNameFormat + '\'' +
                ", header=" + header +
                ", headerLineCount=" + headerLineCount +
                ", headerFormat='" + headerFormat + '\'' +
                ", footer=" + footer +
                ", footerLineCount=" + footerLineCount +
                ", footerFormat='" + footerFormat + '\'' +
                ", location='" + location + '\'' +
                ", lock=" + lock +
                ", multiRecInd='" + multiRecInd + '\'' +
                ", multiRecPos='" + multiRecPos + '\'' +
                ", sources=" + sources +
                ", contactName='" + contactName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", roles=" + roles +
                ", tags=" + tags +
                ", partitionKeys=" + partitionKeys +
                ", primaryKeys=" + primaryKeys +
                ", ingestType='" + ingestType + '\'' +
                ", attributes=" + attributes +
                ", sensitiveData=" + sensitiveData +
                ", schemas=" + schemas +
                ", trsfms=" + trsfms +
                ", zone='" + zone + '\'' +
                ", businessDomain='" + businessDomain + '\'' +
                ", sourceSystemCode='" + sourceSystemCode + '\'' +
                ", copyBook='" + copyBook + '\'' +
                ", deleteFlag='" + deleteFlag + '\'' +
                ", schedule=" + schedule +
                ", name='" + name + '\'' +
                ", namespace='" + namespace + '\'' +
                ", version='" + version + '\'' +
                ", auditFlag=" + auditFlag +
                ", audit=" + audit +
                '}';
    }


    /*
    public Map<String, Object> getMap(){
        
        Map<String, Object> values = new HashMap<String, Object>();

        values.put("name", getName());
        values.put("description",getDescription());
        values.put("namespace",getNamespace());
        values.put("version",getVersion());
        values.put("format",getFormat());
        values.put("delimiter",getDelimiter());
        values.put("encoding",getEncoding());
        values.put("compression",getCompression());
        values.put("fileNameFormat",getFileNameFormat());
        values.put("header",getHeader());
        values.put("headerLineCount",getHeaderLineCount());
        values.put("headerFormat",getHeaderFormat());
        values.put("footer",getFooter());
        values.put("footerLineCount",getFooterLineCount());
        values.put("footerFormat",getFooterFormat());
        values.put("interval",getInterval());
        values.put("unit",getUnit());
        values.put("location",getLocation());
        values.put("lock",getLock());
        values.put("zone",getZone());
        values.put("receiveTime",getStartTime());
        values.put("multiRecInd",getMultiRecInd());
        values.put("multiRecPos",getMultiRecPos());
        values.put("contactName",getContactName());
        values.put("contactEmail",getContactEmail());
        values.put("ingestType",getIngestType());
        values.put("businessDomain",getBusinessDomain());
        values.put("sourceSystemCode",getSourceSystemCode());


        int k = 0;
        for (String role : getRoles()) {
            values.put("roles." + role, k++);
        }

        int z = 0;
        for (String tag : getTags()) {
            values.put("tags." + tag, z++);
        }

        int i = 0;
        for (String partitionKey : getPartitionKeys()) {
            values.put("partitionKeys." + partitionKey, i++);
        }

        int j = 0;
        for (String primaryKey : getPrimaryKeys()) {
            values.put("primaryKeys." + primaryKey, j++);
        }

        for (Attribute objSchema : getAttributes()) {
            values.put("attributes." + objSchema.getKey(), objSchema.getValue());
        }

        for (Name objSchema : getSources()) {
            values.put("sources." + objSchema.getNamespace() + "." + objSchema.getName() + "." + objSchema.getVersion(), null);
        }

        return values;
    }
    */

//    private Map<String, Object> getSchema() throws JsonProcessingException {
//        Map<String, Object> values = new LinkedHashMap<String, Object>();
//        int i = 0;
//
//        for (CIFSchema objSchema : getSchemas()) {
//            objSchema.setId(i++);
//            values.put(objSchema.getName(), objSchema.toJSONSchema());
//        }
//        return values;
//    }



}