import React from "react"
import {RouteComponentProps, withRouter} from "react-router"
import {getCookie} from "../helpers/RequestHelper"
import usericon from "../resources/images/usericon.svg"

interface State {
  showDropdown: boolean
}

class AccountDropdown extends React.Component<RouteComponentProps, State> {

  constructor(props: RouteComponentProps) {
    super(props)

    this.state = { showDropdown: false }
    this.toggleDropdown = this.toggleDropdown.bind(this)
    this.handleOutsideClick = this.handleOutsideClick.bind(this)
  }

  public render() {
    const { showDropdown } = this.state

    return (
      <>
        <a className={"dropdown-link"} onClick={this.toggleDropdown}>
          <img src={usericon}/>
        </a>
        {showDropdown ? (
        <div className="dropdown-menu">
          <div
            role="button"
            tabIndex={0}
            className="dropdown-menu__item"
          >
            <button id="add-user-button" onClick={this.handleAddUsersClick}>Add New User</button>
          </div>
          <div
            role="button"
            tabIndex={0}
            className="dropdown-menu__item"
          >
              <button id="logout-button" onClick={this.doLogout}>Logout</button>
          </div>
        </div>
        ) : null}
        <form id={"logout-form"} method="post" action="/logout">
          <input type="hidden" name="_csrf" value={getCookie("XSRF-TOKEN")}/>
        </form>
      </>
    )
  }

  public toggleDropdown() {
    const { showDropdown } = this.state

    if (!showDropdown) {
      // attach/remove event handler
      document.addEventListener("click", this.handleOutsideClick, false)
    } else {
      document.removeEventListener("click", this.handleOutsideClick, false)
    }

    this.setState({ showDropdown: !showDropdown })
  }

  public handleOutsideClick() {
    this.toggleDropdown()
  }

  private doLogout = () => (document.getElementById("logout-form") as HTMLFormElement).submit()

  private handleAddUsersClick = () => this.props.history.push(`/MadHatterUsers`)
}

export default withRouter(AccountDropdown)
