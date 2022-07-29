package it.polito.tdp.h24Readathon.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.h24Readathon.db.BooksDAO;


public class Model {

	private BooksDAO dao;
	private List<String> allGenres;
	private List<String> allAuthors;
	private List<Book> allBooksForReadathon;
	private List<Book> moreBooks;
	private List<AuthorGenres> authorGenres;
	private List<Book> filteredAll;
	private List<Book> filteredMore;
	
	//strutture dati per la ricorsione
	private List<Book> bestReadathon;
	private double bestTime; //qui memorizzo il tempo avanzante
	private double bestPrice; //qui memorizzo il budget avanzante
	private double minTot;
	private double availableTime;
	
	public Model() {
		this.dao = new BooksDAO();
		this.allGenres = new ArrayList<>();
		this.allAuthors = new ArrayList<>();
	}
	
	public List<String> getAllGenres(){
		this.allGenres = this.dao.getAllGenres();
		return this.allGenres;
	}

	public List<String> getAllAuthors(){
		this.allAuthors = this.dao.getAllAuthors();
		return this.allAuthors;
	}
	
	public AuthorGenres getAuthorGenres(String author) {
		AuthorGenres ag = null;
		ag = this.dao.getAuthorGenres(author);
		return ag;
	}
	
	public List<AuthorGenres> getAuthorGenres(List<String> author){
		this.authorGenres = new ArrayList<>();
		for(String s : author) {
			AuthorGenres ag = this.dao.getAuthorGenres(s);
			this.authorGenres.add(ag);
		}
		return this.authorGenres;
	}
	
	public List<Book> getAllBooksAuthor(int minPages, int maxPages, List<String> authors, double ratings){
		this.allBooksForReadathon = new ArrayList<>();
		for(String a : authors) {
			this.allBooksForReadathon.addAll(this.dao.getBooksByAuthorAndRatings(a, minPages, maxPages, ratings));
		}
		return this.allBooksForReadathon;
	}
	
	public List<Book> getMoreBooks(int minPages, int maxPages, List<String> genres, double ratings){
		this.moreBooks = new ArrayList<>();
		for(String g : genres) {
			this.moreBooks.addAll(this.dao.getBooksByGenreAndRating(g, minPages, maxPages, ratings));
		}
		return this.moreBooks;
	}
	
	public List<String> getCompatibility(String author, List<String> genres) {
		List<String> compatibleGenres = new ArrayList<>();
		AuthorGenres ag = null;
		ag = this.getAuthorGenres(author);
		if(ag != null) {
			for(String gen : ag.getGenres()) {
				for(int i = 0; i < genres.size(); i++) {
					if(gen.equals(genres.get(i))) {
						compatibleGenres.add(gen);
					}
				}
			}
		}
		return compatibleGenres;
	}
	
	public List<Book> filterBooks(List<Book> allBooks, List<String> authors, List<String> genres, List<Book> more){
		//qui devo filtrare i libri: poichè la ricerca viene fatta dando priorità agli autori, se l'autore in osservazione
		//scrive un genere fra quelli selezionati, prendo in considerazione solo i libri di quel genere, altrimenti vorrà dire
		//che vi è un'incompatibilità fra autore e generi e il programma mantiene tutti i libri di quell'autore
		filteredAll = new ArrayList<>();
		filteredMore = new ArrayList<>(more);
		for(String a : authors) {
			List<String> compatible = new ArrayList<>(this.getCompatibility(a, genres));
			if(compatible.size() == 0) { 
				for(Book b : allBooks) {
					if(b.getAuthor().equals(a)) {
						filteredAll.add(b);
					}
				}
			}else {
				for(String gc : compatible) {
					for(Book b : allBooks) {
						if(b.getAuthor().equals(a) && b.getGenre0().equals(gc)) {
							filteredAll.add(b);
						}
					}
				}
			}
		}
		//devo poi togliere eventualmente i libri che ci sono in comune
		for(Book b : filteredAll) {
			if(filteredMore.contains(b)) {
				filteredMore.remove(b);
			}
		}
		return filteredAll;
	}
	
	public List<Book> getFilteredMore(){
		return this.filteredMore;
	}
	public List<Book> getFilteredAll(){
		return this.filteredAll;
	}
	
