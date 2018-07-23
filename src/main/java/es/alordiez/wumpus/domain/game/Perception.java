package es.alordiez.wumpus.domain.game;

public enum Perception {
	SCREAM("scream", "grito", "!!")
	,STENCH("stench", "hedor", "~~")
	,BREEZE("breeze", "brisa", "#")
	,GLITTER("glitter", "brillo", "$")
	,BUMP("bump", "choque", "|")
	,NO_ARROWS("no_arrow", "sin_flecha", "0");
	
	private String en;
	private String es;
	private String icon;
	
	Perception(String en, String es, String icon) {
		this.en = en;
		this.es = es;
		this.icon = icon;
	}

	public String getEn() {
		return en;
	}

	public String getEs() {
		return es;
	}

	public String getIcon() {
		return icon;
	}
}
