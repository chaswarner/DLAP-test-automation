package com.prft.cif.test.repo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * This interface is to connect with Impala
 * @author Anant Gowerdhan
 * @version 1.0
 */
public interface CIFImpala {
    @Update("INVALIDATE METADATA ${tableName}")
    public void invalidateMetadata(@Param("tableName") String tableName);

    @Update("REFRESH ${tableName}")
    public void refreshTable(@Param("tableName") String tableName);
}
