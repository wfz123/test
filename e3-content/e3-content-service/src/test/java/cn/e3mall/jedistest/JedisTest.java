package cn.e3mall.jedistest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 测试java客户端Jedis操作Redis
 * <p>Title: JedisTest</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
public class JedisTest {
	@Test
	//测试jedis
	public void testJedis() {
		//创建一个Jedis对象,需要两个参数,一个是端口号,一个是ip地址
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		jedis.set("test1", "test1");
		System.out.println(jedis.get("test1"));
		//关闭连接
		jedis.close();
	}
	
	//通过JedisPool操作Redis
	@Test
	public void testJedisPool() throws Exception {
		//创建一个JedisPool对象,也需要传递ip地址和端口号
		JedisPool jedisPool = new JedisPool("192.168.25.128",6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("test2", "test2");
		System.out.println(jedis.get("test2"));
		//关闭连接
		jedis.close();
		jedisPool.close();
	}
	@Test
	public void testJedisCluster() throws Exception {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.129", 7001));
		nodes.add(new HostAndPort("192.168.25.129", 7002));
		nodes.add(new HostAndPort("192.168.25.129", 7003));
		nodes.add(new HostAndPort("192.168.25.129", 7004));
		nodes.add(new HostAndPort("192.168.25.129", 7005));
		nodes.add(new HostAndPort("192.168.25.129", 7006));
		//创建一个JedisCluster对象，只需要一个nodes参数
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("test3", "test3");
		System.out.println(jedisCluster.get("test3"));
		jedisCluster.close();
	}
}
