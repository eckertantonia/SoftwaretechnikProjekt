import jsonconfig from "../../../swtp.config.json";

interface NullConsole {
  log: Function;
  error: Function;
}

let logger: Console | NullConsole = {} as Console;

if (jsonconfig.debugConsole) {
  logger = window.console;
} else {
  logger = {
    log: () => {},
    error: () => {},
  };
}

export { logger };
