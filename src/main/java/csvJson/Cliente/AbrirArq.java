package csvJson.Cliente;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AbrirArq {
	
	private FileChooser arq;

	public AbrirArq() {
		arq = new FileChooser();
	}
	
	public File abrir() {
		arq.getExtensionFilters().add(new ExtensionFilter("CSV File (*.csv)", "*.csv"));
		return arq.showOpenDialog(null);
	}
	
	public File salvar() {
		arq.getExtensionFilters().add(new ExtensionFilter("Json File (*.json)", "*.json"));
		return arq.showSaveDialog(null);
	}
}
