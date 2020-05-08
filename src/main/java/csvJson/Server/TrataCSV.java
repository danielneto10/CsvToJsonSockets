package csvJson.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class TrataCSV implements Runnable, ITempos{
	
	private BufferedReader br;
	private String line;
	private Path path;
	private IControladora Icontroladora;
	private int qtd;
	private float tempo;
	
	public TrataCSV(String arq, IControladora IControladora) {
		this.line = "";
		this.path = Paths.get(arq);
		this.Icontroladora = IControladora;
		this.qtd = -1;
	}

	public int qtdLinhas() {
		try {
			br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
			while((line = br.readLine()) != null) {
				qtd++;
			}
			br.close();
			return qtd;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public void run() {
		Instant now = Instant.now();
		try {
			br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
			while((line = br.readLine()) != null) {
				Icontroladora.addDado(line.replaceAll("\"", ""));
			}
			Icontroladora.setLendoCSV(false);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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