package tc.accessibility.market.utils;

public class StringManipulator {
    public static String removeLastSeparator(String title) {
        // Definir os separadores que você quer remover
        char[] separators = {'-', '|', ':'};

        // Variáveis para armazenar o índice da última ocorrência e o separador encontrado
        int lastIndex = -1;

        // Percorre cada separador para encontrar a última ocorrência
        for (char separator : separators) {
            int index = title.lastIndexOf(separator);
            if (index > lastIndex) {
                lastIndex = index;
            }
        }

        // Se encontrar algum separador, retornar a string até a posição dele
        if (lastIndex != -1) {
            return title.substring(0, lastIndex).trim();
        }

        // Se não houver separador, retorna o título original
        return title;
    }
}
