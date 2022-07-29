package it.polito.tdp.h24Readathon.model;

import java.util.ArrayList;
import java.util.List;



public class TestModel {

	public static void main(String[] args) {
		Model m = new Model();
		List<String> genres = new ArrayList<>();
		//genres.add("Fiction");
		genres.add("Young Adult");
		//genres.add("Contemporary");
		genres.add("Nonfiction");
		List<String> authors = new ArrayList<>();
		authors.add("Jennifer L. Armentrout");
		//authors.add("Terry Brooks");
		//authors.add("Robin Roe");
		List<Book> books = new ArrayList<>(m.getAllBooksAuthor(300, 500, authors, 4.35));
		System.out.println(books.size());
		List<Book> more = new ArrayList<>(m.getMoreBooks(300, 500, genres, 4.35));
		System.out.println(more.size());
		List<Book> filtered = new ArrayList<>(m.filterBooks(books, authors, genres, more));
		System.out.println(filtered.size());
		for(Book b : filtered) {
			System.out.println(b.toString()+"\n");
		}
		System.out.println(m.getFilteredMore().size());
		
		m.findReadathon(70, 0, filtered, m.getFilteredMore(), null);
		System.out.println(m.hours(m.getMinTot()));
		System.out.println(m.getTime(m.getMinTot()));
	}

}