	public List<Book> findReadathon(int speed, double pause, List<Book> filteredAll, List<Book> filteredMore, Double budget) {
		this.bestReadathon = new ArrayList<>();
		List<Book> partial = new ArrayList<>();
		if(budget != null)
			this.bestPrice = budget;
		this.bestTime = 0.0;
		this.minTot = 0.0;
		availableTime = 24*60-(pause*60);
		long start = System.nanoTime();
		if(budget == null) {
			findBestReadathon(speed, availableTime, partial, filteredAll, filteredMore);
		}else {
			findBestBudgeted(speed, availableTime, budget, partial, filteredAll, filteredMore);
		}
		long fin = System.nanoTime();
		System.out.println(fin-start);
		
		System.out.println("Libri da leggere: "+this.bestReadathon.size()+"\n");
		for(Book b : this.bestReadathon) {
			System.out.println(b.toString()+"\n");
		}
		this.minTot = availableTime-this.bestTime;
		System.out.println("Tempo impiegato su "+availableTime+" minuti: "+(availableTime-this.bestTime)+"\n");
		System.out.println("Budget impiegato su "+this.bestPrice+" euro: "+this.getTotalPrice(this.bestReadathon));
		return this.bestReadathon;
	}

	private void findBestBudgeted(int speed, double availableTime, Double availableBudget, List<Book> partial, List<Book> filAll,
			List<Book> filMore) {
		if(partial.size() > 0 && partial.size() >= this.bestReadathon.size()) {
			if(!this.isBudgetEnough(availableBudget, this.getMinPrice(filAll, partial)) &&
					!this.remainingBooks(filAll, partial) && filMore.size() > 0) {
				if(!this.isTimeEnough(availableTime, this.getMinPages(filMore, partial), speed)) {
					if( this.bestTime == 0.0 || (this.bestTime > 0.0 && availableTime < this.bestTime 
							&& this.getTotalPrice(partial) <= this.bestPrice) ) {
						this.bestReadathon = new ArrayList<>(partial);
						this.bestTime = availableTime;
						return;
					}
				}else { //Se c'è ancora tempo, vado avanti con la ricorsione
					filAll = new ArrayList<>(this.getRemainingBooks(filMore, partial));
					filMore = new ArrayList<>(filMore);
					this.findBestBudgeted(speed, availableTime, availableBudget, partial, filAll, filMore);
				}
			}else if(!this.isBudgetEnough(availableBudget, this.getMinPrice(filAll, partial)) &&
					this.remainingBooks(filAll, partial) && filMore.size() == 0) { 
				if(!this.isTimeEnough(availableTime, this.getMinPages(filMore, partial), speed)) {
					if( this.bestTime == 0.0 || (this.bestTime > 0.0 && availableTime < this.bestTime 
							&& this.getTotalPrice(partial) <= this.bestPrice) ) {
						this.bestReadathon = new ArrayList<>(partial);
						this.bestTime = availableTime;
						return;
					}
				}
			}	
			if(this.remainingBooks(filAll, partial) && (!this.isBudgetEnough(availableBudget, this.getMinPrice(filAll, partial)) ||
					!this.isTimeEnough(availableTime, this.getMinPages(filAll, partial), speed))) {
				if( this.bestTime == 0.0 || (this.bestTime > 0.0 && availableTime < this.bestTime && 
						this.getTotalPrice(partial) <= this.bestPrice) ) {
					this.bestReadathon = new ArrayList<>(partial);
					this.bestTime = availableTime;
					return;
				}
			}
		}
		for(Book b : this.getRemainingBooks(filAll, partial)) {
			double ttc = this.getTimeCompleted(b.getPages(), speed);
			double price = b.getPriceEUR();
			if(ttc <= availableTime && price <= availableBudget  && !partial.contains(b)) {
				partial.add(b);
				this.findBestBudgeted(speed, availableTime-ttc, availableBudget-price, partial, filAll, filMore);
				partial.remove(b);
			}
		}
	}

