package com.raptor.uploader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author raptor
 * @description MapperTest
 * @date 2021/12/4 16:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {


    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void context() {
        for (int i = 0; i < 30; i++) {
            //threadPoolExecutor.execute(new Runnable() {
            //    @Override
            //    public void run() {
            //        System.out.println("当前线程："+Thread.currentThread().getName());
            //    }
            //});
            System.out.println(System.nanoTime());
        }
    }
}
