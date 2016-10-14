package com.bzj.dragon.mongodb.dao;

import com.bzj.dragon.mongodb.utils.ReflectionUtils;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.List;
import java.util.Set;

/**
 * mongo操作基类
 * User:aaronbai@tcl.com
 * Date:2016-10-14
 * Time:13:49
 */

public abstract class BaseMongoDAOImpl<T> {

    protected MongoTemplate mongoTemplate;

    /**
     * 保存数据
     * @param entity
     * @return
     */
    public T save(T entity) {
        mongoTemplate.insert(entity);
        return entity;
    }

    /**
     * 保存数据
     * @param entity
     * @param collectionName
     * @return
     */
    public T save(T entity,String collectionName){
        mongoTemplate.save(entity,collectionName);
        return entity;
    }

    /**
     * 通过id查找
     * @param id
     * @return
     */
    public T findById(String id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    /**
     * 指定集合通过id查找数据
     * @param id
     * @param collectionName
     * @return
     */
    public T findById(String id, String collectionName) {
        return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
    }

    /**
     * 查询所有
     * @return
     */
    public List<T> findAll(){
        return mongoTemplate.findAll(this.getEntityClass());
    }

    /**
     * 删除所有类型的数据
     */
    public void remove() {
        mongoTemplate.findAll(this.getEntityClass());

    }

    /**
     * 删除指定数据
     * @param entity
     */
    public void remove(T entity){
        mongoTemplate.remove(entity);
    }

    /**
     * 删除指定集合中的数据
     * @param entity
     * @param collectionName
     */
    public void remove(T entity,String collectionName){
        mongoTemplate.remove(entity,collectionName);
    }

    public boolean collectionExists(){
        return mongoTemplate.collectionExists(this.getEntityClass());
    }

    public boolean collectionExists(String collectionName){
        return  mongoTemplate.collectionExists(collectionName);
    }

    public DBCollection createCollection(){
        return mongoTemplate.createCollection(this.getEntityClass());
    }

    public DBCollection createCollection(String collectionName){
        return mongoTemplate.createCollection(collectionName);
    }

    public void dropCollection(){
        mongoTemplate.dropCollection(this.getEntityClass());
    }
    public void dropCollection(String collectionName){
        mongoTemplate.dropCollection(collectionName);
    }

    public DBCollection getCollection(String collectionName){
        return mongoTemplate.getCollection(collectionName);
    }

    public Set<String> getCollectionNames(){
        return mongoTemplate.getCollectionNames();
    }

    public MongoConverter getConverter(){
        return mongoTemplate.getConverter();
    }

    public DB gitDb(){
        return mongoTemplate.getDb();
    }

    public ScriptOperations scriptOps(){
        return mongoTemplate.scriptOps();
    }

    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    private Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 注入mongodbTemplate
     *
     * @param mongoTemplate
     */
    protected abstract void setMongoTemplate(MongoTemplate mongoTemplate);

}
