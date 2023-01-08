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

package com.alibaba.cloud.sentinel.datasource.config;

import java.time.Duration;
import java.util.List;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.sentinel.datasource.factorybean.RedisDataSourceFactoryBean;


/**
 * Redis Properties class Using by {@link DataSourcePropertiesConfiguration} and
 * {@link RedisDataSourceFactoryBean}.
 *
 * @author <a href="mailto:wangiegie@gmail.com">lengleng</a>
 */
public class RedisDataSourceProperties extends AbstractDataSourceProperties {

	public RedisDataSourceProperties() {
		super(RedisDataSourceFactoryBean.class.getName());
	}

	/**
	 * redis server host.
	 */
	private String host = "localhost";

	/**
	 * redis server port.
	 */
	private int port = 6379;

	/**
	 * redis server password.
	 */
	private String password;

	/**
	 * redis server default select database.
	 */
	private int database;

	/**
	 * redis server timeout.
	 */
	private Duration timeout;


	/**
	 * data key in Redis.
	 */
	private String ruleKey;

	/**
	 * channel to subscribe in Redis.
	 */
	private String channel;


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
	public void preCheck(String dataSourceName) {
		super.preCheck(dataSourceName);
		if (StringUtils.isEmpty(ruleKey)) {
			throw new IllegalArgumentException(
					"RedisDataSource  ruleKey can not be empty");
		}

		if (StringUtils.isEmpty(channel)) {
			throw new IllegalArgumentException(
					"RedisDataSource  channel can not be empty");
		}

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
