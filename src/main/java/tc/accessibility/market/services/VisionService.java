package tc.accessibility.market.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tc.accessibility.market.DTOs.ProductDetectionDTO;
import tc.accessibility.market.controllers.errors.CloudVisionApiExpection;
import tc.accessibility.market.utils.StringManipulator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Documentação utilizada: https://cloud.google.com/vision/docs/libraries?hl=pt-br
@Service
public class VisionService {
    /**
     * Autentica a aplicação no Google Cloud utilizando as credenciais fornecidas.
     *
     * Esta função converte a variável de ambiente `GOOGLE_APPLICATION_CREDENTIALS`,
     * que deve conter o JSON das credenciais, em um objeto `GoogleCredentials`
     * suportado pela biblioteca do Google.
     *
     * @return GoogleCredentials - As credenciais do Google Cloud.
     * @throws IOException se a variável de ambiente `GOOGLE_APPLICATION_CREDENTIALS` não estiver definida
     * ou se ocorrer um erro ao processar o JSON das credenciais.
     *
     */
    private GoogleCredentials getCredentials() throws IOException {
        String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsJson == null) {
            throw new IOException("GOOGLE_CREDENTIALS_JSON não está definida.");
        }

        return GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()));
    }

    /**
     * Cria e configura o cliente responsável por interagir com a API do Google Cloud Vision.
     *
     * Esta função inicializa um `ImageAnnotatorClient`, a classe que gerencia as interações com a API
     * do Google Cloud Vision, configurando as credenciais necessárias para realizar as requisições.
     *
     * @return ImageAnnotatorClient - O cliente configurado para interagir com a API do Google Cloud Vision.
     * @throws IOException se ocorrer um erro ao carregar as credenciais ou ao configurar o cliente.
     */
    private ImageAnnotatorClient createClient() throws IOException {
        GoogleCredentials credentials = getCredentials();
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();
        return ImageAnnotatorClient.create(settings);
    }

    /**
     * Analisa a imagem fornecida para detectar e extrair textos usando a API do Google Cloud Vision.
     *
     * Esta função recebe uma imagem no formato `MultipartFile`, a converte em um formato compatível com a API,
     * e utiliza a funcionalidade de detecção de texto para identificar textos presentes na imagem.
     * O resultado é retornado como um `TextDetectionDTO`.
     *
     * @param image a imagem a ser analisada, fornecida como um arquivo multipart.
     * @return TextDetectionDTO - Um objeto contendo o texto detectado e uma lista de outros textos relevantes na imagem.
     * @throws IOException se ocorrer um erro ao ler a imagem ou ao interagir com a API do Google Cloud Vision.
     * @throws CloudVisionApiExpection se a API do Google Cloud Vision retornar um erro durante a análise da imagem.
     * @throws RuntimeException se ocorrer qualquer outro erro inesperado durante o processamento.
     */
    public ProductDetectionDTO analyzeImage(MultipartFile image) throws IOException {
        try(ImageAnnotatorClient vision = createClient()){
            // Transforma a imagem em um ByteString
            InputStream inputStream = image.getInputStream();
            ByteString byteString = ByteString.readFrom(inputStream);

            /*
             * Utiliza o ByteString para construir a imagem e enviar no request utilizando
             * a feature de detecção de texto.
             */
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(byteString).build();

            Feature featureWebDetection = Feature.newBuilder()
                    .setType(Feature.Type.WEB_DETECTION)
                    .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().setImage(img)
                            .addFeatures(featureWebDetection)
                            .build();

            requests.add(request);

            // Realiza a requisição para o Google Cloud Vision API e captura a resposta
            AnnotateImageResponse response = vision.batchAnnotateImages(requests).getResponses(0);

            // Caso ocorra algum erro no retorno do Google Cloud Vision API, lança uma exceção específica
            if (response.hasError()) {
                throw new CloudVisionApiExpection(response.getError().getMessage());
            }

            List<WebDetection.WebPage> webAnnotation = response.getWebDetection().getPagesWithMatchingImagesList();
            List<String>productsFound = new ArrayList<>();
            for(WebDetection.WebPage page:webAnnotation){
                if(page.getUrl().contains(".br")){
                    productsFound.add(StringManipulator.removeLastSeparator(page.getPageTitle())) ;
                }
            }

            Collections.reverse(productsFound);
            // Retorna um objeto de transferência para o controller responsável
            return new ProductDetectionDTO(productsFound);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
