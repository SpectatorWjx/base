package com.wang.base.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: wjx
 * @date: 2019/10/23 10:31
 * @description: redis工具类
 */
@Component
public class RedisUtil {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    /*常量，各种实现方式都行，这里读取application.yml*/
    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 判断key下是否有值
     *
     * @param key 判断的key
     */
    public Boolean hasToken(String key) {
        return stringRedisTemplate.opsForHash().getOperations().hasKey("token:"+key);
    }


    /**
     * 判断此token是否在黑名单中
     * @param token
     * @return
     */
    public Boolean isBlackList(String token){
        return hasKey("blacklist",token);
    }

    /**
     * 将token加入到redis黑名单中
     * @param token
     */
    public void addBlackList(String token){
        hset("blacklist", token,"true");
    }


    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getTokenValidTimeByToken(String token) {
        return stringRedisTemplate.opsForHash().get("token:"+token, "tokenValidTime");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getUsernameByToken(String token) {
        return stringRedisTemplate.opsForHash().get("token:"+token, "username");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getClientByToken(String token) {
        return stringRedisTemplate.opsForHash().get("token:"+token, "clientId");
    }

    /**
     * 查询token下的过期时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getExpirationTimeByToken(String token) {
        return stringRedisTemplate.opsForHash().get("token:"+token, "expirationTime");
    }

    /**
     * 设置token
     * @param token
     * @param username
     * @param clientId
     */
    public void setTokenRefresh(String token,String username,String clientId){
        //刷新时间
        Integer expire = validTime*24*60*60*1000;

        hset("token:"+token, "tokenValidTime", DateUtil.getAddDayTime(validTime),expire);
        hset("token:"+token, "expirationTime",DateUtil.getAddDaySecond(expirationSeconds),expire);
        hset("token:"+token, "username",username,expire);
        hset("token:"+token, "clientId",clientId,expire);
    }

    /**
     * 删除token
     *
     * @param key 查询的key
     */
    public void deleteToken(String key) {
        stringRedisTemplate.opsForHash().getOperations().delete("token:"+key);
    }


    /**------------------------------------手机验证码-----------------------------------------------**/

    /**
     * 字符串存入值
     * @param expire 过期时间（毫秒计）
     * @param key
     * */
    public void setPhoneMsg(String key, String value,Integer expire){
        stringRedisTemplate.opsForValue().set("phone:"+key,value, expire,TimeUnit.SECONDS);
    }
    /**
     * 获取手机验证码
     * @param key
     * */
    public Object getPhoneMsg(String key){
        return stringRedisTemplate.opsForValue().get("phone:"+key);
    }

    /**
     * 判断key下是否有值
     *
     * @param key 判断的key
     */
    public Boolean hasPhoneMsg(String key) {
        return stringRedisTemplate.opsForHash().getOperations().hasKey("phone:"+key);
    }
    /**
     * 删除验证码
     *
     * @param key 查询的key
     */
    public void deletePhoneMsg(String key) {
        stringRedisTemplate.opsForHash().getOperations().delete("phone:"+key);
    }


    /** -------------------key相关操作--------------------- */



        /**
         * 查询key,支持模糊查询
         *
         * @param key 传过来时key的前后端已经加入了*，或者根据具体处理
         * */
        public Set<String> keys(String key){
            return stringRedisTemplate.keys(key);
        }


        /**
         * 字符串存入值
         * @param expire 过期时间（毫秒计）
         * @param key
         * */
        public void set(String key, String value,Integer expire){
            stringRedisTemplate.opsForValue().set(key,value, expire,TimeUnit.SECONDS);
        }

        /**
         * 删出key
         * 这里跟下边deleteKey（）最底层实现都是一样的，应该可以通用
         * @param key
         * */
        public void delete(String key){
            stringRedisTemplate.opsForValue().getOperations().delete(key);
        }

        /**
         * 添加单个
         * 默认过期时间为两小时
         * @param key    key
         * @param filed  filed
         * @param domain 对象
         */
        public void hset(String key,String filed,Object domain){
            stringRedisTemplate.opsForHash().put(key, filed, domain);
        }

        /**
         * 添加单个
         * @param key    key
         * @param filed  filed
         * @param domain 对象
         * @param expire 过期时间（毫秒计）
         */
        public void hset(String key,String filed,Object domain,Integer expire){
            stringRedisTemplate.opsForHash().put(key, filed, domain);
            stringRedisTemplate.expire(key, expire,TimeUnit.SECONDS);
        }

        /**
         * 添加HashMap
         *
         * @param key    key
         * @param hm    要存入的hash表
         */
        public void hset(String key, HashMap<String,Object> hm){
            stringRedisTemplate.opsForHash().putAll(key,hm);
        }

        /**
         * 如果key存在就不覆盖
         * @param key
         * @param filed
         * @param domain
         */
        public void hsetAbsent(String key,String filed,Object domain){
            stringRedisTemplate.opsForHash().putIfAbsent(key, filed, domain);
        }

        /**
         * 查询key和field所确定的值
         *
         * @param key 查询的key
         * @param field 查询的field
         * @return HV
         */
        public Object hget(String key,String field) {
            return stringRedisTemplate.opsForHash().get(key, field);
        }

        /**
         * 查询该key下所有值
         *
         * @param key 查询的key
         * @return Map<HK, HV>
         */
        public Object hget(String key) {
            return stringRedisTemplate.opsForHash().entries(key);
        }

        /**
         * 删除key下所有值
         *
         * @param key 查询的key
         */
        public void deleteKey(String key) {
            stringRedisTemplate.opsForHash().getOperations().delete(key);
        }

        /**
         * 判断key和field下是否有值
         *
         * @param key 判断的key
         * @param field 判断的field
         */
        public Boolean hasKey(String key,String field) {
            return stringRedisTemplate.opsForHash().hasKey(key,field);
        }

        /**
         * 判断key下是否有值
         *
         * @param key 判断的key
         */
        public Boolean hasKey(String key) {
            return stringRedisTemplate.opsForHash().getOperations().hasKey(key);
        }


        /**
         * 序列化key
         *
         * @param key
         * @return
         */
        public byte[] dump(String key) {
            return stringRedisTemplate.dump(key);
        }


        /**
         * 设置过期时间
         *
         * @param key
         * @param timeout
         * @param unit
         * @return
         */
        public Boolean expire(String key, long timeout, TimeUnit unit) {
            return stringRedisTemplate.expire(key, timeout, unit);
        }

        /**
         * 设置过期时间
         *
         * @param key
         * @param date
         * @return
         */
        public Boolean expireAt(String key, Date date) {
            return stringRedisTemplate.expireAt(key, date);
        }

        /**
         * 将当前数据库的 key 移动到给定的数据库 db 当中
         *
         * @param key
         * @param dbIndex
         * @return
         */
        public Boolean move(String key, int dbIndex) {
            return stringRedisTemplate.move(key, dbIndex);
        }

        /**
         * 移除 key 的过期时间，key 将持久保持
         *
         * @param key
         * @return
         */
        public Boolean persist(String key) {
            return stringRedisTemplate.persist(key);
        }

        /**
         * 返回 key 的剩余的过期时间
         *
         * @param key
         * @param unit
         * @return
         */
        public Long getExpire(String key, TimeUnit unit) {
            return stringRedisTemplate.getExpire(key, unit);
        }

        /**
         * 返回 key 的剩余的过期时间
         *
         * @param key
         * @return
         */
        public Long getExpire(String key) {
            return stringRedisTemplate.getExpire(key);
        }

        /**
         * 从当前数据库中随机返回一个 key
         *
         * @return
         */
        public String randomKey() {
            return stringRedisTemplate.randomKey();
        }

        /**
         * 修改 key 的名称
         *
         * @param oldKey
         * @param newKey
         */
        public void rename(String oldKey, String newKey) {
            stringRedisTemplate.rename(oldKey, newKey);
        }

        /**
         * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
         *
         * @param oldKey
         * @param newKey
         * @return
         */
        public Boolean renameIfAbsent(String oldKey, String newKey) {
            return stringRedisTemplate.renameIfAbsent(oldKey, newKey);
        }

        /**
         * 返回 key 所储存的值的类型
         *
         * @param key
         * @return
         */
        public DataType type(String key) {
            return stringRedisTemplate.type(key);
        }

        /** -------------------string相关操作--------------------- */

        /**
         * 设置指定 key 的值
         * @param key
         * @param value
         */
        public void set(String key, String value) {
            stringRedisTemplate.opsForValue().set(key, value);
        }

        /**
         * 获取指定 key 的值
         * @param key
         * @return
         */
        public String get(String key) {
            return stringRedisTemplate.opsForValue().get(key);
        }

        /**
         * 返回 key 中字符串值的子字符
         * @param key
         * @param start
         * @param end
         * @return
         */
        public String getRange(String key, long start, long end) {
            return stringRedisTemplate.opsForValue().get(key, start, end);
        }

        /**
         * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
         *
         * @param key
         * @param value
         * @return
         */
        public String getAndSet(String key, String value) {
            return stringRedisTemplate.opsForValue().getAndSet(key, value);
        }

        /**
         * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
         *
         * @param key
         * @param offset
         * @return
         */
        public Boolean getBit(String key, long offset) {
            return stringRedisTemplate.opsForValue().getBit(key, offset);
        }

        /**
         * 批量获取
         *
         * @param keys
         * @return
         */
        public List<String> multiGet(Collection<String> keys) {
            return stringRedisTemplate.opsForValue().multiGet(keys);
        }

        /**
         * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
         *
         * @param key
         * @param offset
         *            位置
         * @param value
         *            值,true为1, false为0
         * @return
         */
        public boolean setBit(String key, long offset, boolean value) {
            return stringRedisTemplate.opsForValue().setBit(key, offset, value);
        }

        /**
         * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
         *
         * @param key
         * @param value
         * @param timeout
         *            过期时间
         * @param unit
         *            时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
         *            秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
         */
        public void setEx(String key, String value, long timeout, TimeUnit unit) {
            stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
        }

        /**
         * 只有在 key 不存在时设置 key 的值
         *
         * @param key
         * @param value
         * @return 之前已经存在返回false,不存在返回true
         */
        public boolean setIfAbsent(String key, String value) {
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        }

        /**
         * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
         *
         * @param key
         * @param value
         * @param offset
         *            从指定位置开始覆写
         */
        public void setRange(String key, String value, long offset) {
            stringRedisTemplate.opsForValue().set(key, value, offset);
        }

        /**
         * 获取字符串的长度
         *
         * @param key
         * @return
         */
        public Long size(String key) {
            return stringRedisTemplate.opsForValue().size(key);
        }

        /**
         * 批量添加
         *
         * @param maps
         */
        public void multiSet(Map<String, String> maps) {
            stringRedisTemplate.opsForValue().multiSet(maps);
        }

        /**
         * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
         *
         * @param maps
         * @return 之前已经存在返回false,不存在返回true
         */
        public boolean multiSetIfAbsent(Map<String, String> maps) {
            return stringRedisTemplate.opsForValue().multiSetIfAbsent(maps);
        }

        /**
         * 增加(自增长), 负数则为自减
         *
         * @param key
         * @param increment
         * @return
         */
        public Long incrBy(String key, long increment) {
            return stringRedisTemplate.opsForValue().increment(key, increment);
        }

        /**
         *
         * @param key
         * @param increment
         * @return
         */
        public Double incrByFloat(String key, double increment) {
            return stringRedisTemplate.opsForValue().increment(key, increment);
        }

        /**
         * 追加到末尾
         *
         * @param key
         * @param value
         * @return
         */
        public Integer append(String key, String value) {
            return stringRedisTemplate.opsForValue().append(key, value);
        }

        /** -------------------hash相关操作------------------------- */

        /**
         * 获取存储在哈希表中指定字段的值
         *
         * @param key
         * @param field
         * @return
         */
        public Object hGet(String key, String field) {
            return stringRedisTemplate.opsForHash().get(key, field);
        }

        /**
         * 获取所有给定字段的值
         *
         * @param key
         * @return
         */
        public Map<Object, Object> hGetAll(String key) {
            return stringRedisTemplate.opsForHash().entries(key);
        }

        /**
         * 获取所有给定字段的值
         *
         * @param key
         * @param fields
         * @return
         */
        public List<Object> hMultiGet(String key, Collection<Object> fields) {
            return stringRedisTemplate.opsForHash().multiGet(key, fields);
        }

        public void hPut(String key, String hashKey, String value) {
            stringRedisTemplate.opsForHash().put(key, hashKey, value);
        }

        public void hPutAll(String key, Map<String, String> maps) {
            stringRedisTemplate.opsForHash().putAll(key, maps);
        }

        /**
         * 仅当hashKey不存在时才设置
         *
         * @param key
         * @param hashKey
         * @param value
         * @return
         */
        public Boolean hPutIfAbsent(String key, String hashKey, String value) {
            return stringRedisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
        }

        /**
         * 删除一个或多个哈希表字段
         *
         * @param key
         * @param fields
         * @return
         */
        public Long hDelete(String key, Object... fields) {
            return stringRedisTemplate.opsForHash().delete(key, fields);
        }

        /**
         * 查看哈希表 key 中，指定的字段是否存在
         *
         * @param key
         * @param field
         * @return
         */
        public boolean hExists(String key, String field) {
            return stringRedisTemplate.opsForHash().hasKey(key, field);
        }

        /**
         * 为哈希表 key 中的指定字段的整数值加上增量 increment
         *
         * @param key
         * @param field
         * @param increment
         * @return
         */
        public Long hIncrBy(String key, Object field, long increment) {
            return stringRedisTemplate.opsForHash().increment(key, field, increment);
        }

        /**
         * 为哈希表 key 中的指定字段的整数值加上增量 increment
         *
         * @param key
         * @param field
         * @param delta
         * @return
         */
        public Double hIncrByFloat(String key, Object field, double delta) {
            return stringRedisTemplate.opsForHash().increment(key, field, delta);
        }

        /**
         * 获取所有哈希表中的字段
         *
         * @param key
         * @return
         */
        public Set<Object> hKeys(String key) {
            return stringRedisTemplate.opsForHash().keys(key);
        }

        /**
         * 获取哈希表中字段的数量
         *
         * @param key
         * @return
         */
        public Long hSize(String key) {
            return stringRedisTemplate.opsForHash().size(key);
        }

        /**
         * 获取哈希表中所有值
         *
         * @param key
         * @return
         */
        public List<Object> hValues(String key) {
            return stringRedisTemplate.opsForHash().values(key);
        }

        /**
         * 迭代哈希表中的键值对
         *
         * @param key
         * @param options
         * @return
         */
        public Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
            return stringRedisTemplate.opsForHash().scan(key, options);
        }

        /** ------------------------list相关操作---------------------------- */

        /**
         * 通过索引获取列表中的元素
         *
         * @param key
         * @param index
         * @return
         */
        public String lIndex(String key, long index) {
            return stringRedisTemplate.opsForList().index(key, index);
        }

        /**
         * 获取列表指定范围内的元素
         *
         * @param key
         * @param start
         *            开始位置, 0是开始位置
         * @param end
         *            结束位置, -1返回所有
         * @return
         */
        public List<String> lRange(String key, long start, long end) {
            return stringRedisTemplate.opsForList().range(key, start, end);
        }

        /**
         * 存储在list头部
         *
         * @param key
         * @param value
         * @return
         */
        public Long lLeftPush(String key, String value) {
            return stringRedisTemplate.opsForList().leftPush(key, value);
        }

        /**
         *
         * @param key
         * @param value
         * @return
         */
        public Long lLeftPushAll(String key, String... value) {
            return stringRedisTemplate.opsForList().leftPushAll(key, value);
        }

        /**
         *
         * @param key
         * @param value
         * @return
         */
        public Long lLeftPushAll(String key, Collection<String> value) {
            return stringRedisTemplate.opsForList().leftPushAll(key, value);
        }

        /**
         * 当list存在的时候才加入
         *
         * @param key
         * @param value
         * @return
         */
        public Long lLeftPushIfPresent(String key, String value) {
            return stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
        }

        /**
         * 如果pivot存在,再pivot前面添加
         *
         * @param key
         * @param pivot
         * @param value
         * @return
         */
        public Long lLeftPush(String key, String pivot, String value) {
            return stringRedisTemplate.opsForList().leftPush(key, pivot, value);
        }

        /**
         *
         * @param key
         * @param value
         * @return
         */
        public Long lRightPush(String key, String value) {
            return stringRedisTemplate.opsForList().rightPush(key, value);
        }

        /**
         *
         * @param key
         * @param value
         * @return
         */
        public Long lRightPushAll(String key, String... value) {
            return stringRedisTemplate.opsForList().rightPushAll(key, value);
        }

        /**
         *
         * @param key
         * @param value
         * @return
         */
        public Long lRightPushAll(String key, Collection<String> value) {
            return stringRedisTemplate.opsForList().rightPushAll(key, value);
        }

        /**
         * 为已存在的列表添加值
         *
         * @param key
         * @param value
         * @return
         */
        public Long lRightPushIfPresent(String key, String value) {
            return stringRedisTemplate.opsForList().rightPushIfPresent(key, value);
        }

        /**
         * 在pivot元素的右边添加值
         *
         * @param key
         * @param pivot
         * @param value
         * @return
         */
        public Long lRightPush(String key, String pivot, String value) {
            return stringRedisTemplate.opsForList().rightPush(key, pivot, value);
        }

        /**
         * 通过索引设置列表元素的值
         *
         * @param key
         * @param index
         *            位置
         * @param value
         */
        public void lSet(String key, long index, String value) {
            stringRedisTemplate.opsForList().set(key, index, value);
        }

        /**
         * 移出并获取列表的第一个元素
         *
         * @param key
         * @return 删除的元素
         */
        public String lLeftPop(String key) {
            return stringRedisTemplate.opsForList().leftPop(key);
        }

        /**
         * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
         *
         * @param key
         * @param timeout
         *            等待时间
         * @param unit
         *            时间单位
         * @return
         */
        public String lBLeftPop(String key, long timeout, TimeUnit unit) {
            return stringRedisTemplate.opsForList().leftPop(key, timeout, unit);
        }

        /**
         * 移除并获取列表最后一个元素
         *
         * @param key
         * @return 删除的元素
         */
        public String lRightPop(String key) {
            return stringRedisTemplate.opsForList().rightPop(key);
        }

        /**
         * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
         *
         * @param key
         * @param timeout
         *            等待时间
         * @param unit
         *            时间单位
         * @return
         */
        public String lBRightPop(String key, long timeout, TimeUnit unit) {
            return stringRedisTemplate.opsForList().rightPop(key, timeout, unit);
        }

        /**
         * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
         *
         * @param sourceKey
         * @param destinationKey
         * @return
         */
        public String lRightPopAndLeftPush(String sourceKey, String destinationKey) {
            return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                    destinationKey);
        }

