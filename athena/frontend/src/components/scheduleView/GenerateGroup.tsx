import React from "react"
import RequestHelper from "../../helpers/RequestHelper"

interface State {
  generateResponse: string
}

export default class GenerateGroup extends React.Component<{}, State> {

  constructor(props: {}) {
    super(props)
    this.state = {
      generateResponse: ""
    }
  }

  public render() {
    return (
      <div>
        <button id="minerva-generate-schedule" className="button-basic" onClick={this.handleClick}>
          Generate Schedule
        </button>
        <div className="message-status">
          {this.state.generateResponse}
        </div>
      </div>
    )
  }

  private handleClick = async () => {
    let errors: boolean
    errors = await RequestHelper.post("/api/minerva")
    if (errors) {
      this.setState({generateResponse: "Error Sending Request"})
    } else {
      this.setState({generateResponse: "Successful Request"})
    }
  }

}
