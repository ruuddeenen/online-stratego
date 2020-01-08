package service.controllers.rest;

import models.Pawn.Pawn;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class StrategoController {

    @RequestMapping("/formations/random")
    public List<Pawn> getRandomFormation() {
        return null;
    }
}
