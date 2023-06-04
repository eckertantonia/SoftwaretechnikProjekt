package de.hsrm.mi.team3.swtp.domain;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tile repraesentiert das einzelne Strassenstueck Es beinhaltet alle Informationen, die fuer die
 * Bot Fahrzeuge relevant sind Tiles landen in der RoadMap
 */
@Getter
@Setter
public class StreetBlock {

  Logger logger = LoggerFactory.getLogger(StreetBlock.class);

  private String type;
  private int rotation;
  private int[] position = {0, 0};
  private boolean blocked;
  private int[] exits;

  public StreetBlock(String blockType, int rotation, int posX, int posY, boolean blocked) {
    this.type = blockType;
    this.rotation = rotation;
    this.position[0] = posX;
    this.position[1] = posY;
    this.blocked = blocked;
    setExits();
  }

  /**
   * Set Exits for StreetBlocks. Catches IndexOutOfBoundsException if StreetBlock type name does not
   * conform to format "type-name".
   */
  private void setExits() {
    String[] input = this.type.split("-");
    try {
      switch (input[1]) {
        case "cross":
          this.exits = new int[] {0, 90, 180, 270};
          break;
        case "straight":
          this.exits = new int[] {90, 270};
          break;
        case "curve":
          this.exits = new int[] {270, 0};
          break;
        case "t":
          this.exits = new int[] {180, 270, 0};
          break;
        case "dead":
          this.exits = new int[] {270};
          break;
        case "finish":
          this.exits = new int[] {0, 180};
          break;
        default:
          this.exits = new int[] {};
          return;
      }
    } catch (IndexOutOfBoundsException e) {
      // logger.info("type name does not conform to format \"type-name\"");
      return;
    }

    if (this.rotation > 0 && this.exits.length > 0 && this.exits.length < 4) {
      for (int i = 0; i < exits.length; i++) {
        int newRotation = this.exits[i] + this.rotation;
        this.exits[i] = newRotation > 270 ? (newRotation - 360) : newRotation;
      }
    }
  }

  public boolean isCrossroad() {
    return (this.type.contains("-t") || this.type.contains("-cross"));
  }
}
