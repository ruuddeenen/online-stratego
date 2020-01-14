package service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import models.pawns.*;

import java.io.IOException;

public class PawnDeserializer extends JsonDeserializer<Pawn> {
    @Override
    public Pawn deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode pawn = codec.readTree(jsonParser);

        switch (pawn.get("name").textValue()){
            case "Bomb": return  codec.treeToValue(pawn, Bomb.class);
            case "Captain": return codec.treeToValue(pawn, Captain.class);
            case "Colonel": return codec.treeToValue(pawn, Colonel.class);
            case "Flag": return codec.treeToValue(pawn, Flag.class);
            case "General": return codec.treeToValue(pawn, General.class);
            case "Lieutenant": return codec.treeToValue(pawn, Lieutenant.class);
            case "Major": return codec.treeToValue(pawn, Major.class);
            case "Marshal": return codec.treeToValue(pawn, Marshal.class);
            case "Miner": return codec.treeToValue(pawn, Miner.class);
            case "Scout": return codec.treeToValue(pawn, Scout.class);
            case "Sergeant": return codec.treeToValue(pawn, Sergeant.class);
            case "Spy": return codec.treeToValue(pawn, Spy.class);
            default: return codec.treeToValue(pawn, UnknownPawn.class);
        }
    }
}
