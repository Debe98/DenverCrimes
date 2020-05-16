package it.polito.tdp.crimes.model;

import java.time.Month;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao = new EventsDao();
	Graph<String, DefaultWeightedEdge> grafo;
	List <String> maxPercorso;
	
	
	public List<String> setCategories() {
		return dao.listAllCategories();
	}
	
	public List<Month> setMonts() {
		return dao.listAllMonths();
	}

	public Graph<String, DefaultWeightedEdge> createGraph(String eventClass, Month mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getTypeStrict(eventClass, mese));
		List <Arco> archi= dao.getArchi(eventClass, mese);
		for (Arco a : archi)
			Graphs.addEdge(grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
		//System.out.println(grafo.vertexSet().size());
		//System.out.println(archi.size());
		//System.out.println(grafo.edgeSet().size());
		return grafo;
	}
	
	public double getPesoMedio() {
		int cnt = 0;
		int somma = 0;
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			somma += grafo.getEdgeWeight(e);
			cnt++;
		}
		if (cnt == 0) cnt = 1;
		return (double) somma /cnt;
	}

	public List <String> getMaxPercorso(DefaultWeightedEdge e) {
		maxPercorso = new LinkedList<String>();
		String partenza = grafo.getEdgeSource(e);
		String arrivo = grafo.getEdgeTarget(e);
		
		List <String> inseriti = new LinkedList<String>();
		inseriti.add(partenza);
		ricercaMassimo(inseriti, arrivo);
		return maxPercorso;
	}

	private void ricercaMassimo(List<String> inseriti, String arrivo) {
		List <String> prossimi = Graphs.neighborListOf(grafo, inseriti.get(inseriti.size()-1));
		prossimi.removeAll(inseriti);
		
		if (inseriti.get(inseriti.size()-1).equals(arrivo)) {
			if (inseriti.size() > maxPercorso.size())
				 maxPercorso = new LinkedList<String>(inseriti);
			return;
		}
		
		if (prossimi.isEmpty()) return;
		
		for (String s : prossimi) {
			inseriti.add(s);
			ricercaMassimo(inseriti, arrivo);
			inseriti.remove(inseriti.size()-1);
		}
	}
}
