package it.polito.tdp.PremierLeague.model;

public class SquadraPeggiore {

	Team squadra;
	Integer differenza;
	public SquadraPeggiore(Team squadra, Integer differenza) {
		super();
		this.squadra = squadra;
		this.differenza = differenza;
	}
	public Team getSquadra() {
		return squadra;
	}
	public void setSquadra(Team squadra) {
		this.squadra = squadra;
	}
	public Integer getDifferenza() {
		return differenza;
	}
	public void setDifferenza(Integer differenza) {
		this.differenza = differenza;
	}
	
	
	
	
}
