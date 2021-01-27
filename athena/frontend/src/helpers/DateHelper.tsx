import moment, {Moment} from "moment"

export default class DateHelper {
  public static getMonthOffsetForQuarter = (date: moment.Moment) => (
    date.month() - DateHelper.getQuarterStart(date).month()
  )

  public static getStartDateOfWeek = (date: Moment) => moment(date).startOf("isoWeek")

  public static isDayInDayRange = (dateToCheck: Date, startDate: Date, endDate: Date): boolean => (
    startDate <= dateToCheck && dateToCheck <= endDate
  )

  private static getQuarterStart(date: moment.Moment) {
    return moment().quarter(date.quarter()).startOf("quarter")
  }
}
