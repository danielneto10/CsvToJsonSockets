package csvJson.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import com.google.gson.JsonElement;


public class Controladora  implements IControladora{
	private List<String> filaCSV;
	private List<JsonElement> filaJson;
	private boolean opCSV;
	private boolean opJson;
	private BufferedReader ler;
	private String csvPath;
	private String jsonPath;
	
	private int totLinhas = -1;
	
	private float tempoLeitura;
	private float tempoParse;
	private float tempoGravacao;
	
	private int csvLidos;
	private int jsonLidos;
	private int gravados;
	private Socket cliente;
	
	public Controladora(Socket cliente) {
		filaCSV = new Vector<String>();
		filaJson = new Vector<JsonElement>();
		this.cliente = cliente;
		try {
			this.ler =  new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
			this.csvPath = ler.readLine();
			this.jsonPath = ler.readLine();
			ler.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.csvLidos = 0;
		this.jsonLidos = 0;
	}
	
	///////////////// METODOS INICIAR ///////////////////
	
	public void iniciar() {
		iniciarTrataCSV();
		iniciarParseJson();
		iniciarSalvajSON();
	}
	
	public void iniciarTrataCSV() {
		TrataCSV trataCSV = new TrataCSV(csvPath, this);
		setLendoCSV(true);
		totLinhas = trataCSV.qtdLinhas();
		
		try {
			DataOutputStream totlinha = new DataOutputStream(cliente.getOutputStream());
			totlinha.writeInt(totLinhas);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread tCsv = new Thread(trataCSV);
		tCsv.start();
	}
	
	public void iniciarParseJson() {
		ParseJson parseJson = new ParseJson(this);
		setLendoJson(true);
		Thread tParse = new Thread(parseJson);
		tParse.start();
	}
	
	public void iniciarSalvajSON() {
		SalvaJson salvaJson = new SalvaJson(jsonPath, this);
		Thread tSalvar = new Thread(salvaJson);
		tSalvar.start();
	}
	
	//////////////// CSV /////////////////////
	
	public synchronized void addDado(String dado) {
		this.filaCSV.add(dado);
		this.csvLidos++;
	}
	
	public synchronized String getListCSV() {
		if(filaCSV.size() > 0) {
			return filaCSV.remove(0);
		}
		return null;
	}

	public synchronized void setLendoCSV(boolean op) {

		this.opCSV = op;
	}

	public synchronized boolean getContinuarCSV() {
		return opCSV || filaCSV.size() > 0;
	}

	public synchronized int getQtdCSVLidos() {
		return this.csvLidos - 1;
	}
	
	/////////////////// JSON //////////////////////////
	
	public void addDadoJson(JsonElement obj) {
		filaJson.add(obj);
		this.jsonLidos++;
	}

	public JsonElement getListJson() {
		if(filaJson.size() > 0) {
			return filaJson.remove(0);
		}
		return null;
	}

	public void setLendoJson(boolean op) {
		this.opJson = op;
	}

	public synchronized boolean getContinuarJson() {
		return opJson || filaJson.size() > 0;
	}
	
	public int getQtdJsonLidos() {
		return this.jsonLidos;
	}
	
	///////////// totLinhas /////////////////////////////
	
	public synchronized int getLinhas() {

		return totLinhas;
	}
	

	public int getDadosGravados() {
		return this.gravados;
	}
	

	public void addDadosGravados() {
		this.gravados++;
	}

	////////// ProgressBar ////////////////////
	
	
}
