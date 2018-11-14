import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

class Leitor {
    private static String path;
    int NumSectors;
    int NumTracks;
    int StartPosition;
    String linhaArq;
    ArrayList<String> numEmString = new ArrayList<String>();
    List<Integer> pesos = new ArrayList<>();
    public List<List<Integer>> processos = new ArrayList<>();


    public Leitor(String path) {
        this.path = path;
        try {
            FileReader arq = new FileReader(path);
            BufferedReader lerArq = new BufferedReader(arq);

            // Lendo cada linha do arquivo
            while ((linhaArq = lerArq.readLine()) != null) {

                // Pegando cada elemento separado por vírgula no arquivo
                for (String caracteres : linhaArq.split(",")) {

                    // Adicionando os elementos em um ArrayList
                    numEmString.add(caracteres);
                }
            }

            int k = 3;

            //Pega os valores de NumSectors e NumTracks e StartPosition separado do resto, so
            //consegui dessa forma pq eu nao consegui ler de uma forma melhor
            NumSectors = Integer.parseInt(numEmString.get(0).replaceAll("\\D", ""));
            NumTracks = Integer.parseInt(numEmString.get(1).replaceAll("\\D", ""));
            StartPosition = Integer.parseInt(numEmString.get(2).replaceAll("\\D", ""));


            int aux = 3;
            for (int i = 0; i < ((numEmString.size() / 4) - 1); i++) {
                for (int j = 0; j < 4; j++) {
                    // Caso o arquivo do Processo comece com RX=X é feito um tratamento para remover o "=" o numero e a letra anterior
                    if (k == aux) {
                        pesos.add(Integer.parseInt(numEmString.get(k).substring(numEmString.get(k).lastIndexOf("=") + 1)));
                        aux += 4;
                    }
                    //Caso seja separado por virgura 10,10,10 é ja colocado nos pesos
                    else
                        pesos.add(Integer.parseInt(numEmString.get(k)));
                    k++;
                }
                processos.add(pesos);
                pesos = new ArrayList<>();
            }
            arq.close();

        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (InputMismatchException f) {
            /*System.out.print("Erro ocorreu em: " + i + " " + j);*/
            f.printStackTrace();
        } catch (IOException f) {
            /*System.out.print("Erro ocorreu em: " + i + " " + j);*/
            f.printStackTrace();
        }
    }
}
