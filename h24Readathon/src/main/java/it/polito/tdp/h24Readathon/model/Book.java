package it.polito.tdp.h24Readathon.model;

public class Book {
	
	private int bookId;
	private String title;
	private String author;
	private String description;
	private String bookFormat;
	private int pages;
	private Double ratings;
	private String genre0;
	private String genre1;
	private String genre2;
	private Double priceEUR;
	
	public Book(int bookId, String title, String author, String description, String bookFormat, int pages,
			Double ratings, String genre0, String genre1, String genre2, Double priceEUR) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.description = description;
		this.bookFormat = bookFormat;
		this.pages = pages;
		this.ratings = ratings;
		this.genre0 = genre0;
		this.genre1 = genre1;
		this.genre2 = genre2;
		this.priceEUR = priceEUR;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBookFormat() {
		return bookFormat;
	}

	public void setBookFormat(String bookFormat) {
		this.bookFormat = bookFormat;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Double getRatings() {
		return ratings;
	}

	public void setRatings(Double ratings) {
		this.ratings = ratings;
	}

	public String getGenre0() {
		return genre0;
	}

	public void setGenre0(String genre0) {
		this.genre0 = genre0;
	}

	public String getGenre1() {
		return genre1;
	}

	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	public String getGenre2() {
		return genre2;
	}

	public void setGenre2(String genre2) {
		this.genre2 = genre2;
	}

	public Double getPriceEUR() {
		return priceEUR;
	}

	public void setPriceEUR(Double priceEUR) {
		this.priceEUR = priceEUR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + title + " - " + author 	+ " - " + pages + " - " + genre0
				+ " - " + genre1 + " - " + genre2 + " - " + priceEUR + " euro";
	}
}
