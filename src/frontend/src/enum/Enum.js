export default class Enum {
  static entries;

  constructor() {
    throw new Error("Enum class, don't instantiate");
  }

  static get keys() {
    return Object.keys(this.entries);
  }

  static get values() {
    return Object.values(this.entries);
  }

  static get list() {
    return Object.entries(this.entries).map(([a, b]) => ({
      id: b,
      name: a,
    }));
  }

  static exists(property) {
    if (this.entries === undefined) {
      return false;
    }

    return this.entries.hasOwnProperty(property);
  }

  static getValue(key) {
    return this.entries[key];
  }
}
