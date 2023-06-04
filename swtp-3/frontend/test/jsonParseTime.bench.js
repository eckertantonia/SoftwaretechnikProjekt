import { bench, expect } from "vitest";
import { gridToJson, jsonToState } from "../src/services/JSONparser.ts";

let streetState = [];
for (let row = 1; row <= 100; row++) {
  for (let col = 1; col <= 100; col++) {
    const newStreet = {
      streetType: "straight-road",
      rotation: 90,
      posX: col,
      posY: row,
    };
    streetState.push(newStreet);
  }
}
let stringifiedState = JSON.stringify(streetState);

bench("gridstate parsing to json time elapsed", () => {
  const start = new Date().getTime();
  gridToJson(streetState);
  const elapsed = new Date().getTime() - start;
  const elapsedInSeconds = elapsed / 1000;
  expect(elapsedInSeconds).toBeLessThan(0.1);
});

bench("parsing from json to state time elapsed", () => {
  const start = new Date().getTime();
  jsonToState(stringifiedState);
  const elapsed = new Date().getTime() - start;
  const elapsedInSeconds = elapsed / 1000;
  expect(elapsedInSeconds).toBeLessThan(0.1);
});
