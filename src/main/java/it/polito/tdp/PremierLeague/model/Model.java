package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleDirectedWeightedGraph <Team, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map <Integer, Team> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		dao.listAllTeams(idMap);
	}
	
	public void creaGrafo() {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo vertici
		Graphs.addAllVertices(grafo,idMap.values());
		
		//aggiungo archi
		for (Adiacenza a : dao.getAdiacenze(idMap) ) {
			if(a.getT1().getPunti()-a.getT2().getPunti()>0) { //vuol dire che arco da t1 a t2
			Graphs.addEdge(grafo, a.getT1(), a.getT2(), a.getPeso());
			}
			if(a.getT1().getPunti()-a.getT2().getPunti()<0) { //vuol dire che arco da t2 a t1
				Graphs.addEdge(grafo, a.getT2(), a.getT1(), a.getPeso());
				}
			}
		
		System.out.println("Grafo creato con "+this.grafo.vertexSet().size()+" vertici e "+this.grafo.edgeSet().size()+" archi");
		}
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Set <Team> getTeams(){
		return this.grafo.vertexSet();
	}
	
	}
	

