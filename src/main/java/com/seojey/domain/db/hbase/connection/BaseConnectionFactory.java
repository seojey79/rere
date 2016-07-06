package com.seojey.domain.db.hbase.connection;

/**
 * Created by coupang on 2015. 8. 27..
 */


import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/***
 * HBase connection factory.
 */
public interface BaseConnectionFactory {
	/***
	 * Implementation should return {@link org.apache.hadoop.hbase.client.HConnection}
	 * @return HConnection
	 * @throws java.io.IOException
	 */
	Connection getInstance() throws IOException;

    Table getTable(String tableName) throws IOException;

	void close(Table table) throws IOException;

}