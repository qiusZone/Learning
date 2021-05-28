package com.qius.ziptools;

import cn.hutool.core.lang.Console;

import java.io.File;


/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author qiusong
 * @create 2019/9/19
 * @see
 * @since 1.0.0
 */
public class ZipDemo {

    public static void main(String[] args) {

        // zip test
        File srcFile = new File("E:\\test\\ziptest\\jdk-8u151-windows-x64.EXE");
        String destFilePath = "E:\\test\\ziptest\\ziptest_temp";
        File destFile = new File(destFilePath);
//        String zipFile = "E:\\test\\ziptest\\jdk-8u151-windows-x64.zip";
//        File zipfsdf = new File(zipFile);
//        Console.log(zipfsdf.getName());
//        Console.log("----: " + FileUtil.exist(zipfsdf));
//        long start = System.currentTimeMillis();
//        String result2 = ZipUtil.zip(srcFile, destFile, "123");
//        Console.log("result: " + result2);
//        Console.log(System.currentTimeMillis() - start);
//        String result1 = ZipUtil.zip(srcFile, "123");
//        Console.log(result1);
//        String result = ZipUtil.zip("E:\\test\\ziptest\\test2", "123");
//        Console.log(result);

//        String path = "E:\\test\\ziptest\\unziptest_temp\\test2";
//        File pathFile = new File(path);
//        Console.log(FileUtil.isDirectory(path));

        // unzip test
        String srcZipPath = "E:\\test\\ziptest\\ziptest_temp\\jdk-8u151-windows-x64.zip";
        String destPath1 = "E:\\test\\ziptest\\unziptest_temp";

        File srcZipFile = new File(srcZipPath);
        File destPath1File = new File(destPath1);

        String resultFile = ZipUtil.unzip(srcZipPath, destPath1, "123");
        Console.log(resultFile);
//        String resultFile1 = ZipUtil.unzip(srcZipFile,destPath1File, "123");
//        Console.log(resultFile1);
//        String resultFile2 = ZipUtil.unzip(srcZipFile, "123");
//        Console.log(resultFile2);
//        String resultFile3 = ZipUtil.unzip(srcZipPath, "123");
//        Console.log(resultFile3);
        // 解压到不同路径
//        ZipUtil.unzip(srcZipFile, destFile2, "123");
        // 解压到相同路径
//        ZipUtil.unzip(srcZipFile, destFile1, "123");

    }
}
