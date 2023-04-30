package pl.kk.panelservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.panelservice.models.GameData;
import pl.kk.panelservice.services.PanelService;

@RestController
@AllArgsConstructor
public class PanelController {

    private PanelService panelService;

    @GetMapping("/getGameResult")
//    public List<List<Unit>> getBoard() {
    public GameData getGameResult() {
        return panelService.getGameResult();
    }

    @GetMapping("/")
    public ModelAndView showHomePage() {
        return panelService.showHomePage();
    }

}
