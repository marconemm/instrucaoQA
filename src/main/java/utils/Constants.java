package utils;

public class Constants {

    public enum CHAVES {
		F_1, F_2, F_3, F_4;
	}

	public static short MAX_SEARCH_ATTEMPTS = 5;
	public static short MAX_WAIT_ATTEMPTS = 15;
//	public static final int TEST_ATTEMPTS = 0;
	public static final String IS_LOGGED = "isLogged";
	public static final String CLIENT_MCI = "clientCPF";
	public static final String IS_READY = "isTheMassReady";

	public static String getNullMsg(String locator) {
		return String.format("Não foi encontrado um elemento com o localizador \"%s\"", locator);
	}

}
