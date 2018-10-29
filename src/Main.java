import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) throws Exception{
        Leitor L = new Leitor("in.txt");

        L.imprimeEntrada();
        L.FCFS(L.pesos);
    }



}
