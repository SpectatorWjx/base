package com.wang.base.config.mongo;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.net.UnknownHostException;


@Configuration
public class MongoConfig {

    /**
     * 自动注入video mongodb
     */
    @Value("${spring.data.mongodb.file.uri}")
    private String MONGO_FILE;

    /**
     * 自动注入image mongodb
     */
    @Value("${spring.data.mongodb.image.uri}")
    private String MONGO_IMAGE;


    // ===================== 连接到 mongodb image 服务器 =================================

    @Bean
    @Primary
    public MongoDbFactory imageDBFactory(){
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_IMAGE));
    }

    @Bean(name = "imageMongoTemplate")
    @Primary
    public MongoTemplate imageMongoTemplate() {
        return new MongoTemplate(imageDBFactory(), this.mappingMongoConverter(imageDBFactory()));
    }

    /**
     * mongo
     * @return
     */
    @Bean("imageGridFSBucket")
    public GridFSBucket imageGridFSBucket() {
        MongoDatabase db = imageDBFactory().getDb();
        return GridFSBuckets.create(db);
    }

    @Bean("imageGridFsTemplate")
    public GridFsTemplate imageGridFsTemplate(){
        return new GridFsTemplate(this.imageDBFactory(), mappingMongoConverter(imageDBFactory()));
    }

    // ==================== 连接到 mongodb video 服务器 ======================================

    @Bean
    public MongoDbFactory fileDBFactory(){
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_FILE));
    }

    @Bean(name = "fileMongoTemplate")
    public MongoTemplate fileMongoTemplate(){
        return new MongoTemplate(this.fileDBFactory(), mappingMongoConverter(fileDBFactory()));
    }

    /**
     * mongo
     * @return
     */
    @Bean("fileGridFSBucket")
    public GridFSBucket fileGridFSBucket() {
        MongoDatabase db = fileDBFactory().getDb();
        return GridFSBuckets.create(db);
    }

    @Bean("fileGridFsTemplate")
    public GridFsTemplate fileGridFsTemplate(){
        return new GridFsTemplate(this.fileDBFactory(), mappingMongoConverter(fileDBFactory()));
    }




    /**
     * 使用自定义的typeMapper去除写入mongodb时的“_class”字段
     *
     * @return
     * @throws Exception
     */
    private MappingMongoConverter mappingMongoConverter(MongoDbFactory dbFactory){
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(dbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

}