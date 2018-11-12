

package com.prft.cif.test.conversion;

import com.google.inject.Inject;
import com.prft.cif.test.metadata.*;
import com.prft.cif.test.util.CIFDatasetUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class converts excel metadata workbook to HBase dataset object.
 * <p></p>
 *
 * @author Vishvas Patel
 * @author Anant Gowerdhan
 */
public class MetadataWorkbook {
    @Inject
    private static Logger logger;

    @Inject
    private Properties properties;

    private static int sheetRowNo = 0;

    /**
     * @param file
     * @param sheetIndex
     * @param typeIn
     * @return
     */
    public CIFDataset getDataset(File file, int sheetIndex, String typeIn) throws IOException, JSONException {

        XSSFSheet sheet = null;
        XSSFSheet auditSheet = null;

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));

        //Get first/desired sheet from the workbook
        if (workbook != null) {
            sheet = workbook.getSheetAt(sheetIndex);
            auditSheet = workbook.getSheetAt(sheetIndex+1);
        }
        CIFDataset dataset = new CIFDataset();
        dataset.setAttributes(new ArrayList<>());
        dataset.setSchemas(new ArrayList<>());
        dataset.setTrsfms(new ArrayList<>());
        dataset.setAudit(new ArrayList<>());
        dataset.setSources(new ArrayList<>());
        dataset.setLock(false);
        dataset.setZone(typeIn.trim().toLowerCase());
        dataset.setSchedule(new CIFSchedule());
        setDataset(sheet, dataset, typeIn);
        String type = typeIn.trim().toLowerCase();

        if (type.equals("raw")) {
            dataset.setCompression(".bz2");
            setRawDatasetSchema(sheet, dataset);
        } else if (type.equals("curate")) {
            dataset.setCompression(".snappy");
            setCurateDatasetSchema(sheet, dataset);
        } else {
            return null;
        }
        dataset.setRoles(CIFDatasetUtil.getRoles(dataset));

        if (dataset.getAuditFlag())
            setAudit(auditSheet,dataset);


        workbook.close();
        return dataset;
    }

    /**
     * @param sheet
     * @param dataset
     * @param datasetType
     */
    private void setDataset(XSSFSheet sheet, CIFDataset dataset, String datasetType) throws JSONException {
        sheetRowNo = sheet.getFirstRowNum();

        Map<String, String> map = reverseProperties();
        JSONObject json = new JSONObject();

        for (sheetRowNo = sheetRowNo + 2; sheetRowNo <= sheet.getLastRowNum(); sheetRowNo++) {
            Row row = sheet.getRow(sheetRowNo);
            String keyCell = row.getCell(0).getStringCellValue().trim();
            Cell valueCell = row.getCell(1);
            if (keyCell.trim().equals("Column Level Metadata")) {
                break;
            }
            List list=null;
            String valueList;
            if (map.containsKey(keyCell)) {
                String attributeName = map.get(keyCell);
                switch (attributeName) {
                    case "namespace_1":
                        Object val = cellToObject(valueCell);
                        if(datasetType.equals("curate"))
                            loadObject(dataset, "namespace", val.toString());
                        loadObject(dataset,"businessDomain",val.toString());

                        break;
                    case "namespace":
                          Object val1 = cellToObject(valueCell);
                          if (datasetType.equals("raw"))
                              loadObject(dataset, "namespace", val1.toString());
                        loadObject(dataset, "sourceSystemCode", val1.toString());
                        break;
                    case "sources":
                        if (datasetType.equals("curate")) {
                            dataset.getSources().add(new CIFName(dataset.getNamespace(), dataset.getName(), dataset.getVersion()));
                        }
                        break;
//                    case "roles":
//                        valueList = cellToObject(valueCell).toString();
//                        list = new ArrayList<String>(Arrays.asList(valueList.split("\\s*,\\s*")));
//                        loadObject(dataset, "roles", list);
//                        break;
                    case "tags":
                        valueList = cellToObject(valueCell).toString();
                        list = new ArrayList<String>(Arrays.asList(valueList.split("\\s*,\\s*")));
                        loadObject(dataset, "tags", list);
                        break;
                    case "partitionKeys":
                        valueList = cellToObject(valueCell).toString().trim();
                        if (datasetType.equals("curate") && StringUtils.isNotBlank(valueList) && valueList.length()>0) {
                            list = new ArrayList<String>(Arrays.asList(valueList.split("\\s*,\\s*")));
                            loadObject(dataset, "partitionKeys", list);
                        }
                        else{
                            list= new ArrayList<String>();
                        }
                        loadObject(dataset, "partitionKeys", list);
                        break;
                    case "primaryKeys":
                        valueList = cellToObject(valueCell).toString().trim();
                        if (datasetType.equals("curate") && StringUtils.isNotBlank(valueList) && valueList.length()>0) {
                            list = new ArrayList<String>(Arrays.asList(valueList.split("\\s*,\\s*")));
                            loadObject(dataset, "primaryKeys", list);
                        }
                        else{
                            list= new ArrayList<String>();
                        }
                        loadObject(dataset, "primaryKeys", list);
                        break;
                    case "ingestType":
                        loadObject(dataset, attributeName, cellToObject(valueCell).toString().toLowerCase().trim());
                        break;
                    case "fileNameFormat":
                        loadObject(dataset, attributeName, valueCell.toString()+".*");
                        break;
                    case "connection":
                        if(StringUtils.isNotBlank(cellToObject(valueCell).toString())){
                            json.put(attributeName,cellToObject(valueCell));
                        }
                        break;
                    case "database":
                        if(StringUtils.isNotBlank(cellToObject(valueCell).toString())){
                            json.put(attributeName,cellToObject(valueCell));
                        }
                        break;
                    case "table":
                        if(StringUtils.isNotBlank(cellToObject(valueCell).toString())){
                            json.put(attributeName,cellToObject(valueCell));
                        }
                        break;
                    case "copyBook":
                        loadObject(dataset, attributeName, cellToObject(valueCell).toString().trim());
                        break;
                    case "hours":
                        loadObject(dataset.getSchedule(),"hours",cellToObject(valueCell));
                        break;
                    case "minutes":
                        loadObject(dataset.getSchedule(),"minutes",cellToObject(valueCell));
                        break;
                    case "weekDays":
                        loadObject(dataset.getSchedule(),"weekDays",cellToObject(valueCell));
                        break;
                    case "monthDays":
                        loadObject(dataset.getSchedule(),"monthDays",cellToObject(valueCell));
                        break;
                    case "month":
                        loadObject(dataset.getSchedule(),"month",cellToObject(valueCell));
                        break;
                    case "startTime":
                        loadObject(dataset.getSchedule(),"startTime",cellToObject(valueCell));
                        break;
                    case "endTime":
                        loadObject(dataset.getSchedule(),"endTime",cellToObject(valueCell));
                        break;
                    case "auditFlag":
                        Object val2 = cellToObject(valueCell);
                        if(val2.toString().trim().equals("Yes"))
                            loadObject(dataset,"auditFlag",true);
                        else
                            loadObject(dataset,"auditFlag",false);
                        break;
                    default:
                        loadObject(dataset, attributeName, cellToObject(valueCell));
                        break;
                }
            } else {

                CIFAttribute attribute = new CIFAttribute(keyString(keyCell), cellToObject(valueCell).toString());
                dataset.getAttributes().add(attribute);
            }
        }
        if (datasetType.equals("raw")) {
            setIngestionTrsfms(dataset,json);
        }
    }

    /**
     * @param dataset
     */
    private void setIngestionTrsfms(CIFDataset dataset, JSONObject json) {
        CIFTransformation trsfms = new CIFTransformation();
        List<String> datasetSourceList = new ArrayList<String>();
        List<CIFTransformation.Column> columnList = new ArrayList<CIFTransformation.Column>();
        trsfms.setColumns(columnList);
        trsfms.setSource(datasetSourceList);

        switch (dataset.getIngestType().toLowerCase()) {
            case "file":
                trsfms.setId(0);
                trsfms.setName(CIFDatasetUtil.getRawHbaseRowKey(dataset) + ".flume");
                datasetSourceList.add(CIFDatasetUtil.getRawHbaseRowKey(dataset));
                trsfms.setDescription(CIFDatasetUtil.getRawHbaseRowKey(dataset) + " Flume copy from external source to RAW");
                trsfms.setCriteria("");
                String destinationLocation = "/" + dataset.getNamespace() + "/" + dataset.getName() + "/" + dataset.getVersion();
                trsfms.setExpression("source:,destination:" + destinationLocation);
                break;
            case "multirecord file":
                break;
            case "database":
                trsfms.setId(0);
                trsfms.setName(CIFDatasetUtil.getRawHbaseRowKey(dataset) + ".sqoop");
                trsfms.setDescription(CIFDatasetUtil.getRawHbaseRowKey(dataset) + " Sqoop get data from Table to RAW");
                trsfms.setExpression("");
                trsfms.setCriteria(json.toString());
                break;
            case "stream":
                break;
            default:
                break;
        }
        dataset.getTrsfms().add(trsfms);
    }

    /**
     * @param sheet
     * @param dataset
     */
    private void setRawDatasetSchema(XSSFSheet sheet, CIFDataset dataset) {
        int schemaId = 0;
        for (sheetRowNo = sheetRowNo + 3; sheetRowNo <= sheet.getLastRowNum(); sheetRowNo++) {

            Row row = sheet.getRow(sheetRowNo);
            CellType type = row.getCell(0).getCellTypeEnum();
            if(type==null || type == CellType._NONE || type==CellType.BLANK)continue;
            CIFSchema schema = new CIFSchema();
            loadObject(schema, "id", schemaId++);
            loadObject(schema, "name", cellToObject(row.getCell(0)));
            loadObject(schema, "description", cellToObject(row.getCell(1)));
            loadObject(schema, "type", getColumnDataType(cellToObject(row.getCell(2)).toString()));
            loadObject(schema, "position", cellToObject(row.getCell(3)));
            loadObject(schema, "size", cellToObject(row.getCell(4)));
            loadObject(schema, "required", cellToObject(row.getCell(5)));
            loadObject(schema, "format", cellToObject(row.getCell(10)));
            loadObject(schema, "sensitive", cellToObject(row.getCell(14)));
            loadObject(schema, "rules", " ");
            dataset.getSchemas().add(schema);
        }
    }

    /**
     * @param sheet
     * @param dataset
     */
    private void setCurateDatasetSchema(XSSFSheet sheet, CIFDataset dataset) {
        CIFTransformation trsfms = new CIFTransformation();
        List<String> datasetSourceList = new ArrayList<String>();
        List<CIFTransformation.Column> columnList = new ArrayList<CIFTransformation.Column>();
        String rawKeyString = CIFDatasetUtil.getRawHbaseRowKey(dataset);
        datasetSourceList.add(rawKeyString);
        trsfms.setId(0);
        trsfms.setName(rawKeyString + ".trsfms");
        trsfms.setSource(datasetSourceList);
        trsfms.setDescription(rawKeyString + " transformation from RAW to Curate Zone");
        trsfms.setColumns(columnList);
        trsfms.setExpression("");
        trsfms.setCriteria("");
        CIFTransformation.Column column = null;
        int schemaId = 0;
        int trsfmsId = 0;
        int lastPositionValue=0;

        List<String> rawColumnList = new ArrayList<String>();

        for (sheetRowNo = sheetRowNo + 3; sheetRowNo <= sheet.getLastRowNum(); sheetRowNo++) {
            Row row = sheet.getRow(sheetRowNo);

            String sourceColumnName = cellToObject(row.getCell(0)).toString(); //Adding row columnName into the list
            if(StringUtils.isNotBlank(sourceColumnName))rawColumnList.add(sourceColumnName);


            CellType type = row.getCell(11).getCellTypeEnum();//Skipping curate row if curate column is Blank
            if(type==null || type == CellType._NONE || type==CellType.BLANK)continue;

            CIFSchema schema = new CIFSchema();
            loadObject(schema, "id", schemaId++);
            loadObject(schema, "name", cellToObject(row.getCell(11)));
            loadObject(schema, "description", cellToObject(row.getCell(1)));
            loadObject(schema, "type", getColumnDataType(cellToObject(row.getCell(12)).toString()));
            loadObject(schema, "position", cellToObject(row.getCell(3))); lastPositionValue=schema.getPosition();
            loadObject(schema, "size", cellToObject(row.getCell(13)));
            loadObject(schema, "required", cellToObject(row.getCell(5)));
            loadObject(schema, "format", cellToObject(row.getCell(16)));
            loadObject(schema, "sensitive", cellToObject(row.getCell(14)));
            loadObject(schema, "rules", " ");

            List<String> columnSource = new ArrayList<String>();
            //String sourceColumnName= cellToObject(row.getCell(0)).toString();
            if(StringUtils.isNotBlank(sourceColumnName))columnSource.add(rawKeyString + "." + sourceColumnName);

            String expression=null;
            if (cellToObject(row.getCell(15)) != null && !cellToObject(row.getCell(15)).toString().isEmpty()) {

                String cellValue = cellToObject(row.getCell(15)).toString();
                System.out.println("Expression is not empty. Expression:===>"+ cellValue);

                if(StringUtils.isBlank(sourceColumnName)) {
                    System.out.println("Source Column is Empty. Current list:==>" + rawColumnList.toString());
                    for (String rowColumnName : rawColumnList) {
                        System.out.println("Source Column:===>" + rowColumnName);
                        if (cellValue.contains(rowColumnName)) {
                            String columnName = rawKeyString + "." + rowColumnName;
                            expression = cellValue.replace(rowColumnName, columnName);
                            columnSource.add(columnName);
                            break; // Curate Expression contains any previous row column than replace it value and break it
                        }
                    }
                }else{
                    if (cellValue.contains("udfTableLookup") || cellValue.contains("udfFileNameExtract") ) {
                        expression = cellValue;
                    }else if (cellValue.contains("sourceDataset")) {
                        expression = cellValue.replace("sourceDataset", rawKeyString);
                    } else if (cellValue.contains(sourceColumnName)) {
                        String columnName = rawKeyString + "." + sourceColumnName;
                        expression = cellValue.replace(sourceColumnName, columnName);
                    }
                }
            }else{
                expression = rawKeyString+"."+sourceColumnName;
            }
            column = new CIFTransformation.Column(trsfmsId++, columnSource, cellToObject(row.getCell(11)).toString(), expression);
            trsfms.getColumns().add(column);

            dataset.getSchemas().add(schema);
        }
        //Schema(id,name,description,type,required,size,position,encrypted,masked, String format)
        dataset.getSchemas().add(new CIFSchema(schemaId,"timestamp_","timestamp","TIMESTAMP",false,0,++lastPositionValue,"","None"," "));
        schemaId++;
        dataset.getSchemas().add(new CIFSchema(schemaId,"cflag_","Current Flag","INT",false,0,++lastPositionValue,"","None"," "));
        schemaId++;
        dataset.getSchemas().add(new CIFSchema(schemaId,"dflag_","Delete Flag","INT",false,0,++lastPositionValue,"","None"," "));

        dataset.getTrsfms().add(trsfms);
    }


    private void setAudit(XSSFSheet auditSheet, CIFDataset dataset) throws JSONException {

        Integer auditSheetRowNo = auditSheet.getFirstRowNum();

        CIFAudit audit = new CIFAudit();
        CIFAuditType cifAuditType = null;
        List<CIFAuditType> cifAuditTypeList = new ArrayList<>();

        loadObject(audit,"auditLocation",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
        loadObject(audit,"auditSuffix",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
        loadObject(audit,"auditFileIdentifier",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
        loadObject(audit,"auditFileIdentifierField",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
        loadObject(audit,"auditFileNamePattern",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
        loadObject(audit,"auditFileCount",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;

        audit.setAuditTypes(cifAuditTypeList);

        for (auditSheetRowNo = auditSheetRowNo + 1; auditSheetRowNo <= auditSheet.getLastRowNum(); auditSheetRowNo++) {
            Row row = auditSheet.getRow(auditSheetRowNo);
            if (row.getCell(0).toString().equals("Audit Type")){
                cifAuditType = new CIFAuditType();

                loadObject(cifAuditType,"type",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
                loadObject(cifAuditType,"field",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++; auditSheetRowNo++;
                loadObject(cifAuditType,"row",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
                loadObject(cifAuditType,"startPos",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
                loadObject(cifAuditType,"size",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
                loadObject(cifAuditType,"rawCriteria",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1))); auditSheetRowNo++;
                loadObject(cifAuditType,"curateCriteria",cellToObject(auditSheet.getRow(auditSheetRowNo).getCell(1)));
                loadObject(cifAuditType,"rowLine",0);
                loadObject(cifAuditType,"hash","");
                audit.getAuditTypes().add(cifAuditType);

            }else{
                break;
            }
        }
        dataset.getAudit().add(audit);
    }


        /**
         * @param dataset
         * @param attribute
         * @param value
         */
    private void loadObject(Object dataset, String attribute, Object value) {
        try {
            BeanUtils.setProperty(dataset, attribute, value);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
    }

//    private Boolean checkCellNotEmpty(Cell cell){
//        CellType type = cell.getCellTypeEnum();
//        if(type==null || type == CellType._NONE || type==CellType.BLANK)return false;
//        else return true;
//    }
    /**
     * @param cell
     * @return
     */
    private Object cellToObject(Cell cell) {
        if(cell == null){
            return new Object();
        }
        CellType type = cell.getCellTypeEnum();
        //System.out.println("~~~~~~ Cell Type:==>|"+type+"|Cell Value in Call:===>|"+ cell+"|****");
        if(type==null || type == CellType._NONE || type==CellType.BLANK){
            String value = cleanString(cell.getStringCellValue());
            //System.out.println("CellToObject _NONE Value:===>|"+ cell +"|<====>"+ value );
            return new String();
        }else if (type == CellType.STRING) {
            String value = cleanString(cell.getStringCellValue());
            //System.out.println("CellToObject String Value:==>|"+ cell +"<====>"+ value );
            return value;
        }else if (type == CellType.BOOLEAN) {
            Boolean value = cell.getBooleanCellValue();
            //System.out.println("CellToObject Boolean Value:===>|"+ cell +"|<====>"+ value );
            return value;
        }else if (type == CellType.NUMERIC) {
            if (cell.getCellStyle().getDataFormatString().contains("%")) {
                return cell.getNumericCellValue() * 100;
            }
            return numeric(cell);
        }else if (type == CellType.FORMULA) {
            CellType formulaType = cell.getCachedFormulaResultTypeEnum();
            if (formulaType == CellType.NUMERIC)
                return numeric(cell);
            if (formulaType == CellType.STRING)
                return cleanString(cell.getRichStringCellValue().toString());
        }
        return new Object();
    }

    /**
     * @param str
     * @return
     */
    private String cleanString(String str) {
        String temp=  str.replace("\n", "").replace("\r", "").trim();
        if(StringUtils.isBlank(temp)){
            return "";
        }else{
            return temp;
        }
    }

    /**
     * @param str
     * @return
     */
    private String keyString(String str) {
        String temp =  str.replace("\n", "").replace("\r", "").replace(" ", "").trim();
        if(StringUtils.isBlank(temp)){
            return "";
        }else{
            return temp;
        }
    }

    /**
     * @param cell
     * @return
     */
    private Object numeric(Cell cell) {
        return Double.valueOf(cell.getNumericCellValue());
    }

    /**
     * @param typeIn
     * @return
     */
    private String getColumnDataType(String typeIn) {
        String columnType;
        switch (typeIn) {
            case "VARCHAR":
                columnType = "STRING";
                break;
            case "DECIMAL":
                columnType = "DOUBLE";
                break;
            default:
                columnType = typeIn;
                break;
        }
        return columnType;
    }

    /**
     * @return
     */
    private Map<String, String> reverseProperties() {
        Map<String, String> map = new HashMap<String, String>();

        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            map.put(value.trim(), key.trim());
        }

        return map;
    }
}
