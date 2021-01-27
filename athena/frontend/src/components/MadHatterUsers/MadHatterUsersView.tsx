import * as React from "react"
import RequestHelper from "../../helpers/RequestHelper"
import MadHatterUser from "../../models/MadHatterUser"
import BackButton from "../BackButton/BackButton"
import MadHatterUserForm from "./MadHatterUserForm"
import MadHatterUsersTable from "./MadHatterUsersTable"

interface State {
  madHatterUsers: MadHatterUser[]
}

class MadHatterUsersView extends React.Component<{}, State> {
  constructor(props: any) {
    super(props)
    this.state = {madHatterUsers: []}
  }

  public componentWillMount = async () => {
    this.retrieveMadHatterUsers()
  }

  public render = () => (
    (
      <div className="mad-hatter-user-details">
        <BackButton />
        <MadHatterUserForm createMadHatterUser={this.createMadHatterUser}
                           madHatterUsers={this.state.madHatterUsers}/>
        <MadHatterUsersTable madHatterUsers={this.state.madHatterUsers}/>
      </div>
    )
  )

  private createMadHatterUser = async (
    lastName: string,
    firstName: string,
    email: string,
    amu: string
  ): Promise<any> => {
    const jsonData: string = JSON.stringify({
      lastName,
      firstName,
      email,
      amu
    })

    try {
      await RequestHelper.postJson("/api/MadHatterUsers", jsonData)
      this.retrieveMadHatterUsers()
    } catch (e) {
      return
    }
  }

  private retrieveMadHatterUsers = () => {
    RequestHelper.get("/api/MadHatterUsers").then((response) => {
      this.setState({...response.body})
    })
  }

}

export default MadHatterUsersView
