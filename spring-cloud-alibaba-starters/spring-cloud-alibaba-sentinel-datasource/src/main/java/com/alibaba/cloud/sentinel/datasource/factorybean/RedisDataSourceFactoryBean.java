/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.sentinel.datasource.factorybean;

import java.time.Duration;
import java.util.List;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.redis.RedisDataSource;
import com.alibaba.csp.sentinel.datasource.redis.config.RedisConnectionConfig;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * A {@link FactoryBean} for creating {@link RedisDataSource} instance.
 *
 * @author <a href="mailto:wangiegie@gmail.com">lengleng</a>
 * @see RedisDataSource
 */
public class RedisDataSourceFactoryBean implements FactoryBean<RedisDataSource> {
	private String host;

	private int port;

	private int database;

	private Duration timeout;


	private Converter converter;

	/**
	 * data key in Redis.
	 */
	private String ruleKey;

	/**
	 * channel to subscribe in Redis.
	 */
	private String channel;

	/**
	 * redis server password.
	 */
	private String password;


	private Sentinel sentinel;

	private Cluster cluster;


	/**
	 * Cluster properties.
	 */
	public static class Cluster {

		/**
		 * Comma-separated list of "host:port" pairs to bootstrap from. This represents an
		 * "initial" list of cluster nodes and is required to have at least one entry.
		 */
		private List<String> nodes;

		/**
		 * Maximum number of redirects to follow when executing commands across the
		 * cluster.
		 */
		private Integer maxRedirects;

		public List<String> getNodes() {
			return this.nodes;
		}

		public void setNodes(List<String> nodes) {
			this.nodes = nodes;
		}

		public Integer getMaxRedirects() {
			return this.maxRedirects;
		}

		public void setMaxRedirects(Integer maxRedirects) {
			this.maxRedirects = maxRedirects;
		}

	}

	/**
	 * Redis sentinel properties.
	 */
	public static class Sentinel {

		/**
		 * Name of the Redis server.
		 */
		private String master;

		/**
		 * Comma-separated list of "host:port" pairs.
		 */
		private List<String> nodes;

		/**
		 * Password for authenticating with sentinel(s).
		 */
		private String password;

		public String getMaster() {
			return this.master;
		}

		public void setMaster(String master) {
			this.master = master;
		}

		public List<String> getNodes() {
			return this.nodes;
		}

		public void setNodes(List<String> nodes) {
			this.nodes = nodes;
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

	@Override
	public RedisDataSource getObject() {
		RedisConnectionConfig.Builder builder = RedisConnectionConfig.builder();
		if(sentinel==null && cluster==null){
			builder.withHost(host).withPort(port).withDatabase(database);
		}else if (sentinel!=null&&cluster==null){
			sentinel.getNodes().forEach(node -> {
				try {
					String[] parts = StringUtils.split(node, ":");
					Assert.state(parts.length == 2, "Must be defined as 'host:port'");
					builder.withRedisSentinel(parts[0], Integer.parseInt(parts[1]));
				}
				catch (RuntimeException ex) {
					throw new IllegalStateException(
							"Invalid redis sentinel property " + node, ex);
				}
			});
			builder.withSentinelMasterId(sentinel.getMaster());
		}else if (sentinel == null){
			cluster.getNodes().forEach(node -> {
				try {
					String[] parts = StringUtils.split(node, ":");
					Assert.state(parts.length == 2, "Must be defined as 'host:port'");
					builder.withRedisCluster(parts[0], Integer.parseInt(parts[1]));
				}
				catch (RuntimeException ex) {
					throw new IllegalStateException(
							"Invalid redis Cluster property " + node, ex);
				}
			});
		}else {
			throw new IllegalStateException(
					"Invalid redis sentinel property ");
		}
		if (timeout != null) {
			builder.withTimeout(timeout.toMillis());
		}

		if (StringUtils.hasText(password)) {
			builder.withPassword(password);
		}

		return new RedisDataSource<List<FlowRule>>(builder.build(), ruleKey, channel,
				converter);
	}

	@Override
	public Class<?> getObjectType() {
		return RedisDataSource.class;
	}

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public Duration getTimeout() {
		return timeout;
	}

	public void setTimeout(Duration timeout) {
		this.timeout = timeout;
	}

	public Sentinel getSentinel() {
		return sentinel;
	}

	public void setSentinel(Sentinel sentinel) {
		this.sentinel = sentinel;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
}
