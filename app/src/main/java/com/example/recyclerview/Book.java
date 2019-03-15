package com.example.recyclerview;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "book_table")
public class Book {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "description")
    String description;
    private int imageResource;
    @ColumnInfo(name = "date")
    String date;
    @ColumnInfo(name = "thumbnail_url")
    private String thumbnailUrl;
    @Ignore
    List<String> authors;
    @ColumnInfo(name = "author")
    String author;

    private String publisher;
    private int pageCount;
    private String language;
    private String previewLink;
    @Ignore
    private List<String> categories;

    private String category;

    private boolean addedToFavorite;

    public Book() {
        addedToFavorite = false;
    }

    @Ignore
    public Book(String title, String description, String author) {
        addedToFavorite=false;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    @Ignore
    public Book(String title, String description, String date, String thumbnailUrl, List<String> authors) {
        addedToFavorite = false;
        this.title = title;
        this.description = description;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.authors = authors;
    }

    @Ignore
    public Book(String title, String description, int imageResource) {
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
        addedToFavorite =  false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getAuthor() {
        if(author==null){
            try {
                author=authors.get(0);
            }catch (Exception e){

            }
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        if(category==null){
            try {
                category= categories.get(0);
            }catch (Exception e){
            }
        }
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean getAddedToFavorite() {
        return addedToFavorite;
    }

    public void setAddedToFavorite(boolean addedToFavorite) {
        this.addedToFavorite = addedToFavorite;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageResource=" + imageResource +
                ", date='" + date + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", authors=" + authors +
                ", publisher='" + publisher + '\'' +
                ", pageCount=" + pageCount +
                ", language='" + language + '\'' +
                ", previewLink='" + previewLink + '\'' +
                ", categories=" + categories +
                '}';
    }
}
