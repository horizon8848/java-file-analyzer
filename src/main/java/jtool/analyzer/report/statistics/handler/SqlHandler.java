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
package jtool.analyzer.report.statistics.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jtool.analyzer.calculate.AbstratHandler;
import jtool.analyzer.report.statistics.QueryResultWrapper;
import jtool.analyzer.report.statistics.dto.SqlQueryDTO;
import jtool.analyzer.report.statistics.dto.StatisticsSourceDataDTO;

/**
 * <strong>数据库处理</strong><br>
 * 数据表格式：<br>
 * field - |class_name|method_name|param_list|<br>
 * row   - |jtool.analyzer.report.statistics.handler.SqlHandler|execute|java.lang.Object|<br>
 * row   - |jtool.analyzer.report.statistics.handler.MethodStatisticsHandler|execute|java.lang.Object|
 * 
 * @author cjt
 * @date Aug 25, 2023 1:47:41 PM
 */
public final class SqlHandler extends AbstratHandler {

	@Override
	public Object execute(Object input) {
		SqlQueryDTO condition = (SqlQueryDTO) input;
		// 数据库查询示例
		String sql = String.format("select * from %s limit %s, %s", condition.getStatisticsTable(),
				condition.getOffset(), condition.getRows());
		List<StatisticsSourceDataDTO> dataList = condition.getPool().query(sql,
				new QueryResultWrapper<List<StatisticsSourceDataDTO>>() {

					@Override
					public List<StatisticsSourceDataDTO> wrap(ResultSet resultSet) {
						try {
							List<StatisticsSourceDataDTO> dataList = new ArrayList<>(resultSet.getRow());
							while (resultSet.next()) {
								StatisticsSourceDataDTO data = new StatisticsSourceDataDTO();
								data.setClassName(resultSet.getString("class_name"));
								data.setMethodName(resultSet.getString("method_name"));
								data.setParamList(resultSet.getString("param_list"));
								dataList.add(data);
							}
							return dataList;
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return Collections.emptyList();
					}

				});
		return dataList;
	}

}
