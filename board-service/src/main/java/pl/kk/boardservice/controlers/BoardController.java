package pl.kk.boardservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.boardservice.models.GameData;
import pl.kk.boardservice.models.Unit;
import pl.kk.boardservice.services.BoardService;

import java.util.List;

@RestController
@AllArgsConstructor
public class BoardController {

    private BoardService boardService;

    @GetMapping("/getBoard")
//    public List<List<Unit>> getBoard() {
    public GameData getBoard() {
        return boardService.getBoard();
    }
    @GetMapping("/")
    public ModelAndView showHomePage() {
        return boardService.showHomePage();
    }

}
