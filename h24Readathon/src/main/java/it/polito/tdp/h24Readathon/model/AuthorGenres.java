package it.polito.tdp.h24Readathon.model;

import java.util.ArrayList;
import java.util.List;

public class AuthorGenres {
	
	private String author;
	private List<String> genres;
	public AuthorGenres(String author) {
		super();
		this.author = author;
		this.genres = new ArrayList<>();
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<String> getGenres() {
		return genres;
	}
	public void setGenre(String genre) {
		this.genres.add(genre);
	}
	@Override
	public String toString() {
		String s = author+" - ";
		for(String g : genres) {
			s+= g+" ";
		}
		return s;
	}
}