	private void findBestReadathon(int speed, double availableTime, List<Book> partial, List<Book> filAll, List<Book> filMore) {
		//caso terminale: non c'è sufficiente tempo per completare nemmeno il libro più piccolo ancora a disposizione.
		// Sovrascrivo la bestSolution solo se partial ha maggior numero di libri e il minor tempo libero
		if(partial.size() > 0 && partial.size() >= this.bestReadathon.size()) {
			if(!this.remainingBooks(filAll, partial) && filMore.size() > 0) {
				if(!this.isTimeEnough(availableTime, this.getMinPages(filMore, partial), speed)) {
					//se non c'è più tempo per ulteriori libri: finito
					if( this.bestTime == 0.0 || (this.bestTime > 0.0 && availableTime < this.bestTime) ) {
						this.bestReadathon = new ArrayList<>(partial);
						this.bestTime = availableTime;
						return;
					}
				}else {
					//Se c'è ancora tempo, vado avanti con la ricorsione
					filAll = new ArrayList<>(this.getRemainingBooks(filMore, partial));
					filMore = new ArrayList<>(filMore);
					this.findBestReadathon(speed, availableTime, partial, filAll, filMore);
				}
			}else { // non ci sono più libri in generale: se non c'è più tempo ---> fine
				if(!this.isTimeEnough(availableTime, this.getMinPages(filMore, partial), speed)) {
					//se non c'è più tempo per ulteriori libri: finito
					if( this.bestTime == 0.0 || (this.bestTime > 0.0 && availableTime < this.bestTime) ) {
						this.bestReadathon = new ArrayList<>(partial);
						this.bestTime = availableTime;
						return;
					}
				}
			}			
			//se non ho ancora finito con i libri degli autori e in più non c'è più tempo: finisco la ricorsione
			if(this.remainingBooks(filAll, partial) && !this.isTimeEnough(availableTime, this.getMinPages(filAll, partial), speed)) {
				if( this.bestTime == 0.0 || (this.bestTime > 0.0 && availableTime < this.bestTime) ) {
					this.bestReadathon = new ArrayList<>(partial);
					this.bestTime = availableTime;
					return;
				}
			}
		}
		//caso normale: accetto un libro solo se non è già presente e può essere completato nel tempo rimanente
		for(Book b : this.getRemainingBooks(filAll, partial)) {
			double ttc = this.getTimeCompleted(b.getPages(), speed);
			if(ttc <= availableTime && !partial.contains(b)) {
				partial.add(b);
				this.findBestReadathon(speed, availableTime-ttc, partial, filAll, filMore);
				partial.remove(b);
			}
		}
	}
	
	public double getTimeCompleted(int pages, int speed) {
		double pg = (double) pages;
		double rSpeed = 60.0/(double)speed;
		double completed = pg*rSpeed;
		return completed;
	}
	
	public double getTotalPrice(Collection<Book> partial) {
		double tot = 0.0;
		for(Book b : partial) {
			tot += b.getPriceEUR();
		}
		return tot;
	}
	public Set<Book> getRemainingBooks(Collection<Book> all, Collection<Book> partial){
		Set<Book> remaining = new HashSet<>(all);
		for(Book b : partial) {
			if(remaining.contains(b)){
				remaining.remove(b);
			}
		}
		return remaining;
	}
	
	public boolean remainingBooks(Collection<Book> all, Collection<Book> partial){
		Set<Book> remaining = new HashSet<>(this.getRemainingBooks(all, partial));
		if(remaining.size() != 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public Integer getMinPages(Collection<Book> all, Collection<Book> partial) {
		Set<Book> remaining = new HashSet<>(this.getRemainingBooks(all, partial));
		Integer min = null;
		for(Book b : remaining) {
			if(min == null) {
				min = b.getPages();
			}else if(b.getPages() < min) {
				min = b.getPages();
			}
		}
		return min;
	}
	
	public Double getMinPrice(Collection<Book> all, Collection<Book> partial) {
		Set<Book> remaining = new HashSet<>(this.getRemainingBooks(all, partial));
		Double min = null;
		for(Book b : remaining) {
			if(min == null) {
				min = b.getPriceEUR();
			}else if(b.getPriceEUR() < min) {
				min = b.getPriceEUR();
			}
		}
		return min;
	}
	
	public boolean isTimeEnough(double availableTime, Integer minPg, int speed) {
		if(minPg == null) {
			return false; //non ci sono più libri rimanenti ---> fine
		}
		double ttc = this.getTimeCompleted(minPg, speed);
		if( ttc <= availableTime) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isBudgetEnough(double availableBudget, Double price) {
		if(price == null) {
			return false;
		}
		if(price <= availableBudget) {
			return true;
		}else {
			return false;
		}
	}
	
	public double hours(double t) {
		double h = t/60;
		return h;
	}
	
	public double getMinTot() {
		return this.minTot;
	}
	
	public String getTime(double hh) {
		//double hh = this.hours(t);
		BigDecimal bd = new BigDecimal(String.valueOf(hh));
		int ore = bd.intValue();
		String m = bd.subtract(new BigDecimal(ore)).toPlainString();
		double min = Double.parseDouble(m);
		int minuti = (int)(60*min);
		
		return ""+ore+" ore e "+minuti+" minuti";
	}
	
	public double getPrice() {
		return this.getTotalPrice(this.bestReadathon);
	}
	
	public void deleteBookRead(Book b) {
		int del = this.dao.deleteBookRead(b);
		if(del > 0) {
			System.out.println("Libro cancellato");
		}
	}
}
