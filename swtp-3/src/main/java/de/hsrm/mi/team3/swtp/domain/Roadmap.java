package de.hsrm.mi.team3.swtp.domain;

import java.util.EnumMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/** RoadMap rray of StreetBlocks generated from roomMap string for easy access */
public class Roadmap {

  private static final int SIZE = 50;
  private StreetBlock[][] streetBlockMap = new StreetBlock[SIZE][SIZE];

  public Roadmap(String mapstring) {
    if (!mapstring.isEmpty()) {
      JSONArray jsonArray = new JSONArray(mapstring);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject obj = jsonArray.getJSONObject(i);
        int x = obj.getInt("posX");
        int y = obj.getInt("posY");
        this.streetBlockMap[y - 1][x - 1] =
            new StreetBlock(
                obj.get("streetType").toString(), obj.getInt("rotation"), x - 1, y - 1, false);
      }
    }
  }

  /**
   * @param x Koordinate von der die Nachbarn geholt werden sollen
   * @param y Koordinate von der die Nachbarn geholt werden sollen
   * @param rotation Richtung in welche nach Nachbarn gesucht werden soll
   * @return EnumMap mit den Nachbar Tiles
   */
  public Map<VehicleNeighbour, StreetBlock> getNeighbours(int x, int y, int rotation) {
    Map<VehicleNeighbour, StreetBlock> neighbours = new EnumMap<>(VehicleNeighbour.class);
    if (rotation == 90) {
      // Drehung nach Bildschirm-rechts
      neighbours.put(VehicleNeighbour.VEHICLELEFT, getStreetBlock(x, y - 1));
      neighbours.put(VehicleNeighbour.VEHICLETOPLEFT, getStreetBlock(x + 1, y - 1));
      neighbours.put(VehicleNeighbour.VEHICLETOP, getStreetBlock(x + 1, y));
      neighbours.put(VehicleNeighbour.VEHICLETOPRIGHT, getStreetBlock(x + 1, y + 1));
      neighbours.put(VehicleNeighbour.VEHICLERIGHT, getStreetBlock(x, y + 1));
    } else if (rotation == 180) {
      // Drehung nach Bildschirm-unten
      neighbours.put(VehicleNeighbour.VEHICLELEFT, getStreetBlock(x + 1, y));
      neighbours.put(VehicleNeighbour.VEHICLETOPLEFT, getStreetBlock(x + 1, y + 1));
      neighbours.put(VehicleNeighbour.VEHICLETOP, getStreetBlock(x, y + 1));
      neighbours.put(VehicleNeighbour.VEHICLETOPRIGHT, getStreetBlock(x - 1, y + 1));
      neighbours.put(VehicleNeighbour.VEHICLERIGHT, getStreetBlock(x - 1, y));

    } else if (rotation == 270) {
      // Drehung nach Bildschirm-links
      neighbours.put(VehicleNeighbour.VEHICLELEFT, getStreetBlock(x, y + 1));
      neighbours.put(VehicleNeighbour.VEHICLETOPLEFT, getStreetBlock(x - 1, y + 1));
      neighbours.put(VehicleNeighbour.VEHICLETOP, getStreetBlock(x - 1, y));
      neighbours.put(VehicleNeighbour.VEHICLETOPRIGHT, getStreetBlock(x - 1, y - 1));
      neighbours.put(VehicleNeighbour.VEHICLERIGHT, getStreetBlock(x, y - 1));

    } else {
      // keine Drehung Ausrichtung nach Bildschirm-oben
      neighbours.put(VehicleNeighbour.VEHICLELEFT, getStreetBlock(x - 1, y));
      neighbours.put(VehicleNeighbour.VEHICLETOPLEFT, getStreetBlock(x - 1, y - 1));
      neighbours.put(VehicleNeighbour.VEHICLETOP, getStreetBlock(x, y - 1));
      neighbours.put(VehicleNeighbour.VEHICLETOPRIGHT, getStreetBlock(x + 1, y - 1));
      neighbours.put(VehicleNeighbour.VEHICLERIGHT, getStreetBlock(x + 1, y));
    }

    return neighbours;
  }

  public void setStreetBlock(String tilename, int rotation, int x, int y, boolean blocked) {
    this.streetBlockMap[x][y] = new StreetBlock(tilename, rotation, x, y, blocked);
  }

  public StreetBlock getStreetBlock(int x, int y) {
    if (x < 0 || y < 0 || x > SIZE - 1 || y > SIZE - 1) {
      return null;
    }
    if (this.streetBlockMap[x][y] != null) {
      return this.streetBlockMap[x][y];
    }
    return null;
  }

  public StreetBlock[][] getStreetBlockMap() {
    return this.streetBlockMap;
  }
}
