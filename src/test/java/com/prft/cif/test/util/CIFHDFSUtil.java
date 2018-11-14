package com.prft.cif.test.util;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;

import java.io.IOException;

public class CIFHDFSUtil {

    private Configuration conf;

    private FileSystem fs;

    @Inject
    public void configuration(@Named("hdfs.namenode.url") String hdfsNameNodeUrl) throws IOException {
        conf = new Configuration();
        conf.set("fs.defaultFS", hdfsNameNodeUrl);
        fs = FileSystem.get(conf);
    }

    public void createDirectory(String file, String permission) throws IOException {
        Path src = new Path(file);
        if (!fs.exists(src)) {
            fs.mkdirs(src, new FsPermission(permission));
        }
    }

    public void moveFile(String srcFile, String destFile) throws IOException {
        Path src = new Path(srcFile);
        Path dest = new Path(destFile);
        if (fs.exists(src) && fs.exists(dest.getParent())) {
            fs.rename(src, dest);
        }
    }

    public void CopyFromLocal(String srcFile, String destFile) throws IOException {
        Path src = new Path(srcFile);
        Path dest = new Path(destFile);
        if (fs.exists(dest.getParent())) {
            fs.copyFromLocalFile(src, dest);
        }
    }


    public void deleteFile(String file) throws IOException {
        Path path = new Path(file);
        if (fs.exists(path)) {
            fs.delete(path, true);

        }
    }

    public ContentSummary getContentSummary(String file) throws IOException {
        Path path = new Path(file);
        if (fs.exists(path)) {
            return fs.getContentSummary(path);
        }
        return null;
    }

    public boolean writeFileToHDFS(String file, StringBuilder jsonData, String permission) throws IOException {

        Path path = new Path(file);
        createDirectory(path.getParent().toString(), permission);
        FSDataOutputStream outputStream = fs.create(path);
        if (outputStream!=null) {
            outputStream.writeBytes(jsonData.toString());
            outputStream.close();
            return true;
        }
        return false;
    }

    public StringBuilder readFileFromHDFS(String file) throws IOException{

        StringBuilder readJson = new StringBuilder();

        if(!StringUtils.isBlank(file) ){
            FSDataInputStream in = fs.open(new Path(file));
            readJson.append(IOUtils.toString(in, "UTF-8"));
            in.close();

        }

        return readJson;
    }
}
