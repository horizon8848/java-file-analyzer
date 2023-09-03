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
package jtool.analyzer.core;

import java.io.File;

import jtool.analyzer.constant.DirectoryTypeEnum;
import jtool.analyzer.entity.FileDirectory;
import jtool.analyzer.entity.JavaFileInfo;

/**
 * java项目扫描
 * 
 * @author cjt
 * @date   Aug 24, 2023 11:17:02 AM
 */
public class JavaProjectScanner implements Scanner {

	private FileDirectory fileDirectory;
	
	@Override
	public Scanner scan(String directoryPath) {
		File directory = new File(directoryPath);
		if(!directory.exists()) {
			return null;
		}
		
		fileDirectory = new FileDirectory();
		fileDirectory.setType(DirectoryTypeEnum.DIRECTORY.getType());
		fileDirectory.setName(directory.getName());
		fileDirectory.setPath(new String[]{directory.getName()});
		this.deepScan(directory.listFiles(), fileDirectory);
		return this;
	}
	
	@Override
	public FileDirectory getFileDirectory() {
		return this.fileDirectory;
	}
	
	private void deepScan(File[] children, FileDirectory parent) {
		if(children.length == 0) {
			return;
		}
		for(File child : children) {
			// 判断类型
			int type = DirectoryTypeEnum.DIRECTORY.getType();
			if(!child.isDirectory()) {
				// 判断是否字节码文件
				String[] fileNameSplit = child.getName().split("\\.");
				type = "class".equals(fileNameSplit[1]) ? DirectoryTypeEnum.CLASS.getType() : DirectoryTypeEnum.OTHER.getType();
			}
			
			if(DirectoryTypeEnum.CLASS.getType() == type) {
				// java字节码解析
				JavaFileInfo current = this.getJavaMethodAnalysis(child);
				current.setPath(this.getPathArray(parent, child));
				parent.addChildren(current);
			} else {
				FileDirectory current = new FileDirectory();
				current.setType(type);
				current.setName(child.getName());
				current.setPath(this.getPathArray(parent, child));
				parent.addChildren(current);
				
				if(child.isDirectory()) {	
					deepScan(child.listFiles(), current);
				}
			}
		}
		
	}
	
	private String[] getPathArray(FileDirectory parent, File child) {
		String[] path = new String[parent.getPath().length + 1];
		System.arraycopy(parent.getPath(), 0, path, 0, parent.getPath().length);
		path[path.length - 1] = child.getName();
		return path;
	}
	
	public JavaFileInfo getJavaMethodAnalysis(File byteCode) {
		Analyzer<JavaFileInfo> analyzer = new JavaMethodAnalyzer(byteCode);
		analyzer.execute();
		return analyzer.getAnalysisResult();
	}

}
