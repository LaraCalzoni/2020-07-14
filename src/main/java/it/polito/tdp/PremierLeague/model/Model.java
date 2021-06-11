package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
	
	public void simula(int numero) {
		
				
		for (Match m : this.dao.getPartiteOrdinate()) {
			Team squadraCasa = idMap.get(m.getTeamHomeID());
			Team squadraTrasferta = idMap.get(m.getTeamAwayID());
			squadraCasa.setReporter(numero);
			squadraTrasferta.setReporter(numero);
			List<SquadraPeggiore> lista1 = this.getSquadreMigliori(squadraCasa);
			List<SquadraPeggiore> lista2 = this.getSquadreBattute(squadraTrasferta);
			List<SquadraPeggiore> lista3 = this.getSquadreMigliori(squadraTrasferta);
			List<SquadraPeggiore> lista4 = this.getSquadreBattute(squadraCasa);
			
			if(m.getResultOfTeamHome()==1) { //vuol dire che ha vinto squadra in casa
				
				double x = Math.random();
				Random random = new Random();
				
				if(x<=0.5) {
					if(squadraCasa.getReporter()>0) {
						
						if(lista1.size()>0) {
							squadraCasa.decreaseReporter();
							SquadraPeggiore t =lista1.get(random.nextInt(lista1.size()));
							t.getSquadra().increaseReporter();
						}
						
					}
				}
				
				
				if(x<=0.2) {
					
					if(squadraTrasferta.getReporter()>0) {
						int numReporter = squadraTrasferta.getReporter();
						int numBocciati = (int) (Math.random()*(numReporter-0));
						
						if(lista2.size()>0) {
							squadraTrasferta.setReporter(numReporter-numBocciati);
							SquadraPeggiore t =lista2.get(random.nextInt(lista2.size()));
							t.getSquadra().setReporter(t.getSquadra().getReporter()+numBocciati);
							
						}
							
						
						
					}
				}
				
			}
			
		if(m.getResultOfTeamHome()==-1) { //vuol dire che ha vinto squadra in trasferta
				
				double x = Math.random();
				Random random = new Random();
				
				if(x<=0.5) {
					if(squadraTrasferta.getReporter()>0) {
						
						if(lista3.size()>0) {
							squadraTrasferta.decreaseReporter();
							SquadraPeggiore t =lista3.get(random.nextInt(lista3.size()));
							t.getSquadra().increaseReporter();
						}
						
					}
				}
				
				
				if(x<=0.2) {
					
					if(squadraCasa.getReporter()>0) {
						int numReporter = squadraCasa.getReporter();
						int numBocciati = (int) (Math.random()*(numReporter-0));
						
						if(lista4.size()>0) {
							squadraCasa.setReporter(numReporter-numBocciati);
							SquadraPeggiore t =lista4.get(random.nextInt(lista4.size()));
							t.getSquadra().setReporter(t.getSquadra().getReporter()+numBocciati);
							
						}
							
						
						
					}
				}
				
			}
			
			
			
		}
		
		
		
	}
	
	public List <Match> getMatches(){
		return dao.listAllMatches();
	}
	
	public Team getTeam(Integer id) {
		for(Team t : this.getTeams()) {
			if(t.getTeamID() == id) {
				return t;
			}
		}
		return null;
	}
	
	
	
	}
	

