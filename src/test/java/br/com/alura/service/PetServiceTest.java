package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PetServiceTest {

    private final ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);
    private final PetService petService = new PetService(client);
    private final HttpResponse<String> response = mock(HttpResponse.class);

    @Test
    public void deveVerificarSeDispararRequisicaoPostSeraChamadoTestarSaidaDoTeste() throws IOException, InterruptedException {
        // Simula a entrada do usuário
        String userInput = String.format("Testes%spets.csv", System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        // Configura o comportamento do mock
        when(client.dispararRequisicaoPost(anyString(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(200); // Define um código de status de sucesso para a resposta

        // Captura a saída do console
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        // Chama o método a ser testado
        petService.importarPetsDoAbrigo();

        // Verifica se o método foi chamado pelo menos uma vez
        verify(client, atLeast(1)).dispararRequisicaoPost(anyString(), any());

        // Verifica se a mensagem de sucesso foi impressa no console
        String expectedMessage = "Pet cadastrado com sucesso:"; // Ajuste essa mensagem de acordo com o que o método imprime
        String actualOutput = baos.toString(); // Captura a saída do console

        Assertions.assertTrue(actualOutput.contains(expectedMessage), "A mensagem de sucesso não foi exibida corretamente.");
    }

    // Você pode adicionar outros testes, como por exemplo:
    @Test
    void importarPetsDoAbrigoQuandoArquivoInvalido() throws IOException, InterruptedException {
        // Simula a entrada do usuário
        String userInput = String.format("Testes%spets.csv", System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        // Configura o comportamento do mock
        when(client.dispararRequisicaoPost(anyString(), any())).thenReturn(response);
        when(response.statusCode()).thenReturn(400); // Simula um status de erro

        // Captura a saída do console
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        // Chama o método que você está testando1
        petService.importarPetsDoAbrigo();

        // Verifica se o método foi chamado pelo menos uma vez
        verify(client, atLeast(1)).dispararRequisicaoPost(anyString(), any());

        // Verifica se a mensagem de erro foi impressa no console1
        String expectedMessage = "Erro ao cadastrar o pet:";
        String actualOutput = baos.toString(); // Captura a saída do console

        Assertions.assertTrue(actualOutput.contains(expectedMessage), "A mensagem de erro não foi exibida corretamente.");
    }

    @Test
    void testarImporteDePets() throws IOException, InterruptedException {

        // Simula a entrada do usuário
        String userInput = String.format("Testes%s", System.lineSeparator()); // %s para inserir nova linha
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        // Configura o comportamento do mock para a requisição GET
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);
        when(response.statusCode()).thenReturn(404); // Simula um código de status 404
        when(response.body()).thenReturn(""); // Simula o corpo da resposta como string vazia

        // Captura a saída do console
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        // Chama o método listarPetsDoAbrigo()
        petService.listarPetsDoAbrigo();

        // Verifica se o método GET foi chamado pelo menos uma vez
        verify(client, atLeast(1)).dispararRequisicaoGet(anyString());

        // Verifica se a mensagem de erro foi impressa no console
        String expectedMessage = "ID ou nome não cadastrado!";
        String actualOutput = baos.toString(); // Captura a saída do console
        System.out.println(actualOutput);
        Assertions.assertTrue(actualOutput.contains(expectedMessage), "A mensagem de erro não foi exibida corretamente.");


    }



}



