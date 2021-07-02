package sawant.rutikesh.todoList;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import sawant.rutikesh.todoList.datamodel.TodoData;
import sawant.rutikesh.todoList.datamodel.TodoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {
        private List<TodoItem> todoItems;
        @FXML
        private ListView<TodoItem> todoItemListView;
        @FXML
        private TextArea centerTextArea;
        @FXML
        private Label dueDateLabel;
        @FXML
        private BorderPane mainBorderPane;
        @FXML
        private ContextMenu listContextMenu;
        @FXML
        private ToggleButton filterToggleButton;
        private FilteredList<TodoItem> filteredList;
        public void initialize() {
                /*TodoItem item1 = new TodoItem("Mom's Birthday", "Its mom's birthday on 7 " +
                        "September 2021", LocalDate.of(2021, Month.SEPTEMBER, 7));
                TodoItem item2 = new TodoItem("My birthday ", "Its my birthday on 23 november",
                        LocalDate.of(2021, Month.NOVEMBER, 23));
                TodoItem item3 = new TodoItem("Assignment due", "Assignment of DBE lab for 6th" +
                        "module", LocalDate.of(2021, Month.JUNE, 6));
                TodoItem item4 = new TodoItem("Harry's marriage", "Harry is marrying to Harshada" +
                        " on 31st of July", LocalDate.of(2021, Month.JULY, 31));
                TodoItem item5 = new TodoItem("Recharge Jio number", "Recharge Jio number" +
                        " 9011962014", LocalDate.of(2021, Month.JULY, 23));
                todoItems = new ArrayList<>();
                todoItems.add(item1);
                todoItems.add(item2);
                todoItems.add(item3);
                todoItems.add(item4);
                todoItems.add(item5);
        
                TodoData.getInstance().setTodoItems(todoItems);*/
                listContextMenu = new ContextMenu();//creating contextMenu
                MenuItem deleteMenuItem=new MenuItem("Delete");//creating a menuItem.
                //setting action handler to context menu
                deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                                deleteItem(todoItemListView.getSelectionModel().getSelectedItem());
                        }
                });
                listContextMenu.getItems().add(deleteMenuItem);
                todoItems = TodoData.getInstance().getTodoItems();
                todoItemListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, newItem) -> {
                        if (newItem != null) {
                                TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                                centerTextArea.setText(item.getLongDescription());
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                                dueDateLabel.setText(df.format(item.getDueDate()));
                        }
                });
                filteredList=new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(),(s)->true);//to filter
                // all lists at the start and then if user clicks on the toggle button we should filter item for
                // today's date.
                SortedList<TodoItem> sortedList=new SortedList<>(filteredList,
                        (TodoItem o1,TodoItem o2)->{
                                return o1.getDueDate().compareTo(o2.getDueDate());
                        });
//                todoItemListView.setItems(TodoData.getInstance().getTodoItems());
                todoItemListView.setItems(sortedList);//to show items according to sorted order that we defined using
                // lambda function
                todoItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                todoItemListView.getSelectionModel().selectFirst();
                todoItemListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
                        @Override
                        public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                                ListCell<TodoItem> cell = new ListCell<>() {
                                        @Override
                                        protected void updateItem(TodoItem todoItem, boolean b) {
                                                super.updateItem(todoItem, b);
                                                if (b) {
                                                        setText(null);
                                                } else {
                                                        setText(todoItem.getShortDescription());
                                                        if (todoItem.getDueDate().equals(LocalDate.now())) {
                                                                setTextFill(Color.RED);
                                                        }else{
                                                                setTextFill(Color.BLACK);
                                                        }
                                                }
                                        }
                                };
                                cell.emptyProperty().addListener((obs,wasEmpty,isNowEmpty)->{
                                        if(isNowEmpty){
                                                cell.setContextMenu(null);
                                        }else{
                                                cell.setContextMenu(listContextMenu);
                                        }
                                });
                                return cell;
                        }
                });
        }
        
        private void deleteItem(TodoItem selectedItem) {
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Todo Item");
                alert.setHeaderText("Delete Item : "+selectedItem.getShortDescription());
                alert.setContentText("Are you sure to delete item ?");
                Optional<ButtonType> result=alert.showAndWait();
                if(result.isPresent() && result.get().equals(ButtonType.OK)){
                        TodoData.getInstance().deleteTodoItem(selectedItem);
                }
        }
        
        @FXML
        public void showNewItemDialog() {
                System.out.println("in dialog func");
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(mainBorderPane.getScene().getWindow());
                dialog.setTitle("New Todo Item");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("TodoItemDialog.fxml"));
                try {
                        dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                        System.out.println("Couldn't load dialog");
                        e.printStackTrace();
                        return;
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                        DialogController controller = fxmlLoader.getController();
                        TodoItem newItem = controller.processResults();
//                        todoItemListView.getItems().setAll(TodoData.getInstance().getTodoItems());
                        todoItemListView.getSelectionModel().select(newItem);
                }
        }
        
        public void handleClickedListView() {
                TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                centerTextArea.setText(item.getLongDescription());
                dueDateLabel.setText(item.getDueDate().toString());
        }
        @FXML
        public void onKeyPressed(KeyEvent keyEvent) {
                TodoItem selectedItem=todoItemListView.getSelectionModel().getSelectedItem();
                if(selectedItem!=null){
                        if(keyEvent.getCode().equals(KeyCode.DELETE)){
                                deleteItem(selectedItem);
                        }
                }
        }
        @FXML
        public void handleFilterButton(ActionEvent actionEvent) {
                TodoItem selectedItem=todoItemListView.getSelectionModel().getSelectedItem();
                if(filterToggleButton.isSelected()){
                        filteredList.setPredicate((TodoItem s)->s.getDueDate().equals(LocalDate.now()));
                        if(filteredList.isEmpty()){
                                centerTextArea.clear();
                                dueDateLabel.setText("");
                        }else if(filteredList.contains(selectedItem)){
                                todoItemListView.getSelectionModel().select(selectedItem);
                        }else {
                                todoItemListView.getSelectionModel().selectFirst();
                        }
                }else{
                        filteredList.setPredicate((s)->true);
                        todoItemListView.getSelectionModel().select(selectedItem);
                }
        }
        
        public void handleExit(ActionEvent actionEvent) {
                Platform.exit();
        }
}
