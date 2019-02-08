package com.prft.cif.test.util;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.commons.lang.StringUtils;

public class HdfsUtil {

    FileSystem fs=null;

    public void  initilizeHdfs() throws Exception {
        // ====== Init HDFS File System Object
        Configuration conf = new Configuration();
// Set FileSystem URI
        conf.set("fs.defaultFS", "hdfs://nameservice9");
/*// Because of Maven
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
// Set HADOOP user
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        System.setProperty("hadoop.home.dir", "/");*/
        //Get the filesystem - HDFS
        fs = FileSystem.get(conf);
        System.out.println("file system obj--hdfs-->"+fs.toString());
        System.out.println("file system conf--hdfs-->"+conf.toString());
    }

    public void copyFileToHdfs(String srcFile, String destFile) throws Exception{

            Path src = new Path(srcFile);
            Path dest = new Path(destFile);
               if(!(StringUtils.isBlank(src.toString()) && StringUtils.isBlank(dest.toString()))) {
                   fs.copyFromLocalFile(src, dest);
               }
        }
    }

