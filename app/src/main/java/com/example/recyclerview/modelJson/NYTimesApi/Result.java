package com.example.recyclerview.modelJson.NYTimesApi;

import java.util.List;

public class Result
{
    public String title;
    public String description;
    public String contributor;
    public String author;
    public String contributor_note;
    public double price;
    public String age_group;
    public String publisher;
    public List<Isbn> isbns;
    public List<RanksHistory> ranks_history;
    public List<Review> reviews;

    public Result() {
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

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContributor_note() {
        return contributor_note;
    }

    public void setContributor_note(String contributor_note) {
        this.contributor_note = contributor_note;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAge_group() {
        return age_group;
    }

    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<Isbn> getIsbns() {
        return isbns;
    }

    public void setIsbns(List<Isbn> isbns) {
        this.isbns = isbns;
    }

    public List<RanksHistory> getRanks_history() {
        return ranks_history;
    }

    public void setRanks_history(List<RanksHistory> ranks_history) {
        this.ranks_history = ranks_history;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
