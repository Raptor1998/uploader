package com.raptor.uploader;

import com.raptor.uploader.entity.FileInfo;
import com.raptor.uploader.mapper.FileInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    FileInfoMapper fileInfoMapper;

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

    @Test
    @Transactional(rollbackFor = Exception.class)
    @Commit
    public void context2() throws InterruptedException {
        try {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileOriginalName("originalName");
            fileInfo.setFileSize(121L);
            fileInfo.setFileRealPath("/raptor");
            fileInfo.setFileUploadTime(new Date());
            fileInfo.setFileSuffix(".jpg");
            fileInfo.setFileUnionName("asdsad");
            fileInfo.setFileUrl("http://asdas");
            fileInfo.setMd5("qwe");
            int insert = fileInfoMapper.insert(fileInfo);
            if (insert>0){
                System.out.println("插入完成");
            }
            Thread.sleep(5000);
            System.out.println(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
