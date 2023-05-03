package pl.kk.panelservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.panelservice.models.GameData;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class PanelService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final WebClient.Builder webClientBuilder;

    public Flux<GameData> getGameResultAsync() throws InterruptedException {

        log.info("Starting NON-BLOCKING Controller!" + " --- " + System.currentTimeMillis());

        for (int i = 0; i < 2; i++) {
            Flux<GameData> gameDataFlux = webClientBuilder.build()
                    .get()
                    .uri("http://board-service/getBoard")
                    .retrieve()
                    .bodyToFlux(GameData.class);
            gameDataFlux.subscribe(tweet -> log.info(tweet.toString() + " --- " + System.currentTimeMillis()));
            Thread.sleep(50);
        }


        log.info("Exiting NON-BLOCKING Controller!" + " --- " + System.currentTimeMillis());
        return Flux.just(GameData.builder().build());
    }

    public Flux<GameData> getGameResultAsync2() {
        log.info("Poczatek async");
        Flux<GameData> gameDataFlux = webClientBuilder.build().get()
                .uri("http://board-service/getBoard")
                .retrieve()
                .bodyToFlux(GameData.class);
        log.info("Koniec async");
        gameDataFlux.subscribe(gd -> log.info(gd.toString()));
        return gameDataFlux;
    }


    public GameData getGameResult() {
        log.info("Poczatek sync");
//        todo: sync with RestTemplate
//        GameData gamedata = restTemplate.getForObject("http://board-service/getBoard", GameData.class);

//        todo: sync with WebClient
        GameData gamedata = webClientBuilder.build().get()
                .uri("http://board-service/getBoard")
                .retrieve()
                .bodyToMono(GameData.class)
                .block();

        log.info("Koniec sync");
        return gamedata;
    }

    public Flux<String> getTest() {
        log.info("Poczatek sync");
//        todo: sync with RestTemplate
//        GameData gamedata = restTemplate.getForObject("http://board-service/getBoard", GameData.class);

//        todo: sync with WebClient
        Flux<String> gamedata = webClientBuilder.build().get()
                .uri("http://board-service/getTest")
                .retrieve()
                .bodyToFlux(String.class);

        gamedata.subscribe(log::info);
        log.info("Koniec sync");
        return gamedata;
    }

    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index2.html");
        return modelAndView;
    }

}
