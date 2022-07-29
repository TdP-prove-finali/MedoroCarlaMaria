package it.polito.tdp.h24Readathon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.h24Readathon.model.AuthorGenres;
import it.polito.tdp.h24Readathon.model.Book;
import it.polito.tdp.h24Readathon.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController {
	
	private Model model;
	private List<String> genres;
	private List<String> authors;
	private List<Book> readathon;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAuthor;

    @FXML
    private Button btnGenre;

    @FXML
    private Button btnResetAuthor;

    @FXML
    private CheckBox checkBudget;

    @FXML
    private ComboBox<String> cmbAuthors;

    @FXML
    private ComboBox<String> cmbGenres;

    @FXML
    private TableColumn<Book, String> dtAuthor;

    @FXML
    private TableColumn<Book, String> dtDescription;

    @FXML
    private TableColumn<Book, String> dtFormat;

    @FXML
    private TableColumn<Book, String> dtGenre;

    @FXML
    private TableColumn<Book, Integer> dtPages;

    @FXML
    private TableColumn<Book, Double> dtPrice;

    @FXML
    private TableColumn<Book, Double> dtRatings;

    @FXML
    private TableView<Book> dtResult;

    @FXML
    private TableColumn<Book, String> dtTitle;

    @FXML
    private TextField txtBudget;

    @FXML
    private TextField txtMaxPages;

    @FXML
    private TextField txtMinPages;

    @FXML
    private TextField txtPause;

    @FXML
    private TextField txtRating;

    @FXML
    private TextArea txtResult;

    @FXML
    private Slider sdrSpeed;

    @FXML
    void doAccept(ActionEvent event) {
    	for(Book b : this.readathon) {
    		this.model.deleteBookRead(b);
    	}
    	//aggiorno le combo box con generi e autori
    	this.cmbAuthors.getItems().clear();
    	this.cmbAuthors.getItems().addAll(this.model.getAllAuthors());
    	this.cmbGenres.getItems().clear();
    	this.cmbGenres.getItems().addAll(this.model.getAllGenres());
    	this.genres = new ArrayList<>();
    	this.authors = new ArrayList<>();
    }

    @FXML
    void doAddAuthor(ActionEvent event) {
    	String autore = this.cmbAuthors.getValue();
    	if(autore == null) {
    		this.txtResult.setText("Attenzione, non hai selezionato alcun genere!");
    		return;
    	}
    	this.authors.add(autore);
    	if(this.authors.size() == 3) {
    		//disabilita la check box e il pulsante perchè al massimo possiamo avere da 1 a 3 generi
    		this.cmbAuthors.setDisable(true);
    		this.btnAuthor.setDisable(true);
    	}
    }

    @FXML
    void doAddGenre(ActionEvent event) {
    	String genere = this.cmbGenres.getValue();
    	if(genere == null) {
    		this.txtResult.setText("Attenzione, non hai selezionato alcun genere!");
    		return;
    	}
    	this.genres.add(genere);
    	if(this.genres.size() == 3) {
    		//disabilita la check box e il pulsante perchè al massimo possiamo avere da 1 a 3 generi
    		this.cmbGenres.setDisable(true);
    		this.btnGenre.setDisable(true);
    	}
    }

    @FXML
    void doReset(ActionEvent event) {
    	//ripulisci tutti i campi
    	this.txtBudget.clear();
    	this.txtMaxPages.clear();
    	this.txtMinPages.clear();
    	this.txtPause.clear();
    	this.txtRating.clear();
    	this.txtResult.clear();
    	this.sdrSpeed.valueProperty().set(30);
    	this.cmbAuthors.valueProperty().set(null);
    	this.cmbGenres.valueProperty().set(null);
    	//disabilita i campi opzionali
    	this.txtBudget.setDisable(true);
    	//setta le checkbox a false
    	this.checkBudget.setSelected(false);
    	//riattiva i campi per il genere se disattivati
    	this.cmbGenres.setDisable(false);
    	this.cmbGenres.valueProperty().set(null);
    	this.btnGenre.setDisable(false);
    	this.cmbAuthors.setDisable(false);
    	this.cmbAuthors.valueProperty().set(null);
    	this.btnAuthor.setDisable(false);
    	this.genres = new ArrayList<>();
    	this.authors = new ArrayList<>();
    	//cancella i risultati dalla tabella
    	this.dtResult.getItems().clear();
    	//resetto la readathon
    	this.readathon = new ArrayList<>();
    }

    @FXML
    void generateReadathon(ActionEvent event) {
    	this.txtResult.clear();
    	this.dtResult.getItems().clear();
    	//recupera input e controlla siano stati inseriti correttamente
    	int speed;
    	try {
    		speed = (int)this.sdrSpeed.getValue();
    		if(speed < 30 || speed > 70 ) {
    			this.txtResult.appendText("Devi inserire un numero positivo, compreso fra 30 e 70, come numero di pagine lette mediamente in un'ora\n");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi selezionare un numero intero come numero di pagine lette mediamente in un'ora\n");
    		return;
    	}
    	int minPages;
    	int maxPages;
    	try {
    		minPages = Integer.parseInt(this.txtMinPages.getText());
    		if(minPages <=0 ) {
    			this.txtResult.appendText("Devi inserire un numero positivo, possibilmente maggiore di 0, come numero minimo di pagine per un libro\n");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Il numero minimo di pagine che devono avere i libri deve essere un numero intero\n");
    		return;
    	}
    	try {
    		maxPages = Integer.parseInt(this.txtMaxPages.getText());
    		if(maxPages <=0 ) {
    			this.txtResult.appendText("Devi inserire un numero positivo, possibilmente maggiore di 0, come numero massimo di pagine per un libro\n");
    			return;
    		}
    		if(maxPages <= minPages) {
    			this.txtResult.appendText("Devi inserire un numero di pagine massimo che sia superiore (e non uguale) al minimo");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Il numero massimo di pagine che devono avere i libri deve essere un numero intero\n");
    		return;
    	}
    	double pause;
    	try {
    		pause = Double.parseDouble(this.txtPause.getText());
    		if(pause < 0 ) {
    			this.txtResult.appendText("Devi inserire un numero positivo come ore di pausa\n");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Per le ore di pausa devi inserire un numero nel seguente formato: 0, 0.5, 1, 1.75, 2.3, 2.5, ...\n");
    		return;
    	}
    	//controlla che ci sia almeno un genere
    	if(this.genres.size() == 0) {
    		this.txtResult.appendText("Devi selezionare da un minimo di 1 a un massimo di 3 generi\n");
    		return;
    	}
    	//controlla che ci sia almeno un autore privilegiato
    	if(this.authors.size() == 0) {
			this.txtResult.appendText("Devi selezionare da un minimo di 1 a un massimo di 3 autori\n");
			return;
		}
    	double rating;
    	try {
    		rating = Double.parseDouble(this.txtRating.getText());
    		if(rating <=0 ) {
    			this.txtResult.appendText("Devi inserire un numero positivo, possibilmente maggiore di 0, come minimo rating medio\n");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire un numero, eventualmente non intero, come minimo rating medio\n");
    		return;
    	}
    	//controlli sugli input opzionali: se una checkbox è selezionata allora i valori devono esserci e devono essere corretti
    	Double budget = null;
    	if(this.checkBudget.isSelected()) {
    		try {
        		budget = Double.parseDouble(this.txtBudget.getText());
        		if(budget <=0 ) {
        			this.txtResult.appendText("Devi inserire un numero positivo, possibilmente maggiore di 0, come budget\n");
        			return;
        		}
        	}catch(NumberFormatException e) {
        		this.txtResult.appendText("Devi inserire un numero, eventualmente non intero, come budget\n");
        		return;
        	}
    	}
    	this.txtResult.appendText("Velocità di lettura: "+speed+"\n");
    	//una volta fatti i controlli posso incominciare a selezionare i libri fra cui scegliere per la readathon
    	List<Book> allBooks = new ArrayList<>(this.model.getAllBooksAuthor(minPages, maxPages, authors, rating));
    	List<Book> moreBooks = new ArrayList<>(this.model.getMoreBooks(minPages, maxPages, genres, rating));
    	List<Book> filteredBooks = new ArrayList<>(this.model.filterBooks(allBooks, authors, genres, moreBooks));
    	List<Book> filteredMore = new ArrayList<>(this.model.getFilteredMore());
    	
    	if(filteredBooks.size() == 0 && filteredMore.size() == 0) {
    		this.txtResult.appendText("La selezione non ha prodotto risultati. Prova ad impostare parametri diversi\n");
    	}else {
    		//avvio la ricorsione
        	readathon = new ArrayList<>(this.model.findReadathon(speed, pause, filteredBooks, filteredMore, budget));
        	
        	//risultati
        	this.txtResult.appendText("La soluzione è stata calcolata a partire da "+(filteredBooks.size() + filteredMore.size())+" libri\n\n");
        	if(readathon.size() > 0) {
        		this.txtResult.appendText("Libri trovati: "+readathon.size()+"\n");
            	this.txtResult.appendText("Tempo impiegato su "+this.model.getTime((double)(24-pause))+": circa "+this.model.getTime(this.model.hours(this.model.getMinTot()))+"\n");
            	String price = (String) String.format("%.2f", this.model.getPrice());
            	this.txtResult.appendText("Costo totale dei libri: "+ price+" euro\n");
            	this.dtResult.setItems(FXCollections.observableArrayList(readathon));
        	}else {
        		this.txtResult.appendText("Nessun libro rispetta i vincoli della readathon. Probabilmente, il budget complessivo è troppo basso.");
        	}
        	
    	}
    }

    @FXML
    void resetAuthors(ActionEvent event) {
    	if(this.authors.size() == 3) {
    		this.cmbAuthors.setDisable(false);
    		this.btnAuthor.setDisable(false);
    	}
    	this.cmbAuthors.valueProperty().setValue(null);
    	this.authors = new ArrayList<>();
    }

    @FXML
    void resetGenres(ActionEvent event) {
    	if(this.genres.size() == 3) {
    		this.cmbGenres.setDisable(false);
    		this.btnGenre.setDisable(false);
    	}
    	this.cmbGenres.valueProperty().setValue(null);
    	this.genres = new ArrayList<>();
    }

    @FXML
    void selectBudget(ActionEvent event) {
    	if(this.txtBudget.isDisabled()) {
    		//se il nodo è disabilitato lo abilito
    		this.txtBudget.setDisable(false);
    	}else {
    		this.txtBudget.clear();
    		this.txtBudget.setDisable(true);
    	}
    }

    @FXML
    void initialize() {
        assert btnAuthor != null : "fx:id=\"btnAuthor\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGenre != null : "fx:id=\"btnGenre\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnResetAuthor != null : "fx:id=\"btnResetAuthor\" was not injected: check your FXML file 'Scene.fxml'.";
        assert checkBudget != null : "fx:id=\"checkBudget\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAuthors != null : "fx:id=\"cmbAuthors\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenres != null : "fx:id=\"cmbGenres\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtAuthor != null : "fx:id=\"dtAuthor\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtDescription != null : "fx:id=\"dtDescription\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtFormat != null : "fx:id=\"dtFormat\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtGenre != null : "fx:id=\"dtGenre\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtPages != null : "fx:id=\"dtPages\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtPrice != null : "fx:id=\"dtPrice\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtRatings != null : "fx:id=\"dtRatings\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtResult != null : "fx:id=\"dtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert dtTitle != null : "fx:id=\"dtTitle\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtBudget != null : "fx:id=\"txtBudget\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMaxPages != null : "fx:id=\"txtMaxPages\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinPages != null : "fx:id=\"txtMinPages\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPause != null : "fx:id=\"txtPause\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRating != null : "fx:id=\"txtRating\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert sdrSpeed != null : "fx:id=\"sdrSpeed\" was not injected: check your FXML file 'Scene.fxml'.";
        
        this.dtTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        this.dtAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        this.dtDescription.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
        this.dtFormat.setCellValueFactory(new PropertyValueFactory<Book, String>("bookFormat"));
        this.dtPages.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
        this.dtGenre.setCellValueFactory(new PropertyValueFactory<Book, String>("genre0"));
        this.dtRatings.setCellValueFactory(new PropertyValueFactory<Book, Double>("ratings"));
        this.dtPrice.setCellValueFactory(new PropertyValueFactory<Book, Double>("priceEUR"));
    }

    public void setModel(Model m) {
    	this.model = m;
    	this.cmbAuthors.getItems().clear();
    	this.cmbAuthors.getItems().addAll(this.model.getAllAuthors());
    	this.cmbGenres.getItems().clear();
    	this.cmbGenres.getItems().addAll(this.model.getAllGenres());
    	this.authors = new ArrayList<>();
    	this.genres = new ArrayList<>();
    }
}

