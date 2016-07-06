package com.seojey.domain.db.hbase.connection;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by coupang on 2015. 9. 1..
 */
public class ConnectionPoolFactory implements BaseConnectionFactory {
    private static final int DEFAULT_NUM_OPERATIONS = 500000;
	private Configuration hbaseConf;
	private Connection connectionPool;

    private ExecutorService internalPool;

    private final int threads;

	public ConnectionPoolFactory(Configuration hbaseConf) {
		this.hbaseConf = hbaseConf;

        this.threads = Runtime.getRuntime().availableProcessors() * 4;

        // Daemon threads are great for things that get shut down.
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setDaemon(true).setNameFormat("internal-pol-%d").build();


        this.internalPool = Executors.newFixedThreadPool(threads, threadFactory);
	}

	protected Configuration createConfiguration() {
		return HBaseConfiguration.create();
		//		return applicationContext.getBean(RecommendationDomainApplicationContextConfig.class).getHbaseConfigurationFactoryBean().getObject();
	}

	protected Connection createHConnectionPool() throws IOException {
		if (internalPool != null) {
            internalPool = createExecutorService();
		}
        return ConnectionFactory.createConnection(hbaseConf, internalPool);
	}

	private ExecutorService createExecutorService() {
		return Executors.newFixedThreadPool(threads * 2);
	}

	/**
	 * 기존 0.94에서 ZooKeeperConnectionException 대신 IOException 으로 변경됨
	 * @return HConnection ; connection
	 * @throws java.io.IOException
	 */
	public synchronized Connection getConnection() throws IOException {
		if (hbaseConf == null) {
			hbaseConf = createConfiguration();
		}

		if (connectionPool == null || connectionPool.isClosed()) {
			connectionPool = createHConnectionPool();
		}

		return connectionPool;
	}

	public Connection getInstance() throws IOException {
		return getConnection();
	}

	@Override
	public Table getTable(String tableName) throws IOException {
        Connection hConn = getInstance();
		return hConn.getTable(TableName.valueOf(tableName));
	}

    @Override
    public void close(Table table) throws IOException {
        table.close();
    }


}
