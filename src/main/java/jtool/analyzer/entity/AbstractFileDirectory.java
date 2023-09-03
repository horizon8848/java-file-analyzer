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
package jtool.analyzer.entity;

/**
 * 文件目录抽象类
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:00:58 AM
 */
public abstract class AbstractFileDirectory {

	/**
	 * 文件类型 {@link jtool.analyzer.constant.DirectoryTypeEnum}
	 */
	private int type;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 路径
	 */
	private String[] path;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getPath() {
		return path;
	}

	public void setPath(String[] path) {
		this.path = path;
	}

}
