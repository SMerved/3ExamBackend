package dtos;

public class CombinedDTO {
    String joke1 ="", reference1 ="", joke2 ="", reference2 ="";

    public CombinedDTO(String joke1, String reference1, String joke2, String reference2) {
        this.joke1 = joke1;
        this.reference1 = reference1;
        this.joke2 = joke2;
        this.reference2 = reference2;
    }

    public CombinedDTO(ChuckDTO chuckDTO, DadDTO dadDTO) {

        this.joke1 = chuckDTO.getValue();
        this.reference1 = chuckDTO.url;

        this.joke2 = dadDTO.getJoke();
        this.reference2 = dadDTO.getId();

    }
}
