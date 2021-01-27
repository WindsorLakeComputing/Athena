import {Moment} from "moment"
import moment from "moment"
import * as React from "react"
import {RouteComponentProps, withRouter} from "react-router"
import DateHelper from "../../helpers/DateHelper"
import RequestHelper from "../../helpers/RequestHelper"
import {Absence} from "../../models/Absence"
import {Maintainer} from "../../models/Maintainer"
import trashicon from "../../resources/images/trashicon.svg"
import AbsenceCell from "./AbsenceCell"
import {SharedMaintainerFields} from "./SharedMaintainerFields"

interface Props extends RouteComponentProps<{}> {
  maintainer: Maintainer
  certificateHeaders: string[]
  date: Moment
  reloadMaintainer: (maintainerId: number) => void
}

class WeeklyMaintainerRowWithoutRouter extends React.Component<Props, {}> {
  constructor(props: Props) {
    super(props)
  }

  public render = () => (
      <tr className="maintainer" onClick={this.handleMaintainerRowClick}>
        <SharedMaintainerFields {...this.props}/>
        {this.getWeekdayColumns()}
        {this.getWeekendColumn()}
      </tr>
  )

  private getWeekdayColumns = () => (
    <>
      {
        this.getWeekdays(DateHelper.getStartDateOfWeek(this.props.date))
        .map((date, index) => {
          const matchingAbsences = this.props.maintainer.absences.filter((absence: Absence) =>
            DateHelper.isDayInDayRange(date.toDate(), absence.startDate, absence.endDate))

          return (<AbsenceCell key={index} absences={matchingAbsences}/>)
        })
      }
    </>
  )

  private getWeekdays = (startOfWeek: Moment): Moment[] =>
    [startOfWeek,
      moment(startOfWeek).add(1, "days"),
      moment(startOfWeek).add(2, "days"),
      moment(startOfWeek).add(3, "days"),
      moment(startOfWeek).add(4, "days")]

  private getWeekendColumn = (): JSX.Element => {
    const startOfWeek = DateHelper.getStartDateOfWeek(this.props.date)
    const saturday = moment(startOfWeek).add(5, "days")
    const sunday = moment(startOfWeek).add(6, "days")

    const absencesOnWeekend = this.props.maintainer.absences.filter((absence: Absence) =>
      DateHelper.isDayInDayRange(saturday.toDate(), absence.startDate, absence.endDate)
      || DateHelper.isDayInDayRange(sunday.toDate(), absence.startDate, absence.endDate))

    if (absencesOnWeekend.length > 0) {
      return <AbsenceCell key={6} absences={absencesOnWeekend}/>
    } else {
      const matchingStandbys = this.props.maintainer.standbys.filter((standby) =>
        moment(standby.startDate).isSame(saturday, "day") && moment(standby.endDate).isSame(sunday, "day"))

      if (this.props.maintainer.isOnStandbyForWeek(this.props.date)) {
        return (
          <td className={"weekend-standby-cell"}>
            <button className={"standby-delete-button"}
                    onClick={(e) => this.deleteStandby(e, matchingStandbys[0].id, this.props.maintainer.id)}>
              <img className={"trash-icon"} src={trashicon}/>&nbsp;
              Standby
            </button>
          </td>
        )
      } else {
        return (
          <td className={"weekend-standby-cell"}>
            <button className={"standby-add-button"}
                    onClick={(e) => this.addStandby(e, this.props.maintainer.id, saturday, sunday)}>
              Add Standby
            </button>
          </td>
        )
      }
    }
  }

  private handleMaintainerRowClick = () => {
    this.props.history.push(`/maintainer/${this.props.maintainer.id}`)
  }

  private addStandby = async (e: React.MouseEvent<HTMLElement>,
                              maintainerId: number,
                              startDate: moment.Moment,
                              endDate: moment.Moment) => {
    e.stopPropagation()

    const jsonData: string = JSON.stringify({
      maintainerId,
      startDate: startDate.format("YYYY-MM-DD"),
      endDate: endDate.format("YYYY-MM-DD")
    })

    let errors: boolean
    errors = await RequestHelper.postJson(`/api/maintainer/standby`, jsonData)
    if (errors) {
      return false
    }

    this.props.reloadMaintainer(maintainerId)
  }

  private deleteStandby = async (e: React.MouseEvent<HTMLElement>, standbyId: number, maintainerId: number) => {
    e.stopPropagation()

    let errors: boolean
    errors = await RequestHelper.delete(`/api/maintainer/standby/${standbyId}`)
    if (errors) {
      return false
    }

    this.props.reloadMaintainer(maintainerId)
  }
}

const WeeklyMaintainerRow = withRouter(WeeklyMaintainerRowWithoutRouter)
export default WeeklyMaintainerRow
