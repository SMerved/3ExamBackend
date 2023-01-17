package facades;

import com.google.gson.Gson;
import dtos.CombinedDTO;
import dtos.ChuckDTO;
import dtos.DadDTO;
import utils.HttpUtils;

import java.io.IOException;


public class JokeFetcher {
    public static void main(String[] args) throws IOException {


        String chuck = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        String dad = HttpUtils.fetchData("https://icanhazdadjoke.com");

        Gson gson = new Gson();

        ChuckDTO chuckDTO = gson.fromJson(chuck, ChuckDTO.class);
        DadDTO dadDTO = gson.fromJson(dad,DadDTO.class);



        System.out.println("JSON fetched from chucknorris:");
        System.out.println(chuckDTO);
        System.out.println("JSON fetched from dadjokes:");
        System.out.println(dadDTO);
        
       
    }


    public static CombinedDTO getJokes() throws IOException{

        String chuck = HttpUtils.fetchData("https://api.chucknorris.io/jokes/random");
        String dad = HttpUtils.fetchData("https://icanhazdadjoke.com");

        Gson gson = new Gson();

        ChuckDTO chuckDTO = gson.fromJson(chuck, ChuckDTO.class);
        DadDTO dadDTO = gson.fromJson(dad,DadDTO.class);


        return new CombinedDTO(chuckDTO,dadDTO);
    }
}
