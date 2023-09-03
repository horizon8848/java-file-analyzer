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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import jtool.analyzer.calculate.workgroup.AbstractCalculateWorkerGroup;
import jtool.analyzer.calculate.workgroup.StatisticsPipelineManager;
import jtool.analyzer.report.entity.ClassMethodNodeEntity;
import jtool.analyzer.report.statistics.dto.SqlQueryDTO;
import jtool.analyzer.report.statistics.handler.MethodStatisticsHandler;
import jtool.analyzer.report.statistics.handler.SqlHandler;

/**
 * 方法统计工作组
 * 
 * @author cjt
 * @date   Aug 28, 2023 10:26:46 AM
 */
public class MethodStatisticsWorkerGroup extends AbstractCalculateWorkerGroup<SqlQueryDTO, Map<String, Integer>> {

	/**
	 * 目录结构索引
	 */
	private Map<String, Map<String, ClassMethodNodeEntity>> index;
	
	private MysqlConnectionPool pool;
	private String statisticsTable;
	
	private static SqlHandler sqlHandler = new SqlHandler();
	private static MethodStatisticsHandler methodStatisticsHandler = new MethodStatisticsHandler();
	
	public MethodStatisticsWorkerGroup(Map<String, Map<String, ClassMethodNodeEntity>> index) {
		super(sqlHandler, methodStatisticsHandler);
		this.index = index;
	}
	
	public MethodStatisticsWorkerGroup(Map<String, Map<String, ClassMethodNodeEntity>> index, int workerNum) {
		super(workerNum, sqlHandler, methodStatisticsHandler);
		this.index = index;
	}
	
	/**
	 * 设置数据库连接池
	 * @param pool
	 */
	public void setMysqlConnectionPool(MysqlConnectionPool pool) {
		this.pool = pool;
	}
	
	public void setStatisticsTable(String statisticsTable) {
		this.statisticsTable = statisticsTable;
	}

	@Override
	public void assignment(StatisticsPipelineManager<SqlQueryDTO, Map<String, Integer>> manager) {
		String sql = "select count(0) from  " + statisticsTable;
		Integer count = pool.query(sql, new QueryResultWrapper<Integer>() {

			@Override
			public Integer wrap(ResultSet resultSet) {
				try {
					if(resultSet.next()) {
						return resultSet.getInt(1);						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return 0;
			}
			
		});
		int rows = 1000;
		if(count > 0) {
			int pages = count / rows;
			if(count % rows > 0) {
				pages++;
			}
			for(int i = 0; i < pages; i++) {
				SqlQueryDTO sqlQueryDTO = new SqlQueryDTO();
				sqlQueryDTO.setPool(pool);
				sqlQueryDTO.setStatisticsTable(statisticsTable);
				sqlQueryDTO.setOffset(i * rows);
				sqlQueryDTO.setRows(rows);
				boolean createResult = manager.createPipeline(sqlQueryDTO);
				if(!createResult) {
					// 阻塞，直到成功为止
					while(true) {
						try {
							Thread.sleep(2000);
							boolean createAgain = manager.createPipeline(sqlQueryDTO);
							if(createAgain) {
								break;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public void resultProcess(Map<String, Integer> data) {
		for(Map.Entry<String, Integer> entry : data.entrySet()) {
			// 根据方法名全程查询索引
			String[] arrs = entry.getKey().split("#");
			if(!index.containsKey(arrs[0])) {
				continue;
			}
			Map<String, ClassMethodNodeEntity> secondIndex = index.get(arrs[0]);
			String secondKey = arrs[1] + "#" + arrs[2];
			if(!secondIndex.containsKey(secondKey)) {
				continue;
			}
			secondIndex.get(secondKey).addTimes(entry.getValue());
		}
	}
}
