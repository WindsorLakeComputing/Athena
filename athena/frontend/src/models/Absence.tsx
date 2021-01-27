export class Absence {
  public static fromApiResponse(absence: any) {
    return new Absence(absence.id,
      absence.reason,
      absence.location,
      new Date(Date.parse(`${absence.startDate} 00:00:00`)),
      new Date(Date.parse(`${absence.endDate} 00:00:00`)),
      absence.hours
    )
  }
  public id: number
  public reason: string
  public location: string
  public startDate: Date
  public endDate: Date
  public hours: string

  constructor(
    id: number,
    reason: string,
    location: string,
    startDate: Date,
    endDate: Date,
    hours: string
  ) {
    this.id = id
    this.reason = reason
    this.location = location
    this.startDate = startDate
    this.endDate = endDate
    this.hours = hours
  }
}
