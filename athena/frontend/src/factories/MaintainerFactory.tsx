import Certificate from "../models/Certificate"
import {Maintainer} from "../models/Maintainer"
import Section from "../models/Section"
import Shift from "../models/Shift"

export class MaintainerFactory {
  public static build = (attrs: Partial<Maintainer> = {}): Maintainer => {
    const firstShiftPref = new Shift("M")
    const secondShiftPref = new Shift("S")
    const thirdShiftPref = new Shift("D")
    const certificates: Certificate[] = [new Certificate("X")]
    const maintainer = new Maintainer(
      1,
      "firstName",
      "lastName",
      "4321",
      firstShiftPref,
      secondShiftPref,
      thirdShiftPref,
      certificates,
      5, "E6",
      new Shift("D"),
      new Section("Weapons"),
      [],
      [])

    return Object.assign(maintainer, attrs)
  }

  public static buildApiResponse = (attrs: any) => {
    return Object.assign({
      firstName: "TIMOTHY J",
      lastName: "ANNESE",
      employeeId: "5971",
      certificates: [{name: "I/E", id: 3}],
      firstShiftPreference: {name: "D", id: 1},
      secondShiftPreference: {name: "S", id: 2},
      thirdShiftPreference: {name: "M", id: 3},
      level: "3",
      rank: "Sgt",
      section: {name: "Production", id: 3},
      shift: {name: "S", id: 2},
      absences: [],
      standbys: [],
      id: 1
    }, attrs)
  }
}
