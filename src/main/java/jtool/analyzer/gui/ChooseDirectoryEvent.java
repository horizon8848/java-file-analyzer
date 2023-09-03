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

import java.io.File;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * 文件目录选择事件
 * 
 * @author cjt
 * @date   Sep 1, 2023 3:44:38 PM
 */
public class ChooseDirectoryEvent implements EventHandler<ActionEvent> {

	private Stage primaryStage;
	
	private TextField textField;
	
	private DirectoryChooser directoryChooser;

	public ChooseDirectoryEvent(Stage primaryStage, TextField textField) {
		this(primaryStage, textField, new DirectoryChooser());
	}

	public ChooseDirectoryEvent(Stage primaryStage, TextField textField, DirectoryChooser directoryChooser) {
		super();
		this.primaryStage = primaryStage;
		this.textField = textField;
		this.directoryChooser = directoryChooser;
	}

	@Override
	public void handle(ActionEvent event) {
		File file = directoryChooser.showDialog(primaryStage);
		if(Objects.nonNull(file)) {
			textField.setText(file.getAbsolutePath());
			textField.setTooltip(new Tooltip(file.getAbsolutePath()));
		}
	}

}
