
package dbinsertions;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author user
 */
public class FXMLController implements Initializable {
    
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private Label idLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label pagesLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private TextField idTextFld;
    @FXML
    private TextField titleTxtFld;
    @FXML
    private TextField authorTxtFld;
    @FXML
    private TextField pagesTxtFld;
    @FXML
    private TextField yearTxtFld;
    @FXML
    private TableColumn<Book, Integer> tblIdCol;
    @FXML
    private TableColumn<Book, String> tblTitleCol;
    @FXML
    private TableColumn<Book, String> tblAuthCol;
    @FXML
    private TableColumn<Book, Integer> tblPageCol;
    @FXML
    private TableColumn<Book, Integer> tblYearCol;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button insertBtn;
    @FXML
    private Button clearbtn;
    @FXML
    private Button exit;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource()== insertBtn){
        insertData();
        } 
        
        else if(event.getSource() == updateBtn) {
        updateRow();
        }
        
        else if (event.getSource() == deleteBtn){
            deleteRow();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showBook();
    }    
    
    private Connection getConnection(){ // method that will always establish our connection when called.
    Connection conn;
    
    try{
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
            + "library?useLegacyDatetimeCode=false&serverTimezone=Africa/Lagos", "root", "");
         return conn;
    }
    catch(Exception ex){
       // System.out.println("I think my connection string didn't work :-(");
        System.out.println("Error!!" + ex.getMessage());
        return null;
    }
    }
    
    public ObservableList<Book> getBookList(){ // now creating statement
    ObservableList<Book> bookList = FXCollections.observableArrayList();
    Connection conn = getConnection(); // establishing connection here
    String query = "SELECT * FROM books";
    Statement st; // instanting the statement class
    ResultSet rs;// instantiating the resultset class.
    
    
    try{
    st = conn.createStatement(); // here we have created a statement
    rs = st.executeQuery(query);// here we have ran d query n passing d reply into resultset
    // now we need to iterate thru all the resultset hence why we will create a new book instance for each
    //of the rows being returned as thus:
    Book book; // this is like an import so when u want to use book, u wont need to use Book book = new Book().
    // u will just say book = new book...
    while(rs.next()){ // means as long as d rs has more rows/elements, then do d below:
    book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
            rs.getInt("page"), rs.getInt("year"));
    // U must enter the column names using their cases as exactly as they are in the DB table in phpmyadmin
    //or else the code above wont work and u wont be able to get data from DB.
    bookList.add(book);
    }
    }
    catch(Exception ex){
        System.out.println("I could not get data from the database :-(");
    }
    return bookList;
    } // at this stage we have books being held by our result set now we need a method to
    //set the books unto the table in their respective columns hence the showBook() below:
    
    public void showBook(){
    ObservableList<Book> list = getBookList(); // this line means we have an observable list of type BOOk
    //and we have named it as list and now we are putting the data that is returned to us from getBookList()
    // into this list.
    // now that we have put those data on the list, we need to build and set a value factory for our columns
    //in the table ie: we r setting where the columns will get their values from. hence:
    tblIdCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
    tblTitleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
    tblAuthCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
    tblPageCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("page"));
    tblYearCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
    
    booksTable.setItems(list);
    }
    
    private void insertData(){
    String insertionQuery = "INSERT INTO books VALUES (" + idTextFld.getText() +", '" + titleTxtFld.getText() +"','" + authorTxtFld.getText() +"',"
            + "" + pagesTxtFld.getText() +"," + yearTxtFld.getText() +" )";
    // note the '' that our title n author columns were put into - cos they r of string values.
    executeQuery(insertionQuery);// click red assit n let sys creat this method below:
    showBook();// this is called here so we can always see the insertions we make.
    }

    private void executeQuery(String insertionQuery) {
        Connection conn = getConnection(); // our active connection is passed into conn here. 
        Statement st;
        try{
            st = conn.createStatement();//our connection has created a statement n passed it into conn
            st.executeUpdate(insertionQuery);// now conn with the power above can execute the update in insertinQuery.
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void updateRow(){
    
        String updatingQuery = "UPDATE books SET title ='" + titleTxtFld.getText() +"', author = '" + authorTxtFld.getText() +"',"
                + "page = " + pagesTxtFld.getText() +", year = " + yearTxtFld.getText() +" WHERE id  =" + idTextFld.getText() +" ";
    
        executeQuery(updatingQuery);
        showBook();
    }
    
    private void deleteRow(){
        String deletionQuery = "DELETE FROM books WHERE id = " + idTextFld.getText() +"";
        executeQuery(deletionQuery);
        showBook();
}
    

    @FXML
    private void onMouseClicked(MouseEvent event) {
        
        Book book = booksTable.getSelectionModel().getSelectedItem(); // this will get the row of the table that has been selected.
        idTextFld.setText("" +book.getId()); // the "" & + shld be used for sys to allow int value be used.
        titleTxtFld.setText(book.getTitle());
        authorTxtFld.setText(book.getAuthor());
        pagesTxtFld.setText("" +book.getPage());
        yearTxtFld.setText("" +book.getYear());
    }

    @FXML
    private void clearerbtnpress(ActionEvent event) {
        
        idTextFld.clear();
        titleTxtFld.clear();
        authorTxtFld.clear();
        pagesTxtFld.clear();
        yearTxtFld.clear();
    }

    @FXML
    private void onExitBtnClick(ActionEvent event) {
        System.exit(0);
    }
}
