package pl.kk.panelservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.panelservice.models.GameData;
import pl.kk.panelservice.models.PlayersList;

@Service
@AllArgsConstructor
public class PanelService {

    private RestTemplate restTemplate;
    private WebClient webClient;

    //    public List<List<Unit>> getBoard() {

    @CircuitBreaker(name = "getGameResult", fallbackMethod = "getGameResultFallback")
//    @Retry(name = "getBoard")
    public GameData getGameResult() {

        return restTemplate.getForObject("http://board-service/getBoard", GameData.class);
    }

    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index2.html");
        return modelAndView;
    }

    public GameData getGameResultFallback(Exception ex) {

        return GameData.builder()
                .totalTimeMillisecs(1000L)
                .totalTurns(100)
                .build();
    }
}
