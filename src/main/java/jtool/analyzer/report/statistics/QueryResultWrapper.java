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

/**
 * 查询结果转换
 * @param <T>
 * @author cjt
 * @date   Aug 29, 2023 2:47:55 PM
 */
public abstract class QueryResultWrapper<T> {

	/**
	 * 数据转换
	 * @param resultSet
	 * @return
	 */
	public abstract T wrap(ResultSet resultSet);
}
