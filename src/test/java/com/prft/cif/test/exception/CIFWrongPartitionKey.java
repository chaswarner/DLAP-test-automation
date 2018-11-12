package com.prft.cif.test.exception;

public class CIFWrongPartitionKey extends RuntimeException {
    public CIFWrongPartitionKey(String partitionKey){
        super("Wrong partition key '"+partitionKey+"' provided.");
    }
}
