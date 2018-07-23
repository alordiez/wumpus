package es.alordiez.wumpus.domain.game;

public enum Element {
	WUMPUS("WUMPUS", "monstruo", "X")
	,HUNTER("HUNTER", "cazador", "H")
	,PIT("PIT", "pozo", "O")
	,GOLD("GOLD", "oro", "€")
	;
	
	private String en;
	private String es;
	private String icon;
	
	Element(String en, String es, String icon) {
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
