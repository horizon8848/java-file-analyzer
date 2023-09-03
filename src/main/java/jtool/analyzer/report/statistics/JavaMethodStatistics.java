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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jtool.analyzer.report.entity.ChildNodeBaseEntity;
import jtool.analyzer.report.entity.ClassMethodNodeEntity;

/**
 * 方法调用统计(供参考)
 * 
 * @author cjt
 * @date   Aug 24, 2023 11:08:01 AM
 */
public class JavaMethodStatistics {

	private MysqlConnectionPool pool;
	private String statisticsTable;
	
	private List<? super ChildNodeBaseEntity> nodes;
	
	public JavaMethodStatistics(List<? super ChildNodeBaseEntity> nodes, MysqlConnectionPool pool, String statisticsTable) {
		this.nodes = nodes;
		this.pool = pool;
		this.statisticsTable = statisticsTable;
	}
	
	/**
	 * 执行统计
	 * @return
	 */
	public JavaMethodStatistics execute() {
		Map<String, Map<String, ClassMethodNodeEntity>> index = this.convertReferenceIndex();
		MethodStatisticsWorkerGroup workerGroup = new MethodStatisticsWorkerGroup(index);
		workerGroup.setMysqlConnectionPool(pool);
		workerGroup.setStatisticsTable(statisticsTable);
		workerGroup.start();
		return this;
	}

	public List<? super ChildNodeBaseEntity> getNodes() {
		return nodes;
	}

	/**
	 * <p>转换成二级引用索引</p>
	 * 说明：这里定义为Map<包名, Map<类加方法名, 引用类>>，也可以转换为Map<类名, Map<类下的方法, 引用类>>，处理方式和索引定义自行决定
	 * @param nodes 项目结构
	 * @return
	 */
	private Map<String, Map<String, ClassMethodNodeEntity>> convertReferenceIndex() {
		Map<String, Map<String, ClassMethodNodeEntity>> index = new HashMap<>(16);
		// 遍历找到有方法的节点
		this.createIndex(nodes, index);
		return index;
	}

	private void createIndex(List<? super ChildNodeBaseEntity> nodes,
			Map<String, Map<String, ClassMethodNodeEntity>> index) {
		for (Object node : nodes) {
			if (node instanceof ClassMethodNodeEntity m) {
				Map<String, ClassMethodNodeEntity> secondIndex;
				if (index.containsKey(m.getPackagePath())) {
					secondIndex = index.get(m.getPackagePath());
				} else {
					secondIndex = new HashMap<>(16);
					index.put(m.getPackagePath(), secondIndex);
				}
				secondIndex.put(String.format("%s#%s", m.getClassName(), m.getUniqueMethod()), m);
			} else if (node instanceof ChildNodeBaseEntity b) {
				this.createIndex(b.getChildren(), index);
			}
		}
	}
}
