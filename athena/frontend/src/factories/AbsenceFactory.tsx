import {Absence} from "../models/Absence"

export class AbsenceFactory {
  public static build = (attrs: Partial<Absence> = {}): Absence => {
    const defaultAbsence = new Absence(
      1,
      "Default Reason",
      "Default Location",
      new Date(),
      new Date(),
      ""
    )

    return Object.assign(defaultAbsence, attrs)
  }
}
