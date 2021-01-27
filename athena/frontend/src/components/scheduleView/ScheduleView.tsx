import moment from "moment"
import * as React from "react"
import {Maintainer} from "../../models/Maintainer"
import CalendarWrapper from "./CalendarWrapper"
import MaintainerList from "./MaintainerList"

interface Props {
  maintainers: Maintainer[],
  reloadMaintainer: (maintainerId: number) => void,
  date: moment.Moment,
  handleDateChange: (date: moment.Moment | null) => void
}

class ScheduleView extends React.Component<Props, {}> {

  public render = () => (
    <>
      <CalendarWrapper
        date={this.props.date}
        onDateChange={this.props.handleDateChange}
      />
      <MaintainerList
        maintainers={this.props.maintainers}
        date={this.props.date}
        reloadMaintainer={this.props.reloadMaintainer}
      />
    </>
  )
}

export default ScheduleView
