package pl.kk.panelservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.panelservice.models.GameData;
import pl.kk.panelservice.services.PanelService;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@RequestMapping("/api/panel")
public class PanelController {

    private PanelService panelService;

    @GetMapping("/getGameResultAsync")
    public Flux<GameData> getGameResultAsync() throws InterruptedException {
        return panelService.getGameResultAsync();
    }

    @GetMapping("/getGameResult")
    public GameData getGameResult() {
        return panelService.getGameResult();
    }

//    @GetMapping("/")
//    public ModelAndView showHomePage() {
//        return panelService.showHomePage();
//    }

}
