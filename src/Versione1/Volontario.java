package Versione1;

public class Volontario extends Utente{
	
	private String nickname;
	
	public Volontario(Credenziali credenziali) {
		super(credenziali);
		//this.nickname = nickname;
		// TODO Auto-generated constructor stub
	}

	/*public Volontario(String nickname) {
		super();
		this.nickname = nickname;
	}*/

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
	

}
