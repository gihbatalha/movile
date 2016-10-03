package hello;

public class Cancelamento {
	
	private String nomeOperadora = "";
	private int total = 0;
	private int voluntario = 0;
	private int involuntario = 0;
	
	public Cancelamento(String nomeOperadora) {
		super();
		this.nomeOperadora = nomeOperadora;
	}
		
	public String getNomeOperadora() {
		return nomeOperadora;
	}


	public void setNomeOperadora(String nomeOperadora) {
		this.nomeOperadora = nomeOperadora;
	}

	public void addVoluntario(){
		voluntario++;
		total++;		
	}
	public void addInvoluntario(){
		involuntario++;
		total++;		
	}
	public int getTotal() {
		return total;
	}
	public int getVolutario() {
		return voluntario;
	}
	public int getInvoluntario() {
		return involuntario;
	}

	
	
}
