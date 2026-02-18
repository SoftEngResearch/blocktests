package edu.brown.cs.actions;

import edu.brown.cs.board.*;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

/**
 * Action responsible for placing the settlements during game setup.
 * 
 * @author anselvahle
 *
 */
public class PlaceInitialSettlement implements FollowUpAction {

  private static final String ID = "placeSettlement";
  private int _playerID;
  private boolean _isSetup;
  private int _settlementNum;
  private Referee _ref;
  private Intersection _intersection;
  private static final String VERB = "place a Settlement";

  public PlaceInitialSettlement(int playerID, int settlementNum) {
    _playerID = playerID;
    _isSetup = false;
    _settlementNum = settlementNum;
    assert settlementNum == 1 || settlementNum == 2; // Only supporting placing
                                                     // the first two
                                                     // settlements.
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    if (!_isSetup) {
      throw new UnsupportedOperationException(
          "A FollowUpAction must be setup before it is executed.");
    }
    Player player = _ref.getPlayerByID(_playerID);
    if (!_intersection.canPlaceSettlement(_ref, _playerID)) {
      return ImmutableMap.of(player.getID(), new ActionResponse(false,
          "You cannot build a Settlement at that location.", null));
    }

    // The Action:
    player.useSettlement();
    _intersection.placeSettlement(player);
    _ref.getSetup().setLastBuiltSettlement(_intersection);
    _ref.removeFollowUp(this);

    ActionResponse respToPlayer = null;
    ActionResponse respToRest = null;
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    switch (_settlementNum) {
    case 1:
      respToPlayer = new ActionResponse(true,
          "You placed your first Settlement.", null);
      String message1 = String.format("%s placed their first Settlement.",
          player.getName());
      respToRest = new ActionResponse(true, message1, null);
      break;
    case 2:
      respToPlayer = new ActionResponse(
          true,
          "You placed your second Settlement. You've received your initial resources based on this Settlement.",
          null);
      String message2 = String.format("%s placed their second Settlement.",
          player.getName());
      respToRest = new ActionResponse(true, message2, null);
      HexCoordinate coord1 = _intersection.getPosition().getCoord1();
      HexCoordinate coord2 = _intersection.getPosition().getCoord2();
      HexCoordinate coord3 = _intersection.getPosition().getCoord3();
      List<Resource> resToAdd = new ArrayList<>();
      _ref.getBoard()
          .getTiles()
          .forEach(
              (tile) -> {
                // BLOCKTEST EVAL: https://github.com/seansegal/tincisnotcatan/blob/f7d098c51037cb586147c1bbd5b18c2239df7b32/src/main/java/edu/brown/cs/actions/PlaceInitialSettlement.java#L89-L98
                blocktest().given(tile, new Tile(0, new HexCoordinate(0, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(),  new HashMap<PathCoordinate, Path>(), TileType.ORE))
                        .given(coord1, new HexCoordinate(0, 0, 0)).given(coord2, new HexCoordinate(10, 0, 0)).given(coord3, new HexCoordinate(20, 0, 0))
                        .given(resToAdd, new ArrayList<Resource>())
                        .checkEq(resToAdd.iterator().next().toString(), "ore");
                blocktest().given(tile, new Tile(0, new HexCoordinate(0, 9, 0), new HashMap<IntersectionCoordinate, Intersection>(),  new HashMap<PathCoordinate, Path>(), TileType.ORE))
                        .given(coord1, new HexCoordinate(0, 0, 0)).given(coord2, new HexCoordinate(10, 0, 0)).given(coord3, new HexCoordinate(20, 0, 0))
                        .given(resToAdd, new ArrayList<Resource>())
                        .checkTrue(resToAdd.isEmpty());
                blocktest().given(tile, new Tile(0, new HexCoordinate(0, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(),  new HashMap<PathCoordinate, Path>(), TileType.DESERT))
                        .given(coord1, new HexCoordinate(0, 0, 0)).given(coord2, new HexCoordinate(10, 0, 0)).given(coord3, new HexCoordinate(20, 0, 0))
                        .given(resToAdd, new ArrayList<Resource>())
                        .checkTrue(resToAdd.isEmpty());
                blocktest().given(tile, new Tile(0, new HexCoordinate(20, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(),  new HashMap<PathCoordinate, Path>(), TileType.ORE))
                        .given(coord1, new HexCoordinate(0, 10, 0)).given(coord2, new HexCoordinate(10, 0, 0)).given(coord3, new HexCoordinate(20, 0, 0))
                        .given(resToAdd, new ArrayList<Resource>())
                        .checkEq(resToAdd.iterator().next().toString(), "ore");
                blocktest().given(tile, new Tile(0, new HexCoordinate(10, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(),  new HashMap<PathCoordinate, Path>(), TileType.ORE))
                        .given(coord1, new HexCoordinate(0, 10, 0)).given(coord2, new HexCoordinate(10, 0, 0)).given(coord3, new HexCoordinate(20, 0, 0))
                        .given(resToAdd, new ArrayList<Resource>())
                        .checkEq(resToAdd.iterator().next().toString(), "ore");
                if (tile.getCoordinate().equals(coord1)
                    || tile.getCoordinate().equals(coord2)
                    || tile.getCoordinate().equals(coord3)) {
                  if (tile.getType().getType() != null) {
                    resToAdd.add(tile.getType().getType());

                  }
                }
              });
      resToAdd.forEach((res) -> {
        player.addResource(res, 1, _ref.getBank());
      });
      break;
    default:
      return Collections.emptyMap();
    }

    // Formulate responses and send:
    for (Player p : _ref.getPlayers()) {
      if (p.equals(player)) {
        toReturn.put(p.getID(), respToPlayer);
      } else {
        toReturn.put(p.getID(), respToRest);
      }
    }
    return toReturn;
  }

  @Override
  public JsonObject getData() {
    JsonObject json = new JsonObject();
    json.addProperty("message", "Please place a Settlement");
    return json;
  }

  @Override
  public String getID() {
    return PlaceInitialSettlement.ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject params) {
    _ref = ref;
    _isSetup = true;
    if(playerID != _playerID){
      throw new IllegalArgumentException("Can only be setup with the correct player");
    }
    _intersection = _ref.getBoard().getIntersections().get(toIntersectionCoordinate(params.get("coordinate")
        .getAsJsonObject()));
    if(_intersection == null){
      throw new IllegalArgumentException("The intersection does not exist");
    }


  }

  @Override
  public String getVerb() {
    return VERB;
  }

  private IntersectionCoordinate toIntersectionCoordinate(JsonObject object) {
    JsonObject coord1 = object.get("coord1").getAsJsonObject();
    JsonObject coord2 = object.get("coord2").getAsJsonObject();
    JsonObject coord3 = object.get("coord3").getAsJsonObject();
    HexCoordinate h1 = new HexCoordinate(coord1.get("x").getAsInt(), coord1
        .get("y").getAsInt(), coord1.get("z").getAsInt());
    HexCoordinate h2 = new HexCoordinate(coord2.get("x").getAsInt(), coord2
        .get("y").getAsInt(), coord2.get("z").getAsInt());
    HexCoordinate h3 = new HexCoordinate(coord3.get("x").getAsInt(), coord3
        .get("y").getAsInt(), coord3.get("z").getAsInt());
    return new IntersectionCoordinate(h1, h2, h3);
  }

}
