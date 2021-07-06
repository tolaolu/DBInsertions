/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbinsertions;

/**
 *
 * @author admin
 */
public class Book {
    
    private int Id, year, page;
    private String title, author;

    public Book(int Id, String title, String author, int page, int year) {
        this.Id = Id;
        this.title = title;
        this.author = author;
        this.page = page;
        this.year = year;
        
    }

    public int getId() {
        return Id;
    }
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public int getPage() {
        return page;
    }
    public int getYear() {
        return year;
    }
    
}
