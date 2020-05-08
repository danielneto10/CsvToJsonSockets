package csvJson.Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class SalvaJson implements Runnable, ITempos{

	private IControladora icontroladora;
	private Path path;
	private BufferedWriter escrever;
	private float tempo;

	public SalvaJson(String path, IControladora icontroladora) {
		this.icontroladora = icontroladora;
		try {
			this.path = Files.createFile(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		Instant now = Instant.now();
		try {
			escrever = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			while(icontroladora.getContinuarJson()) {
				JsonElement obj = icontroladora.getListJson();
				if(obj == null) {
					synchronized (this) {
						try {
							this.wait(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				else {
					escrever.write(gson.toJson(obj));
					icontroladora.addDadosGravados();
				}
			}
			escrever.close();
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
