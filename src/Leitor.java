import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

class Leitor {
    private static String path;
    private int NumSectors;
    private int NumTracks;
    private int StartPosition;
    String linhaArq;
    ArrayList<String> numEmString = new ArrayList<String>();
    List<Integer> pesoos = new ArrayList<>();
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


            int aux=3;
            for (int i = 0; i < ((numEmString.size() / 4) - 1); i++) {
                for (int j = 0; j < 4; j++) {
                    // Convertendo cada string em Double e armazenando em 'pesos'
                    //Essa parte teria que fazer alguma logica para poder sempre ir pegando de quatro em quatro
                    //Pq so aceita um arquivo com 7 processos
                    if (k == aux) {
                        pesoos.add(Integer.parseInt(numEmString.get(k).substring(numEmString.get(k).lastIndexOf("=") + 1)));
                        aux+=4;
                    }else
                        pesoos.add(Integer.parseInt(numEmString.get(k)));
                    k++;
                }
                processos.add(pesoos);
                pesoos = new ArrayList<>();
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

        for (int i = 0; i < processos.size(); i++) {
            tempMedEspera+=cilindroanterior-processos.get(i).get(0);
            tempMedAcesso+= processos.get(i).get(2)+Math.abs(processos.get(i).get(1)-valoranterior)+processos.get(i).get(3);
            aux=processos.get(i).get(2)+Math.abs(processos.get(i).get(1)-valoranterior)+processos.get(i).get(3);
            cilindroanterior += aux;
            valoranterior=processos.get(i).get(1);
        }

        resultado[0]=tempMedAcesso/processos.size();
        resultado[1]=tempMedEspera/processos.size();
        return resultado;
    }

    public double[] SCAN() {
        double tempMedAcesso=0;
        double tempMedEspera=0;
        double cilindroanterior=0;
        double aux=0;
        double[] resultado = new double[2];
        int valoranterior=StartPosition;

        for (int i = 0; i < processos.size(); i++) {
            tempMedEspera+=cilindroanterior-processos.get(i).get(0);
            tempMedAcesso+= processos.get(i).get(2)+Math.abs(processos.get(i).get(1)-valoranterior)+processos.get(i).get(3);
            aux=processos.get(i).get(2)+Math.abs(processos.get(i).get(1)-valoranterior)+processos.get(i).get(3);
            cilindroanterior += aux;
            valoranterior=processos.get(i).get(1);
        }

        resultado[0]=tempMedAcesso/processos.size();
        resultado[1]=tempMedEspera/processos.size();
        return resultado;
    }

    public void imprimeEntrada(){
        System.out.println("NumSectors: "+NumSectors);
        System.out.println("NumTracks: "+NumTracks);
        System.out.println("StartPosition: "+StartPosition);
        int cont=1;
        for (List i: processos) {
            System.out.println("R"+cont+": "+i);
            cont++;
        }
    }

    public void escreveArquivo() {
        double[] fcfs, scan;
        fcfs = FCFS();
        scan = SCAN();
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
                    "-AccessTime=%.2f%n" +
                    "-WaitingTime=%.2f%n" +
                    "C-SCAN%n" +
                    "-AccessTime=%n" +
                    "-WaitingTime=%n" +
                    "C-LOOK%n" +
                    "-AccessTime=%n" +
                    "-WaitingTime=%n" +
                    "MY%n" +
                    "-AccessTime=??.??%n" +
                    "-WaitingTime=??.??", fcfs[0], fcfs[1], scan[0], scan[1]);


            arq.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
