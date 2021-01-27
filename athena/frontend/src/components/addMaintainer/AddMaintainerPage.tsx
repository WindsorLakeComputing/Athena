import {Component, default as React} from "react"
import RequestHelper from "../../helpers/RequestHelper"
import BackButton from "../BackButton/BackButton"
import AddMaintainerForm from "./AddMaintainerForm"

interface Props {
  getMaintainerList: () => any
}

class AddMaintainerPage extends Component<Props, {}> {
  constructor(props: any) {
    super(props)
  }

  public render() {
    return <div className="add-maintainer-page">
      <BackButton />
      <AddMaintainerForm
        createMaintainer={this.createMaintainer}
        getMaintainerList={this.props.getMaintainerList}
      />
    </div>
  }

  private createMaintainer = async (
    lastName: string,
    firstName: string
  ): Promise<boolean> => RequestHelper.postJson("/api/maintainers", JSON.stringify({firstName, lastName}))
}

export default AddMaintainerPage
