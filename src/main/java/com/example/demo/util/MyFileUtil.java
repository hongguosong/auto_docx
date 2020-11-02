package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019/11/6 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

public class MyFileUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(MyFileUtil.class);
    public static final String separator = "/|\\\\";
    public static void unRar(File dir, MultipartFile multipartFile) throws IOException, RarException{
        Junrar.extract(multipartFile.getInputStream(), dir);
    }

    public static File unSingle(File dir, MultipartFile multipartFile) throws IOException{
        InputStream is = multipartFile.getInputStream(); // 把当前条目的字节数据转换成Inputstream流
        String path = dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename();
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        toWrite(is, file);
        return file;
    }

    public static void unZip(File dir, MultipartFile multipartFile) throws IOException, RarException{
        ZipInputStream zis = new ZipInputStream(multipartFile.getInputStream());
        java.util.zip.ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            // System.out.printf("条目信息： 名称%1$b, 大小%2$d, 压缩时间%3$d \n",
            // entry.getName(), entry.getSize(), entry.getTime());
            if (entry.isDirectory()) { // is dir
                // System.out.println(entry.getName() + "是一个目录");
                File f = new File(dir.getAbsolutePath() + File.separator + entry.getName());
                if (!f.exists())
                    f.mkdirs();
            } else { //
                byte[] data = getByte(zis); // 获取当前条目的字节数组
                InputStream is = new ByteArrayInputStream(data); // 把当前条目的字节数据转换成Inputstream流
                String[] names = entry.getName().split(separator);
                String path = dir.getAbsolutePath() + File.separator;
                path += join(names, File.separator);
                //System.out.println(path);
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                    toWrite(is, file);
                }
            }

        }
    }

    /**
     * 向file文件写入字节
     * @param ins
     * @param file
     */
    public static void toWrite(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 获取条目byte[]字节
     * @param zis
     * @return
     */
    public static byte[] getByte(InflaterInputStream zis) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            byte[] buf = null;
            int length = 0;

            while ((length = zis.read(temp, 0, 1024)) != -1) {
                bout.write(temp, 0, length);
            }

            buf = bout.toByteArray();
            bout.close();
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public static String join(Object[] o, String flag) {
        StringBuffer str_buff = new StringBuffer();

        for (int i = 0, len = o.length; i < len; i++) {
            str_buff.append(String.valueOf(o[i]));
            if (i < len - 1)
                str_buff.append(flag);
        }

        return str_buff.toString();
    }

    /**
     * 扫描上传文件
     * @param folderPath 文件目录
     * @return
     */
    public static List<String> scanUploadFiles(String folderPath){
        File directory = new File(folderPath);
        ArrayList<String> scanFiles = new ArrayList<String>();
        if (directory.isDirectory()) {
            File[] filelist = directory.listFiles();
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
                    scanFiles.addAll(scanUploadFiles(filelist[i].getAbsolutePath()));
                }
                else {
                    if(filelist[i].getName().toLowerCase().endsWith(".adb")){
                        scanFiles.add(filelist[i].getAbsolutePath());
                    }
                }
            }
        }
        return scanFiles;
    }
}
