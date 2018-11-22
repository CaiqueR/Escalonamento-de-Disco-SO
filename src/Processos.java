import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Processos {
    // Criacao de todas as variaveis que serao utilizadas

    private int NumSectors;
    private int NumTracks;
    private int StartPosition;
    private List<List<Integer>> processos;

    public Processos(Leitor L) {
        this.StartPosition = L.StartPosition;
        this.NumSectors = L.NumSectors;
        this.NumTracks = L.NumTracks;
        this.processos = L.processos;
    }

    private double[] FCFS() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        //Pega o resultado do cilindro anterior e soma com o valoratual
        double resultadoCilindroAnterior = 0;
        // Pega o valor do cilindro anterior e atribui a variavel
        int valorCilindroAnterior = StartPosition;

        // variavel para verificar se o processo ja chegou
        int tempMedParcialdeEspera = 0;

        double[] resultado = new double[2];


        for (int i = 0; i < processos.size(); i++) {
            // Verificar se o processo chegou
            if (processos.get(i).get(0) <= tempMedParcialdeEspera) {
                tempMedEspera += resultadoCilindroAnterior - processos.get(i).get(0);
                tempMedAcesso += processos.get(i).get(2) + Math.abs(processos.get(i).get(1) - valorCilindroAnterior) + processos.get(i).get(3);
                resultadoCilindroAnterior += processos.get(i).get(2) + Math.abs(processos.get(i).get(1) - valorCilindroAnterior) + processos.get(i).get(3);
                valorCilindroAnterior = processos.get(i).get(1);

                tempMedParcialdeEspera += tempMedAcesso;
            }
        }

        resultado[0] = tempMedAcesso / processos.size();
        resultado[1] = tempMedEspera / processos.size();
        return resultado;
    }

    private double[] SSTF() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        //Pega o resultado do cilindro anterior e soma com o valoratual
        double resultadoCilindroAnterior = 0;

        // Pega o valor do cilindro anterior e atribui a variavel
        int valorCilindroAnterior = StartPosition;

        // Varial com funcao de obter o valor mais perto do cilindro atual, é atribuido como
        // valor inicial 9999999 para se conseguir o menor valor
        double maisPerto = 9999999;

        // variavel para verificar se o processo ja chegou
        int tempMedParcialdeEspera = 0;

        // Variavel para guardar a posicao que foi obtida do processo mais perto
        int pos = 0;

        int valorteste = StartPosition;

        // Variavel para armazenar numeros de processos existentes, pois sao removidos
        int numeroDeProcessos = processos.size();

        double[] resultado = new double[2];

        // Criado um novo List, pois os processos sao removidos da lista, entao e criada um
        // lista nova para armazenar os valores e nao atrapalhar os outros metodos
        List<List<Integer>> processossstf = new ArrayList<>(processos);

        for (int i = 0; i < numeroDeProcessos; i++) {
            for (int j = 0; j < processossstf.size(); j++) {
                // Condicao para verificar o processo mais perto e verificar se o processo chegou
                if (Math.abs(processossstf.get(j).get(1) - valorteste) <= maisPerto && processossstf.get(j).get(0) <= tempMedParcialdeEspera) {
                    maisPerto = Math.abs(processossstf.get(j).get(1) - valorteste);
                    pos = j;
                }

            }
            tempMedEspera += resultadoCilindroAnterior - processossstf.get(pos).get(0);
            tempMedAcesso += processossstf.get(pos).get(2) + Math.abs(processossstf.get(pos).get(1) - valorCilindroAnterior) + processossstf.get(pos).get(3);
            resultadoCilindroAnterior += processossstf.get(pos).get(2) + Math.abs(processossstf.get(pos).get(1) - valorCilindroAnterior) + processossstf.get(pos).get(3);
            valorCilindroAnterior = processossstf.get(pos).get(1);

            valorteste = processossstf.get(pos).get(1);
            maisPerto = 9999999;
            tempMedParcialdeEspera += tempMedAcesso;
            processossstf.remove(pos);
        }

        resultado[0] = tempMedAcesso / numeroDeProcessos;
        resultado[1] = tempMedEspera / numeroDeProcessos;
        return resultado;
    }

    private double[] SCAN() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        //Pega o resultado do cilindro anterior e soma com o valoratual
        double resultadoCilindroAnterior = 0;

        // Pega o valor do cilindro anterior e atribui a variavel
        int valorCilindroAnterior = StartPosition;

        // Variavel para verificar se o cilindro procurado foi encontrado
        boolean achou = false;

        // Variavel usada como auxiliar para pegar a posicao obtida
        int pos = 0;

        // Variavel para armazenar numeros de processos existentes, pois sao removidos
        int numeroDeProcessos = processos.size();

        // variavel para verificar se o processo ja chegou
        int tempMedParcialdeEspera = 0;

        // Criado um novo List, pois os processos sao removidos da lista, entao e criada um
        // lista nova para armazenar os valores e nao atrapalhar os outros metodos
        List<List<Integer>> processosscan = new ArrayList<>(processos);

        double[] resultado = new double[2];

        // Variavel para buscar o processo, iniciada com o primeiro processo
        // para busca-lo inicialmente
        int valorBuscado = processosscan.get(0).get(1);

        // Variavel para obter o primeiro valor dos processos
        int auxBuscado = valorBuscado;

        int controlador = 0;

        // Variavel utilizada para quando o cilindro bater no NumTracks ou 0
        int variavelSomaAuxiliar = 0;

        boolean auxiliar = true;
        // Organizando a List para conseguir pegar sempre os maiores processos e depois voltar nos menores que restaram
        processosscan = processosscan.stream().sorted(Comparator.comparing(x -> x.get(1))).collect(Collectors.toList());

        while (processosscan.size() != 0) {
            //For para percorrer todos os processos e verificar se existem
            for (int j = valorCilindroAnterior; j < NumSectors; j++) {

                // For para verificar se o cilindro atual e o desejado, caso seja, e pego sua
                // posicao e atribuido true a variavel achou
                if (j == valorBuscado)
                    for (int k = 0; k < processosscan.size(); k++) {
                        // If para verificar encontrar o primeiro processo R1
                        if (processosscan.get(k).get(1) == valorBuscado && processosscan.get(k).get(0) <= tempMedParcialdeEspera && auxiliar) {
                            pos = k;
                            achou = true;
                            auxiliar = false;
                        }

                        if (processosscan.get(k).get(1) == j && processosscan.get(k).get(0) <= tempMedParcialdeEspera && auxiliar == false) {
                            pos = k;
                            achou = true;
                            System.out.println(processosscan.get(k).get(1));
                            System.out.println(processosscan);

                        }
                        if (auxiliar == false)
                            valorBuscado = j + 1;

                    }

                // If para verificar se o cilindro chegou ate o final(NumTracks)
                if (j == NumSectors - 1) {
                    variavelSomaAuxiliar += Math.abs(j - valorCilindroAnterior) + Math.abs(valorBuscado - j);
                    valorCilindroAnterior = 0;
                    for (int l = valorBuscado; l >= 0; l--) {

                        // For para verificar se o cilindro atual e o desejado, caso seja, e pego sua
                        // posicao e atribuido true a variavel achou
                        if (l == valorBuscado)
                            for (int k = 0; k < processosscan.size(); k++) {
                                // Verificar se o processo desejado é igual ao que estamos percorrendo e verificar se o processo chegou
                                if (processosscan.get(k).get(1) == valorBuscado && processosscan.get(k).get(0) <= tempMedParcialdeEspera && auxiliar) {
                                    pos = k;
                                    achou = true;
                                    // Auxiliar usada para nao enctrar mais neste if apos encontrar R1
                                    auxiliar = false;
                                }

                                // If para encontrar os processos restantes depois de ter encontrado o primeiro processo R1
                                if (processosscan.get(k).get(1) == l && processosscan.get(k).get(0) <= tempMedParcialdeEspera && auxiliar == false) {
                                    pos = k;
                                    achou = true;
                                }

                                if (auxiliar == false)
                                    valorBuscado = l - 1;

                                if (l == 0) {
                                    achou = true;

                                    // Se controlador for 0 é feito a soma na variavaelSomaAuxiliar
                                    // e adicionado 1 a variavel controlador para nao entrar nesse if novamente
                                    if (controlador == 0) {
                                        variavelSomaAuxiliar += valorCilindroAnterior + processosscan.get(pos).get(1);
                                        controlador++;
                                    }
                                }


                                // Verificar se caso tenha achado o processo desejado, se sim
                                // os calculos sao feitos
                                if (achou && processosscan.get(pos).get(1) == auxBuscado) {
                                    tempMedEspera += resultadoCilindroAnterior - processosscan.get(pos).get(0);
                                    tempMedAcesso += processosscan.get(pos).get(2) + processosscan.get(pos).get(3) + variavelSomaAuxiliar;
                                    resultadoCilindroAnterior += processosscan.get(pos).get(2) + processosscan.get(pos).get(3) + variavelSomaAuxiliar;
                                    valorCilindroAnterior = processosscan.get(pos).get(1);
                                    variavelSomaAuxiliar = 0;
                                    processosscan.remove(pos);
                                    pos = 0;
                                    achou = false;
                                    tempMedParcialdeEspera += tempMedAcesso;

                                } else if (achou && controlador == 1 && l == 0) {
                                    tempMedEspera += resultadoCilindroAnterior - processosscan.get(pos).get(0);
                                    tempMedAcesso += processosscan.get(pos).get(2) + processosscan.get(pos).get(3) + variavelSomaAuxiliar;
                                    resultadoCilindroAnterior += processosscan.get(pos).get(2) + processosscan.get(pos).get(3) + variavelSomaAuxiliar;
                                    valorCilindroAnterior = processosscan.get(pos).get(1);
                                    variavelSomaAuxiliar = 0;
                                    processosscan.remove(pos);
                                    pos = 0;
                                    achou = false;
                                    tempMedParcialdeEspera += tempMedAcesso;
                                    controlador++;

                                } else if (achou) {
                                    tempMedEspera += resultadoCilindroAnterior - processosscan.get(pos).get(0);
                                    tempMedAcesso += processosscan.get(pos).get(2) + Math.abs(processosscan.get(pos).get(1) - valorCilindroAnterior) + processosscan.get(pos).get(3) + variavelSomaAuxiliar;
                                    resultadoCilindroAnterior += processosscan.get(pos).get(2) + Math.abs(processosscan.get(pos).get(1) - valorCilindroAnterior) + processosscan.get(pos).get(3) + variavelSomaAuxiliar;
                                    valorCilindroAnterior = processosscan.get(pos).get(1);
                                    variavelSomaAuxiliar = 0;
                                    processosscan.remove(pos);
                                    pos = 0;
                                    achou = false;
                                    tempMedParcialdeEspera += tempMedAcesso;
                                }

                                // Somente pode atribuir true para achou se o cilindro estiver no 0
                                if (l == 0)
                                    achou = true;

                            }


                    }
                }


                // Verificar se caso tenha achado o processo desejado, se sim
                // os calculos sao feitos
                if (achou) {
                    for (int k = 0; k < processosscan.size(); k++) {
                        tempMedEspera += resultadoCilindroAnterior - processosscan.get(k).get(0);
                        tempMedAcesso += processosscan.get(k).get(2) + Math.abs(processosscan.get(k).get(1) - valorCilindroAnterior) + processosscan.get(k).get(3);
                        resultadoCilindroAnterior += processosscan.get(k).get(2) + Math.abs(processosscan.get(k).get(1) - valorCilindroAnterior) + processosscan.get(k).get(3);
                        valorCilindroAnterior = processosscan.get(k).get(1);
                        variavelSomaAuxiliar = 0;
                        processosscan.remove(k);
                        pos = 0;
                        tempMedParcialdeEspera += tempMedAcesso;
                    }
                }

            }
        }

        resultado[0] = tempMedAcesso / numeroDeProcessos;
        resultado[1] = tempMedEspera / numeroDeProcessos;
        return resultado;
    }

    private double[] CSCAN() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        //Pega o resultado do cilindro anterior e soma com o valoratual
        double resultadoCilindroAnterior = 0;

        // Pega o valor do cilindro anterior e atribui a variavel
        int valorCilindroAnterior = StartPosition;

        // Variavel para verificar se o cilindro procurado foi encontrado
        boolean achou = false;

        // Variavel usada como auxiliar para pegar a posicao obtida
        int pos = 0;

        // Variavel para armazenar numeros de processos existentes, pois sao removidos
        int numeroDeProcessos = processos.size();

        // variavel para verificar se o processo ja chegou
        int tempMedParcialdeEspera = 0;

        // Criado um novo List, pois os processos sao removidos da lista, entao e criada um
        // lista nova para armazenar os valores e nao atrapalhar os outros metodos
        List<List<Integer>> processoscscan = new ArrayList<>(processos);

        double[] resultado = new double[2];

        // Variavel para buscar o processo, iniciada com o primeiro processo
        // para busca-lo inicialmente
        int valorBuscado = processoscscan.get(0).get(1);

        // Variavel utilizada para quando o cilindro bater no NumTracks ou 0
        int variavelSomaAuxiliar = 0;


        boolean auxiliar = true;

        // Organizando a List para conseguir pegar sempre os maiores processos e depois voltar nos menores que restaram
        processoscscan = processoscscan.stream().sorted(Comparator.comparing(x -> x.get(1))).collect(Collectors.toList());

        while (processoscscan.size() != 0) {
            //For para percorrer todos os processos e verificar se existem
            for (int j = valorCilindroAnterior; j < NumSectors; j++) {

                // For para verificar se o cilindro atual e o desejado, caso seja, e pego sua
                // posicao e atribuido true a variavel achou
                // If para verificar se a posicao atual do For e igual ao valor buscado
                if (j == valorBuscado)
                    for (int k = 0; k < processoscscan.size(); k++) {
                        // If para verificar encontrar o primeiro processo R1
                        if (processoscscan.get(k).get(1) == valorBuscado && processoscscan.get(k).get(0) <= tempMedParcialdeEspera && auxiliar) {
                            pos = k;
                            achou = true;
                            // Auxiliar usada para nao enctrar mais neste if apos encontrar R1
                            auxiliar = false;
                        }

                        // If para encontrar os processos restantes depois de ter encontrado o primeiro processo R1
                        if (processoscscan.get(k).get(1) == j && processoscscan.get(k).get(0) <= tempMedParcialdeEspera && auxiliar == false) {
                            pos = k;
                            achou = true;

                        }

                        if (auxiliar == false)
                            valorBuscado = j + 1;
                    }

                // If para verificar se o cilindro chegou ate o final(NumTracks)
                if (j == NumSectors - 1) {
                    variavelSomaAuxiliar += Math.abs(j - valorCilindroAnterior) + Math.abs(0 - j);
                    valorCilindroAnterior = 0;
                    if (auxiliar == false)
                        valorBuscado = 0;
                }

                // Verificar se caso tenha achado o processo desejado, se sim
                // os calculos sao feitos
                if (achou) {
                    tempMedEspera += resultadoCilindroAnterior - processoscscan.get(pos).get(0);
                    tempMedAcesso += processoscscan.get(pos).get(2) + Math.abs(processoscscan.get(pos).get(1) - valorCilindroAnterior) + processoscscan.get(pos).get(3) + variavelSomaAuxiliar;
                    resultadoCilindroAnterior += processoscscan.get(pos).get(2) + Math.abs(processoscscan.get(pos).get(1) - valorCilindroAnterior) + processoscscan.get(pos).get(3) + variavelSomaAuxiliar;
                    valorCilindroAnterior = processoscscan.get(pos).get(1);
                    variavelSomaAuxiliar = 0;
                    processoscscan.remove(pos);
                    pos = 0;
                    achou = false;
                    tempMedParcialdeEspera += tempMedAcesso;
                }

            }
        }

        resultado[0] = tempMedAcesso / numeroDeProcessos;
        resultado[1] = tempMedEspera / numeroDeProcessos;
        return resultado;
    }

    private double[] CLOOK() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        //Pega o resultado do cilindro anterior e soma com o valoratual
        double resultadoCilindroAnterior = 0;

        // Pega o valor do cilindro anterior e atribui a variavel
        int valorCilindroAnterior = StartPosition;

        // Variavel para verificar se o cilindro procurado foi encontrado
        boolean achou = false;

        // Variavel usada como auxiliar para pegar a posicao obtida
        int pos = 0;

        // Variavel para armazenar numeros de processos existentes, pois sao removidos
        int numeroDeProcessos = processos.size();

        // variavel para verificar se o processo ja chegou
        int tempMedParcialdeEspera = 0;

        // Criado um novo List, pois os processos sao removidos da lista, entao e criada um
        // lista nova para armazenar os valores e nao atrapalhar os outros metodos
        List<List<Integer>> processosclook = new ArrayList<>(processos);

        // Variavel que obtem o maior valor do cilindro de todos os processos
        int valorfinal = processosclook.stream().sorted(Comparator.comparing(x -> x.get(1))).collect(Collectors.toList()).get(processosclook.size() - 1).get(1) + 1;

        double[] resultado = new double[2];

        //For para percorrer todos os processos e verificar se existem
        for (int j = processosclook.get(0).get(1); j <= valorfinal; j++) {
            // Organizando a List para conseguir pegar sempre os maiores processos e depois voltar nos menores que restaram
            processosclook = processosclook.stream().sorted(Comparator.comparing(x -> x.get(1))).collect(Collectors.toList());

            // For para verificar se o cilindro atual e o desejado, caso seja, e pego sua
            // posicao e atribuido true a variavel achou
            for (int k = 0; k < processosclook.size(); k++) {
                // Verificar se o processo desejado é igual ao que estamos percorrendo e verificar se o processo chegou
                if (processosclook.get(k).get(1) == j && processosclook.get(k).get(0) <= tempMedParcialdeEspera) {
                    pos = k;
                    achou = true;
                }
            }

            // Verificacao para saber se todos os processos foram percorridos ate o maior
            // se tiver sido percorrido ate o ultimo entra no if e é feito o restante
            // dos processos que sobraram
            if (j == valorfinal) {
                for (int k = 0; k < processosclook.size(); k++) {
                    // Verificar se o processo chegou
                    if (processos.get(k).get(0) <= tempMedParcialdeEspera) {
                        tempMedEspera += resultadoCilindroAnterior - processosclook.get(k).get(0);
                        tempMedAcesso += processosclook.get(k).get(2) + Math.abs(processosclook.get(k).get(1) - valorCilindroAnterior) + processosclook.get(k).get(3);
                        resultadoCilindroAnterior += processosclook.get(k).get(2) + Math.abs(processosclook.get(k).get(1) - valorCilindroAnterior) + processosclook.get(k).get(3);
                        valorCilindroAnterior = processosclook.get(k).get(1);
                        achou = false;
                        tempMedParcialdeEspera += tempMedAcesso;
                    }
                }
            }

            // Verificar se caso tenha achado o processo desejado, se sim
            // os calculos sao feitos
            if (achou) {
                tempMedEspera += resultadoCilindroAnterior - processosclook.get(pos).get(0);
                tempMedAcesso += processosclook.get(pos).get(2) + Math.abs(processosclook.get(pos).get(1) - valorCilindroAnterior) + processosclook.get(pos).get(3);
                resultadoCilindroAnterior += processosclook.get(pos).get(2) + Math.abs(processosclook.get(pos).get(1) - valorCilindroAnterior) + processosclook.get(pos).get(3);
                valorCilindroAnterior = processosclook.get(pos).get(1);
                processosclook.remove(pos);
                pos = 0;
                achou = false;
                tempMedParcialdeEspera += tempMedAcesso;
            }

        }

        resultado[0] = tempMedAcesso / numeroDeProcessos;
        resultado[1] = tempMedEspera / numeroDeProcessos;
        return resultado;
    }

    private double[] MY() {
        double tempMedAcesso = 0;
        double tempMedEspera = 0;
        //Pega o resultado do cilindro anterior e soma com o valoratual
        double resultadoCilindroAnterior = 0;

        // Pega o valor do cilindro anterior e atribui a variavel
        int valorCilindroAnterior = StartPosition;

        // Variavel para verificar se o cilindro procurado foi encontrado
        boolean achou = false;

        // Variavel usada como auxiliar para pegar a posicao obtida
        int pos = 0;

        // Variavel para armazenar numeros de processos existentes, pois sao removidos
        int numeroDeProcessos = processos.size();

        // variavel para verificar se o processo ja chegou
        int tempMedParcialdeEspera = 0;

        // Criado um novo List, pois os processos sao removidos da lista, entao e criada um
        // lista nova para armazenar os valores e nao atrapalhar os outros metodos
        List<List<Integer>> processosmy = new ArrayList<>(processos);

        // Variavel que obtem o maior valor do cilindro de todos os processos
        int valorfinal = processosmy.stream().sorted(Comparator.comparing(x -> x.get(1))).collect(Collectors.toList()).get(processosmy.size() - 1).get(1) + 1;

        Comparator<List<Integer>> comparator = Comparator.comparingLong(x -> x.get(1));
        Comparator<List<Integer>> reverseComparator = comparator.reversed();

        double[] resultado = new double[2];

        //For para percorrer todos os processos e verificar se existem
        for (int j = processosmy.get(0).get(1); j <= valorfinal; j++) {
            // Organizando a List para conseguir pegar sempre os maiores processos e depois voltar nos menores que restaram
            processosmy = processosmy.stream().sorted(Comparator.comparing(x -> x.get(1))).collect(Collectors.toList());

            // For para verificar se o cilindro atual e o desejado, caso seja, e pego sua
            // posicao e atribuido true a variavel achou
            for (int k = 0; k < processosmy.size(); k++) {
                // Verificar se o processo desejado é igual ao que estamos percorrendo e verificar se o processo chegou
                if (processosmy.get(k).get(1) == j && processosmy.get(k).get(0) <= tempMedParcialdeEspera) {
                    pos = k;
                    achou = true;
                }
            }

            // Verificacao para saber se todos os processos foram percorridos ate o maior
            // se tiver sido percorrido ate o ultimo entra no if e é feito o restante
            // dos processos que sobraram
            if (j == valorfinal) {
                for (int k = 0; k < processosmy.size(); k++) {
                    // Verificar se o processo chegou
                    if (processos.get(k).get(0) <= tempMedParcialdeEspera) {
                        // Inverte a lista para voltar atendendo os processos restantes
                        processosmy= processosmy.stream().sorted(reverseComparator).collect(Collectors.toList());
                        tempMedEspera += resultadoCilindroAnterior - processosmy.get(k).get(0);
                        tempMedAcesso += processosmy.get(k).get(2) + Math.abs(processosmy.get(k).get(1) - valorCilindroAnterior) + processosmy.get(k).get(3);
                        resultadoCilindroAnterior += processosmy.get(k).get(2) + Math.abs(processosmy.get(k).get(1) - valorCilindroAnterior) + processosmy.get(k).get(3);
                        valorCilindroAnterior = processosmy.get(k).get(1);
                        achou = false;
                        tempMedParcialdeEspera += tempMedAcesso;
                    }
                }
            }

            // Verificar se caso tenha achado o processo desejado, se sim
            // os calculos sao feitos
            if (achou) {
                tempMedEspera += resultadoCilindroAnterior - processosmy.get(pos).get(0);
                tempMedAcesso += processosmy.get(pos).get(2) + Math.abs(processosmy.get(pos).get(1) - valorCilindroAnterior) + processosmy.get(pos).get(3);
                resultadoCilindroAnterior += processosmy.get(pos).get(2) + Math.abs(processosmy.get(pos).get(1) - valorCilindroAnterior) + processosmy.get(pos).get(3);
                valorCilindroAnterior = processosmy.get(pos).get(1);
                processosmy.remove(pos);
                pos = 0;
                achou = false;
                tempMedParcialdeEspera += tempMedAcesso;
            }

        }

        resultado[0] = tempMedAcesso / numeroDeProcessos;
        resultado[1] = tempMedEspera / numeroDeProcessos;
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
        double[] fcfs, sstf, scan, cscan, clook, my;
        fcfs = FCFS();
        sstf = SSTF();
        scan = SCAN();
        cscan = CSCAN();
        clook = CLOOK();
        my = MY();

        // Formatando para exibir somente duas casas apos a virgula e
        // nao arredondar os valores
        DecimalFormat df = new DecimalFormat("#00.00");
        df.setMaximumFractionDigits(2);
        df.setRoundingMode(RoundingMode.DOWN);

        try {
            FileWriter arq = new FileWriter("out.txt");
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.printf("FCFS%n" +
                    "-AccessTime=" + df.format(fcfs[0]) + "%n" +
                    "-WaitingTime=" + df.format(fcfs[1]) + "%n" +
                    "SSTF%n" +
                    "-AccessTime=" + df.format(sstf[0]) + "%n" +
                    "-WaitingTime=" + df.format(sstf[1]) + "%n" +
                    "SCAN%n" +
                    "-AccessTime=" + df.format(scan[0]) + "%n" +
                    "-WaitingTime=" + df.format(scan[1]) + "%n" +
                    "C-SCAN%n" +
                    "-AccessTime=" + df.format(cscan[0]) + "%n" +
                    "-WaitingTime=" + df.format(cscan[1]) + "%n" +
                    "C-LOOK%n" +
                    "-AccessTime=" + df.format(clook[0]) + "%n" +
                    "-WaitingTime=" + df.format(clook[1]) + "%n" +
                    "MY%n" +
                    "-AccessTime=" + df.format(my[0]) + "%n" +
                    "-WaitingTime=" + df.format(my[1])+"%n");


            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
