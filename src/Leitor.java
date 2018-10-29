import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;

class Leitor {
    private static String path;
    public static int[][] pesos = null;
    private int NumSectors;
    private int NumTracks;
    private int StartPosition;
    ArrayList<String> numEmString = new ArrayList<String>();
    String linhaArq;

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

    public double[] FCFS() {
        double tempMedAcesso=0;
        double tempMedEspera=0;
        double cilindroanterior=0;
        double aux=0;
        double[] resultado = new double[2];
        int valoranterior=StartPosition;

        for (int i = 0; i < pesos.length; i++) {
            tempMedEspera+=cilindroanterior-pesos[i][0];
            tempMedAcesso+= pesos[i][2]+Math.abs(pesos[i][1]-valoranterior)+pesos[i][3];
            aux=pesos[i][2]+Math.abs(pesos[i][1]-valoranterior)+pesos[i][3];
            cilindroanterior += aux;
            valoranterior=pesos[i][1];
        }

        resultado[0]=tempMedAcesso/pesos.length;
        resultado[1]=tempMedEspera/pesos.length;
        return resultado;
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

    public void escreveArquivo() {
        double[] fcfs;
        fcfs = FCFS();
        try {
            FileWriter arq = new FileWriter("out.txt");
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.printf("FCFS%n" +
                    "-AccessTime=%.2f%n" +
                    "-WaitingTime=%.2f%n" +
                    "SSTF%n" +
                    "-AccessTime=%n" +
                    "-WaitingTime=%n" +
                    "SCAN%n" +
                    "-AccessTime=%n" +
                    "-WaitingTime=%n" +
                    "C-SCAN%n" +
                    "-AccessTime=%n" +
                    "-WaitingTime=%n" +
                    "C-LOOK%n" +
                    "-AccessTime=%n" +
                    "-WaitingTime=%n" +
                    "MY%n" +
                    "-AccessTime=??.??%n" +
                    "-WaitingTime=??.??", fcfs[0], fcfs[1]);


            arq.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Ordena em ordem crescente a ordem de chegada da matriz
    public int[][] ordenador(){
        int[] aux = new int[4];

        for(int i = 0; i<pesos.length; i++) {
            for (int j = 0; j < pesos.length - 1; j++) {
                if (pesos[j][0] > pesos[j + 1][0]) {
                    for (int k = 0; k < aux.length; k++)
                        aux[k] = pesos[j][k];
                    for (int k = 0; k < aux.length; k++)
                        pesos[j][k] = pesos[j + 1][k];
                    for (int k = 0; k < aux.length; k++)
                        pesos[j + 1][k] = aux[k];

                }
            }
        }
        return pesos;
    }
}
