package com.qius.ziptools;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

/**
 * 文件加密压缩(zip格式)和zip文件解压缩
 *
 * @author qiusong
 * @since 1.0.0
 */
public class ZipUtil {

    private ZipUtil() {
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到当前文件夹
     *
     * @param srcPath  要压缩的文件或文件夹路径
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String srcPath, String password) {

        File srcFile = new File(srcPath);
        String destPath = srcFile.getParent();
        String dest = zip(srcPath, destPath, password);

        return dest;
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到当前文件夹
     *
     * @param srcFile  要压缩的文件或文件夹路径(File格式)
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(File srcFile, String password) {

        String destPath = srcFile.getParent();
        String dest = zip(srcFile.getPath(), destPath, password);

        return dest;
    }


    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置
     *
     * @param srcFile  要压缩的文件或文件夹路径(File格式)
     * @param destFile 压缩文件存放路径
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(File srcFile, File destFile, String password) {

        String srcPath = srcFile.getAbsolutePath();
        String destPath = destFile.getAbsolutePath();

        return zip(srcPath, destPath, password);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
     *
     * @param src      要压缩的文件或文件夹路径
     * @param dest     压缩文件存放路径
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败
     */
    public static String zip(String src, String dest, String password) {

        File srcFile = new File(src);

        // 规范dest路径
        if (FileUtil.isDirectory(dest)) {
            if (!dest.endsWith("\\")) {
                dest += "\\";
            }
        }
        // 生成相应的压缩文件存放地址
        dest = buildDestinationZipFilePath(srcFile, dest);
        File destFile = new File(dest);


        // 采用无加密方式压缩子目录下的内容
        File resultFile = cn.hutool.core.util.ZipUtil.zip(destFile, false, srcFile);


        // 将子目录的压缩包进行处理
        String resultFileName = resultFile.getName().substring(0, resultFile.getName().lastIndexOf("."));

        String tempFilePath = resultFile.getParent() + File.separator + resultFileName + DateUtil.current(false);

        File tempFile = FileUtil.mkdir(tempFilePath);
        FileUtil.move(resultFile, tempFile, true);

        // 设置压缩参数
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);

            ArrayList<File> temp = new ArrayList<>();
            // 加密文件夹
            if (tempFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                File[] subFiles = tempFile.listFiles();
                Collections.addAll(temp, subFiles);
                // 生成加密文件
                zipFile.createZipFile(temp, parameters);
//                zipFile.addFile(tempFile,parameters);
                FileUtil.del(tempFile);
//                Console.log("zip temp " + temp);
                return dest;
            } else {
                // 加密文件
                zipFile.addFile(tempFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            e.printStackTrace();
            // 删除临时创建的文件
            FileUtil.del(tempFilePath);
        }
        return null;
    }

    /**
     * 解压zip文件到当前文件夹
     *
     * @param srcZipFile 待解压的zip文件的路径(File格式)
     * @param password   ZIP文件的密码
     * @return 最后解压文件存放的绝对路径，如果为null则说明解压失败
     */
    public static String unzip(File srcZipFile, String password) {

        String dest = srcZipFile.getParent();

        return unzip(srcZipFile.getPath(), dest, password);
    }

    /**
     * 解压zip文件到当前文件夹
     *
     * @param srcZip   待解压的zip文件的路径
     * @param password ZIP文件的密码
     * @return 最后解压文件存放的绝对路径，如果为null则说明解压失败
     */
    public static String unzip(String srcZip, String password) {

        File zipFile = new File(srcZip);
        String dest = zipFile.getParent();

        return unzip(srcZip, dest, password);
    }

    /**
     * 解压zip文件到目标文件夹
     *
     * @param srcZipFile 待解压的zip文件的路径(File格式)
     * @param destFile   解压文件存放路径
     * @param password   文件密码
     * @return 最后解压文件存放的绝对路径，如果为null则说明解压失败
     */
    public static String unzip(File srcZipFile, File destFile, String password) {

        return unzip(srcZipFile.getPath(), destFile.getPath(), password);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param dest     解压目录
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static String unzip(String srcZip, String dest, String password) {

        File zipFile = new File(srcZip);
        File destFile = new File(dest);
        // 生成解压目录
        String destFileName = zipFile.getName().substring(0, zipFile.getName().lastIndexOf("."));
        String destFilePath = destFile.getAbsolutePath() + File.separator + destFileName;
        // 临时的destFilePath 用于存放子目录压缩文件
        String destFilePathTemp = destFile.getAbsolutePath() + File.separator + "temp" + DateUtil.current(false);
        FileUtil.mkdir(destFilePathTemp);

        // 判断当前目录是否有与destFilePath文件名相同的目录
        int num = 0;
        String initalPath = destFilePath;
        while (FileUtil.isDirectory(destFilePath) && !initalPath.isEmpty() && num >= 0) {
            destFilePath = initalPath + "_" + num;
            num++;
        }
        FileUtil.mkdir(destFilePath);

        try {
            // 解压文件
            ZipFile zFile = new ZipFile(zipFile);
            zFile.setFileNameCharset("GBK");
            if (!zFile.isValidZipFile()) {
                throw new ZipException("压缩文件不合法,可能被损坏.");
            }

            File destDir = new File(destFilePath);
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }
            if (zFile.isEncrypted()) {
                zFile.setPassword(password.toCharArray());
            }
            zFile.extractAll(destFilePath);

            List<FileHeader> headerList = zFile.getFileHeaders();
            List<File> extractedFileList = new ArrayList<>();
            for (FileHeader fileHeader : headerList) {
                if (!fileHeader.isDirectory()) {
                    extractedFileList.add(new File(destDir, fileHeader.getFileName()));
                }
            }
            File[] extractedFiles = new File[extractedFileList.size()];
            extractedFileList.toArray(extractedFiles);

            // 解压子目录,并完成相应完整目录的创建
            File[] unzipFile = FileUtil.ls(destFilePath);
            File destFileTemp = new File(destFilePathTemp);
            FileUtil.move(unzipFile[0], destFileTemp, true);
            File[] destFileTmpList = FileUtil.ls(destFilePathTemp);

            cn.hutool.core.util.ZipUtil.unzip(destFileTmpList[0].getPath(), destFilePath);
            FileUtil.del(destFileTemp);

            return destFilePath;
        } catch (ZipException e) {
            e.printStackTrace();
            // 删除解压过程中创建的文件夹
            FileUtil.del(destFilePath);
            FileUtil.del(destFilePathTemp);
        }

        return null;
    }

    /**
     * 构建压缩文件存放路径,如果不存在将会创建
     * 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     *
     * @param srcFile   源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */
    private static String buildDestinationZipFilePath(File srcFile, String destParam) {

        // 压缩目标路径为空时，压缩目标路径在源文件路径下
        if (StringUtils.isEmpty(destParam)) {
            // 源文件（文件夹）的路径下创建对应的压缩目标文件
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
            } else {
                // 源文件（文件）的路径下创建对应的压缩目标文件
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";
            }
        } else { // 目标文件存在时
            // 在指定路径不存在的情况下将其创建出来
            createDestDirectoryIfNecessary(destParam);
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ".zip";
            }
        }
        return destParam;
    }


    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */
    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

}
