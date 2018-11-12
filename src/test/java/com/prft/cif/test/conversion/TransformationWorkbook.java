package com.prft.cif.test.conversion;

import com.google.inject.Inject;
import com.prft.cif.test.metadata.*;
import com.prft.cif.test.repository.HBaseRepository;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author Harshil Shah
 */
public class TransformationWorkbook {

    private final Logger LOGGER = Logger.getLogger(this.getClass());

    @Inject
    private Properties properties;

    private static int sheetRowNo = 0;

    private static HBaseRepository hbase;

    public void setHbase(HBaseRepository hbase) {
        TransformationWorkbook.hbase = hbase;
    }

    /**
     * @param file
     * @param sheetIndex
     * @param typeIn
     * @return
     */
    public CIFDataset getDataset(File file, int sheetIndex, String typeIn) throws IOException, JSONException {

        XSSFSheet sheet = null;

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));

        if (workbook != null) {
            sheet = workbook.getSheetAt(sheetIndex);
        }
        CIFDataset dataset = new CIFDataset();
        dataset.setAttributes(new ArrayList<CIFAttribute>());
        dataset.setSchemas(new ArrayList<CIFSchema>());
        dataset.setTrsfms(new ArrayList<CIFTransformation>());
        dataset.setSources(new ArrayList<CIFName>());
        dataset.setAudit(new ArrayList<>());
        dataset.setSchedule(new CIFSchedule());
        dataset.setLock(false);
        dataset.setZone(typeIn.trim().toLowerCase());
        setPublishDataset(sheet, dataset, hbase);
        setPublishDatasetSchema(sheet, dataset);
        setPublishDatasetTrsfms(sheet, dataset);
        dataset.setRoles(CIFDatasetUtil.getRoles(dataset));
        workbook.close();
        return dataset;
    }

    /**
     * @param sheet
     * @param dataset
     */
    private void setPublishDataset(XSSFSheet sheet, CIFDataset dataset, HBaseRepository hbase) throws JSONException, IOException {
        sheetRowNo = sheet.getFirstRowNum();

        Map<String, String> map = reverseProperties();

        for (sheetRowNo = sheetRowNo + 2; sheetRowNo <= sheet.getLastRowNum(); sheetRowNo++) {
            Row row = sheet.getRow(sheetRowNo);
            String keyCell = row.getCell(0).getStringCellValue().trim();
            Cell valueCell = row.getCell(1);
            if (keyCell.trim().equals("Column Level Metadata")) {
                break;
            }
            List list;
            String valueList;
            if (map.containsKey(keyCell)) {
                String attributeName = map.get(keyCell);
                switch (attributeName) {
                    case "namespace_1":
                        Object val = cellToObject(valueCell);
                        loadObject(dataset,"businessDomain",val.toString());
                        loadObject(dataset, "namespace", val.toString());
                        break;
                    case "sources":
                        valueList = cellToObject(valueCell).toString();
                        List<String> sourceList = new ArrayList<String>(Arrays.asList(valueList.split("\\s*,\\s*")));
                        for(String str : sourceList) {
                            String key = hbase.getLatestDatasetRowkey(str);
                            if (!key.isEmpty()) {
                                List<String> rowKey = new ArrayList<String>(Arrays.asList(key.split("\\.")));
                                dataset.getSources().add(new CIFName(rowKey.get(0), rowKey.get(1), rowKey.get(2)));
                            }
                            else {
                                List<String> tempRow = Arrays.asList(str.split("\\."));
                                dataset.getSources().add(new CIFName(tempRow.get(0), tempRow.get(1), dataset.getVersion()));
                            }
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
                        if (StringUtils.isNotBlank(valueList) && valueList.length()>0) {
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
                        if (StringUtils.isNotBlank(valueList) && valueList.length()>0) {
                            list = new ArrayList<String>(Arrays.asList(valueList.split("\\s*,\\s*")));
                            loadObject(dataset, "primaryKeys", list);
                        }
                        else{
                            list= new ArrayList<String>();
                        }
                        loadObject(dataset, "primaryKeys", list);
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
                    default:
                        loadObject(dataset, attributeName, cellToObject(valueCell));
                        break;
                }
            } else {

                CIFAttribute attribute = new CIFAttribute(keyString(keyCell), cellToObject(valueCell).toString());
                dataset.getAttributes().add(attribute);
            }
        }

    }


    /**
     * @param sheet
     * @param dataset
     */
    private void setPublishDatasetSchema(XSSFSheet sheet, CIFDataset dataset) {
        int schemaId = 0;
        int lastPositionValue=0;

        for (sheetRowNo = sheetRowNo + 3; sheetRowNo <= sheet.getLastRowNum(); sheetRowNo++) {

            Row row = sheet.getRow(sheetRowNo);
            String keyCell = row.getCell(0).getStringCellValue().trim();
            if (keyCell.trim().equals("SQL")) {
                break;
            }

            CIFSchema schema = new CIFSchema();

            loadObject(schema, "id", schemaId++);
            loadObject(schema, "name", cellToObject(row.getCell(0)));
            loadObject(schema, "description", cellToObject(row.getCell(1)));
            loadObject(schema, "position", schema.getId()); lastPositionValue=schema.getPosition();
            loadObject(schema, "type", getColumnDataType(cellToObject(row.getCell(2)).toString()));
            loadObject(schema, "size", cellToObject(row.getCell(3)));
            loadObject(schema, "required", false);
            loadObject(schema, "format", " ");
            loadObject(schema, "sensitive", cellToObject(row.getCell(4)));
            loadObject(schema, "rules", cellToObject(row.getCell(5)));
            dataset.getSchemas().add(schema);
        }
        //Schema(id,name,description,type,required,size,position,encrypted,masked, String format)
        dataset.getSchemas().add(new CIFSchema(schemaId,"timestamp_","timestamp","TIMESTAMP",false,0,++lastPositionValue,"","None", " "));
        schemaId++;
        dataset.getSchemas().add(new CIFSchema(schemaId,"cflag_","Current Flag","INT",false,0,++lastPositionValue,"","None"," "));
        schemaId++;
        dataset.getSchemas().add(new CIFSchema(schemaId,"dflag_","Delete Flag","INT",false,0,++lastPositionValue,"","None"," "));
    }

    /**
     * @param sheet
     * @param dataset
     */
    private void setPublishDatasetTrsfms(XSSFSheet sheet, CIFDataset dataset) {
        CIFTransformation trsfms;
        List<String> datasetSourceList = new ArrayList<String>();
        List<CIFTransformation.Column> columnList;
        int trsfmsSetId = 0;

        for(CIFName cifName : dataset.getSources())
            datasetSourceList.add(cifName.getNamespace()+"."+cifName.getName()+"."+cifName.getVersion());

        for (sheetRowNo = sheetRowNo + 1; sheetRowNo <= sheet.getLastRowNum(); sheetRowNo++) {

            Row row = sheet.getRow(sheetRowNo);
            trsfms = new CIFTransformation();
            columnList = new ArrayList<CIFTransformation.Column>();

            String keyCell = row.getCell(0).getStringCellValue().trim();
            Cell valueCell = row.getCell(1);

            trsfms.setId(trsfmsSetId++);
            trsfms.setName(cellToObject(valueCell).toString());
            trsfms.setSource(datasetSourceList);
            trsfms.setDescription("Transformation Curate to Publish");
            trsfms.setColumns(columnList);
            trsfms.setExpression(keyCell);
            trsfms.setCriteria("");

            dataset.getTrsfms().add(trsfms);
        }
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
            LOGGER.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * @param cell
     * @return
     */
    private Object cellToObject(Cell cell) {
//        if(cell == null){
//            return new Object();
//        }
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
