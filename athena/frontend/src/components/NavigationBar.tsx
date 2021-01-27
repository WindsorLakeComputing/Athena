import * as React from "react"
import {Route} from "react-router"
import AccountDropdown from "./AccountDropdown"
import PrintIcon from "./PrintIcon"
import UploadGroup from "./scheduleView/UploadGroup"

interface Props {
  userName: string | null,
  getMaintainerList: () => void
}

const NavigationBar = (props: Props) => (
  <div className="global_header">
    <div
      className="home_button"
      onClick={() => {
        location.hash = "/"
      }}
    >
      <div className="app-name">Maintainer Management</div>
    </div>
    <Route
      exact
      path={"/"}
      render={() => (
        <div className="action_bar">
          <UploadGroup getMaintainerList={props.getMaintainerList}/>
        </div>
      )}
    />
    <div>
      {props.userName && <span className={"welcome-message"}>Welcome {props.userName}</span>}
      <PrintIcon/>
      <AccountDropdown/>
    </div>
  </div>
)

export default NavigationBar
