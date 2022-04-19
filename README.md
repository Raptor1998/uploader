# Uploader

## environment

+ Springboot 2.3.5 
+ Mybatis 1.3.2

## content-path

`/api/uploader`

## upload request process 

### second upload

1. 计算文件的md5值，查询是否已经存在
2. 是，根据文件名称和md5值实现秒传；否，直接上传

### block upload

1. 计算文件md5值，根据需求切片，得到总片数

2. ```java
   /**
    * 分块上传
    *
    * @param originalName 文件原名
    * @param file         文件
    * @param chunks       共分几块
    * @param chunk        第几块
    * @param size         文件总大小
    * @param md5          文件的md5
    * @return
    */
   @PostMapping("/block/single")
   public Result uploadBigSingleFile(@RequestParam("originalName") String originalName,
                                     @RequestParam("file") MultipartFile file,
                                     @RequestParam("chunks") Integer chunks,
                                     @RequestParam("chunk") Integer chunk,
                                     @RequestParam("size") Long size,
                                     @RequestParam("md5") String md5) {
       uploaderService.uploadWithBlock(originalName, file, chunks, size, chunk, md5);
       return ResultUtil.success(null);
   }
   ```

3. 每上传完成一次，将上传的切片信息存储在数据库

4. 查询数据库中所有关于此文件的切片是否达到总的切片数

5. 将文件信息保存

# properties

```properties
#线程池
uploader.thread.coreSize=20
uploader.thread.maxSize=200
uploader.thread.keepAliveTime=10
#文件全路径
uploader.file.path=/uploader/document/
#uploader.file.path=E://codeDemo//uploader//files//
#文件限制大小
uploader.file.size=100
#文件后缀名
uploader.file.extension=txt,doc,docx,ppt,pptx,pdf,zip,jpeg,gif,jpg,mmap,mp4,avi,
#静态服务器
uploader.file.prefixUrl=http://127.0.0.1:9999/document/
#保存的文件是否以year/month/day分区
uploader.file.folderRole=false
```

# SQL

```sql
-- ----------------------------
-- Table structure for block_file
-- ----------------------------
DROP TABLE IF EXISTS `block_file`;
CREATE TABLE `block_file`  (
                               `block_file_id` int(4) NOT NULL AUTO_INCREMENT,
                               `block_file_chunk` int(11) NOT NULL COMMENT '第几块',
                               `block_file_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'md5',
                               `upload_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上传时间',
                               `temp_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '临时文件的存储位置',
                               PRIMARY KEY (`block_file_id`) USING BTREE,
                               UNIQUE INDEX `block_file_chunk`(`block_file_chunk`, `block_file_md5`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
                              `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件详情',
                              `file_original_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原文件名',
                              `file_union_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一标识',
                              `file_real_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实路径',
                              `file_suffix` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '拓展名',
                              `file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络地址',
                              `file_size` bigint(20) NOT NULL COMMENT '文件大小',
                              `file_upload_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上传时间',
                              `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件的md5值',
                              PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

```



# reference

[文件上传工具类（JAVA）](https://www.jianshu.com/p/d58d275ac1f4?ivk_sa=1024320u)

[gaoyuyue/MyUploader-Backend](https://github.com/gaoyuyue/MyUploader-Backend)

