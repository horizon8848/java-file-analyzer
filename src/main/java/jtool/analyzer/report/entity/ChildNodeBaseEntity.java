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

import java.util.ArrayList;
import java.util.List;

/**
 * 子节点
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:01:38 AM
 */
public class ChildNodeBaseEntity {

	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 类型 {@link jtool.analyzer.constant.DirectoryTypeEnum}
	 */
	private Integer type;
	
	/**
	 * 子节点
	 */
	private List<? super ChildNodeBaseEntity> children = new ArrayList<>();
	
	/**
	 * 包路径
	 */
	private String packagePath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<? super ChildNodeBaseEntity> getChildren() {
		return children;
	}

	public void setChildren(List<? super ChildNodeBaseEntity> children) {
		this.children = children;
	}
	
	public void addChild(ChildNodeBaseEntity child) {
		this.children.add(child);
	}

	public String getPackagePath() {
		return packagePath;
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	@Override
	public String toString() {
		return "ChildNodeBaseEntity [name=" + name + ", type=" + type + ", children=" + children + ", packagePath="
				+ packagePath + "]";
	}
	
}
