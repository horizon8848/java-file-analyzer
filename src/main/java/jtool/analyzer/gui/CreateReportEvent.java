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
package jtool.analyzer.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import jtool.analyzer.report.JavaProjectReport;
import jtool.analyzer.utils.StringUtils;

/**
 * 生成报告按钮事件
 * 
 * @author cjt
 * @date   Sep 1, 2023 3:55:08 PM
 */
public class CreateReportEvent implements EventHandler<ActionEvent> {

	private TextField projectPathTextField;
	
	private TextField outputTextField;
	
	private TextField dbTextField;
	
	private TextField userTextField;
	
	private PasswordField passwordField;
	
	private TextField tableTextField;

	public CreateReportEvent(TextField projectPathTextField, TextField outputTextField, TextField dbTextField,
			TextField userTextField, PasswordField passwordField, TextField tableTextField) {
		super();
		this.projectPathTextField = projectPathTextField;
		this.outputTextField = outputTextField;
		this.dbTextField = dbTextField;
		this.userTextField = userTextField;
		this.passwordField = passwordField;
		this.tableTextField = tableTextField;
	}

	@Override
	public void handle(ActionEvent event) {
		if(StringUtils.isBlank(projectPathTextField.getText())) {
			this.showAlert("扫描路径不能为空！");
			return;
		}
		if(StringUtils.isBlank(outputTextField.getText())) {
			this.showAlert("报告输出路径不能为空！");
			return;
		}
		if(StringUtils.isNotBlank(dbTextField.getText())
				&& (StringUtils.isBlank(userTextField.getText())
						|| StringUtils.isBlank(passwordField.getText())
						|| StringUtils.isBlank(tableTextField.getText()))) {
			this.showAlert("请完善数据库配置信息！");
			return;
		}

		JavaProjectReport report;
		if (StringUtils.isNotBlank(dbTextField.getText())) {
			report = new JavaProjectReport(projectPathTextField.getText(), dbTextField.getText(),
					userTextField.getText(), passwordField.getText(), tableTextField.getText());
		} else {
			report = new JavaProjectReport(projectPathTextField.getText());
		}
		report.setReportOutputPath(outputTextField.getText());
		report.output();
		this.showSuccess();
	}

	/**
	 * 提示框
	 * @param errorMsg
	 */
	private void showAlert(String errorMsg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("错误");
		alert.setHeaderText("参数校验失败");
		alert.setContentText(errorMsg);
		alert.show();
	}
	
	private void showSuccess() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("提示");
		alert.setHeaderText("执行成功");
		alert.setContentText("报告已生成");
		alert.show();
	}
}
