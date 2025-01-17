/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.SquadraPeggiore;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	double media;
	private Model model;
	
	public FXMLController() {
		media=0;
	}

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	
    	txtResult.clear();
    	Team squadra = this.cmbSquadra.getValue();
    	if(squadra == null) {
    		txtResult.appendText("Selezionare una squadra!");
    		return;
    	}
    	
    	txtResult.appendText("SQUADRE PEGGIORI: "+"\n");
    	for(SquadraPeggiore b : this.model.getSquadreBattute(squadra)) {
    		
    		txtResult.appendText(b.getSquadra()+" --> "+b.getDifferenza()+"\n");
    	}
    	
    	txtResult.appendText("\n"+"SQUADRE MIGLIORI: "+"\n");
    	for(SquadraPeggiore b : this.model.getSquadreMigliori(squadra)) {
    		
    		txtResult.appendText(b.getSquadra()+" --> "+b.getDifferenza()+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	this.model.creaGrafo();
    	txtResult.setText("Grafo creato con "+model.nVertici()+" vertici e "+model.nArchi()+" archi."+"\n");
    	this.cmbSquadra.getItems().addAll(model.getTeams());
    	
    }

    /*
     Al termine del campionato, stampare:
		• quanti reporter hanno assistito, in media, ad ogni partita, indipendentemente dalle squadre a cui
		erano assegnati;
		• Il numero di partite per cui il numero totale di reporter (indipendentemente dalla squadra) era
		critico, ovvero minore della soglia X.
     */
    
    
    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	int numeroReporter;
    	int soglia;
    	try{
    		numeroReporter = Integer.parseInt(this.txtN.getText());
    		soglia = Integer.parseInt(this.txtX.getText());
    	}
    	catch(NumberFormatException e) {
    		return;
    	}
    	
    	this.model.simula(numeroReporter);
    	
    	for(Match m : this.model.getMatches()) {
    		Team sCasa = this.model.getTeam(m.getTeamHomeID());
    		Team sTrasferta = this.model.getTeam(m.getTeamAwayID());
    		media += sCasa.getReporter()+sTrasferta.getReporter();
    		
    	}
    	
    	txtResult.appendText("\n"+"Numero di reporter che hanno assistito in media ad ogni partita: "+media/(this.model.getMatches().size()*2));
    	
    	int count = 0;
    	for(Match mm : this.model.getMatches()) {
    		if(mm.getReporter()<soglia) {
    			count++;
    		}
    	}
    	
    	txtResult.appendText("\n"+"Numero di partite per cui il numero totale di reporter era critico: "+count);
    			
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
