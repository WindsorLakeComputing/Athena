import moment from "moment"
import * as React from "react"
import {DayPickerSingleDateController} from "react-dates"
import "react-dates/initialize"
import "react-dates/lib/css/_datepicker.css"
import DateHelper from "../../helpers/DateHelper"

interface Props {
  date: moment.Moment | null,
  onDateChange: (date: moment.Moment | null) => void,
}

const CalendarWrapper = ({date, onDateChange}: Props) => (
  <div id="calendar-wrapper">
    <DayPickerSingleDateController
      hideKeyboardShortcutsPanel={true}
      date={date}
      onDateChange={onDateChange}
      numberOfMonths={3}
      onFocusChange={() => null}
      focused={true}
      initialVisibleMonth={() => moment().subtract(DateHelper.getMonthOffsetForQuarter(moment()), "months")}
      orientation={"horizontal"}
      firstDayOfWeek={1}
      enableOutsideDays={true}
    />
  </div>
)

export default CalendarWrapper
