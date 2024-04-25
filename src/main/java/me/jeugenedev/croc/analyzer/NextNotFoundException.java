package me.jeugenedev.croc.analyzer;

/**
 * Класс {@code NextNotFoundException} реализует непроверяемое исключение,
 * возникающее при итерационном анализе текста классами {@link TextAnalyzer}.
 */
public class NextNotFoundException extends RuntimeException {
    /**
     * Текстовый анализатор, спровоцировавший выброс исключения.
     */
    private final TextAnalyzer textAnalyzer;

    /**
     * Создает экземпляр исключения на основе текстового анализатора.
     * @param textAnalyzer Анализатор, который выбрасывает исключение
     */
    public NextNotFoundException(TextAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    /**
     * Переопределяет реализацию получения сообщения.
     * @return Сообщение с подробным описанием причины выброса исключения
     */
    @Override
    public String getMessage() {
        return """
                The %s class cannot find the next sequence because it has reached \
                the end of the text being analyzed"""
                .formatted(this.textAnalyzer.getClass().getName());
    }
}
