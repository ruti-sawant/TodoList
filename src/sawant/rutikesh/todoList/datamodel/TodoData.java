package sawant.rutikesh.todoList.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodoData {
        private static final TodoData instance = new TodoData();
        private static final String filename = "TodoListItems.txt";
        private ObservableList<TodoItem> todoItems;
        private DateTimeFormatter df;
        
        public static TodoData getInstance() {
                return instance;
        }
        
        private TodoData() {
                df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        
        public void loadTodoItems() throws IOException {
                todoItems = FXCollections.observableArrayList();
                Path path = Paths.get(filename);
                String input;
                try (BufferedReader br = Files.newBufferedReader(path)) {
                        while ((input = br.readLine()) != null) {
                                String[] strings = input.split("\t");
                                TodoItem item = new TodoItem(strings[0], strings[1], LocalDate.parse(strings[2], df));
                                todoItems.add(item);
                        }
                }
        }
        
        public ObservableList<TodoItem> getTodoItems() {
                return todoItems;
        }
        
        /*public void setTodoItems(List<TodoItem> todoItems) {
                this.todoItems = todoItems;
        }*/
        
        public void storeTodoItems() throws IOException {
                Path path = Paths.get(filename);
                
                try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                        for (TodoItem item : todoItems) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(item.getShortDescription()).append('\t');
                                sb.append(item.getLongDescription()).append('\t');
                                sb.append(item.getDueDate().format(df)).append('\n');
                                bw.write(sb.toString());
                        }
                }
        }
        public void deleteTodoItem(TodoItem item){
                todoItems.remove(item);
        }
}
