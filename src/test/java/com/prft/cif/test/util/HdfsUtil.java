package com.prft.cif.test.util;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsUtil {

    FileSystem fs=null;

    public void  initilizeHdfs() throws Exception {
        // ====== Init HDFS File System Object
        Configuration conf = new Configuration();
// Set FileSystem URI
        conf.set("fs.defaultFS", "hdfs://nameservice9");
// Because of Maven
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
// Set HADOOP user
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        System.setProperty("hadoop.home.dir", "/");
        //Get the filesystem - HDFS
        FileSystem fs = FileSystem.get(conf);

    }

    public void copyFileToHdfs(String srcFile, String destFile) throws Exception{

            Path src = new Path(srcFile);
            Path dest = new Path(destFile);
            if (fs.exists(src) && fs.exists(dest.getParent())) {
                fs.copyFromLocalFile(src, dest);

            }
        }
    }

