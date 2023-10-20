import Enum from './Enum.js';

export default class Scrolling extends Enum {
  static get entries() {
    return {
      down: 'DOWN',
      up: 'UP',
    };
  }

  static get DOWN() {
    return this.entries.down;
  }

  static get UP() {
    return this.entries.up;
  }
}
