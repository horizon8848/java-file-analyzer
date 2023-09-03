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
package jtool.analyzer.report.entity;

import java.util.List;

/**
 * 项目根节点
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:01:50 AM
 */
public class ProjectRootEntity {

	/**
	 * 根目录名字
	 */
	private String rootName;
	
	/**
	 * 子节点
	 */
	private List<? super ChildNodeBaseEntity> children;

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public List<? super ChildNodeBaseEntity> getChildren() {
		return children;
	}

	public void setChildren(List<? super ChildNodeBaseEntity> children) {
		this.children = children;
	}
}
