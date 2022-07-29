package it.polito.tdp.h24Readathon.db;

public class TestDAO {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BooksDAO dao = new BooksDAO();
		
		System.out.println(dao.getAllGenres().size());
		System.out.println(dao.getAllAuthors().size());

	}

}
