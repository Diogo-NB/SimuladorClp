package Controllers;

public class IOController {
    // Armazena as entradas por word (2 words, 8 bits cada)

    private byte[] inputWords = new byte[2];  // 16 bits total
    // Armazena as saídas por word (2 words, 8 bits cada)
    private byte[] outputWords = new byte[2]; // 16 bits total

    // Define o bit da entrada, por word (1 ou 2) e bit (0-7)
    public void setInputBit(int word, int bit, boolean value) {
        if (word < 1 || word > 2 || bit < 0 || bit > 7) {
            throw new IllegalArgumentException("Word ou bit inválido");
        }
        if (value) {
            inputWords[word - 1] |= (1 << bit);  // set bit
        } else {
            inputWords[word - 1] &= ~(1 << bit); // clear bit
        }
    }

    // Lê o bit da entrada
    public boolean getInputBit(int word, int bit) {
        if (word < 1 || word > 2 || bit < 0 || bit > 7) {
            throw new IllegalArgumentException("Word ou bit inválido");
        }
        return (inputWords[word - 1] & (1 << bit)) != 0;
    }

    // Define o bit da saída, por word e bit
    public void setOutputBit(int word, int bit, boolean value) {
        if (word < 1 || word > 2 || bit < 0 || bit > 7) {
            throw new IllegalArgumentException("Word ou bit inválido");
        }
        if (value) {
            outputWords[word - 1] |= (1 << bit);
        } else {
            outputWords[word - 1] &= ~(1 << bit);
        }
    }

    // Lê o bit da saída
    public boolean getOutputBit(int word, int bit) {
        if (word < 1 || word > 2 || bit < 0 || bit > 7) {
            throw new IllegalArgumentException("Word ou bit inválido");
        }
        return (outputWords[word - 1] & (1 << bit)) != 0;
    }

    // Métodos para obter a palavra inteira (byte) da entrada
    public byte getInputWord(int word) {
        if (word < 1 || word > 2) {
            throw new IllegalArgumentException("Word inválido");
        }
        return inputWords[word - 1];
    }

    // Métodos para obter a palavra inteira (byte) da saída
    public byte getOutputWord(int word) {
        if (word < 1 || word > 2) {
            throw new IllegalArgumentException("Word inválido");
        }
        return outputWords[word - 1];
    }

    // Definir a palavra inteira da entrada
    public void setInputWord(int word, byte value) {
        if (word < 1 || word > 2) {
            throw new IllegalArgumentException("Word inválido");
        }
        inputWords[word - 1] = value;
    }

    // Definir a palavra inteira da saída
    public void setOutputWord(int word, byte value) {
        if (word < 1 || word > 2) {
            throw new IllegalArgumentException("Word inválido");
        }
        outputWords[word - 1] = value;
    }
}
