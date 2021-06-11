package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	
	/*
	 le squadre battute da s, ovvero quelle arrivate in una posizione di classifica peggiore,
	 ordinate per differenza punti con s.
	 */
	public List <SquadraPeggiore> getSquadreBattute(Team squadra){
		List <SquadraPeggiore> result = new ArrayList<>();
		for(Team t : this.getTeams()) {
			if(t.getPunti()< squadra.getPunti()) {
				SquadraPeggiore s = new SquadraPeggiore(t, squadra.getPunti()-t.getPunti());
				result.add(s);
			}
		}
		Collections.sort(result, new Comparator <SquadraPeggiore> () {

			@Override
			public int compare(SquadraPeggiore o1, SquadraPeggiore o2) {
				
				return o1.getDifferenza()-o2.getDifferenza();
			}
			
		});
		
		return result;
		
		
	}
	
	public List <SquadraPeggiore> getSquadreMigliori(Team squadra){
		List <SquadraPeggiore> result = new ArrayList<>();
		for(Team t : this.getTeams()) {
			if(t.getPunti()> squadra.getPunti()) {
				SquadraPeggiore s = new SquadraPeggiore(t, t.getPunti()-squadra.getPunti());
				result.add(s);
			}
		}
		Collections.sort(result, new Comparator <SquadraPeggiore> () {

			@Override
			public int compare(SquadraPeggiore o1, SquadraPeggiore o2) {
				
				return o1.getDifferenza()-o2.getDifferenza();
			}
			
		});
		
		return result;
		
		
	}
	
	
	}
	

