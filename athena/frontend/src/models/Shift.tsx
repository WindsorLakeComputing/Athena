export default class Shift {
  public name: string

  constructor(name: string) {
    this.name = name
  }

  public friendlyName() {
    switch (this.name) {
      case "D":
        return "Day"
      case "S":
        return "Swing"
      case "M":
        return "Mid"
      default:
        return ""
    }
  }
}