        /**
         * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
         *
         * @param sourceKey
         * @param destinationKey
         * @param timeout
         * @param unit
         * @return
         */
        public String lBRightPopAndLeftPush(String sourceKey, String destinationKey,
                                            long timeout, TimeUnit unit) {
            return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey,
                    destinationKey, timeout, unit);
        }

        /**
         * 删除集合中值等于value得元素
         *
         * @param key
         * @param index
         *            index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
         *            index<0, 从尾部开始删除第一个值等于value的元素;
         * @param value
         * @return
         */
        public Long lRemove(String key, long index, String value) {
            return stringRedisTemplate.opsForList().remove(key, index, value);
        }

        /**
         * 裁剪list
         *
         * @param key
         * @param start
         * @param end
         */
        public void lTrim(String key, long start, long end) {
            stringRedisTemplate.opsForList().trim(key, start, end);
        }

        /**
         * 获取列表长度
         *
         * @param key
         * @return
         */
        public Long lLen(String key) {
            return stringRedisTemplate.opsForList().size(key);
        }

        /** --------------------set相关操作-------------------------- */

        /**
         * set添加元素
         *
         * @param key
         * @param values
         * @return
         */
        public Long sAdd(String key, String... values) {
            return stringRedisTemplate.opsForSet().add(key, values);
        }

        /**
         * set移除元素
         *
         * @param key
         * @param values
         * @return
         */
        public Long sRemove(String key, Object... values) {
            return stringRedisTemplate.opsForSet().remove(key, values);
        }

        /**
         * 移除并返回集合的一个随机元素
         *
         * @param key
         * @return
         */
        public String sPop(String key) {
            return stringRedisTemplate.opsForSet().pop(key);
        }

        /**
         * 将元素value从一个集合移到另一个集合
         *
         * @param key
         * @param value
         * @param destKey
         * @return
         */
        public Boolean sMove(String key, String value, String destKey) {
            return stringRedisTemplate.opsForSet().move(key, value, destKey);
        }

        /**
         * 获取集合的大小
         *
         * @param key
         * @return
         */
        public Long sSize(String key) {
            return stringRedisTemplate.opsForSet().size(key);
        }

        /**
         * 判断集合是否包含value
         *
         * @param key
         * @param value
         * @return
         */
        public Boolean sIsMember(String key, Object value) {
            return stringRedisTemplate.opsForSet().isMember(key, value);
        }

        /**
         * 获取两个集合的交集
         *
         * @param key
         * @param otherKey
         * @return
         */
        public Set<String> sIntersect(String key, String otherKey) {
            return stringRedisTemplate.opsForSet().intersect(key, otherKey);
        }

        /**
         * 获取key集合与多个集合的交集
         *
         * @param key
         * @param otherKeys
         * @return
         */
        public Set<String> sIntersect(String key, Collection<String> otherKeys) {
            return stringRedisTemplate.opsForSet().intersect(key, otherKeys);
        }

        /**
         * key集合与otherKey集合的交集存储到destKey集合中
         *
         * @param key
         * @param otherKey
         * @param destKey
         * @return
         */
        public Long sIntersectAndStore(String key, String otherKey, String destKey) {
            return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKey,
                    destKey);
        }

        /**
         * key集合与多个集合的交集存储到destKey集合中
         *
         * @param key
         * @param otherKeys
         * @param destKey
         * @return
         */
        public Long sIntersectAndStore(String key, Collection<String> otherKeys,
                                       String destKey) {
            return stringRedisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                    destKey);
        }

        /**
         * 获取两个集合的并集
         *
         * @param key
         * @param otherKeys
         * @return
         */
        public Set<String> sUnion(String key, String otherKeys) {
            return stringRedisTemplate.opsForSet().union(key, otherKeys);
        }

        /**
         * 获取key集合与多个集合的并集
         *
         * @param key
         * @param otherKeys
         * @return
         */
        public Set<String> sUnion(String key, Collection<String> otherKeys) {
            return stringRedisTemplate.opsForSet().union(key, otherKeys);
        }

        /**
         * key集合与otherKey集合的并集存储到destKey中
         *
         * @param key
         * @param otherKey
         * @param destKey
         * @return
         */
        public Long sUnionAndStore(String key, String otherKey, String destKey) {
            return stringRedisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
        }

        /**
         * key集合与多个集合的并集存储到destKey中
         *
         * @param key
         * @param otherKeys
         * @param destKey
         * @return
         */
        public Long sUnionAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
            return stringRedisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
        }

        /**
         * 获取两个集合的差集
         *
         * @param key
         * @param otherKey
         * @return
         */
        public Set<String> sDifference(String key, String otherKey) {
            return stringRedisTemplate.opsForSet().difference(key, otherKey);
        }

        /**
         * 获取key集合与多个集合的差集
         *
         * @param key
         * @param otherKeys
         * @return
         */
        public Set<String> sDifference(String key, Collection<String> otherKeys) {
            return stringRedisTemplate.opsForSet().difference(key, otherKeys);
        }

        /**
         * key集合与otherKey集合的差集存储到destKey中
         *
         * @param key
         * @param otherKey
         * @param destKey
         * @return
         */
        public Long sDifference(String key, String otherKey, String destKey) {
            return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKey,
                    destKey);
        }

        /**
         * key集合与多个集合的差集存储到destKey中
         *
         * @param key
         * @param otherKeys
         * @param destKey
         * @return
         */
        public Long sDifference(String key, Collection<String> otherKeys,
                                String destKey) {
            return stringRedisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                    destKey);
        }

        /**
         * 获取集合所有元素
         *
         * @param key
         * @return
         */
        public Set<String> setMembers(String key) {
            return stringRedisTemplate.opsForSet().members(key);
        }

        /**
         * 随机获取集合中的一个元素
         *
         * @param key
         * @return
         */
        public String sRandomMember(String key) {
            return stringRedisTemplate.opsForSet().randomMember(key);
        }

        /**
         * 随机获取集合中count个元素
         *
         * @param key
         * @param count
         * @return
         */
        public List<String> sRandomMembers(String key, long count) {
            return stringRedisTemplate.opsForSet().randomMembers(key, count);
        }

        /**
         * 随机获取集合中count个元素并且去除重复的
         *
         * @param key
         * @param count
         * @return
         */
        public Set<String> sDistinctRandomMembers(String key, long count) {
            return stringRedisTemplate.opsForSet().distinctRandomMembers(key, count);
        }

        /**
         *
         * @param key
         * @param options
         * @return
         */
        public Cursor<String> sScan(String key, ScanOptions options) {
            return stringRedisTemplate.opsForSet().scan(key, options);
        }

        /**------------------zSet相关操作--------------------------------*/

        /**
         * 添加元素,有序集合是按照元素的score值由小到大排列
         *
         * @param key
         * @param value
         * @param score
         * @return
         */
        public Boolean zAdd(String key, String value, double score) {
            return stringRedisTemplate.opsForZSet().add(key, value, score);
        }

        /**
         *
         * @param key
         * @param values
         * @return
         */
        public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> values) {
            return stringRedisTemplate.opsForZSet().add(key, values);
        }

        /**
         *
         * @param key
         * @param values
         * @return
         */
        public Long zRemove(String key, Object... values) {
            return stringRedisTemplate.opsForZSet().remove(key, values);
        }

        /**
         * 增加元素的score值，并返回增加后的值
         *
         * @param key
         * @param value
         * @param delta
         * @return
         */
        public Double zIncrementScore(String key, String value, double delta) {
            return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
        }

        /**
         * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
         *
         * @param key
         * @param value
         * @return 0表示第一位
         */
        public Long zRank(String key, Object value) {
            return stringRedisTemplate.opsForZSet().rank(key, value);
        }

        /**
         * 返回元素在集合的排名,按元素的score值由大到小排列
         *
         * @param key
         * @param value
         * @return
         */
        public Long zReverseRank(String key, Object value) {
            return stringRedisTemplate.opsForZSet().reverseRank(key, value);
        }

        /**
         * 获取集合的元素, 从小到大排序
         *
         * @param key
         * @param start
         *            开始位置
         * @param end
         *            结束位置, -1查询所有
         * @return
         */
        public Set<String> zRange(String key, long start, long end) {
            return stringRedisTemplate.opsForZSet().range(key, start, end);
        }

        /**
         * 获取集合元素, 并且把score值也获取
         *
         * @param key
         * @param start
         * @param end
         * @return
         */
        public Set<ZSetOperations.TypedTuple<String>> zRangeWithScores(String key, long start,
                                                                       long end) {
            return stringRedisTemplate.opsForZSet().rangeWithScores(key, start, end);
        }

        /**
         * 根据Score值查询集合元素
         *
         * @param key
         * @param min
         *            最小值
         * @param max
         *            最大值
         * @return
         */
        public Set<String> zRangeByScore(String key, double min, double max) {
            return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
        }

        /**
         * 根据Score值查询集合元素, 从小到大排序
         *
         * @param key
         * @param min
         *            最小值
         * @param max
         *            最大值
         * @return
         */
        public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                              double min, double max) {
            return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        }

        /**
         *
         * @param key
         * @param min
         * @param max
         * @param start
         * @param end
         * @return
         */
        public Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key,
                                                                              double min, double max, long start, long end) {
            return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max,
                    start, end);
        }

        /**
         * 获取集合的元素, 从大到小排序
         *
         * @param key
         * @param start
         * @param end
         * @return
         */
        public Set<String> zReverseRange(String key, long start, long end) {
            return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
        }

        /**
         * 获取集合的元素, 从大到小排序, 并返回score值
         *
         * @param key
         * @param start
         * @param end
         * @return
         */
        public Set<TypedTuple<String>> zReverseRangeWithScores(String key,
                                                               long start, long end) {
            return stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, start,
                    end);
        }

        /**
         * 根据Score值查询集合元素, 从大到小排序
         *
         * @param key
         * @param min
         * @param max
         * @return
         */
        public Set<String> zReverseRangeByScore(String key, double min,
                                                double max) {
            return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        }

        /**
         * 根据Score值查询集合元素, 从大到小排序
         *
         * @param key
         * @param min
         * @param max
         * @return
         */
        public Set<TypedTuple<String>> zReverseRangeByScoreWithScores(
                String key, double min, double max) {
            return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,
                    min, max);
        }

        /**
         *
         * @param key
         * @param min
         * @param max
         * @param start
         * @param end
         * @return
         */
        public Set<String> zReverseRangeByScore(String key, double min,
                                                double max, long start, long end) {
            return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max,
                    start, end);
        }

        /**
         * 根据score值获取集合元素数量
         *
         * @param key
         * @param min
         * @param max
         * @return
         */
        public Long zCount(String key, double min, double max) {
            return stringRedisTemplate.opsForZSet().count(key, min, max);
        }

        /**
         * 获取集合大小
         *
         * @param key
         * @return
         */
        public Long zSize(String key) {
            return stringRedisTemplate.opsForZSet().size(key);
        }

        /**
         * 获取集合大小
         *
         * @param key
         * @return
         */
        public Long zZCard(String key) {
            return stringRedisTemplate.opsForZSet().zCard(key);
        }

        /**
         * 获取集合中value元素的score值
         *
         * @param key
         * @param value
         * @return
         */
        public Double zScore(String key, Object value) {
            return stringRedisTemplate.opsForZSet().score(key, value);
        }

        /**
         * 移除指定索引位置的成员
         *
         * @param key
         * @param start
         * @param end
         * @return
         */
        public Long zRemoveRange(String key, long start, long end) {
            return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
        }

        /**
         * 根据指定的score值的范围来移除成员
         *
         * @param key
         * @param min
         * @param max
         * @return
         */
        public Long zRemoveRangeByScore(String key, double min, double max) {
            return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        }

        /**
         * 获取key和otherKey的并集并存储在destKey中
         *
         * @param key
         * @param otherKey
         * @param destKey
         * @return
         */
        public Long zUnionAndStore(String key, String otherKey, String destKey) {
            return stringRedisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
        }

        /**
         *
         * @param key
         * @param otherKeys
         * @param destKey
         * @return
         */
        public Long zUnionAndStore(String key, Collection<String> otherKeys,
                                   String destKey) {
            return stringRedisTemplate.opsForZSet()
                    .unionAndStore(key, otherKeys, destKey);
        }

        /**
         * 交集
         *
         * @param key
         * @param otherKey
         * @param destKey
         * @return
         */
        public Long zIntersectAndStore(String key, String otherKey,
                                       String destKey) {
            return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKey,
                    destKey);
        }

        /**
         * 交集
         *
         * @param key
         * @param otherKeys
         * @param destKey
         * @return
         */
        public Long zIntersectAndStore(String key, Collection<String> otherKeys,
                                       String destKey) {
            return stringRedisTemplate.opsForZSet().intersectAndStore(key, otherKeys,
                    destKey);
        }

        /**
         *
         * @param key
         * @param options
         * @return
         */
        public Cursor<TypedTuple<String>> zScan(String key, ScanOptions options) {
            return stringRedisTemplate.opsForZSet().scan(key, options);
        }

}
