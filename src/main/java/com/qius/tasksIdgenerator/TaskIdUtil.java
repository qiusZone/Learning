package com.qius.tasksIdgenerator;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.File;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务单号生成工具类，保证获取的id在正常服务或者系统重启后全局唯一性
 * 调用该工具类的初始状态需要通过{@link #setCurrentMaxTaskId(String, int)}来设置当前最大的taskId
 * 之后直接调用{@link #getTaskSequence(String)}即可直接获取对应的单号
 *
 * @author qiusong
 * @since 1.0.0
 */
public class TaskIdUtil {

    /**
     * cache 存储任务单号的缓存 Guava cache
     */
    private static Cache<String, Integer> cache = CacheBuilder.newBuilder()
            // 设置cache的最大size
            .maximumSize(100)
            // 设置并发数为1
            .concurrencyLevel(1)
            .build();

//    /**
//     * 默认存储的filePath
//     */
//    private static String defaultFilePath = System.getProperty("user.dir") + File.separator + "temp.properties";

//    /**
//     * 测试用file
//     */
//    private static File testFile = FileUtil.touch(new File(defaultFilePath));

    /**
     * lock 可重入锁
     */
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 私有构造器
     */
    private TaskIdUtil() {

    }

    /**
     * 获取单例
     *
     * @return 返回TaskIdUtil实体
     */
    public static TaskIdUtil getInstance() {
        return TaskIdInstance.INSTANCE;
    }

    /**
     * 获取任务单id,并完成任务单id的自增
     *
     * @return 自增后的任务单id 不存在时直接返回null
     */
    public Object getTaskId(String taskType) {
        return cache.getIfPresent(taskType + "TaskId");
    }

    /**
     * 根据taskType:CY,BZ,CBX类型生成任务单号
     *
     * @param taskType CY,BZ,CBX
     * @return 任务单号taskSequence 如果cache中不存在当前taskId,则向
     */
    public String getTaskSequence(String taskType) {

        // 生成taskSequence所需时间
        DateTime date = DateUtil.date(new Date());
        String year = String.format("%04d", date.year());
        String month = String.format("%02d", date.monthStartFromOne());
        String day = String.format("%02d", date.dayOfMonth());
        String time = year + month + day;

        String taskSequence = String.format("%016d", 0);

        try {
            lock.lock();
            // 判断缓存中是否已有taskType类型的TaskId值
            if (null != cache.getIfPresent(taskType + "TaskId")) {

                int currentTaskId = cache.getIfPresent(taskType + "TaskId");
                // 从cache中读取当前任务单id并创建taskSequence
                taskSequence = createTaskSequence(taskType, time, currentTaskId);
            }
        } finally {
            lock.unlock();
        }

        return taskSequence;
    }

    /**
     * 从外部接口获取当前任务单类型taskType和taskId的当前currentMaxTaskId
     *
     * @param taskType         CY,BZ,CBX
     * @param CurrentMaxTaskId 系统中当前已分配的最大taskId
     */
    public void setCurrentMaxTaskId(String taskType, int CurrentMaxTaskId) {

        // 将CurrentMaxTaskId写入缓存
        cache.put(taskType + "TaskId", CurrentMaxTaskId);

    }

    /**
     * 每天00:00定时cache清零
     */
    public void createTimingSchedule() {

        // 定时规则
        String configRule = "0 0 * * *";
        CronUtil.schedule(configRule, new Task() {
            @Override
            public void execute() {
                cache.put("CYTaskId", 0);
                cache.put("BZTaskId", 0);
                cache.put("CBXTaskId", 0);
                Console.log("reset cache ");
                Console.log(getTaskId("CY"));
                Console.log(getTaskId("BZ"));
                Console.log(getTaskId("CBX"));
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }


    // ----------------------------------------------------------------Private method start

    /**
     * 创建任务单号TaskSequence
     * @param taskType      CY,BZ,CBX
     * @param currentTaskId 当前任务单id(6位或5位)
     * @return 返回更新后的taskId
     */
    private String createTaskSequence(String taskType, String time, int currentTaskId) {

        String taskNum = taskType + "TaskId";

        String taskSequence = taskType + time;

        // 更新currentTaskId并写入缓存
        currentTaskId++;
        cache.put(taskNum, currentTaskId);
//        FileUtil.appendUtf8String(currentTaskId + "\r\n", testFile);
        // 根据taskType类型生成taskSequence
        if (taskType.equals("CBX")) {
            taskSequence = taskSequence + String.format("%05d", currentTaskId);
            // taskId超过99999时 返回错误码
            if(currentTaskId % 100000 == 0) {
                taskSequence = String.format("%016d", 1);
            }
        } else {
            taskSequence = taskSequence + String.format("%06d", currentTaskId);
            // taskId超过999999时 返回错误码
            if(currentTaskId % 1000000 == 0) {
                taskSequence = String.format("%016d", 1);
            }
        }

        return taskSequence;
    }

    /**
     * 静态内部类生成单例模式的工具类
     */
    private static class TaskIdInstance {
        private static final TaskIdUtil INSTANCE = new TaskIdUtil();
    }
}
