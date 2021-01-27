import {Component, default as React, FormEvent} from "react"
import MadHatterUser from "../../models/MadHatterUser"
import {AthenaForm} from "../AthenaForm"
import Input from "../Input"
import AmuDropdown from "./AmuDropdown"

interface State {
  [key: string]: string | boolean

  firstName: string
  lastName: string
  email: string
  amu: string
  isFormSubmitted: boolean
  isDuplicate: boolean
}

interface Props {
  createMadHatterUser: (lastName: string, firstName: string, email: string, amu: string) => Promise<any>
  madHatterUsers: MadHatterUser[]
}

class MadHatterUserForm extends Component<Props, State> {
  constructor(props: any) {
    super(props)
    this.state = {
      firstName: "",
      lastName: "",
      email: "",
      amu: "",
      isFormSubmitted: false,
      isDuplicate: false
    }
  }

  public render = () =>
    <AthenaForm onSubmit={this.submitMadHatterUser}
                className={this.state.isFormSubmitted ? "form submitted" : "form"}
                autoComplete="off">
      <h2>Add New User</h2>
      <Input
        label="LAST NAME"
        id="last_name"
        error="please complete"
        onChange={this.handleChange("lastName")}
        inputProps={{
          value: this.state.lastName,
          maxLength: 100,
          required: true
        }}
      />
      <Input
        label="FIRST NAME"
        id="first_name"
        error={"please complete"}
        onChange={this.handleChange("firstName")}
        inputProps={{
          value: this.state.firstName,
          maxLength: 100,
          required: true
        }}
      />
      <Input
        label="EMAIL"
        id="email"
        error={"please complete"}
        onChange={this.handleChange("email")}
        inputProps={{
          value: this.state.email,
          maxLength: 100,
          required: true,
          pattern: "[a-z0-9A-Z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$"
        }}
      />
      <div className="amu">
        <div className="input_container">
          <label htmlFor="amu-dropdown">AMU</label>
        </div>
        <AmuDropdown onAmuSelect={this.handleAmuChange} selectedAmu={this.state.amu}/>
      </div>
      <input
        type="submit"
        className="create_user button-basic"
        value="SUBMIT"
      />
      {this.state.isDuplicate ?
        <div className="error_text create_error">
          This user already exists
        </div> : ""
      }
    </AthenaForm>

  private handleChange = (key: string) => (event: any) => {
    this.setState({[key]: event.target.value})
  }

  private handleAmuChange = (amu: string) => {
    this.setState({amu})
  }

  private isDuplicateUser = () =>
    this.props.madHatterUsers.filter((user) => user.email === this.state.email).length > 0

  private submitMadHatterUser = (event: FormEvent) => {
    if (!this.isDuplicateUser()) {
      this.props.createMadHatterUser(
        this.state.lastName,
        this.state.firstName,
        this.state.email,
        this.state.amu
      ).then(() => this.resetState())
    } else {
      this.setState({isFormSubmitted: true, isDuplicate: true})
    }
  }

  private resetState = () => (
    this.setState({
      firstName: "",
      lastName: "",
      email: "",
      isFormSubmitted: false
    })
  )
}

export default MadHatterUserForm
