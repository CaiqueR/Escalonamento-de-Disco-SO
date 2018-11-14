import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Processos {
    private int NumSectors;
    private int NumTracks;
    private int StartPosition;
    private ArrayList<String> numEmString = new ArrayList<String>();
    private List<Integer> pesoos = new ArrayList<>();
    private List<List<Integer>> processos = new ArrayList<>();

    public Processos(Leitor L) {
        this.StartPosition = L.StartPosition;
        this.NumSectors = L.NumSectors;
        this.NumTracks = L.NumTracks;
        this.numEmString = L.numEmString;
        this.pesoos = L.pesos;
        this.processos = L.processos;
    }

    private double[] FCFS() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        double cilindroanterior = 0;
        double aux = 0;
        double[] resultado = new double[2];
        int valoranterior = StartPosition;

        for (int i = 0; i < processos.size(); i++) {
            tempMedEspera += cilindroanterior - processos.get(i).get(0);
            tempMedAcesso += processos.get(i).get(2) + Math.abs(processos.get(i).get(1) - valoranterior) + processos.get(i).get(3);
            aux = processos.get(i).get(2) + Math.abs(processos.get(i).get(1) - valoranterior) + processos.get(i).get(3);
            cilindroanterior += aux;
            valoranterior = processos.get(i).get(1);
        }

        resultado[0] = tempMedAcesso / processos.size();
        resultado[1] = tempMedEspera / processos.size();
        return resultado;
    }

    private double[] SSTF() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        double cilindroanterior = 0;
        double aux = 0;
        double maisperto = 9999999;
        int pos = 0;
        double[] resultado = new double[2];
        int valoranterior = StartPosition;
        int valorteste = StartPosition;
        int aux2 = processos.size();
        int espera = 0;
        for (int i = 0; i < aux2; i++) {
            for (int j = 0; j < processos.size(); j++) {
                if (Math.abs(processos.get(j).get(1) - valorteste) <= maisperto && processos.get(j).get(0) <= espera) {
                    maisperto = Math.abs(processos.get(j).get(1) - valorteste);
                    pos = j;

                }

            }
            tempMedEspera += cilindroanterior - processos.get(pos).get(0);
            tempMedAcesso += processos.get(pos).get(2) + Math.abs(processos.get(pos).get(1) - valoranterior) + processos.get(pos).get(3);
            aux = processos.get(pos).get(2) + Math.abs(processos.get(pos).get(1) - valoranterior) + processos.get(pos).get(3);
            cilindroanterior += aux;
            valoranterior = processos.get(pos).get(1);

            valorteste = processos.get(pos).get(1);
            maisperto = 9999999;
            System.out.println(processos.get(pos));
            espera += tempMedAcesso;
            processos.remove(pos);
        }

        resultado[0] = tempMedAcesso / aux2;
        resultado[1] = tempMedEspera / aux2;
        return resultado;
    }

    private double[] SCAN() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        double cilindroanterior = 0;
        double aux = 0;
        double[] resultado = new double[2];
        int valoranterior = StartPosition;

        for (int i = 0; i < processos.size(); i++) {
            tempMedEspera += cilindroanterior - processos.get(i).get(0);
            tempMedAcesso += processos.get(i).get(2) + Math.abs(processos.get(i).get(1) - valoranterior) + processos.get(i).get(3);
            aux = processos.get(i).get(2) + Math.abs(processos.get(i).get(1) - valoranterior) + processos.get(i).get(3);
            cilindroanterior += aux;
            valoranterior = processos.get(i).get(1);
        }

        resultado[0] = tempMedAcesso / processos.size();
        resultado[1] = tempMedEspera / processos.size();
        return resultado;
    }

    private double[] CSCAN() {
        double[] resultado = new double[2];
        return resultado;
    }

    private double[] CLOOK() {
        double[] resultado = new double[2];
        return resultado;
    }

    private double[] MY() {
        double[] resultado = new double[2];
        return resultado;
    }


    public void imprimeEntrada() {
        System.out.println("NumSectors: " + NumSectors);
        System.out.println("NumTracks: " + NumTracks);
        System.out.println("StartPosition: " + StartPosition);
        int cont = 1;
        for (List i : processos) {
            System.out.println("R" + cont + ": " + i);
            cont++;
        }
    }

    public void escreveArquivo() {
        double[] fcfs, sstf, scan;
        fcfs = FCFS();
        scan = SCAN();
        sstf = SSTF();
        try {
            FileWriter arq = new FileWriter("out.txt");
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.printf("FCFS%n" +
                    "-AccessTime=%.2f%n" +
                    "-WaitingTime=%.2f%n" +
                    "SSTF%n" +
                    "-AccessTime=%.2f%n" +
                    "-WaitingTime=%.2f%n" +
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
                    "-WaitingTime=??.??", fcfs[0], fcfs[1], sstf[0], sstf[1], scan[0], scan[1]);


            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
