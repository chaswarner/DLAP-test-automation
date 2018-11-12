package com.prft.cif.test.repo.query;

import com.prft.cif.test.exception.CIFWrongPartitionKey;
import com.prft.cif.test.metadata.CIFSchema;
import com.prft.cif.test.repo.query.CIFFileType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static java.util.Optional.ofNullable;

public class CIFHiveQuery {

    private CIFHiveQuery(){}

    public static String dropTable(String tableName){
        return "DROP TABLE IF EXISTS "+tableName;
    }

    public static String dropView(String viewName){
        return "DROP VIEW IF EXISTS "+viewName;
    }

    public static String createView(String viewName, String tableName){
        return "CREATE VIEW IF NOT EXISTS "+viewName+" AS Select * from "+tableName;
    }

    public static String createTable(String tableName, List<CIFSchema> cifSchema, List<String> partitionKeys, CIFFileType fileType, String location) throws CIFWrongPartitionKey {
        Map<String,String> columnNameAndTypeMap = new LinkedHashMap<>();
        for (CIFSchema schema:cifSchema) {
            columnNameAndTypeMap.put(schema.getName(),schema.getType());
        }

        StringJoiner createQuery = new StringJoiner(" ");
        createQuery.add("CREATE EXTERNAL TABLE IF NOT EXISTS");
        createQuery.add(escapeAsNeeded(tableName)); // CREATE EXTERNAL TABLE IF NOT EXISTS ABC_TABLE
        String partitionStatement = null;
        if (partitionKeys != null && !partitionKeys.isEmpty() && partitionKeys.size()>0) {
            StringJoiner partitions = new StringJoiner(", ", "PARTITIONED BY (", ")\n");  // , PARTITIONED BY ( , )
            for (String key:partitionKeys) {
                if(columnNameAndTypeMap.containsKey(key)){
                    partitions.add(escapeAsNeeded(key) + " " + columnDataType(columnNameAndTypeMap.get(key).toUpperCase()));
                    columnNameAndTypeMap.remove(key);
                }else{
                    System.out.println("Wrong Partition key:"+ key);
                    throw new CIFWrongPartitionKey(key);
                }
            }
            
//            partitionKeys.forEach(key ->{
//                CIFSchema partitionColumn = cifSchema.stream().filter(sc -> sc.getName().equalsIgnoreCase(key)).findAny().orElse(null);
//                System.out.println("Partition key:"+ key + "ParitionColumn"+ partitionColumn);
//                if(partitionColumn != null){
//                    System.out.println(" Partition columns in not null.");
//                    cifSchema.remove(partitionColumn);
//                    partitions.add(escapeAsNeeded(partitionColumn.getName()) + " " + columnDataType(partitionColumn.getType().toUpperCase()));
//                    System.out.println("Partition syntax:"+ partitions.toString());
//                }
//                else{
//                    System.out.println("Wrong Partition key:"+ partitionColumn.getName());
//                    throw new CIFWrongPartitionKey(key);
//                }
//            });
            partitionStatement = partitions.toString();
        }

        StringJoiner schemaColumns = new StringJoiner(",\n", " (\n", ")\n"); // ( , )
        columnNameAndTypeMap.forEach((key,value) -> {
            schemaColumns.add(escapeAsNeeded(key) + " " + columnDataType(value)); //(A,B)
        });

        createQuery.add(schemaColumns.toString());
        ofNullable(partitionStatement).ifPresent(partitionString -> createQuery.add(partitionString));
        createQuery.add("STORED AS ");
        createQuery.add(fileType.toString());
        createQuery.add("LOCATION ");
        createQuery.add("'" + location + "'\n");

        System.out.println("Create Table String:"+ createQuery.toString());
        return createQuery.toString();
    }

//    public static String grantSelectOnTableToRoles(String tableName, List<String> roles) {
//        StringBuilder result = new StringBuilder();
//        result.append("GRANT SELECT ON TABLE " + escapeAsNeeded(tableName) + " TO");
//        for (String role:roles) {
//            result.append(" ROLE "+escapeAsNeeded(role)+",");
//        }
//        return result.substring(0,result.length()-1);
//    }

//    public static String grantSelectOnTableToRoles(String tableName, String role) {
//        return "GRANT SELECT ON TABLE " + escapeAsNeeded(tableName) + " TO ROLE "+escapeAsNeeded(role);
//    }

    public static String createSecureView(String secureViewName, String tableName, List<CIFSchema> cifSchema){
        String databaseName= tableName.split("\\.")[0];
        String table= tableName.split("\\.")[1];
        StringBuilder stringBuilder = new StringBuilder("CREATE VIEW IF NOT EXISTS "+ secureViewName +" AS SELECT ");
        for (CIFSchema schema : cifSchema){
            if(schema.getSensitive().equalsIgnoreCase("PHI") || schema.getSensitive().equalsIgnoreCase("PII")){
                stringBuilder.append("udfDecrypt("+table + "." + schema.getName().toLowerCase() + ",\'" + databaseName + "\') AS " + schema.getName().toLowerCase() + ", ");
            }else{
                stringBuilder.append(table + "." + schema.getName().toLowerCase() + ", ");
            }
        }
        return stringBuilder.substring(0,stringBuilder.length()-2) + " from " + tableName;
    }

    private static String escapeAsNeeded(String doesItContain) {
        if (containsSpecial(doesItContain))
            return "`" + doesItContain + "`";
        else
            return doesItContain;
    }

    private static boolean containsSpecial(String testString) {
        String specialCharacters[] = {"+", "*", "?", "-", ".", "(", ")", "[", "]", "{", "}", "^", "$"};
        for (String character : specialCharacters) {
            if (testString.contains(character))
                return true;
        }
        return false;
    }

    private static String columnDataType(String typeIn){
        String columnType;
        switch (typeIn) {
//            case "VARCHAR":
//                columnType = "STRING";
//                break;
            case "LONG":
                columnType = "BIGINT";
                break;
            default:
                columnType=typeIn;
                break;
        }
        return columnType;
    }
}


