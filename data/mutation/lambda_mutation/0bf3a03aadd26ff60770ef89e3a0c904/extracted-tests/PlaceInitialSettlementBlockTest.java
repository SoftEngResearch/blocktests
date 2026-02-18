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
import org.junit.Test;
import static org.junit.Assert.*;
import static edu.brown.cs.actions.PlaceInitialSettlement.*;

public class PlaceInitialSettlementBlockTest {

    @Test
    public void testLine94() throws Exception {
        Tile tile = new Tile(0, new HexCoordinate(0, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(), new HashMap<PathCoordinate, Path>(), TileType.ORE);
        HexCoordinate coord1 = new HexCoordinate(0, 0, 0);
        HexCoordinate coord2 = new HexCoordinate(10, 0, 0);
        HexCoordinate coord3 = new HexCoordinate(20, 0, 0);
        List<Resource> resToAdd = new ArrayList<Resource>();
        if (tile.getCoordinate().equals(coord1) || tile.getCoordinate().equals(coord2) || tile.getCoordinate().equals(coord3)) {
            if (1 == 1) {
                resToAdd.add(tile.getType().getType());
            }
        }
        assertEquals("ore", resToAdd.iterator().next().toString());
    }

    @Test
    public void testLine98() throws Exception {
        Tile tile = new Tile(0, new HexCoordinate(0, 9, 0), new HashMap<IntersectionCoordinate, Intersection>(), new HashMap<PathCoordinate, Path>(), TileType.ORE);
        HexCoordinate coord1 = new HexCoordinate(0, 0, 0);
        HexCoordinate coord2 = new HexCoordinate(10, 0, 0);
        HexCoordinate coord3 = new HexCoordinate(20, 0, 0);
        List<Resource> resToAdd = new ArrayList<Resource>();
        if (tile.getCoordinate().equals(coord1) || tile.getCoordinate().equals(coord2) || tile.getCoordinate().equals(coord3)) {
            if (1 == 1) {
                resToAdd.add(tile.getType().getType());
            }
        }
        assertTrue(resToAdd.isEmpty());
    }

    @Test
    public void testLine102() throws Exception {
        Tile tile = new Tile(0, new HexCoordinate(0, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(), new HashMap<PathCoordinate, Path>(), TileType.DESERT);
        HexCoordinate coord1 = new HexCoordinate(0, 0, 0);
        HexCoordinate coord2 = new HexCoordinate(10, 0, 0);
        HexCoordinate coord3 = new HexCoordinate(20, 0, 0);
        List<Resource> resToAdd = new ArrayList<Resource>();
        if (tile.getCoordinate().equals(coord1) || tile.getCoordinate().equals(coord2) || tile.getCoordinate().equals(coord3)) {
            if (1 == 1) {
                resToAdd.add(tile.getType().getType());
            }
        }
        assertTrue(resToAdd.isEmpty());
    }

    @Test
    public void testLine106() throws Exception {
        Tile tile = new Tile(0, new HexCoordinate(20, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(), new HashMap<PathCoordinate, Path>(), TileType.ORE);
        HexCoordinate coord1 = new HexCoordinate(0, 10, 0);
        HexCoordinate coord2 = new HexCoordinate(10, 0, 0);
        HexCoordinate coord3 = new HexCoordinate(20, 0, 0);
        List<Resource> resToAdd = new ArrayList<Resource>();
        if (tile.getCoordinate().equals(coord1) || tile.getCoordinate().equals(coord2) || tile.getCoordinate().equals(coord3)) {
            if (1 == 1) {
                resToAdd.add(tile.getType().getType());
            }
        }
        assertEquals("ore", resToAdd.iterator().next().toString());
    }

    @Test
    public void testLine110() throws Exception {
        Tile tile = new Tile(0, new HexCoordinate(10, 0, 0), new HashMap<IntersectionCoordinate, Intersection>(), new HashMap<PathCoordinate, Path>(), TileType.ORE);
        HexCoordinate coord1 = new HexCoordinate(0, 10, 0);
        HexCoordinate coord2 = new HexCoordinate(10, 0, 0);
        HexCoordinate coord3 = new HexCoordinate(20, 0, 0);
        List<Resource> resToAdd = new ArrayList<Resource>();
        if (tile.getCoordinate().equals(coord1) || tile.getCoordinate().equals(coord2) || tile.getCoordinate().equals(coord3)) {
            if (1 == 1) {
                resToAdd.add(tile.getType().getType());
            }
        }
        assertEquals("ore", resToAdd.iterator().next().toString());
    }
}
