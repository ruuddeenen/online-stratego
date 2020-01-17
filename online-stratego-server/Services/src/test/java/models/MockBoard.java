package models;

import models.pawns.Pawn;

public class MockBoard extends Board {

    public MockBoard(){
        super();
    }

    public void addPawn(Pawn pawn){
        getPawnList().add(pawn);
    }
}
