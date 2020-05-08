package csvJson.Server;

import com.google.gson.JsonElement;

public interface IControladora {

	public void addDado(String dado);
	public String getListCSV();
	public void setLendoCSV(boolean op);
	public boolean getContinuarCSV();
	public void addDadoJson(JsonElement obj);
	public JsonElement getListJson();
	public void setLendoJson(boolean op);
	public boolean getContinuarJson();
	public int getDadosGravados();
	public void addDadosGravados();
}
