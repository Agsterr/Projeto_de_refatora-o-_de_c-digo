package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Abrigo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class AbrigoService {

   private ClientHttpConfiguration client;

   Scanner scanner = new Scanner(System.in);


    //Instancias


    public AbrigoService() {
    }

    public AbrigoService(ClientHttpConfiguration client) {
        this.client = client;

    }


    public void cadastrarAbrigos() throws IOException, InterruptedException {

        System.out.println("Digite o nome do abrigo:");
        String nome = scanner.nextLine();
        System.out.println("Digite o telefone do abrigo:");
        String telefone = scanner.nextLine();
        System.out.println("Digite o email do abrigo:");
        String email = scanner.nextLine();



        Abrigo  abrigo = new Abrigo(nome,email,telefone);

        String uri = "http://localhost:8080/abrigos";

        HttpResponse response =
                client.dispararRequisicaoPost(uri,abrigo);

        int statusCode = response.statusCode();
        var responseBody = response.body();

        if (statusCode == 200) {
            System.out.println("Abrigo cadastrado com sucesso!");
            System.out.println(responseBody);
        } else if (statusCode == 400 || statusCode == 500) {
            System.out.println("Erro ao cadastrar o abrigo:");
            System.out.println(responseBody);

        }

    }

    public void listarAbrigos() throws IOException, InterruptedException {


        String uri = "http://localhost:8080/abrigos";

        HttpResponse<String> response =
               client.dispararRequisicaoGet(uri);

        String responseBody = response.body();
    Abrigo[] abrigos = new ObjectMapper().readValue( responseBody, Abrigo[].class);

       List<Abrigo> abrigoList = Arrays.stream(abrigos).toList();

       if (abrigoList.isEmpty()){

           System.out.println("Não ha abrigos cadastrados");
       }else {
         mostrarAbrigos(abrigoList);


       }
    }

    private void mostrarAbrigos(List<Abrigo>abrigos){
        System.out.println("Abrigos cadastrados:");
        for (Abrigo abrigo : abrigos) {
            Long id = abrigo.getId();
            String nome = abrigo.getNome();
            System.out.println(id + " - " + nome);
        }
    }
}
