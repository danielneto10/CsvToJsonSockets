package csvJson.Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import csvJson.Server.Controladora;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TelaController implements Initializable{

    @FXML
    private Button btnAbrir;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnConvert;

    @FXML
    private ProgressBar PgBLeitura;

    @FXML
    private ProgressBar PgBGravacao;

    @FXML
    private TextField txtPathAbrir;

    @FXML
    private TextField txtPathSalvar;

    @FXML
    private TextArea txtDados;

    @FXML
    private TextArea txtTempo;

    @FXML
    private ProgressBar PgBConversao;

    private File arqAbrir;
    private File arqSalvar;
    
	private Controladora controller;

	private Task<Void> taskPgBParse;

	private Task<Void> taskPgBLeitura;

	private Task<Void> taskPgBGravacao;
	
	private Socket cliente;
	
	private DataInputStream saida;
	
    ConexaoCliente conexao = new ConexaoCliente();

	private int max;
    
    public void initialize(URL location, ResourceBundle resources) {
    	btnSalvar.setDisable(true);
    	btnConvert.setDisable(true);
	}

	@FXML
    void abrirArq(ActionEvent event) {
    	arqAbrir = new AbrirArq().abrir();
    	if(arqAbrir != null) {
    		btnSalvar.setDisable(false);
    		txtPathAbrir.setText(arqAbrir.getAbsolutePath());
    	}
    	else {
    		System.out.println("Arquivo nulo");
    	}
    }

    @FXML
    void convertArq(ActionEvent event) {
    	cliente = conexao.connectar(arqAbrir.getAbsolutePath(), arqSalvar.getAbsolutePath());
    }

    @FXML
    void salvarArq(ActionEvent event) {
    	arqSalvar = new AbrirArq().salvar();
    	if(arqSalvar != null) {
    		btnConvert.setDisable(false);
    		txtPathSalvar.setText(arqSalvar.getAbsolutePath());
    	}
    	else {
    		System.out.println("Arquivo nulo");
    	}
    }
	
    private void atualizarTela() {
    	try {
			saida = new DataInputStream(cliente.getInputStream());
			max = saida.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
////////////// ProgressBar ///////////////////////////////
    	taskPgBLeitura = new Task<Void>() {

    		@Override
    		protected Void call() throws Exception {
    			
    			while(controller.getContinuarCSV()) {
    				updateProgress(controller.getQtdCSVLidos(), max);
    			}
    			updateMessage("Linhas lidas do CSV: " + controller.getQtdCSVLidos());
    			return null;
    		}
    	};
    	
    	taskPgBParse = new Task<Void>() {
    		
    		@Override
    		protected Void call() throws Exception {
    			
    			while(controller.getContinuarJson()) {
    				updateProgress(controller.getQtdJsonLidos(), max);
    			}
    			updateMessage("Linhas convertidas pra Json: " + controller.getQtdJsonLidos());
    			return null;
    		}
    	};
    	
    	taskPgBGravacao = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
    			while(controller.getContinuarJson()) {
    				updateProgress(controller.getDadosGravados(), max);
    			}
    			updateMessage("Linhas Gravadas: " + controller.getDadosGravados());
    			return null;
			}
		};
		
    	PgBLeitura.progressProperty().bind(taskPgBLeitura.progressProperty());
    	PgBConversao.progressProperty().bind(taskPgBParse.progressProperty());
    	PgBGravacao.progressProperty().bind(taskPgBGravacao.progressProperty());
    	new Thread(taskPgBLeitura).start();
    	new Thread(taskPgBParse).start();
    	new Thread(taskPgBGravacao).start();
    	
/////////////// AppendText ///////////////////////////////
    	
    	taskPgBLeitura.messageProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				txtDados.appendText("\n" + taskPgBLeitura.getMessage());
			}
		});
    	
    	taskPgBParse.messageProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				txtDados.appendText("\n" + taskPgBParse.getMessage());
			}
		});
    	
    	taskPgBGravacao.messageProperty().addListener(new ChangeListener<String>() {

			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				txtDados.appendText("\n" + taskPgBGravacao.getMessage());
			}
		});
    	
    }
	
}
