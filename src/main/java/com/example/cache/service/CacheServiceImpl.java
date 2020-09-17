package com.example.cache.service;

import com.example.cache.dao.UserMapper;
import com.example.cache.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Chris
 * @description
 * @date 2020/9/17 15:09
 */
@Service
//统一配置本类的缓存注解的属性，在类上面统一定义缓存的名字，方法上面就不用标注了，当标记在一个类上时则表示该类所有的方法都是支持缓存的
@CacheConfig(cacheNames = {"user"})
public class CacheServiceImpl implements CacheService {

    @Autowired
    UserMapper userMapper;


    /**
     * 将方法的运行结果进行缓存：以后再要相同的数据，直接从缓存中获取，不用调用方法
     * CacheManager管理多个Cache组件的，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一一个名字；
     * 几个属性：
     * cacheNames/value:指定缓存组件的名字；
     * key缓存数据时使用的key，可以用它来指定，默认是使用方法参数的值 eg:1-方法的返回值
     * 可编写SpEL表达式,eg:
     * #id表示参数id的值 相当于#a0 #p0 #root.args[0]
     * keyGenerator:key的生成器；可以自己指定key的生成器的组件id
     * keyGenerator/key:二选一，用其中一个就行
     * cacheManager:指定缓存管理器，或者指定缓存解析器cacheResolver
     * condition:指定符合条件的情况下才缓存
     * unless:否定缓存，当unless指定的条件为true,方法的返回值就不会被缓存；可以获取到结果集进行判断
     * eg:unless="#result==null"
     * sync:是否使用异步模式
     *
     * @param id
     * @return
     */
    @Cacheable(key = "#p0")
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    //按条件更新缓存

    /**
     * @CachePut：主要针对方法配置，能够根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用 。简单来说就是用户更新缓存数据。
     * 但需要注意的是该注解的value 和 key 必须与要更新的缓存相同，也就是与@Cacheable 相同
     * @param id
     * @return
     */
    @CachePut(key = "#p0")
    @Override
    public User update(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }


    //清除一条缓存，key为要清空的数据

    /**
     *
     * @CacheEvict：配置于函数上，通常用在删除方法上，用来从缓存中移除相应数据。除了同@Cacheable一样的参数之外，它还有下面两个参数：
     *
     * allEntries：非必需，默认为false。当为true时，会移除所有数据。如：@CachEvict(value=”testcache”,allEntries=true)
     * beforeInvocation：非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据。 如：@CachEvict(value=”testcache”，beforeInvocation=true)
     *
     * @param id
     * @return
     */
    @CacheEvict(key="#id")
    @Override
    public int delete(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }


//    //方法调用后清空所有缓存
//    @CacheEvict(allEntries=true)
//    public void delectAll() {
//        userMapper.deleteAll();
//    }
//
//    //方法调用前清空所有缓存
//    @CacheEvict(beforeInvocation=true)
//    public void delectAll() {
//        userMapper.deleteAll();
//    }





}
