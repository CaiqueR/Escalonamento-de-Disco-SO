
public class Main {
    public static void main(String[] args) throws Exception{
        Leitor L = new Leitor("in.txt");
        Processos P = new Processos(L);

        P.imprimeEntrada();
        P.escreveArquivo();
    }



}
