package it.polito.tdp.h24Readathon.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.h24Readathon.model.AuthorGenres;
import it.polito.tdp.h24Readathon.model.Book;



public class BooksDAO {
	
	public List<String> getAllGenres(){
		final String sql = "SELECT DISTINCT b.genre_0 AS genre "
				+ "FROM books b "
				+ "ORDER BY b.genre_0";
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("genre"));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<String> getAllAuthors(){
		final String sql = "SELECT DISTINCT b.author AS author "
				+ "FROM books b "
				+ "ORDER BY b.author";
		List<String> result = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("author"));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Book> getBooksByGenre(String genre, int minPages, int maxPages){
		final String sql = "SELECT DISTINCT * "
				+ "FROM books "
				+ "WHERE genre_0 = ? AND pages > ? AND pages < ? ";
		List<Book> result = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			st.setInt(2, minPages);
			st.setInt(3, maxPages);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Book(res.getInt("book_id"), res.getString("title"), res.getString("author"), 
						res.getString("description"), res.getString("bookformat"), res.getInt("pages"), 
						res.getDouble("rating_value0"), res.getString("genre_0"), res.getString("genre_1"),
						res.getString("genre_2"), res.getDouble("price_EUR") ));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Book> getBooksByGenreAndRating(String genre, int minPages, int maxPages, double ratings){
		final String sql = "SELECT DISTINCT * "
				+ "FROM books "
				+ "WHERE genre_0 = ? AND pages > ? AND pages < ? AND rating_value0 > ?";
		List<Book> result = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			st.setInt(2, minPages);
			st.setInt(3, maxPages);
			st.setDouble(4, ratings);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Book(res.getInt("book_id"), res.getString("title"), res.getString("author"), 
						res.getString("description"), res.getString("bookformat"), res.getInt("pages"), 
						res.getDouble("rating_value0"), res.getString("genre_0"), res.getString("genre_1"),
						res.getString("genre_2"), res.getDouble("price_EUR") ));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public List<Book> getBooksByAuthor(String author, int minPages, int maxPages){
		final String sql = "SELECT DISTINCT * "
				+ "FROM books "
				+ "WHERE author = ? AND pages > ? AND pages < ?";
		List<Book> result = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, author);
			st.setInt(2, minPages);
			st.setInt(3, maxPages);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Book(res.getInt("book_id"), res.getString("title"), res.getString("author"), 
						res.getString("description"), res.getString("bookformat"), res.getInt("pages"), 
						res.getDouble("rating_value0"), res.getString("genre_0"), res.getString("genre_1"),
						res.getString("genre_2"), res.getDouble("price_EUR") ));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}

	public List<Book> getBooksByAuthorAndRatings(String author, int minPages, int maxPages, double ratings){
		final String sql = "SELECT DISTINCT * "
				+ "FROM books "
				+ "WHERE author = ? AND pages > ? AND pages < ? AND rating_value0 > ?";
		List<Book> result = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, author);
			st.setInt(2, minPages);
			st.setInt(3, maxPages);
			st.setDouble(4, ratings);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Book(res.getInt("book_id"), res.getString("title"), res.getString("author"), 
						res.getString("description"), res.getString("bookformat"), res.getInt("pages"), 
						res.getDouble("rating_value0"), res.getString("genre_0"), res.getString("genre_1"),
						res.getString("genre_2"), res.getDouble("price_EUR") ));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public AuthorGenres getAuthorGenres(String author) {
		final String sql = "SELECT DISTINCT genre_0 "
				+ "FROM books "
				+ "WHERE author = ?";
		AuthorGenres result = new AuthorGenres(author);
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, author);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.setGenre(res.getString("genre_0"));
			}
			conn.close();
			res.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
		return result;
	}
	
	public int deleteBookRead(Book b) {
		final String sql = "DELETE FROM books WHERE book_id = ?";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, b.getBookId());
			int res = st.executeUpdate();
			conn.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
}
