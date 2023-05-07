package pl.kk.boardservice.controlers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.kk.boardservice.models.GameData;
import pl.kk.boardservice.services.BoardService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private BoardService boardService;

    @GetMapping("/getBoard")
    public GameData getBoard() {
        return boardService.getBoard();
    }

    @GetMapping("/")
    public ModelAndView showHomePage() {
        return boardService.showHomePage();
    }

}
