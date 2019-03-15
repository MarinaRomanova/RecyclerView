package com.example.recyclerview.modelJson.GoogleBooksApi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;



public class BookJson
{
    @JsonIgnore
    public String kind;
    @JsonIgnore
    public int totalItems;
    @JsonProperty
    public List<Item> items;
}

