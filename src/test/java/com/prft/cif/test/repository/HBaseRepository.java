package com.prft.cif.test.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.prft.cif.test.metadata.*;
import com.prft.cif.test.util.CIFDatasetUtil;
import com.prft.cif.test.util.HbaseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 */
public class HBaseRepository {
    @Inject
    private static Logger logger;

    private  String hbaseNameSpace;

    public Configuration getConf() {
        return conf;
    }

    private static Configuration conf;


    @Inject
    public void configuration(@Named("hbase.zookeeper.quorum") String zkQuorum,
                              @Named("hbase.zookeeper.property.clientPort")String zkClientPort,
                              @Named("hbase.client.retries.number")String retries,
                              @Named("hbase.namespace")String hbaseNameSpace) {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", zkQuorum);
        conf.set("hbase.zookeeper.property.clientPort", zkClientPort);
        conf.set("hbase.client.retries.number",retries);
        this.hbaseNameSpace= CIFDatasetUtil.getEnvironmentDatabasePrefix()+hbaseNameSpace;
    }

    public void putFilePattern(CIFDataset dataset) throws IOException {

        Connection con=null;
        Table table=null;

            con = ConnectionFactory.createConnection(getConf());
            table = con.getTable(TableName.valueOf(hbaseNameSpace + ":filepattern"));

            String rowKey = dataset.getFileNameFormat().toLowerCase();
            long ts = System.currentTimeMillis();
            Get g = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(g);
            if (result.isEmpty()) {
                Put p = createRow(rowKey);
                createFilePatternData(dataset, p, ts);
                table.put(p);
            } else {
                Put put = createRow(rowKey);
                createFilePatternData(dataset, put, ts + 1);
                table.put(put);
            }
            if (table != null) {
                table.close();
            }
            if (con != null) {
                    con.close();
            }
    }

    public String getLatestDatasetRowkey(String stringRow) throws IOException{

        String tableName = hbaseNameSpace + ":dataset";
        logger.debug("Hbase Table:"+tableName);
        Connection con=null;
        Table table=null;
        Scan scan = new Scan();
        ResultScanner scanner = null;
        List<String> rowKeyList = new ArrayList<>();

        // Instantiating HBaseAdmin class.
        con = ConnectionFactory.createConnection(getConf());
        table = con.getTable(TableName.valueOf(tableName));

        PrefixFilter prefixFilter = new PrefixFilter(Bytes.toBytes(stringRow));
        scan.setFilter(prefixFilter);
        scanner = table.getScanner(scan);

        for(Result result : scanner){
            rowKeyList.add(Bytes.toString(result.getRow()));
        }

        Collections.sort(rowKeyList);
        if(rowKeyList.size()>0)
            return rowKeyList.get(rowKeyList.size()-1);
        else
            return "";
    }

    public List<CIFDataset> getFilePatternDatasetList(String fileName) throws IOException {
        logger.debug("$$$$$$$$$$$$$ Hbase Conf~~~~~~~~~~~>"+ this.getConf().get("hbase.zookeeper.quorum"));
        String file = fileName.substring(fileName.lastIndexOf("/")+1).toLowerCase();
        String tableName = hbaseNameSpace + ":filepattern";
        logger.debug("Hbase Table:"+tableName);
        Connection con=null;
        Table table=null;
        Scan scan;
        ResultScanner scanner = null;
        List<CIFDataset> valueList = null;

            // Instantiating HBaseAdmin class.
            con = ConnectionFactory.createConnection(getConf());
            logger.debug(" Hbase Connection Factory con established");
            table = con.getTable(TableName.valueOf(tableName));
            logger.debug("Hbase got table."+ table);
            scan = new Scan();
            scanner = table.getScanner(scan);
            logger.debug("Got Hbase Scanner:"+ scanner);
            valueList = new ArrayList<>();

            for (Result result : scanner) {
                String rowKey = Bytes.toString(result.getRow());
                logger.debug("Hbase getFilePattern FileName====>"+ rowKey);
                if (Pattern.matches(rowKey.trim(),file.trim())) {
                    logger.debug("Inside If: ====> TRUE");
                    for (Cell cell : result.listCells()) {
                        valueList.add(this.getDataset(Bytes.toString(CellUtil.cloneQualifier(cell))));
                        //valueList.add(Bytes.toString(CellUtil.cloneQualifier(cell)));
                        logger.debug("Inside If Cell value: ===>"+cell.toString());
                    }
                }
            }

            if (scanner != null) {
                    scanner.close();
            }
            if (table != null) {
                    table.close();
            }
            if (con != null) {
                    con.close();
            }

        return valueList;
    }

