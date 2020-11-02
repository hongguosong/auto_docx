package com.example.demo.util;/*
 * <p>项目名称: dashflat </p>
 * <p>包名称: com.example.demo.util </p>
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2020/3/23 </p>
 * <p>公司信息: 苏州鸿然信息科技有限公司</p>
 * @author <a href="mail to: 994662950@qq.com" rel="nofollow">ALEX</a>
 * @version v1.0
 * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 生成压缩文件 （zip，rar 格式）
 */
public class CompressUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(CompressUtil.class);
    /**
     * @param path   要压缩的文件路径
     * @param format 生成的格式（zip、rar）d
     */
    public static String generateFile(String path, String format) throws Exception {

        File file = new File(path);
        // 压缩文件的路径不存在
        if (!file.exists()) {
            LOGGER.error("路径 " + path + " 不存在文件，无法进行压缩...");
            throw new Exception("路径 " + path + " 不存在文件，无法进行压缩...");
        }
        // 用于存放压缩文件的文件夹
        String generateFile = file.getParent();
        File compress = new File(generateFile);
        // 如果文件夹不存在，进行创建
        if( !compress.exists() ){
            compress.mkdirs();
        }

        // 目的压缩文件
        String generateFileName = compress.getAbsolutePath() + File.separator + file.getName() + "." + format;

        // 输入流 表示从一个源读取数据
        // 输出流 表示向一个目标写入数据

        // 输出流
        FileOutputStream outputStream = new FileOutputStream(generateFileName);

        // 压缩输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(outputStream));

        generateFile(zipOutputStream,file,"");

        System.out.println("源文件位置：" + file.getAbsolutePath() + "，目的压缩文件生成位置：" + generateFileName);
        // 关闭 输出流
        zipOutputStream.flush();
        zipOutputStream.close();
        return generateFileName;
    }

    /**
     * @param out  输出流
     * @param file 目标文件
     * @param dir  文件夹
     * @throws Exception
     */
    private static void generateFile(ZipOutputStream out, File file, String dir) throws Exception {

        // 当前的是文件夹，则进行一步处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] files = file.listFiles();

            //将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir + File.separator));

            dir = dir.length() == 0 ? "" : dir + File.separator;

            //循环将文件夹中的文件打包
            for (int i = 0; i < files.length; i++) {
                generateFile(out, files[i], dir + files[i].getName());
            }

        } else { // 当前是文件

            // 输入流
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // 标记要打包的条目
            out.putNextEntry(new ZipEntry(dir));
            // 进行写操作
//            int len = 0;
//            byte[] bytes = new byte[1024];
//            while ((len = inputStream.read(bytes)) > 0) {
//                out.write(bytes, 0, len);
//            }

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
//                if(len < 1024){
//                    System.out.println(len);
//                }
            }

            // 关闭输入流
            bis.close();
            fis.close();
            out.flush();
        }

    }
//　　// 测试
//    public static void main(String[] args) {
//        String path = "";
//        String format = "rar";
//
//        try {
//            generateFile(path, format);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//
//    }
}
