package com.example.shilpi.nodelogin;

import java.io.Serializable;


public class Books /*implements Serializable*/ {

    //@SerializedName("id")
    public String id;
    // @SerializedName("bookname")
    public String Name;
    // @SerializedName("author")
    public String Author;
    public String Review;
    public String Buy_here;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getBuy_here() {
        return Buy_here;
    }

    public void setBuy_here(String buy_here) {
        Buy_here = buy_here;
    }

    // @SerializedName("price")


 /*   public Books(Integer id, String bookName, String authorName, Integer price, Integer stock) {
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
        this.price = price;
        this.stock = stock;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }*/
}