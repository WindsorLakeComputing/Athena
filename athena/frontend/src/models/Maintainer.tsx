import moment, {Moment} from "moment"
import DateHelper from "../helpers/DateHelper"
import {Absence} from "./Absence"
import Certificate from "./Certificate"
import Section from "./Section"
import Shift from "./Shift"
import {Standby} from "./Standby"

export class Maintainer {

  public static fromApiResponse(maintainer: any) {
    return new Maintainer(maintainer.id,
      maintainer.firstName,
      maintainer.lastName,
      maintainer.employeeId,
      maintainer.firstShiftPreference ? new Shift(maintainer.firstShiftPreference.name) : new Shift(""),
      maintainer.secondShiftPreference ? new Shift(maintainer.secondShiftPreference.name) : new Shift(""),
      maintainer.thirdShiftPreference ? new Shift(maintainer.thirdShiftPreference.name) : new Shift(""),
      maintainer.certificates,
      maintainer.level,
      maintainer.rank,
      maintainer.shift ? new Shift(maintainer.shift.name) : new Shift("D"),
      maintainer.section,
      maintainer.absences.map(Absence.fromApiResponse),
      maintainer.standbys.map(Standby.fromApiResponse)
    )
  }

  public id: number
  public firstName: string
  public lastName: string
  public employeeId: string
  public firstShiftPreference: Shift
  public secondShiftPreference: Shift
  public thirdShiftPreference: Shift
  public certificates: Certificate[]
  public level: number
  public rank: string
  public shift: Shift
  public section: Section
  public absences: Absence[]
  public standbys: Standby[]

  constructor(
    id: number,
    firstName: string,
    lastName: string,
    employeeId: string,
    firstShiftPreference: Shift,
    secondShiftPreference: Shift,
    thirdShiftPreference: Shift,
    certificates: Certificate[],
    level: number,
    rank: string,
    shift: Shift,
    section: Section,
    absences: Absence[],
    standbys: Standby[]
  ) {
    this.id = id
    this.firstName = firstName
    this.lastName = lastName
    this.employeeId = employeeId
    this.firstShiftPreference = firstShiftPreference
    this.secondShiftPreference = secondShiftPreference
    this.thirdShiftPreference = thirdShiftPreference
    this.certificates = certificates
    this.level = level
    this.rank = rank
    this.shift = shift
    this.section = section
    this.absences = absences
    this.standbys = standbys
  }

  public isOnStandbyForWeek = (date: Moment): boolean => {
    const startOfWeek = DateHelper.getStartDateOfWeek(date)
    const saturday = moment(startOfWeek).add(5, "days")
    const sunday = moment(startOfWeek).add(6, "days")

    const matchingStandbys = this.standbys.filter((standby) =>
      moment(standby.startDate).isSame(saturday, "day") && moment(standby.endDate).isSame(sunday, "day"))

    return matchingStandbys.length > 0
  }
}
