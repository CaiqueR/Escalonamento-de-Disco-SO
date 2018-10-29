import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

class Leitor {
    private static String path;
    int[][] pesos = null;
    private int NumSectors;
    private int NumTracks;
    private int StartPosition;
    ArrayList<String> numEmString = new ArrayList<String>();
    String linhaArq;

    public Leitor(String path) throws Exception {
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

            pesos = new int[7][4];
            int k = 3;

            //Pega os valores de NumSectors e NumTracks e StartPosition separado do resto, so
            //consegui dessa forma pq eu nao consegui ler de uma forma melhor
            NumSectors = Integer.parseInt(numEmString.get(0).replaceAll("\\D", ""));
            NumTracks = Integer.parseInt(numEmString.get(1).replaceAll("\\D", ""));
            StartPosition = Integer.parseInt(numEmString.get(2).replaceAll("\\D", ""));


            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 4; j++) {

                    // Convertendo cada string em Double e armazenando em 'pesos'
                    //Essa parte teria que fazer alguma logica para poder sempre ir pegando de quatro em quatro
                    //Pq so aceita um arquivo com 7 processos
                    if (k == 3 || k == 11 || k == 7 || k == 15 || k == 19 || k == 23 || k == 27)
                        pesos[i][j] = Integer.parseInt(numEmString.get(k).substring(numEmString.get(k).lastIndexOf("=") + 1));
                    else
                        pesos[i][j] = Integer.parseInt(numEmString.get(k));
                    k++;

                }
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

    public void FCFS(int[][] mat) throws Exception {
        double tempMedAcesso=0;
        double tempMedEspera=0;
        double cilindroanterior=0;
        double aux=0;
        Leitor L = new Leitor(path);
        int valoranterior=StartPosition;

        for (int i = 0; i < mat.length; i++) {
            tempMedEspera+=cilindroanterior-mat[i][0];
            tempMedAcesso+= mat[i][2]+Math.abs(mat[i][1]-valoranterior)+mat[i][3];
            aux=mat[i][2]+Math.abs(mat[i][1]-valoranterior)+mat[i][3];
            cilindroanterior += aux;
            valoranterior=mat[i][1];
        }

        System.out.println("Tempo medio de acesso: "+tempMedAcesso/mat.length+
                "\nTempo médio de espera: "+tempMedEspera/mat.length);
    }

    public void imprimeEntrada(){
        System.out.println("NumSectors: "+NumSectors);
        System.out.println("NumTracks: "+NumTracks);
        System.out.println("StartPosition: "+StartPosition);
        for (int i = 0; i < pesos.length; i++) {
            System.out.print("R"+(i+1)+": ");
            for (int j = 0; j < pesos[0].length; j++) {
                System.out.print(pesos[i][j]+" ");
            }
            System.out.println();
        }
    }

}
