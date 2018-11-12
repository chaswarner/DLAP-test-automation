package com.prft.cif.test.repo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * This interface is to connect with Hive and execute queries against it.
 * @author Anant Gowerdhan
 * @version 1.0
 */
public interface CIFHive {
    @Update("${ddlQuery}")
    public void execute(@Param("ddlQuery") String ddlQuery);

    @Update("DROP TABLE IF EXISTS ${tableName}")
    public void dropTable(@Param("tableName") String tableName);

    @Update("CREATE DATABASE IF NOT EXISTS ${databaseName}")
    public void createDatabase(@Param("databaseName") String databaseName);

    @Update("MSCK REPAIR TABLE ${tableName}")
    public void repairTable(@Param("tableName") String tableName);

    @Update("CREATE ROLE ${role}")
    public void createRole(@Param("role") String role);

    @Update("DROP ROLE ${role}")
    public void dropRole(@Param("role") String role);

    @Update("GRANT SELECT ON ${tableName} TO ROLE ${role}")
    public void grantSelectOnTableToRole(@Param("tableName") String tableName, @Param("role") String role);

    @Update("GRANT ${role} TO USER ${user}")
    public void grantRoleToUser(@Param("role") String role, @Param("user") String user);

    @Select("${query}")
    public List<LinkedHashMap> select(@Param("query") String query);
}
