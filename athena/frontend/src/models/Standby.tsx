export class Standby {
  public static fromApiResponse(standby: any) {
    return new Standby(
      standby.id,
      new Date(Date.parse(`${standby.startDate} 00:00:00`)),
      new Date(Date.parse(`${standby.endDate} 00:00:00`))
    )
  }

  public id: number
  public startDate: Date
  public endDate: Date

  constructor(
    id: number,
    startDate: Date,
    endDate: Date
  ) {
    this.id = id
    this.startDate = startDate
    this.endDate = endDate
  }
}
