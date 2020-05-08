package csvJson.Server;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import com.google.gson.JsonObject;

public class ParseJson implements Runnable, ITempos{

	private IControladora icontroladora;
	private List<String> coluna;
	private List<String> valor;
	private int flag;
	private float tempo;
	
	public ParseJson(IControladora icontroladora) {
		this.icontroladora = icontroladora;
		coluna = new Vector<String>();
		valor = new Vector<String>();
		flag = 1;
	}
	
	public void run() {
		Instant now = Instant.now();
		while(icontroladora.getContinuarCSV()) {
			try {
				String linha = icontroladora.getListCSV();
				if(linha == null) {
					synchronized (this) {
						try {
							this.wait(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else if(flag == 1) {
					flag = 0;
					coluna = Arrays.asList(linha.split(","));
				}
				else {
					JsonObject obj = new JsonObject();
					valor = Arrays.asList(linha.split(","));
					for(int i = 0; i < coluna.size(); i++) {
						obj.addProperty(coluna.get(i), valor.get(i));
					}
					icontroladora.addDadoJson(obj);
				}
			} catch (Exception e) {
				System.out.println("Linha vazia");
			}
			
		}
		icontroladora.setLendoJson(false);
		setTempo(now);
	}

	public float getTempo() {
		return this.tempo;
	}

	public void setTempo(Instant now) {
		Instant end = Instant.now();
		float duracao = Duration.between(now, end).toMillis();
		this.tempo = duracao;
	}
	
}
