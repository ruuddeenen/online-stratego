package service.controllers.rest;

import models.pawns.Pawn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public class StrategoController {

    @GetMapping(value = "/formations/random")
    public List<Pawn> getRandomFormation() {
        return null;
    }
}
