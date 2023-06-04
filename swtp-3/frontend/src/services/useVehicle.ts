import { reactive, readonly } from "vue";
/**
 * Interface to save vehicle information
 * @param {string} type - Vehicle Type
 * @param {string} iconSrc - path for the image
 */
export interface IVehicleInformation {
  type: string;
  iconSrc: string;
}
/**
 * State that saves the information about the Vehicle
 */
const state = reactive({
  vehicle: { type: "", iconSrc: "" },
});
/**
 * State Management for the Vehicle
 * @returns Returns the current Vehicle (readonly) and the functions updateCurrentVehiclePosition and updateCurrentVehicle
 */
export function useVehicle() {
  /**
   * Function of the state to update the current Vehicle to a new one
   * @param {IVehicleInformation} vehicle new Vehicle for the state
   */
  function updateCurrentVehicle(vehicle: IVehicleInformation) {
    state.vehicle.type = vehicle.type;
    state.vehicle.iconSrc = vehicle.iconSrc;
  }

  return {
    currentVehicle: readonly(state.vehicle),
    updateCurrentVehicle,
  };
}
