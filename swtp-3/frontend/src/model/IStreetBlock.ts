/**
 * Interface for StreetBlocks
 */
export interface IStreetBlock {
  name: string;
  currentRotation: number;
  possibleRotations: number[];
  possibleVehicleTypes: string[];
  imgPath: string;
}

export class StreetBlock implements IStreetBlock {
  name = "";
  currentRotation = 0;
  imgPath = "";
  possibleRotations = new Array<number>();
  possibleVehicleTypes = new Array<string>();

  /**
   *
   * @param name name of StreetBlock as string
   * @param currentRotation
   * @param possibleRotations array with possible rotations of this StreetBlock
   * @param possibleVehicleTypes array that specifies which vehicle types this StreetBlock is used for
   * @param imgPath path to 2D-image to show in StreetMenu
   */
  constructor(
    name: string,
    currentRotation: number,
    imgPath: string,
    possibleRotations: number[],
    possibleVehicleTypes: string[]
  ) {
    this.name = name;
    this.currentRotation = currentRotation;
    this.imgPath = imgPath;
    this.possibleRotations = possibleRotations;
    this.possibleVehicleTypes = possibleVehicleTypes;
  }
}
