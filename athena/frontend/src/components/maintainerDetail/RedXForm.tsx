import * as React from "react"
import Certificate from "../../models/Certificate"

type RedXSelection = "NONE" | "RED_X" | "LIMITED_RED_X"

interface Props {
  certs: Certificate[]
  onSave: (certs: Certificate[]) => any
}

interface State {
  selectedRedXCert: RedXSelection
}

export class RedXForm extends React.Component<Props, State> {

  constructor(props: Props) {
    super(props)
    this.state = {
      selectedRedXCert: this.getInitialSelection(props.certs)
    }
  }

  public render() {
    const {selectedRedXCert} = this.state
    return (
      <form onSubmit={(e) => e.preventDefault()} className="maintainer-detail-group">
        <div className="red-x-options">
          <h2 className="red-x-title">Red X Qualifications</h2>
          <div className="radio-options">
            <div className="radio-button">
              <input type="radio"
                     id="no-red-x-radio"
                     onChange={() => this.onChange("NONE")}
                     checked={selectedRedXCert === "NONE"}/>
              <label htmlFor="no-red-x-radio">
                Not Red X Qualified
              </label>
            </div>
            <div className="radio-button">
              <input type="radio"
                     id="red-x-radio"
                     onChange={() => this.onChange("RED_X")}
                     checked={selectedRedXCert === "RED_X"}/>
              <label htmlFor="red-x-radio">
                Red X Qualified
              </label>
            </div>
            <div className="radio-button">
              <input type="radio"
                     id="limited-red-x-radio"
                     onChange={() => this.onChange("LIMITED_RED_X")}
                     checked={selectedRedXCert === "LIMITED_RED_X"}/>
              <label htmlFor="limited-red-x-radio">
                Limited Red X
              </label>
            </div>
          </div>
        </div>
      </form>
    )
  }

  private getInitialSelection(certs: Certificate[]): RedXSelection {
    if (!!certs.find((cert) => "X" === cert.name)) {
      return "RED_X"
    } else if (!!certs.find((cert) => "L/X" === cert.name)) {
      return "LIMITED_RED_X"
    } else {
      return "NONE"
    }
  }

  private onChange = (value: RedXSelection) => {
    this.setState({selectedRedXCert: value})
    this.onSave(value)
  }

  private onSave = (selectedXCert: RedXSelection) => {
    const selected = selectedXCert
    const newCerts = this.props.certs.filter((cert) => "X" !== cert.name && "L/X" !== cert.name)
    if (selected === "RED_X") {
      newCerts.push(new Certificate("X"))
    } else if (selected === "LIMITED_RED_X") {
      newCerts.push(new Certificate("L/X"))
    }

    this.props.onSave(newCerts)
  }
}
