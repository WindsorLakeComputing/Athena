import {ChangeEvent, Component, default as React, FormEvent} from "react"
import {AthenaForm} from "../AthenaForm"
import Input from "../Input"

interface Props {
  createMaintainer: (lastName: string, firstName: string) => Promise<boolean>
  getMaintainerList: () => {}
}

interface State {
  [key: string]: string | boolean,

  firstName: string,
  lastName: string,
  isFormSubmitted: boolean
}

class AddMaintainerForm extends Component<Props, State> {

  constructor(props: any) {
    super(props)
    this.state = {
      firstName: "",
      lastName: "",
      isFormSubmitted: false
    }
  }

  public render() {
    return <AthenaForm onSubmit={this.submitAddMaintainer}
                 className={this.state.isFormSubmitted ? "form submitted" : "form"}
                 autoComplete="off">
      <h1>Add New Maintainer</h1>
      <Input label="LAST NAME"
             id="last_name"
             error={"please complete"}
             onChange={this.handleChange("lastName")}
             inputProps={{
               required: true,
               maxLength: 100,
               value: this.state.lastName
             }}
      />

      <Input label="FIRST NAME"
             id="first_name"
             error={"please complete"}
             onChange={this.handleChange("firstName")}
             inputProps={{
               required: true,
               maxLength: 100,
               value: this.state.firstName
             }}
      />

      <input type="submit"
             className="create_maintainer button-basic"
             value="SUBMIT"/>
    </AthenaForm>
  }

  private handleChange = (key: string) => (event: any) => {
    this.setState({[key]: event.target.value})
  }

  private submitAddMaintainer = async (event: FormEvent) => {
    this.setState({isFormSubmitted: true})

    const error = await this.props.createMaintainer(this.state.lastName, this.state.firstName)
    if (!error) {
      this.resetAddMaintainerState()
      this.props.getMaintainerList()
    }
  }

  private resetAddMaintainerState = () => (
    this.setState({
      firstName: "",
      lastName: "",
      isFormSubmitted: false
    })
  )
}

export default AddMaintainerForm
