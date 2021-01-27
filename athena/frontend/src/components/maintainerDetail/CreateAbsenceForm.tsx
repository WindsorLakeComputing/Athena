import {Moment} from "moment"
import * as React from "react"
import {DateRangePicker, FocusedInputShape} from "react-dates"
import "react-dates/initialize"

interface Props {
  createAbsence: (startDate: Moment | null,
                  endDate: Moment | null,
                  reason: string | undefined,
                  hours: string | undefined,
                  location: string | undefined) => Promise<boolean>
}

interface State {
  focusedInput: FocusedInputShape | null
  startDate: Moment | null
  endDate: Moment | null
  reason: string
  reasonValid: boolean
  hours: string
  hoursValid: boolean
  location: string
  locationValid: boolean
}

class CreateAbsenceForm extends React.Component<Props, State> {

  constructor(props: any) {
    super(props)
    this.state = {
      focusedInput: null,
      startDate: null,
      endDate: null,
      reason: "",
      reasonValid: true,
      hours: "",
      hoursValid: true,
      location: "",
      locationValid: true
    }
  }

  public handleReasonChange = (event: any) => {
    this.setState({reason: event.target.value, reasonValid: event.target.checkValidity()})
  }

  public handleHoursChange = (event: any) => {
    this.setState({hours: event.target.value, hoursValid: event.target.checkValidity()})
  }

  public handleLocationChange = (event: any) => {
    this.setState({location: event.target.value, locationValid: event.target.checkValidity()})
  }

  public render() {
    return (
      <div className="create_absence_form maintainer-detail-group">
        <h2>Create Absence</h2>
        <div className="input_form">
          <DateRangePicker
            startDate={this.state.startDate}
            startDateId={"start-date-id"}
            endDate={this.state.endDate}
            endDateId={"end-date-id"}
            minimumNights={0}
            focusedInput={this.state.focusedInput}
            onDatesChange={this.onDatesChange}
            onFocusChange={(focusedInput) => this.setState({focusedInput})}
            hideKeyboardShortcutsPanel={true}
            phrases={{
              chooseAvailableStartDate: (arg) => `${(arg as any).date}`,
              chooseAvailableEndDate: (arg) => `${(arg as any).date}`,
              calendarLabel: "Calendar"
            }}
          />
          <input
            type="text"
            className={this.state.hoursValid ? "valid absence_input" : "invalid absence_input"}
            id="absence_hours"
            value={this.state.hours}
            placeholder={"Hours (e.g. 1100-1200)"}
            onChange={this.handleHoursChange}
            maxLength={20}
            pattern={"^[0-9A-Za-z -.:]{0,20}$"}
          />
          <input type={"text"}
                 className={this.state.locationValid ? "valid absence_input" : "invalid absence_input"}
                 id={"absence_location"}
                 value={this.state.location}
                 placeholder={"Location"}
                 onChange={this.handleLocationChange}
                 maxLength={20}
                 pattern={"^[0-9A-Za-z -.]{0,20}$"}
          />
          <input type={"text"}
                 className={this.state.reasonValid ? "valid absence_input" : "invalid absence_input"}
                 id={"absence_description"}
                 placeholder={"Description"}
                 value={this.state.reason}
                 onChange={this.handleReasonChange}
                 maxLength={20}
                 pattern={"^[0-9A-Za-z ?!.]{1,20}$"}
          />
          <button className="create_absence button-basic"
                  onClick={this.submitAbsence}
                  disabled={!this.state.reasonValid}
          >
            Create
          </button>
        </div>
        <div className="error_row">
          {(!this.state.reasonValid || !this.state.locationValid || !this.state.hoursValid) && <>
            <div className="date_range_picker_error">&nbsp;</div>
            <div className="error_message" id={"hours_error_message"}>
              {this.state.hoursValid ? " " : "Alphanumeric Characters Only"}
            </div>
            <div className="error_message" id={"location_error_message"}>
              {this.state.locationValid ? " " : "Alphanumeric Characters Only"}
            </div>
            <div className="error_message" id={"reason_error_message"}>
              {this.state.reasonValid ? " " : "Alphanumeric Characters Only"}
            </div>
          </>}
        </div>
      </div>)
  }

  private onDatesChange = ({startDate, endDate}: { startDate: Moment | null, endDate: Moment | null }) => {
    this.setState({startDate, endDate})
  }

  private submitAbsence = async () => {
    const absenceStateValid = !!this.state.startDate &&
      !!this.state.endDate &&
      !!this.state.reason &&
      this.state.reasonValid &&
      this.state.locationValid &&
      this.state.hoursValid
    if (absenceStateValid) {
      const success = await this.props.createAbsence(this.state.startDate,
        this.state.endDate,
        this.state.reason,
        this.state.hours,
        this.state.location)
      if (success) {
        this.resetAbsenceState()
      }
    }
  }

  private resetAbsenceState = () => {
    this.setState({startDate: null, endDate: null, reason: "", location: "", hours: ""})
  }
}

export default CreateAbsenceForm
