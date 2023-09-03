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
package jtool.analyzer.report.statistics.dto;

import jtool.analyzer.report.statistics.MysqlConnectionPool;

public class SqlQueryDTO {

	/**
	 * 数据库连接池
	 */
	private MysqlConnectionPool pool;
	
	/**
	 * 统计表
	 */
	private String statisticsTable;
	
	/**
	 * 偏移量
	 */
	private int offset;
	
	/**
	 * 行数
	 */
	private int rows;

	public MysqlConnectionPool getPool() {
		return pool;
	}

	public void setPool(MysqlConnectionPool pool) {
		this.pool = pool;
	}

	public String getStatisticsTable() {
		return statisticsTable;
	}

	public void setStatisticsTable(String statisticsTable) {
		this.statisticsTable = statisticsTable;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "SqlQueryDTO [offset=" + offset + ", rows=" + rows + "]";
	}
}
