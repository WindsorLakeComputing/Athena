import {Component, default as React} from "react"
import BackArrow from "./BackArrow"

class BackButton extends Component<{}, {}> {
  public render = () => (
    <button
      className="back-button button-borderless"
      onClick={this.goHome}
    >
      <BackArrow />
      Back
    </button>
  )

  private goHome = () => {
    window.history.back()
  }
}

export default BackButton
