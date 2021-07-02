package sawant.rutikesh.todoList;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sawant.rutikesh.todoList.datamodel.TodoData;
import sawant.rutikesh.todoList.datamodel.TodoItem;

public class DialogController {
        @FXML
        private TextField shortDescription;
        @FXML
        private TextArea detailsArea;
        @FXML
        private DatePicker deadlinePicker;
        
        @FXML
        public TodoItem processResults() {
                //extracting data from text fields and adding that data to a list in DataItems
                TodoItem newItem = new TodoItem(shortDescription.getText().trim(),
                        detailsArea.getText().trim(), deadlinePicker.getValue());
                TodoData.getInstance().getTodoItems().add(newItem);
                return newItem;
        }
}
