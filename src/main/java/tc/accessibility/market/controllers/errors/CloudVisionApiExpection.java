package tc.accessibility.market.controllers.errors;


public class CloudVisionApiExpection extends Exception {
    public CloudVisionApiExpection(String responseErrorMessage){
        super("Erro na API do Google Cloud Vision: "+ responseErrorMessage);
    }
}