    private void createFilePatternData(CIFDataset dataset, Put p, long ts) {
        String columnFamily = "dataset";
        if(dataset.getZone().equals("raw"))
            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(CIFDatasetUtil.getRawHbaseRowKey(dataset)), ts, null);
        else if(dataset.getZone().equals("curate"))
            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(CIFDatasetUtil.getCurateHbaseRowKey(dataset)), ts, null);
        else
            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(CIFDatasetUtil.getPublishHbaseRowKey(dataset)), ts, null);
    }

    public List<String> getDatasetKeyList(String fileName) throws IOException {
        String file = fileName.substring(fileName.lastIndexOf("/")+1).toLowerCase();
        String tableName = hbaseNameSpace + ":filepattern";
        logger.debug("Hbase Table:"+tableName);
        Connection con=null;
        Table table=null;
        Scan scan;
        ResultScanner scanner = null;
        List<String> valueList = null;

            // Instantiating HBaseAdmin class.
            con = ConnectionFactory.createConnection(getConf());
            table = con.getTable(TableName.valueOf(tableName));
            scan = new Scan();
            scanner = table.getScanner(scan);
            valueList = new ArrayList<>();

            for (Result result : scanner) {
                String rowKey = Bytes.toString(result.getRow());
                logger.debug("Hbase getFilePattern FileName====>"+ rowKey);
                if (Pattern.matches(rowKey.trim(),file.trim())) {
                    logger.debug("Inside If: ====> TRUE");
                    for (Cell cell : result.listCells()) {
                        valueList.add(Bytes.toString(CellUtil.cloneQualifier(cell)));
                        logger.debug("Inside If Cell value: ===>"+cell.toString());
                    }
                }
            }

            if (scanner != null) {
                    scanner.close();
            }

            if (table != null) {
                    table.close();
            }
            if (con != null) {
                    con.close();
            }

        return valueList;
    }

    public void putDataset(CIFDataset dataset) throws IOException {
        Table table = null;
        Connection con=null;

            // Instantiating HBaseAdmin class.
             con = ConnectionFactory.createConnection(getConf());
            Admin admin = con.getAdmin();
            //HBaseAdmin admin = new HBaseAdmin(conf);
            table = con.getTable(TableName.valueOf(hbaseNameSpace + ":dataset"));
            //table = new HTable(conf, hbaseNameSpace + "dataset");

            String rowKey="";
            if(dataset.getZone().equals("raw"))
                rowKey = CIFDatasetUtil.getRawHbaseRowKey(dataset);
            else if(dataset.getZone().equals("curate"))
                rowKey = CIFDatasetUtil.getCurateHbaseRowKey(dataset);
            else
                rowKey = CIFDatasetUtil.getPublishHbaseRowKey(dataset);

            // Checking for Existing Key
            long ts = System.currentTimeMillis();
            Get g = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(g);
            if (result.isEmpty()) {
                Put p = createRow(rowKey);
                createData(dataset, p, ts);
                createSchema(dataset, p, ts);
                createTransformation(dataset, p, ts);
                createAudit(dataset, p, ts);
                table.put(p);
            } else {
                String fileNameFormat = Bytes.toString(result.getValue(Bytes.toBytes("data"),Bytes.toBytes("fileNameFormat")));
                logger.debug("Reingest filenameformat"+ fileNameFormat);
                logger.debug("Deleteinf Old Dataset from filePattern");
                this.deleteFilePatternColumn(fileNameFormat,"dataset",rowKey);
                logger.debug("Deleted Dataset from Filepattern");

                Delete delete = new Delete(Bytes.toBytes(rowKey));
                delete.addFamily(Bytes.toBytes("data"), ts).addFamily(Bytes.toBytes("schema"), ts).addFamily(Bytes.toBytes("trsfms"),ts).addFamily(Bytes.toBytes("audit"), ts);
                Put put = createRow(rowKey);
                createData(dataset, put, ts + 1);
                createSchema(dataset, put, ts + 1);
                createTransformation(dataset, put, ts + 1);
                createAudit(dataset, put, ts + 1);
                table.delete(delete);
                table.put(put);
            }

            if (table != null) {
                    table.close();
            }
            if (con != null) {
                    con.close();
            }
    }

    public void deleteFilePatternColumn(String rowKey, String columnFamily, String columnQualifier) throws IOException {
        String tableName = hbaseNameSpace + ":filepattern";
        Connection con = ConnectionFactory.createConnection(getConf());
        Table table = con.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumns(Bytes.toBytes(columnFamily),Bytes.toBytes(columnQualifier));
        //delete.addColumn(Bytes.toBytes(columnFamily),Bytes.toBytes(columnQualifier));
        table.delete(delete);
        table.close();
    }

    private Put createRow(String rowKey) {
        byte[] row = Bytes.toBytes(rowKey);
        Put p = new Put(row);
        return p;

    }

    private void createData(CIFDataset dataset, Put p, long ts) throws JsonProcessingException {
        String columnFamily = "data";
        List<CIFAttribute> listAttributes = dataset.getAttributes();
        List<CIFName> listSources = dataset.getSources();
        List<String> listRoles = dataset.getRoles();
        List<String> listTags = dataset.getTags();
        List<String> listPartitionKeys = dataset.getPartitionKeys();
        List<String> listPrimaryKeys = dataset.getPrimaryKeys();


            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("name"), ts, Bytes.toBytes(dataset.getName()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("description"), ts, Bytes.toBytes(dataset.getDescription()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("namespace"), ts, Bytes.toBytes(dataset.getNamespace()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("version"), ts, Bytes.toBytes(dataset.getVersion()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("format"), ts, Bytes.toBytes(dataset.getFormat()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("delimiter"), ts, Bytes.toBytes(dataset.getDelimiter()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("encoding"), ts, Bytes.toBytes(dataset.getEncoding()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("compression"), ts, Bytes.toBytes(dataset.getCompression()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("fileNameFormat"), ts, Bytes.toBytes(dataset.getFileNameFormat()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("header"), ts, Bytes.toBytes(dataset.getHeader()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("headerLineCount"), ts, Bytes.toBytes(dataset.getHeaderLineCount()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("headerFormat"), ts, Bytes.toBytes(dataset.getHeaderFormat()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("footer"), ts, Bytes.toBytes(dataset.getFooter()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("footerLineCount"), ts, Bytes.toBytes(dataset.getFooterLineCount()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("footerFormat"), ts, Bytes.toBytes(dataset.getFooterFormat()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("interval"), ts, Bytes.toBytes(dataset.getInterval()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("unit"), ts, Bytes.toBytes(dataset.getUnit()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("receiveTime"), ts, Bytes.toBytes(dataset.getStartTime()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("schedule"), ts, Bytes.toBytes(dataset.getSchedule().toJSONSchema()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("location"), ts, Bytes.toBytes(dataset.getLocation()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("lock"), ts, Bytes.toBytes(dataset.getLock()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("sensitiveData"), ts, Bytes.toBytes(dataset.isSensitiveData()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("zone"), ts, Bytes.toBytes(dataset.getZone()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("businessDomain"), ts, Bytes.toBytes(dataset.getBusinessDomain()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("sourceSystemCode"), ts, Bytes.toBytes(dataset.getSourceSystemCode()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("multiRecInd"), ts, Bytes.toBytes(dataset.getMultiRecInd()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("multiRecPos"), ts, Bytes.toBytes(dataset.getMultiRecPos()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("contactName"), ts, Bytes.toBytes(dataset.getContactName()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("contactEmail"), ts, Bytes.toBytes(dataset.getContactEmail()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("ingestType"), ts, Bytes.toBytes(dataset.getIngestType()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("copyBook"), ts, Bytes.toBytes(dataset.getCopyBook()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("deleteFlag"), ts, Bytes.toBytes(dataset.getDeleteFlag()));

            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("auditFlag"), ts, Bytes.toBytes(dataset.getAuditFlag()));


        int k = 0;
            for (String role : listRoles) {
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("roles." + role), ts, Bytes.toBytes(k++));
            }

            int z = 0;
            for (String tag : listTags) {
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("tags." + tag), ts, Bytes.toBytes(z++));
            }

            int i = 0;
            for (String partitionKey : listPartitionKeys) {
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("partitionKeys." + partitionKey), ts, Bytes.toBytes(i++));
            }

            int j = 0;
            for (String primaryKey : listPrimaryKeys) {
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("primaryKeys." + primaryKey), ts, Bytes.toBytes(j++));
            }

            for (CIFAttribute objSchema : listAttributes) {
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("attributes." + objSchema.getKey()), ts, Bytes.toBytes(objSchema.getValue()));
            }

            for (CIFName objSchema : listSources) {
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("sources." + objSchema.getNamespace() + "." + objSchema.getName() + "." + objSchema.getVersion()), ts, null);
            }
    }

    private void createSchema(CIFDataset dataset, Put p, long ts) throws JsonProcessingException {
        List<CIFSchema> listSchema = dataset.getSchemas();
        int i = 0;
        String columnFamily = "schema";
            for (CIFSchema objSchema : listSchema) {
                    objSchema.setId(i++);
                    p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(objSchema.getName()), ts, Bytes.toBytes(objSchema.toJSONSchema()));
            }
    }

    private void createTransformation(CIFDataset dataset, Put p, long ts) throws JsonProcessingException{
        String columnFamily = "trsfms";
        List<CIFTransformation> listTransform = dataset.getTrsfms();
        int i = 0;

            for (CIFTransformation objSchema : listTransform) {
                    objSchema.setId(i++);
                    String key = "trsfms" + String.format("%04d", objSchema.getId());
                    p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(key), ts, Bytes.toBytes(objSchema.toJSONSchema()));
                    int j = 0;
                    List<CIFTransformation.Column> columnList = objSchema.getColumns();
                    for (CIFTransformation.Column colSchema : columnList) {
                        colSchema.setId(j++);
                        String columnKey = key + "." + String.format("%04d", colSchema.getId());
                        p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnKey), ts, Bytes.toBytes(colSchema.toJSONSchema()));
                    }
            }


    }

    private void createAudit(CIFDataset dataset, Put p, long ts) throws JsonProcessingException{
        String columnFamily = "audit";
        List<CIFAudit> listAudit = dataset.getAudit();
        int i = 0;

        for (CIFAudit objSchema : listAudit) {
            String key = "audit" + String.format("%04d", i++);
            p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(key), ts, Bytes.toBytes(objSchema.toJSONSchema()));
            int j = 0;
            List<CIFAuditType> auditTypeList = objSchema.getAuditTypes();
            for (CIFAuditType auditType : auditTypeList) {
                String columnKey = key + "." + String.format("%04d", j++);
                p.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnKey), ts, Bytes.toBytes(auditType.toJSONSchema()));
            }
        }
    }

    public CIFDataset getDataset(String rowkey) throws IOException {
        Table table=null;
        CIFDataset dataset = null;

            Connection con = ConnectionFactory.createConnection(getConf());
            Admin admin = con.getAdmin();
            table = con.getTable(TableName.valueOf(hbaseNameSpace + ":dataset"));
            Get g = new Get(Bytes.toBytes(rowkey));
            Result result = table.get(g);

            if (result != null) {
                NavigableMap<byte[], byte[]> dataMap = result.getFamilyMap("data".getBytes());
                dataset = buildDataResult(dataMap);

                NavigableMap<byte[], byte[]> schemaMap = result.getFamilyMap("schema".getBytes());
                dataset.setSchemas(buildSchemaResult(schemaMap));

                NavigableMap<byte[], byte[]> transformMap = result.getFamilyMap("trsfms".getBytes());
                dataset.setTrsfms(buildTransfromResult(transformMap));

                NavigableMap<byte[], byte[]> auditMap = result.getFamilyMap("audit".getBytes());
                dataset.setAudit(buildAuditResult(auditMap));

            }
            if (table != null) {
                    table.close();
            }
        return dataset;
    }

    private CIFDataset buildDataResult(NavigableMap<byte[], byte[]> resultMap) throws IOException {
        if (resultMap == null) {
            return null;
        }
        CIFDataset dataset = new CIFDataset();

        String value = HbaseUtil.getHBaseString(resultMap, "name");
        if (value != null && value.length() > 0) {
            dataset.setName(value);
        } else {
            throw new RuntimeException("Dataset name is empty");
        }

        value = HbaseUtil.getHBaseString(resultMap, "namespace");
        if (value != null && value.length() > 0) {
            dataset.setNamespace(value);
        } else {
            throw new RuntimeException("Dataset namespace is empty");
        }

        dataset.setDescription(HbaseUtil.getHBaseString(resultMap, "description"));
        dataset.setVersion(HbaseUtil.getHBaseString(resultMap, "version"));
        dataset.setFormat(HbaseUtil.getHBaseString(resultMap, "format"));
        dataset.setDelimiter(HbaseUtil.getHBaseString(resultMap, "delimiter"));
        dataset.setEncoding(HbaseUtil.getHBaseString(resultMap, "encoding"));
        dataset.setCompression(HbaseUtil.getHBaseString(resultMap, "compression"));
        dataset.setFileNameFormat(HbaseUtil.getHBaseString(resultMap, "fileNameFormat"));
        dataset.setHeader(HbaseUtil.getHBaseBoolean(resultMap, "header"));
        dataset.setHeaderLineCount(HbaseUtil.getHBaseInteger(resultMap, "headerLineCount"));
        dataset.setHeaderFormat(HbaseUtil.getHBaseString(resultMap, "headerFormat"));
        dataset.setFooter(HbaseUtil.getHBaseBoolean(resultMap, "footer"));
        dataset.setFooterLineCount(HbaseUtil.getHBaseInteger(resultMap, "footerLineCount"));
        dataset.setFooterFormat(HbaseUtil.getHBaseString(resultMap, "footerFormat"));
        dataset.setInterval(HbaseUtil.getHBaseInteger(resultMap, "interval"));
        dataset.setUnit(HbaseUtil.getHBaseString(resultMap, "unit"));
        dataset.setStartTime(HbaseUtil.getHBaseString(resultMap, "receiveTime"));
        dataset.setLocation(HbaseUtil.getHBaseString(resultMap, "location"));
        dataset.setLock(HbaseUtil.getHBaseBoolean(resultMap, "lock"));
        dataset.setSensitiveData(HbaseUtil.getHBaseBoolean(resultMap, "sensitiveData"));
        dataset.setZone(HbaseUtil.getHBaseString(resultMap, "zone"));
        dataset.setBusinessDomain(HbaseUtil.getHBaseString(resultMap, "businessDomain"));
        dataset.setSourceSystemCode(HbaseUtil.getHBaseString(resultMap, "sourceSystemCode"));
        dataset.setMultiRecInd(HbaseUtil.getHBaseString(resultMap, "multiRecInd"));
        dataset.setMultiRecPos(HbaseUtil.getHBaseString(resultMap, "multiRecPos"));
        dataset.setContactName(HbaseUtil.getHBaseString(resultMap, "contactName"));
        dataset.setContactEmail(HbaseUtil.getHBaseString(resultMap, "contactEmail"));
        dataset.setIngestType(HbaseUtil.getHBaseString(resultMap, "ingestType"));
        dataset.setCopyBook(HbaseUtil.getHBaseString(resultMap, "copyBook"));
        dataset.setDeleteFlag(HbaseUtil.getHBaseString(resultMap, "deleteFlag"));
        dataset.setRoles(HbaseUtil.getSortedList(resultMap, "roles."));
        dataset.setTags(HbaseUtil.getSortedList(resultMap, "tags."));
        dataset.setPartitionKeys(HbaseUtil.getSortedList(resultMap, "partitionKeys."));
        dataset.setPrimaryKeys(HbaseUtil.getSortedList(resultMap, "primaryKeys."));
        dataset.setAuditFlag(HbaseUtil.getHBaseBoolean(resultMap, "auditFlag"));


        String scheduleString = HbaseUtil.getHBaseString(resultMap, "schedule");
        if(StringUtils.isNotBlank(scheduleString)){
            ObjectMapper mapper = new ObjectMapper();
            CIFSchedule schedule = mapper.readValue(scheduleString, CIFSchedule.class);
            dataset.setSchedule(schedule);
        }else{
            dataset.setSchedule(new CIFSchedule());
        }


        Map<String, String> map = HbaseUtil.getMap(resultMap, "attributes.");
        List<CIFAttribute> attr = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            attr.add(new CIFAttribute(entry.getKey(), entry.getValue()));
        }
        dataset.setAttributes(attr);

        /**
         *  Set Sources
         */

        Map<String, String> srcmap = HbaseUtil.getMap(resultMap, "sources.");
        List<CIFName> nameList = new ArrayList<>();
        for (Map.Entry<String, String> entry : srcmap.entrySet()) {
            String[] key = entry.getKey().split("\\.");
            nameList.add(new CIFName(key[0], key[1], key[2]));
        }
        dataset.setSources(nameList);
        return dataset;
    }

    private List<CIFSchema> buildSchemaResult(NavigableMap<byte[], byte[]> resultMap) throws IOException {
        if (resultMap == null) {
            return null;
        }
        CIFSchema schema;
        List<CIFSchema> schemaList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (byte[] key : resultMap.keySet()) {

            String keyString = new String(key);
            String jsonValueString = HbaseUtil.getHBaseString(resultMap, keyString);

                schema = mapper.readValue(jsonValueString, CIFSchema.class);
                schema.setName(keyString);
                schemaList.add(schema);

        }
        Collections.sort(schemaList);
        return schemaList;
    }

    private List<CIFTransformation> buildTransfromResult(NavigableMap<byte[], byte[]> resultMap) throws IOException {
        if (resultMap == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CIFTransformation transform;
        CIFTransformation.Column column;
        List<CIFTransformation> transformList = new ArrayList<>();
        List<CIFTransformation.Column> columnList;
        String transformKey;
        for (byte[] key : resultMap.keySet()) {
            transformKey = new String(key);
            if (transformKey.length() == "trsfms9999".length()) {

                    transform = mapper.readValue(HbaseUtil.getHBaseString(resultMap, transformKey), CIFTransformation.class);
                    columnList = new ArrayList<>();

                    Map<String, String> srcmap = HbaseUtil.getMap(resultMap, transformKey.concat("."));
                    for (Map.Entry<String, String> entry : srcmap.entrySet()) {
                        column = mapper.readValue(entry.getValue(), CIFTransformation.Column.class);
                        columnList.add(column);
                    }
                    transform.setColumns(columnList);
                    transformList.add(transform);

            }
        }
        return transformList;
    }

    private List<CIFAudit> buildAuditResult(NavigableMap<byte[], byte[]> resultMap) throws IOException {
        if (resultMap == null) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CIFAudit audit;
        CIFAuditType auditType;
        List<CIFAudit> auditList = new ArrayList<>();
        List<CIFAuditType> auditTypeList;
        String auditKey;
        for (byte[] key : resultMap.keySet()) {
            auditKey = new String(key);
            if (auditKey.length() == "audit9999".length()) {
                audit = mapper.readValue(HbaseUtil.getHBaseString(resultMap, auditKey), CIFAudit.class);
                auditTypeList = new ArrayList<>();

                Map<String, String> srcmap = HbaseUtil.getMap(resultMap, auditKey.concat("."));
                for (Map.Entry<String, String> entry : srcmap.entrySet()) {
                    auditType = mapper.readValue(entry.getValue(), CIFAuditType.class);
                    auditTypeList.add(auditType);
                }
                audit.setAuditTypes(auditTypeList);
                auditList.add(audit);
            }
        }
        return auditList;
    }









    public List<String> getRowKeyList(String tableIn) throws IOException {
        String tableName = hbaseNameSpace + ":"+tableIn;
        logger.debug("Hbase Table:"+tableName);

        // Instantiating HBaseAdmin class.
        Connection con = ConnectionFactory.createConnection(getConf());
        Table table = con.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setFilter(new FirstKeyOnlyFilter()).setCaching(1000);
        ResultScanner scanner = table.getScanner(scan);
        List<String> valueList = new ArrayList<>();

        for (Result result : scanner) {
            String rowKey = Bytes.toString(result.getRow());
            if(StringUtils.isNotBlank(rowKey)){
                valueList.add(rowKey);
            }
        }
        if (scanner != null) {
            scanner.close();
        }
        if (table != null) {
            table.close();
        }
        if (con != null) {
            con.close();
        }
        return valueList;
    }

    public Map<String,NavigableMap<byte[],byte[]>> getRowKeyList(String tableIn,String columnFamilyIn) throws IOException {
        String tableName = hbaseNameSpace + ":"+tableIn;
        logger.debug("Hbase Table:"+tableName);

        // Instantiating HBaseAdmin class.
        Connection con = ConnectionFactory.createConnection(getConf());
        Table table = con.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addFamily(columnFamilyIn.getBytes());
        ResultScanner scanner = table.getScanner(scan);
        Map<String,NavigableMap<byte[],byte[]>> valueMap = new LinkedHashMap();

        for (Result result : scanner) {
            String rowKey = Bytes.toString(result.getRow());
            if(StringUtils.isNotBlank(rowKey)){
                valueMap.put(rowKey,result.getFamilyMap(columnFamilyIn.getBytes()));
            }
        }
        if (scanner != null) {
            scanner.close();
        }
        if (table != null) {
            table.close();
        }
        if (con != null) {
            con.close();
        }
        return valueMap;
    }

    public NavigableMap<byte[],byte[]> getRow(String rowKey, String tableIn, String columnFamilyIn) throws IOException {
        Connection con = ConnectionFactory.createConnection(getConf());
        Table table = con.getTable(TableName.valueOf(hbaseNameSpace + ":"+tableIn));
        Get g = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(g);
        NavigableMap<byte[], byte[]> dataMap = null;
        if (result != null) {
            dataMap = result.getFamilyMap(columnFamilyIn.getBytes());
        }
        if (table != null) {
            table.close();
        }
        if (con != null) {
            con.close();
        }
        return dataMap;
    }

    public boolean putRow(String rowKeyIn, String tableIn, String columnFamilyIn,byte[] column,byte[] data) throws IOException {
        byte[] rowKey = Bytes.toBytes(rowKeyIn);
        byte[] columnFamily = Bytes.toBytes(columnFamilyIn);

        Connection con = ConnectionFactory.createConnection(getConf());
        Table table = con.getTable(TableName.valueOf(hbaseNameSpace + ":" + tableIn));
        long ts = System.currentTimeMillis();
        Get g = new Get(rowKey);
        g.addColumn(columnFamily, column);
        Result result = table.get(g);
        if (result.isEmpty()) {
            Put p = new Put(rowKey);
            p.addColumn(columnFamily, column, ts, data);
            table.put(p);
        } else {
            Delete delete = new Delete(rowKey);
            delete.addColumn(columnFamily, column);
            Put p = new Put(rowKey);
            p.addColumn(columnFamily, column, ts + 1, data);
            table.delete(delete);
            table.put(p);
        }
        if (table != null) {
            table.close();
        }
        if (con != null) {
            con.close();
        }
        return true;
    }

    public boolean deleteRowColumn(String rowKeyIn, String tableIn, String columnFamilyIn,byte[] column) throws IOException {
        byte[] rowKey = Bytes.toBytes(rowKeyIn);
        byte[] columnFamily = Bytes.toBytes(columnFamilyIn);
        Connection con = ConnectionFactory.createConnection(getConf());
        Table table = con.getTable(TableName.valueOf(hbaseNameSpace + ":" + tableIn));
        Delete delete = new Delete(rowKey);
        delete.addColumn(columnFamily, column);
        table.delete(delete);
        if (table != null) {
            table.close();
        }
        if (con != null) {
            con.close();
        }
        return true;
    }

    public boolean moveColumn(String fromRowKeyIn, String fromTableIn, String fromColumnFamilyIn,
                                 String toRowKeyIn, String toTableIn, String toColumnFamilyIn, byte[] column ) throws IOException {
        byte[] fromRowKey = Bytes.toBytes(fromRowKeyIn);
        byte[] fromColumnFamily = Bytes.toBytes(fromColumnFamilyIn);
        byte[] toRowKey = Bytes.toBytes(toRowKeyIn);
        byte[] toColumnFamily = Bytes.toBytes(toColumnFamilyIn);
        long ts = System.currentTimeMillis();

        Connection con = ConnectionFactory.createConnection(getConf());
        Table fromTable = con.getTable(TableName.valueOf(hbaseNameSpace + ":" + fromTableIn));
        Get g = new Get(fromRowKey);
        g.addColumn(fromColumnFamily, column);
        Result result = fromTable.get(g);

        if (!result.isEmpty()) {
            Delete delete = new Delete(fromRowKey);
            delete.addColumn(fromColumnFamily, column);

            Table toTable = con.getTable(TableName.valueOf(hbaseNameSpace + ":" + toTableIn));
            Put p = new Put(toRowKey);
            p.addColumn(toColumnFamily, column, ts , result.getValue(fromColumnFamily,column));
            toTable.put(p);
            fromTable.delete(delete);
        }else{
            return false;
        }
        return true;
    }
}
