/*
 * Copyright (c) 2018, Xuliang Huang. All rights reserved.
 */

package com.atguigu.demo.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.net.URL;

public class FastDFSUtils {
    private static StorageClient storageClient;

    static {
        try {
            String path = "/tracker.conf";
            URL url = FastDFSUtils.class.getResource(path);
            String absolutePath = url.getPath();

            ClientGlobal.init(absolutePath);

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            storageClient = new StorageClient(trackerServer, storageServer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除远程文件的操作
     * @param groupName: 你懂的
     * @param remoteFileName: 你懂的
     * @return
     */
    public static int deDelete(String groupName, String remoteFileName) {
        try {
            return storageClient.delete_file(groupName, remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 封装FastDFS客户端的工具方法，负责上传操作。
     * @param fileBuff: 上传文件的字节数组
     * @param originalFileName: 通过MultipartFile对象的getOriginalFilename()得到的原始文件名
     * @return: 上传的结果。上传失败返回null，成功的话，数组下表0为组名，1为远程文件名
     */
    public static String[] doUpload(byte[] fileBuff, String originalFileName) {
        String fileExtName = "";

        if (originalFileName != null) {
            if (originalFileName.contains(".")) {
                int indexOf = originalFileName.lastIndexOf(".");
                fileExtName = originalFileName.substring(indexOf + 1);

            }

        }

        try {
            String[] result = storageClient.upload_file(fileBuff, fileExtName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
}
