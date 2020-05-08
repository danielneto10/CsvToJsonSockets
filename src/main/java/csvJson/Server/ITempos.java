package csvJson.Server;

import java.time.Instant;

public interface ITempos {
	public float getTempo();
	public void setTempo(Instant now);
}
