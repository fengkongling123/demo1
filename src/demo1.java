import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class demo1 {
    //第二次修改，提交并推送到远程的Github仓库上
    //第三次修改，提交并推送到远程的Github仓库上
    //做一次修改的尝试
    //第四次修改，做分支合并
    static int n=1000;
    public static void main(String[] args) {
        System.out.println("程序开始!");
        MyRun s = new MyRun();
        Callable ca1 = new Callable() {
            // 执行写的任务
            @Override
            public Object call() throws Exception {
                s.setName("小白");
                return null;
            }
        };
        Callable ca2 = new Callable() {
            // 执行读的任务
            @Override
            public Object call() throws Exception {
                s.getName();
                return null;
            }
        };
        // 获取当前系统时间毫秒值
        Long start = System.currentTimeMillis();
        // 创建线程池
        ExecutorService es = Executors.newFixedThreadPool(25);
        // 提交写的任务3次
        for (int i = 0; i < 3; i++) {
            es.submit(ca1);
        }
        // 提交读的任务22次
        for (int i = 0; i < 22; i++) {
            es.submit(ca2);
        }

        es.shutdown();// 关闭线程池

        // 判断线程池的线程是否已经结束
        while (true) {
            if (es.isTerminated()) {
                break;
            }
        }
        // 获取程序运行结束时间的毫秒值
        Long end = System.currentTimeMillis();
        System.out.println("程序结束,运行时间" + (end - start) + "毫秒");
    }
}
class MyRun{
    private String name;
    // 创建重入互斥锁对象
    // Lock lock = new ReentrantLock();
    // 创建一个可重入读写锁
    ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock rl = rwl.readLock();// 获取读锁
    ReentrantReadWriteLock.WriteLock wl = rwl.writeLock();// 获取写锁

    // 写的方法,执行一次休眠1秒
    public void setName(String name) {
        wl.lock(); // 加写锁
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            this.name = name;
        } finally {
            wl.unlock();// 释放锁
        }
    }

    // 读的方法,执行一次休眠1秒
    public String getName() {
        rl.lock();// 加读锁
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return name;
        } finally {
            rl.unlock();// 释放锁
        }
    }

}
