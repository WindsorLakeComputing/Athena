import * as React from "react"
import MadHatterUser from "../../models/MadHatterUser"
import TableList from "../TableList"

interface Props {
  madHatterUsers: MadHatterUser[]
}

export default class MadHatterUsersTable extends React.Component<Props, {}> {
  public render = () => (
    <div className="table-list">
      <h2>Current Users</h2>
      <TableList
        columnHeaders={["Last Name", "First Name", "Email", "AMU"]}
        rows={this.buildUserRows()}
      />
    </div>
  )

  private buildUserRows = () => {
    const {madHatterUsers} = this.props
    return madHatterUsers.map(
      (user, index) =>
        <tr key={index}>
          <td>{user.lastName}</td>
          <td>{user.firstName}</td>
          <td>{user.email}</td>
          <td>{user.amu || "ERROR"}</td>
        </tr>
    )
  }
}
