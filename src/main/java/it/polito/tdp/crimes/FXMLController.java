/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<DefaultWeightedEdge> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	DefaultWeightedEdge e = boxArco.getValue();
    	if (e == null) {
    		txtResult.setText("Impossibile effettuare la ricerca");
    		return;
    	}
    	List <String> best = model.getMaxPercorso(e);
    	txtResult.appendText("\nTrovato il seguente percorso attraverso "+best.size()+" archi:\n");
    	for (String s : best)
    		txtResult.appendText(s+"\n");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	Graph<String, DefaultWeightedEdge> grafo = model.createGraph(boxCategoria.getValue(), boxMese.getValue());
    	double peso = model.getPesoMedio();
    	txtResult.setText("Creato grafo con "+grafo.vertexSet().size()+" vertici e "+ grafo.edgeSet().size()+" archi.\n");
    	txtResult.appendText(String.format("Quelli con peso superiore a %.1f sono:\n", peso));
    	List <DefaultWeightedEdge> mostrati = new LinkedList<DefaultWeightedEdge>();
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e) > peso) {
				txtResult.appendText(grafo.getEdgeSource(e)+" - "+grafo.getEdgeTarget(e)+" ("+(int) grafo.getEdgeWeight(e)+")\n");
				mostrati.add(e);
			}
		}
		boxArco.getItems().clear();
		//boxArco.getItems().addAll(grafo.edgeSet());
		boxArco.getItems().addAll(mostrati);
		try {
			boxArco.setValue(boxArco.getItems().get(0));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ma è tutto OK!!");
		}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxCategoria.getItems().addAll(model.setCategories());
    	boxCategoria.setValue(boxCategoria.getItems().get(0));
    	boxMese.getItems().addAll(model.setMonts());
    	boxMese.setValue(boxMese.getItems().get(0));
    }
}
