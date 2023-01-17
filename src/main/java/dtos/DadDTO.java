package dtos;

public class DadDTO {
    String id;
    String joke;
    String url;

    public DadDTO(String id, String joke, String url) {
        this.id = id;
        this.joke = joke;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    @Override
    public String toString() {
        return "DadDTO{" +
                "id='" + id + '\'' +
                ", joke='" + joke + '\'' +
                '}';
    }
}
