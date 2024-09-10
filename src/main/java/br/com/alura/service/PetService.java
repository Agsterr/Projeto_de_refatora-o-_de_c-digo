package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class PetService {


    public PetService() {
    }

    Scanner scanner = new Scanner(System.in);
    private ClientHttpConfiguration client;

    public PetService(ClientHttpConfiguration client) {
        this.client = client;
    }


    public void listarPetsDoAbrigo() throws IOException, InterruptedException {
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Digite o id ou nome do abrigo:");
        String idOuNome = scanner1.nextLine();
        String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";
        HttpResponse<String> response = client.dispararRequisicaoGet(uri);

        int statusCode = response.statusCode();
        if (statusCode == 404 || statusCode == 500) {
            System.out.println("ID ou nome não cadastrado!");
            return; // Sai do método se houver um erro
        }

        String responseBody = response.body();
        if (responseBody == null || responseBody.isEmpty()) {
            System.out.println("Nenhum pet encontrado para o abrigo informado.");
            return;
        }

        Pet[] pets = new ObjectMapper().readValue(responseBody, Pet[].class);
        List<Pet> petList = Arrays.stream(pets).toList();
        System.out.println("Pets cadastrados:");
        for (Pet pet : petList) {
            long id = pet.getId();
            String tipo = pet.getTipo();
            String nome = pet.getNome();
            String raca = pet.getRaca();
            int idade = pet.getIdade();
            System.out.println(id + " - " + tipo + " - " + nome + " - " + raca + " - " + idade + " ano(s)");
        }
    }


    public void importarPetsDoAbrigo() throws IOException, InterruptedException {
        Scanner scanner2 = new Scanner(System.in);

        System.out.println("Digite o id ou nome do abrigo:");
        String idOuNome = scanner2.nextLine();

        System.out.println("Digite o nome do arquivo CSV:");
        String nomeArquivo = scanner2.nextLine();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(nomeArquivo));
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + nomeArquivo);
            return; // Saímos da função, pois não é possível continuar sem o arquivo
        }

        String line;
        while ((line = reader.readLine()) != null) {
            String[] campos = line.split(",");

            // Verifica se a linha possui pelo menos 6 campos
            if (campos.length < 6) {
                System.out.println("Linha inválida no CSV: " + line);
                continue; // Pula esta linha e vai para a próxima
            }

            String tipo = campos[0].toUpperCase();
            String nome = campos[1];
            String raca = campos[2];
            int idade = Integer.parseInt(campos[3]);
            String cor = campos[4];
            Float peso = Float.parseFloat(campos[5]);

            Pet pet = new Pet(tipo, nome, raca, idade, cor, peso);

            String uri = "http://localhost:8080/abrigos/" + idOuNome + "/pets";

            HttpResponse response = client.dispararRequisicaoPost(uri, pet);

            int statusCode = response.statusCode();
            var responseBody = response.body();

            if (statusCode == 200) {
                System.out.println("Pet cadastrado com sucesso: " + nome);
            } else if (statusCode == 404) {
                System.out.println("Id ou nome do abrigo não encontrado!");
            } else if (statusCode == 400 || statusCode == 500) {
                System.out.println("Erro ao cadastrar o pet: " + nome);
                System.out.println(responseBody);
            }
        }

        reader.close(); // Fecha o reader após o loop ter terminado
    }







    @Override
    public String toString() {
        return "PetService{" +
                "client=" + client +
                '}';
    }
}
