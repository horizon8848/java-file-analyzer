/**
 * Copyright cjt(tris73768u@163.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jtool.analyzer.report.statistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Mysql连接池（示例）
 * 
 * @author cjt
 * @date   Aug 29, 2023 2:06:20 PM
 */
public final class MysqlConnectionPool {

	private static final int SIZE = 10;
	private String url;
	private String user;
	private String password;
	private BlockingQueue<Connection> pool = new ArrayBlockingQueue<>(SIZE);

	static {
		try {
			// 加载驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public MysqlConnectionPool(String url, String user, String password) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		
		// 创建5个连接对象
		int i = 0;
		while(i < SIZE) {
			try {
				pool.add(initConnection());
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				i++;				
			}
		}
	}

	/**
	 * 初始化连接
	 * @return
	 * @throws SQLException
	 */
	private Connection initConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
	/**
	 * 查询
	 * @param <T>     返回对象定义
	 * @param sql     sql语句
	 * @param suplier 实现逻辑
	 * @return
	 */
	public <T> T query(String sql, QueryResultWrapper<T> wrapper) {
		Connection connection;
		try {
			connection = pool.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		try(Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);) {
			return wrapper.wrap(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			pool.add(connection);
		}
	}
}
