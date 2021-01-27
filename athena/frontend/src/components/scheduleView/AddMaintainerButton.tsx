import * as React from "react"
import {RouteComponentProps, withRouter} from "react-router"

interface Props extends RouteComponentProps<{}> {
}

class AddMaintainerWithoutRouter extends React.Component<Props, {}> {
  constructor(props: Props) {
    super(props)
  }

  public render = () => (
    <button className="add_maintainer button-basic" onClick={this.handleAddMaintainerClick}>Add Maintainer</button>
  )

  private handleAddMaintainerClick = () => {
    this.props.history.push(`/maintainer/new`)
  }
}

const AddMaintainerButton = withRouter(AddMaintainerWithoutRouter)
export default AddMaintainerButton
