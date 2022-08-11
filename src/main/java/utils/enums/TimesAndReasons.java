package utils.enums;

public enum TimesAndReasons {
    OPEN_CALENDAR("abrir o calendário", 2, false),
    TO_RENDER("a renderização do componente", 3, false),
    LOAD_LOGIN("o carregamento dos elementos de Login", 10, false),
    END_SEC("o encerramento desta sessão", 15, false),
    UPDATE_URL("para a atualização da URL", 20, false),
    PAGE_LOAD("o carregamento da página", 5, false),

    //Compulsory waits:,
    CAP_SCRN("a captura de tela", 0, true),
    TO_SEARCH("obter o resultado da pesquisa", 2, true),
    LOAD_PLAT("carregamento da Plataforma", 30, true),
    PREV_TEST("conclusão de um teste anterior", 10, true);

    private final String reason;
    private final long delay;
    private final boolean isCompulsory;

    TimesAndReasons(String reason, int delay, boolean isCompulsory) {
        this.reason = reason;
        this.delay = delay;
        this.isCompulsory = isCompulsory;
    }

    public String getReason() {
        if (reason == null)
            return "";

        return reason;
    }

    public long getDelay() {
        return delay;
    }

    public boolean isCompulsory() {
        return isCompulsory;
    }
}
