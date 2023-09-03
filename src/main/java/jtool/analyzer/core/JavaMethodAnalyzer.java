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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import jtool.analyzer.collection.SimpleStringStack;
import jtool.analyzer.constant.DirectoryTypeEnum;
import jtool.analyzer.entity.JavaClassMethodInfo;
import jtool.analyzer.entity.JavaFileInfo;

/**
 * java方法分析器
 * 
 * @author cjt
 * @date   Aug 24, 2023 9:00:12 AM
 */
public class JavaMethodAnalyzer extends AnalyzerStruct<File, JavaFileInfo> {

	public JavaMethodAnalyzer(File content) {
		super(content);
	}
	
	@Override
	JavaFileInfo analysis() {
		JavaFileInfo info = new JavaFileInfo();
		info.setType(DirectoryTypeEnum.CLASS.getType());
		try (DataInputStream dis = new DataInputStream(new FileInputStream(super.getContent()));) {
			ClassFile classFile = new ClassFile(dis);
			// 获取类名
			ConstPool constPool = classFile.getConstPool();
			info.setName(super.getContent().getName());
			info.setClassFullName(constPool.getClassName());
			// 获取所有方法
			info.setMethodInfos(this.convertMethodInfo(classFile.getMethods()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			info.setAnalysisMessage("未找到文件");
		} catch (IOException e) {
			e.printStackTrace();
			info.setAnalysisMessage("io异常：" + e.getMessage());
		}

		return info;
	}

	/**
	 * 方法信息提取
	 * 
	 * @param methods
	 * @return
	 */
	private List<JavaClassMethodInfo> convertMethodInfo(List<MethodInfo> methods) {
		if (Objects.isNull(methods) || methods.isEmpty()) {
			return Collections.emptyList();
		}
		// 过滤构造方法
		return methods.stream().filter(method -> !(method.isConstructor() || "<clinit>".equals(method.getName())
				|| method.getName().startsWith("lambda$"))).map(method -> {
					JavaClassMethodInfo info = new JavaClassMethodInfo();
					info.setMethodName(method.getName());
					String descriptor = method.getDescriptor();
					// System.out.println(descriptor);

					SimpleStringStack stack = new SimpleStringStack(8);
					StringBuilder objTemp = new StringBuilder();
					boolean objectBegin = false;
					boolean isParamList = true;
					for (int i = 0; i < descriptor.length(); i++) {
						if (descriptor.charAt(i) == '(' || descriptor.charAt(i) == ')') {
							if (descriptor.charAt(i) == ')') {
								isParamList = false;
							}
							continue;
						}

						if (objectBegin) {
							if (descriptor.charAt(i) == ';') {
								objectBegin = false;
								stack.push(objTemp.toString());
								this.combineAndSetType(objTemp, stack, info, isParamList);
							} else {
								if (descriptor.charAt(i) == '/') {
									objTemp.append('.');
								} else {
									objTemp.append(descriptor.charAt(i));
								}
							}
						} else {
							String baseType = this.convertBaseType(descriptor.charAt(i));
							if ("[]".equals(baseType)) {
								stack.push(baseType);
							} else if ("Object".equals(baseType)) {
								// 对象类型
								objectBegin = true;
								objTemp.delete(0, objTemp.length());
							} else {
								// 基本类型
								stack.push(baseType);
								this.combineAndSetType(objTemp, stack, info, isParamList);
							}
						}

					}
					return info;
				}).toList();
	}
	
	private void combineAndSetType(StringBuilder objTemp, SimpleStringStack stack, JavaClassMethodInfo info,
			boolean isParamList) {
		objTemp.delete(0, objTemp.length());
		// 从栈取值
		while (stack.getIndex() > 0) {
			objTemp.append(stack.pop());
		}
		if (isParamList) {
			info.getParamList().add(objTemp.toString());
		} else {
			info.setReturnType(objTemp.toString());
		}
	}

	private String convertBaseType(char c) {
		String type = "";
		switch (c) {
		case 'B':
			type = "byte";
			break;
		case 'C':
			type = "char";
			break;
		case 'D':
			type = "double";
			break;
		case 'F':
			type = "float";
			break;
		case 'I':
			type = "int";
			break;
		case 'J':
			type = "long";
			break;
		case 'L':
			type = "Object";
			break;
		case 'S':
			type = "short";
			break;
		case 'Z':
			type = "boolean";
			break;
		case 'V':
			type = "void";
			break;
		case '[':
			type = "[]";
		}
		return type;
	}
}
