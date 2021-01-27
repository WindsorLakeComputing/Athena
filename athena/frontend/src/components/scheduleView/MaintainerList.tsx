import moment, {Moment} from "moment"
import * as React from "react"
import DateHelper from "../../helpers/DateHelper"
import {Maintainer} from "../../models/Maintainer"
import Shift from "../../models/Shift"
import AddMaintainerButton from "./AddMaintainerButton"
import WeeklyMaintainerRow from "./WeeklyMaintainerRow"

interface MaintainerListProps {
  maintainers: Maintainer[],
  date: Moment,
  reloadMaintainer: (maintainerId: number) => void
}

interface State {
  currentlySortedColumn: string | undefined
}

const WEEKDAYS = [0, 1, 2, 3, 4]

const MAINTAINER_ATTRIBUTES: string[] = ["Name", "Rank", "Shift", "Level"]

const CERTIFICATIONS: string[] = ["X", "L/X", "I/E", "Run", "HP", "TS", "Bore"]

const weekendHeader = (date: Moment) => DateHelper.getStartDateOfWeek(date).add(5, "days").format("D - ") +
  DateHelper.getStartDateOfWeek(date).add(6, "days").format("D MMM")

const weekdayDates = (date: moment.Moment): string[] => (
  WEEKDAYS.map((dayOfWeek) =>
    DateHelper.getStartDateOfWeek(date).add(dayOfWeek, "days").format("D, MMM"))
)

const assembleHeaders = (date: Moment) =>
  [...MAINTAINER_ATTRIBUTES, ...CERTIFICATIONS, ...weekdayDates(date), weekendHeader(date)]

class MaintainerList extends React.Component<MaintainerListProps, State> {
  constructor(props: MaintainerListProps) {
    super(props)
    this.state = {
      currentlySortedColumn: "Shift"
    }
  }

  public render() {
    return (
      <div className="table-list maintainer-list">
        <div className="maintainer_list_heading">
          <h4>Maintainers</h4>
          <AddMaintainerButton/>
        </div>
        <table>
          <thead>
            <tr>
              {assembleHeaders(this.props.date).map((columnName, index) => {
                let columnClassName = ""
                if (columnName === "Shift") {
                  if (this.state.currentlySortedColumn === "Shift") {
                    columnClassName = "sorted"
                  } else if (this.state.currentlySortedColumn === "Shift-reverse") {
                    columnClassName = "sorted-reverse"
                  } else {
                    columnClassName = "unsorted"
                  }
                } else if (columnName.indexOf("-") > 0) {
                  if (this.state.currentlySortedColumn === "Weekend") {
                    columnClassName = "sorted"
                  } else {
                    columnClassName = "unsorted"
                  }
                }
                return (
                  <th className={columnClassName}
                      id={`header-${columnName.replace(/ /g, "")}`}
                      key={index} onClick={() => {
                    if (this.headerClickedCallback) {
                      this.headerClickedCallback(columnName)
                    }
                  }}>{columnName}</th>
                )
              })}
            </tr>
          </thead>
          <tbody>
          {this.assembleMaintainerRows()}
          </tbody>
        </table>
      </div>
    )
  }

  private assembleMaintainerRows = (): JSX.Element[] => (
    [...this.props.maintainers]
      .sort(this.compareMaintainers)
      .map((maintainer, index) =>
        <WeeklyMaintainerRow
          maintainer={maintainer}
          certificateHeaders={CERTIFICATIONS}
          date={this.props.date}
          key={index}
          reloadMaintainer={this.props.reloadMaintainer}
        />)
  )

  private compareMaintainers = (a: Maintainer, b: Maintainer) => {
    if ((this.state.currentlySortedColumn === "Shift") || (this.state.currentlySortedColumn === "Shift-reverse")) {
      const shift1 = this.state.currentlySortedColumn === "Shift" ? a.shift.name : b.shift.name
      const shift2 = this.state.currentlySortedColumn === "Shift" ? b.shift.name : a.shift.name

      if (shift1 === shift2) {
        return 0
      }

      if (shift1 === "D") {
        return -1
      }

      if (shift2 === "D") {
        return 1
      }

      if (shift1 === "S") {
        return -1
      }

      if (shift2 === "S") {
        return 1
      }

      if (shift1 === "M") {
        return -1
      }

      return 1
    } else if (this.state.currentlySortedColumn === "Weekend") {
      const aOnStandby = a.isOnStandbyForWeek(this.props.date)
      const bOnStandby = b.isOnStandbyForWeek(this.props.date)

      if (aOnStandby === bOnStandby) {
        return 0
      }

      if (aOnStandby) {
        return -1
      }

      if (bOnStandby) {
        return 1
      }

      return 0
    } else {
      return 0
    }
  }

  private headerClickedCallback = (header: string) => {
    const currentlySortedColumn = this.state.currentlySortedColumn
    if (header === "Shift") {
      if (currentlySortedColumn === "Shift") {
        this.setState({currentlySortedColumn: "Shift-reverse"})
      } else {
        this.setState({currentlySortedColumn: "Shift"})
      }
    } else if (header === weekendHeader(this.props.date)) {
        this.setState({currentlySortedColumn: "Weekend"})
    }
  }
}

export default MaintainerList
